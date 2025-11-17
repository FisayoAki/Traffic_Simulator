package com.fisayofinn.sim.core;

/** Single scheduled change in the simulation timeline. */
public class SimulationEvent implements Comparable<SimulationEvent> {

    // Simulation Event Types
    public enum Type {
        ON,   // A traffic source switches ON
        OFF,  // A traffic source switches OFF
        END   // Simulation ends at this time
    }

    private final double time;
    private final Type type;
    private final int userID;

    public SimulationEvent(double time, Type type, int userID) {
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
    public Type getType() { return type; }
    public int getUserID() { return userID; }
}
