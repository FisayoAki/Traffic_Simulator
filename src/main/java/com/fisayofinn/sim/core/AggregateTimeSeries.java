package com.fisayofinn.sim.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the time series of (time, activeSources) for the simulation.
 * Each sample corresponds to a change in number of ON sources.
 */
public class AggregateTimeSeries {

    private final ArrayList<Double> times = new ArrayList<>();
    private final ArrayList<Integer> values = new ArrayList<>();

    /** Add a new sample to the series. */
    public void add(double time, int active) {
        times.add(time);
        values.add(active);
    }

    /** @return unmodifiable list of times. */
    public List<Double> times() {
        return Collections.unmodifiableList(times);
    }

    /** @return unmodifiable list of active source counts. */
    public List<Integer> values() {
        return Collections.unmodifiableList(values);
    }

    /** @return number of samples stored. */
    public int size() {
        return times.size();
    }
}
