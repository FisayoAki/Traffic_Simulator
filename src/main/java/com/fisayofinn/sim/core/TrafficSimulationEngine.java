package com.fisayofinn.sim.core;

import com.fisayofinn.sim.math.ParetoHeavyTailDistribution;
import com.fisayofinn.sim.sources.OnOffTrafficSource;

import java.util.ArrayList;
import java.util.Random;

/**
 * Runs the discrete-event simulation using ON/OFF sources.
 */
public class TrafficSimulationEngine {

    private final SimulationEventQueue eventQueue;
    private final ArrayList<OnOffTrafficSource> sources;
    private final AggregateTimeSeries series;
    private final boolean verbose;
    private double simTime;
    private double endTime;
    private int activeSources;

    public TrafficSimulationEngine(double endTime, int sourceCount) {
        this(endTime, sourceCount, false);
    }

    public TrafficSimulationEngine(double endTime, int sourceCount, boolean verbose) {
        this.endTime = endTime;
        this.simTime = 0.0;
        this.activeSources = 0;
        this.verbose = verbose;
        this.eventQueue = new SimulationEventQueue();
        this.sources = new ArrayList<>();
        this.series = new AggregateTimeSeries();

        for (int i = 1; i <= sourceCount; i++) {
            ParetoHeavyTailDistribution onDur =
                    new ParetoHeavyTailDistribution(1.4, 0.5, new Random(1000 + i));
            ParetoHeavyTailDistribution offDur =
                    new ParetoHeavyTailDistribution(1.8, 0.2, new Random(2000 + i));
            OnOffTrafficSource src = new OnOffTrafficSource(i, onDur, offDur, false);
            sources.add(src);

            double firstTime = src.firstToggleAt(simTime);
            if (firstTime < endTime) {
                eventQueue.addEvent(new SimulationEvent(firstTime, SimulationEventType.ON, src.id()));
            }
        }
        eventQueue.addEvent(new SimulationEvent(endTime, SimulationEventType.END, -1));
    }

    // package-private for tests
    TrafficSimulationEngine(double endTime,
                            ArrayList<OnOffTrafficSource> givenSources,
                            SimulationEventQueue queue,
                            boolean verbose) {
        this.endTime = endTime;
        this.simTime = 0.0;
        this.activeSources = 0;
        this.verbose = verbose;
        this.eventQueue = queue;
        this.sources = givenSources;
        this.series = new AggregateTimeSeries();

        for (OnOffTrafficSource src : sources) {
            double firstTime = src.firstToggleAt(simTime);
            if (firstTime < endTime) {
                eventQueue.addEvent(new SimulationEvent(firstTime, SimulationEventType.ON, src.id()));
            }
        }
        eventQueue.addEvent(new SimulationEvent(endTime, SimulationEventType.END, -1));
    }

    public void run() {
        while (simTime < endTime && !eventQueue.isEmpty()) {
            SimulationEvent e = eventQueue.retrieveEvent();
            if (e == null) {
                break;
            }
            simTime = e.getTime();
            if (e.getType() == SimulationEventType.END) {
                logEvent(e);
                break;
            }

            OnOffTrafficSource src = sources.get(e.getUserID() - 1);

            if (e.getType() == SimulationEventType.ON) {
                src.setOn(true);
                activeSources++;
                series.add(simTime, activeSources);
                logEvent(e);

                double tOff = simTime + src.nextDurationForCurrentState();
                if (tOff < endTime) {
                    eventQueue.addEvent(new SimulationEvent(tOff, SimulationEventType.OFF, src.id()));
                }
            } else if (e.getType() == SimulationEventType.OFF) {
                src.setOn(false);
                activeSources--;
                series.add(simTime, activeSources);
                logEvent(e);

                double tOn = simTime + src.nextDurationForCurrentState();
                if (tOn < endTime) {
                    eventQueue.addEvent(new SimulationEvent(tOn, SimulationEventType.ON, src.id()));
                }
            }
        }
    }

    private void logEvent(SimulationEvent e) {
        if (verbose) {
            System.out.println("Event at t=" + simTime
                    + " type=" + e.getType()
                    + " src=" + e.getUserID()
                    + " active=" + activeSources);
        }
    }

    public double getSimTime() {
        return simTime;
    }

    public int getActiveSources() {
        return activeSources;
    }

    public AggregateTimeSeries getSeries() {
        return series;
    }

    public ArrayList<OnOffTrafficSource> getSources() {
        return sources;
    }

    public int getPeakActiveSources() {
        int max = 0;
        for (int v : series.values()) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public double getAverageActiveSources() {
        if (series.size() == 0) {
            return 0.0;
        }
        int sum = 0;
        for (int v : series.values()) {
            sum += v;
        }
        return sum / (double) series.size();
    }
}
