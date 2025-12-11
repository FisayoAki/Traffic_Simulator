package com.fisayofinn.sim.sources;

import com.fisayofinn.sim.math.ParetoHeavyTailDistribution;

/**
 * Represents a single ON/OFF traffic source with heavy-tailed
 * durations for both ON and OFF states.
 */
public final class OnOffTrafficSource {

    private final int id;                            // Unique source identifier
    private final ParetoHeavyTailDistribution onDurations;  // Distribution for ON times
    private final ParetoHeavyTailDistribution offDurations; // Distribution for OFF times

    private boolean on; // Tracks the current state (ON or OFF)

    /**
     * @param id           identifier (must be >= 1)
     * @param onDurations  distribution for ON durations
     * @param offDurations distribution for OFF durations
     * @param startOn      initial ON/OFF state
     */
    public OnOffTrafficSource(int id,
                              ParetoHeavyTailDistribution onDurations,
                              ParetoHeavyTailDistribution offDurations,
                              boolean startOn) {

        if (id <= 0)
            throw new IllegalArgumentException("id must be >= 1");

        if (onDurations == null || offDurations == null)
            throw new NullPointerException("duration samplers must not be null");

        this.id = id;
        this.on = startOn;
        this.onDurations = onDurations;
        this.offDurations = offDurations;
    }

    /** @return ID of this source (1-based). */
    public int id() { return id; }

    /** @return true if source is currently ON. */
    public boolean isOn() { return on; }

    /** Set the ON/OFF state. */
    public void setOn(boolean value) { this.on = value; }

    /**
     * @return a sampled duration (from ON or OFF distribution depending on state).
     */
    public double nextDurationForCurrentState() {
        return on ? onDurations.sample() : offDurations.sample();
    }

    /**
     * Computes the absolute time of the next toggle relative to the given start time.
     */
    public double firstToggleAt(double startTime) {
        return startTime + nextDurationForCurrentState();
    }
}
