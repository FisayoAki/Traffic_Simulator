package com.fisayofinn.sim.math;

import java.util.Random;

/** Pareto(alpha, xm) sampler via inverse transform. */
public final class HeavyTail {
    private final double alpha, xm;
    private final Random rng;

    public HeavyTail(double alpha, double xm, Random rng) {
        if (alpha <= 0.0) throw new IllegalArgumentException("alpha must be > 0");
        if (xm <= 0.0)    throw new IllegalArgumentException("xm must be > 0");
        if (rng == null)  throw new NullPointerException("rng must not be null");
        this.alpha = alpha;
        this.xm = xm;
        this.rng = rng;
    }

    /** A single Pareto sample (always >= xm). */
    public double sample() {
        double u = rng.nextDouble();           // U in [0,1)
        if (u <= 0.0) u = Double.MIN_VALUE;    // avoid 0 exactly
        return xm / Math.pow(u, 1.0 / alpha);
    }
}