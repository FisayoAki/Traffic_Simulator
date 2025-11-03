package com.fisayofinn.sim.sources;

import com.fisayofinn.sim.math.HeavyTail;

/** Simple ON/OFF source; durations are heavy-tailed. */
public final class OnOffSource {
    private final int id;
    private final HeavyTail onDurations;
    private final HeavyTail offDurations;
    private boolean on;

    public OnOffSource(int id, HeavyTail onDurations, HeavyTail offDurations, boolean startOn) {
        if (id < 0) throw new IllegalArgumentException("id must be >= 0");
        if (onDurations == null || offDurations == null)
            throw new NullPointerException("duration samplers must not be null");
        this.id = id;
        this.onDurations = onDurations;
        this.offDurations = offDurations;
        this.on = startOn;
    }

    public int id() { return id; }
    public boolean isOn() { return on; }
    /** Engine calls this when processing an ON/OFF event. */
    public void setOn(boolean value) { this.on = value; }

    /** Duration until the next toggle given the current state (no mutation). */
    public double nextDurationForCurrentState() {
        return on ? onDurations.sample() : offDurations.sample();
    }

    /** First toggle time from a start time . */
    public double firstToggleAt(double startTime) {
        return startTime + nextDurationForCurrentState();
    }
}
