package com.fisayofinn.sim.core;

import com.fisayofinn.sim.math.HeavyTail;
import com.fisayofinn.sim.sources.OnOffSource;

import java.util.ArrayList;
import java.util.Random;

public class SimulationManager {

    private final EventQueue eventQueue;
    private final ArrayList<OnOffSource> sources;
    private double simTime, endTime;
    private int activeSources;

    public SimulationManager(double endTime, int sourceCount) {
        this.endTime = endTime;
        this.simTime = 0.0;
        this.activeSources = 0;
        this.eventQueue = new EventQueue();
        this.sources = new ArrayList<>();

        // Create sources; all start OFF for Phase 1 (simple, deterministic).
        for (int i = 1; i <= sourceCount; i++) {
            HeavyTail onDurations  = new HeavyTail(1.4, 0.5, new Random(1000 + i));
            HeavyTail offDurations = new HeavyTail(1.8, 0.2, new Random(2000 + i));
            boolean startOn = false;

            OnOffSource src = new OnOffSource(i, onDurations, offDurations, startOn);
            sources.add(src);

            // First event type is ON (since we start OFF).
            double firstTime = src.firstToggleAt(simTime); // now + OFF duration (no mutation)
            eventQueue.addEvent(new Event(firstTime, EventType.ON, src.id()));
        }

        // End-of-simulation sentinel
        eventQueue.addEvent(new Event(endTime, EventType.END, -1));
    }

    public void simLoop() {
        while (simTime < endTime && !eventQueue.isEmpty()) {
            Event e = eventQueue.retrieveEvent();
            simTime = e.getTime();
            processEvent(e);
        }
    }

    private void processEvent(Event event) {
        int srcId = event.getUserID();            // your Event uses 'userID'
        if (event.getType() == EventType.END) {   // stop condition
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
                    eventQueue.addEvent(new Event(tOff, EventType.OFF, src.id()));
                }
                break;

            case OFF:
                // Source becomes OFF now.
                src.setOn(false);
                activeSources--;

                // Next toggle will be ON after an OFF duration.
                double tOn = simTime + src.nextDurationForCurrentState(); // uses current OFF state
                if (tOn < endTime) {
                    eventQueue.addEvent(new Event(tOn, EventType.ON, src.id()));
                }
                break;

            default:
                // (No RECORD handling in this minimal version.)
                break;
        }
    }
}
