package com.fisayofinn.sim.Controller;

import java.util.PriorityQueue;

/**
 * Wraps a PriorityQueue of SimulationEvent, ordered by time (and then userID).
 */
public class SimulationEventQueue {

    private final PriorityQueue<SimulationEvent> pq = new PriorityQueue<>();

    /** Add an event to the queue. */
    public void addEvent(SimulationEvent e) {
        pq.add(e);
    }

    /**
     * Get and remove the earliest event from the queue,
     * or null if the queue is empty.
     */
    public SimulationEvent retrieveEvent() {
        return pq.poll();
    }

    /** @return true if the queue has no more events. */
    public boolean isEmpty() {
        return pq.isEmpty();
    }
}
