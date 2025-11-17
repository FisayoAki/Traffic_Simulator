package com.fisayofinn.sim.core;

import com.fisayofinn.sim.math.HeavyTail;
import com.fisayofinn.sim.sources.OnOffSource;

import java.util.ArrayList;
import java.util.Random;

public class SimulationManager {

    private final EventQueue eventQueue;
    private final ArrayList<OnOffSource> sources;
    private double simTime;
    private final double endTime;
    private int activeSources;

    // Constructor
    public SimulationManager(double endTime, int sourceCount) {
        this.endTime = endTime;
        this.simTime = 0.0;
        this.activeSources = 0;
        this.eventQueue = new EventQueue();
        this.sources = new ArrayList<>();

        // Create sources; all start OFF for Phase 1 .
        for (int i = 1; i <= sourceCount; i++) {
            HeavyTail onDurations = new HeavyTail(1.4, 0.5, new Random(1000 + i));
            HeavyTail offDurations = new HeavyTail(1.4, 0.5, new Random(2000 + i));
            boolean startOn = false;

            OnOffSource src = new OnOffSource(i, onDurations, offDurations, startOn);
            sources.add(src);

            // First event type is ON (since we start OFF).
            double firstTime = src.firstToggleAt(simTime); // now + OFF duration (no mutation)
            eventQueue.addEvent(new Event(firstTime, SimulationEventType.ON, src.id()));
        }

        // End-of-simulation sentinel
        eventQueue.addEvent(new Event(endTime, SimulationEventType.END, -1));
    }

    // Simulation Loop
    public void simLoop() {
        while (simTime < endTime && !eventQueue.isEmpty()) {
            Event e = eventQueue.retrieveEvent();
            simTime = e.getTime();
            processEvent(e);
        }
    }

    // Retrieves Event, changes Source state and creates new Event
    private void processEvent(Event event) {
        int srcId = event.getUserID();
        if (event.getType() == SimulationEventType.END) {   // stop condition
            return;
        }

        OnOffSource src = sources.get(srcId - 1); // ids are 1-based

        switch (event.getType()) {
            case ON:
                // Source becomes ON now.
                src.setOn(true);
                activeSources++;

                // Next toggle will be OFF after an ON duration.
                double tOff = simTime + src.nextDurationForCurrentState(); // uses current ON state
                if (tOff < endTime) {
                    eventQueue.addEvent(new Event(tOff, SimulationEventType.OFF, src.id()));
                }
                break;

            case OFF:
                // Source becomes OFF now.
                src.setOn(false);
                activeSources--;

                // Next toggle will be ON after an OFF duration.
                double tOn = simTime + src.nextDurationForCurrentState(); // uses current OFF state
                if (tOn < endTime) {
                    eventQueue.addEvent(new Event(tOn, SimulationEventType.ON, src.id()));
                }
                break;

            default:
                // (No RECORD handling in this MVP)
                break;
        }
    }

    // Additional Methods for Testing
    public double getSimTime() {
        return simTime;
    }

    public int getActiveSources() {
        return activeSources;
    }


}
