package com.fisayofinn.sim.Controller;

/** A scheduled event in the simulation time */
public class SimulationEvent implements Comparable<SimulationEvent> {

    private final double time;
    private final SimulationEventType type;
    private final int userID;

    public SimulationEvent(double time, SimulationEventType type, int userID) {
        this.time = time;
        this.type = type;
        this.userID = userID;
    }

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
