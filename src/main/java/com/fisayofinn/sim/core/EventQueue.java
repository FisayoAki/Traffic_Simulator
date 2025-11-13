package com.fisayofinn.sim.core;

import java.util.PriorityQueue;

/**
 * Implements a PriorityQueue for adding, removing and sorting Events
 */
public class EventQueue {

    // One priority queue per simulation so it is final
    private final PriorityQueue<Event> eventQueue;

    // Constructor
    public EventQueue() {
        this.eventQueue = new PriorityQueue<Event>();
    }

    // Add event to queue
    public void addEvent(Event myEvent) {
        eventQueue.add(myEvent);
    }

    // Retrieve and Remove the earliest Event in the queue
    public Event retrieveEvent() {
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
