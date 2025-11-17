package com.fisayofinn.sim.core;

import java.util.PriorityQueue;

// Creates the PriorityQueue for adding, removing and sorting Events
public class SimulationEventQueue {

    // One priority queue per simulation so it is final
    private final PriorityQueue<SimulationEvent> eventQueue;

    // Constructor
    public SimulationEventQueue() {
        this.eventQueue = new PriorityQueue<SimulationEvent>();
    }

    // Add event to queue
    public void addEvent(SimulationEvent myEvent) {
        eventQueue.add(myEvent);
    }

    // Retrieve and Remove the earliest Event in the queue
    public SimulationEvent retrieveEvent() {
        return eventQueue.poll();
    }

    // Check if queue has no events to proceed with
    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    public int queueLength() {
        return eventQueue.size();
    }

    public void emptyQueue() {
        eventQueue.clear();
    }

}
