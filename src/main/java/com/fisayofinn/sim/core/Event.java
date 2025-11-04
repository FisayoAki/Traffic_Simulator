package com.fisayofinn.sim.core;

/** Event objects */
public class Event implements Comparable<Event>{

    private double time;
    private EventType type;
    private int userID;

    // Constructor
    public Event(double time, EventType type, int userID) {
        this.time = time;
        this.type = type;
        this.userID = userID;
    }

    // Compare Event objects to find which has priority
    @Override
    public int compareTo(Event other) {
        // If equal timing, prioritise lower userID
        if (this.time == other.time){
            return this.userID < other.userID ? 1 : -1;
        }else {
            return this.time > other.time ? 1 : -1;
        }
    }

    //Getters
    public double getTime() {
        return time;
    }

    public EventType getType(){
        return type;
    }

    public int getUserID() {
        return userID;
    }



}
