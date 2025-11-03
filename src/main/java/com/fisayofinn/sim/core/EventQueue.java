package com.fisayofinn.sim.core;

import java.util.PriorityQueue;

public class EventQueue {

    // One priority queue per simulation so it is final
    private final PriorityQueue<Event> eventQueue;

    // Constructor
    public EventQueue(){
        this.eventQueue = new PriorityQueue<Event>();
    }

    // Add event to queue
    public void addEvent(Event myEvent){
        eventQueue.add(myEvent);
    }

    // Check if queue has no events to proceed with
    public boolean isEmpty(){
        return eventQueue.isEmpty()
    }

}
