package com.fisayofinn.sim.core;

import java.util.PriorityQueue;

// Creates the PriorityQueue for adding, removing and sorting Events
public class SimulationEventQueue {

    // One priority queue per simulation so it is final
    private final PriorityQueue<SimulationEvent> pq = new PriorityQueue<>();

    // Add event to queue
    public void addEvent(SimulationEvent e) {
        pq.add(e);
    }

    // Retrieve and Remove the earliest Event in the queue
    public SimulationEvent retrieveEvent() {
        return pq.poll();
    }

    // Check if queue has no events to proceed with
    public boolean isEmpty() {
        return pq.isEmpty();
    }
}
