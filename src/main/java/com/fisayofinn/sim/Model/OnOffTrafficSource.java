package com.fisayofinn.sim.Model;

/**
 * A single traffic source that alternates between ON and OFF periods.
 * ON and OFF durations are sampled from Pareto distributions.
 */
public final class OnOffTrafficSource {

    private final int id;
    private final ParetoHeavyTailDistribution onDurations;
    private final ParetoHeavyTailDistribution offDurations;
    private boolean on;

    public OnOffTrafficSource(int id,
                              ParetoHeavyTailDistribution onDurations,
                              ParetoHeavyTailDistribution offDurations,
                              boolean startOn) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be >= 1");
        }
        if (onDurations == null || offDurations == null) {
            throw new NullPointerException("duration samplers must not be null");
        }
        this.id = id;
        this.onDurations = onDurations;
        this.offDurations = offDurations;
        this.on = startOn;
    }

    /** @return the (1-based) ID of this source. */
    public int id() {
        return id;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean value) {
        this.on = value;
    }

    /**
     * Duration until the next toggle based on current state (does not change state).
     */
    public double nextDurationForCurrentState() {
        // If currently ON, sample ON duration, otherwise sample OFF duration
        if (on) {
            return onDurations.sample();
        } else {
            return offDurations.sample();
        }
    }

    /**
     * Absolute time of the next toggle if we start from the given time.
     */
    public double firstToggleAt(double startTime) {
        return startTime + nextDurationForCurrentState();
    }
}
