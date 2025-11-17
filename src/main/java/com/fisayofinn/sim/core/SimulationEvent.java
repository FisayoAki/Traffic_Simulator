package com.fisayofinn.sim.core;

/** Represents a scheduled event in simulation time */
public class SimulationEvent implements Comparable<SimulationEvent> {

    private final double time;                 // Time at which this event occurs
    private final SimulationEventType type;    // ON/OFF/END event type
    private final int userID;                  // ID of the source this event relates to

    public SimulationEvent(double time, SimulationEventType type, int userID) {
        this.time = time;
        this.type = type;
        this.userID = userID;
    }

    //Defines ordering for priority queue
    @Override
    public int compareTo(SimulationEvent other) {
        if (this.time == other.time) {
            return Integer.compare(this.userID, other.userID);
        }
        return Double.compare(this.time, other.time);
    }

    public double getTime() { return time; }
    public SimulationEventType getType() { return type; }
    public int getUserID() { return userID; }
}
