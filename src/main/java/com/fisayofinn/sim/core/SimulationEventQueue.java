package com.fisayofinn.sim.core;

import java.util.PriorityQueue;

/**
 * Simple wrapper around a priority queue of SimulationEvents.
 * Ensures events are retrieved in sorted time order.
 */
public class SimulationEventQueue {

    // Internal priority queue storing events in sorted order
    private final PriorityQueue<SimulationEvent> pq = new PriorityQueue<>();

    /** Add a new event into the queue. */
    public void addEvent(SimulationEvent e) {
        pq.add(e);
    }

    /** Retrieve and remove the earliest event. */
    public SimulationEvent retrieveEvent() {
        return pq.poll();
    }

    /** @return true if no events remain. */
    public boolean isEmpty() {
        return pq.isEmpty();
    }
}
