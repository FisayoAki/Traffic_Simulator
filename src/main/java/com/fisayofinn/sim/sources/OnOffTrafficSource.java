package com.fisayofinn.sim.sources;

import com.fisayofinn.sim.math.ParetoHeavyTailDistribution;

/** A single traffic source that alternates between ON and OFF. */
public final class OnOffTrafficSource {
    private final int id;
    private final ParetoHeavyTailDistribution onDurations;
    private final ParetoHeavyTailDistribution offDurations;
    private boolean on;

    public OnOffTrafficSource(int id,
                              ParetoHeavyTailDistribution onDurations,
                              ParetoHeavyTailDistribution offDurations,
                              boolean startOn) {
        if (id <= 0) throw new IllegalArgumentException("id must be >= 1");
        if (onDurations == null || offDurations == null)
            throw new NullPointerException("duration samplers must not be null");
        this.id = id;
        this.on = startOn;
        this.onDurations = onDurations;
        this.offDurations = offDurations;
    }

    public int id() { return id; }
    public boolean isOn() { return on; }
    public void setOn(boolean value) { this.on = value; }

    /** Duration until the next toggle given the current state (no mutation). */
    public double nextDurationForCurrentState() {
        return on ? onDurations.sample() : offDurations.sample();
    }

    /** Absolute time of the first toggle from a given start time. */
    public double firstToggleAt(double startTime) {
        return startTime + nextDurationForCurrentState();
    }
}
