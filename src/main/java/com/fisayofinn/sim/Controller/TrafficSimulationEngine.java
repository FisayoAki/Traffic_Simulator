package com.fisayofinn.sim.Controller;

import com.fisayofinn.sim.Model.AggregateTimeSeries;
import com.fisayofinn.sim.Model.ParetoHeavyTailDistribution;
import com.fisayofinn.sim.Model.OnOffTrafficSource;

import java.util.ArrayList;
import java.util.Random;

/**
 * Runs a discrete-event simulation of multiple ON/OFF traffic sources.
 * Each source alternates between ON and OFF periods drawn from Pareto distributions.
 */
public class TrafficSimulationEngine {

    // Priority queue of scheduled events (ON, OFF, END)
    private final SimulationEventQueue eventQueue;

    // All traffic sources participating in the simulation
    private final ArrayList<OnOffTrafficSource> sources;

    // Records (time, activeSources) whenever the active count changes
    private final AggregateTimeSeries series;

    // Enables printing of detailed event logs
    private final boolean verbose;

    // Current simulation time
    private double simTime;

    // Simulation end boundary
    private final double endTime;

    // Count of sources currently ON
    private int activeSources;

    /**
     * Default constructor (no verbose logging).
     */
    public TrafficSimulationEngine(double endTime, int sourceCount) {
        this(endTime, sourceCount, false);
    }

    /**
     * Main constructor for creating a simulation with real sources.
     * Generates sourceCount ON/OFF sources with predetermined seeds.
     */
    public TrafficSimulationEngine(double endTime, int sourceCount, boolean verbose) {
        this.endTime = endTime;
        this.simTime = 0.0;
        this.activeSources = 0;
        this.verbose = verbose;

        this.eventQueue = new SimulationEventQueue();
        this.sources = new ArrayList<>();
        this.series = new AggregateTimeSeries();

        // Create each ON/OFF source
        for (int i = 1; i <= sourceCount; i++) {

            // Heavy-tailed ON and OFF durations with fixed seeds for reproducibility
            ParetoHeavyTailDistribution onDur =
                    new ParetoHeavyTailDistribution(1.4, 0.5, new Random(1000 + i));
            ParetoHeavyTailDistribution offDur =
                    new ParetoHeavyTailDistribution(1.8, 0.2, new Random(2000 + i));

            // Start each source in OFF state
            OnOffTrafficSource src = new OnOffTrafficSource(i, onDur, offDur, false);
            sources.add(src);

            // Schedule this source’s first ON event
            double firstTime = src.firstToggleAt(simTime);
            if (firstTime < endTime) {
                eventQueue.addEvent(new SimulationEvent(firstTime, SimulationEventType.ON, src.id()));
            }
        }

        // Schedule end-of-simulation event
        eventQueue.addEvent(new SimulationEvent(endTime, SimulationEventType.END, -1));
    }

    /**
     * Constructor for testing (allows specified source and event queue).
     */
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

        // Schedule first ON event for each test source
        for (OnOffTrafficSource src : sources) {
            double firstTime = src.firstToggleAt(simTime);
            if (firstTime < endTime) {
                eventQueue.addEvent(new SimulationEvent(firstTime, SimulationEventType.ON, src.id()));
            }
        }

        eventQueue.addEvent(new SimulationEvent(endTime, SimulationEventType.END, -1));
    }


    public TrafficSimulationEngine(double endTime,
                                   int sourceCount,
                                   double onAlpha,
                                   double onXm,
                                   double offAlpha,
                                   double offXm,
                                   boolean verbose) {

        this.endTime = endTime;
        this.simTime = 0.0;
        this.activeSources = 0;
        this.verbose = verbose;
        this.eventQueue = new SimulationEventQueue();
        this.sources = new ArrayList<>();
        this.series = new AggregateTimeSeries();

        for (int i = 1; i <= sourceCount; i++) {
            ParetoHeavyTailDistribution onDur =
                    new ParetoHeavyTailDistribution(onAlpha, onXm, new Random(1000 + i));
            ParetoHeavyTailDistribution offDur =
                    new ParetoHeavyTailDistribution(offAlpha, offXm, new Random(2000 + i));

            OnOffTrafficSource src =
                    new OnOffTrafficSource(i, onDur, offDur, false);

            sources.add(src);

            double firstTime = src.firstToggleAt(simTime);
            if (firstTime < endTime) {
                eventQueue.addEvent(
                        new SimulationEvent(firstTime, SimulationEventType.ON, src.id()));
            }
        }

        eventQueue.addEvent(
                new SimulationEvent(endTime, SimulationEventType.END, -1));
    }


    /**
     * Processes events in chronological order until no events remain or END is reached.
     */
    public void run() {
        while (simTime < endTime && !eventQueue.isEmpty()) {

            SimulationEvent e = eventQueue.retrieveEvent();
            if (e == null) break;

            // Advance simulation time to event time
            simTime = e.getTime();

            // Stop at END event
            if (e.getType() == SimulationEventType.END) {
                logEvent(e);
                break;
            }

            OnOffTrafficSource src = sources.get(e.getUserID() - 1);

            // Source turns ON
            if (e.getType() == SimulationEventType.ON) {

                src.setOn(true);
                activeSources++;
                series.add(simTime, activeSources);
                logEvent(e);

                // Schedule when it will turn OFF next
                double tOff = simTime + src.nextDurationForCurrentState();
                if (tOff < endTime) {
                    eventQueue.addEvent(new SimulationEvent(tOff, SimulationEventType.OFF, src.id()));
                }

                // Source turns OFF
            } else if (e.getType() == SimulationEventType.OFF) {

                src.setOn(false);
                activeSources--;
                series.add(simTime, activeSources);
                logEvent(e);

                // Schedule when it will turn ON next
                double tOn = simTime + src.nextDurationForCurrentState();
                if (tOn < endTime) {
                    eventQueue.addEvent(new SimulationEvent(tOn, SimulationEventType.ON, src.id()));
                }
            }
        }
    }

    // Helper for verbose logging of events.
    private void logEvent(SimulationEvent e) {
        if (verbose) {
            System.out.println(
                    "Event at t=" + simTime +
                            " type=" + e.getType() +
                            " src=" + e.getUserID() +
                            " active=" + activeSources
            );
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


    /**
     * Returns the peak number of simultaneously active sources.
     */
    public int getPeakActiveSources() {
        int max = 0;
        for (int v : series.values()) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    /**
     * Returns the simple average number of active sources over all samples.
     */
    public double getAverageActiveSources() {
        if (series.isEmpty()) return 0.0;

        int sum = 0;
        for (int v : series.values()) {
            sum += v;
        }

        return sum / (double) series.size();
    }
}
