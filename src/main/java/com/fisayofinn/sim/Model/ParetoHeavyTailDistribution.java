package com.fisayofinn.sim.Model;

import java.util.Random;

/**
 * Simple Pareto heavy-tail distribution using inverse CDF.
 */
public final class ParetoHeavyTailDistribution {

    private final double alpha;
    private final double xm;
    private final Random rng;

    public ParetoHeavyTailDistribution(double alpha, double xm, Random rng) {
        if (alpha <= 0.0) {
            throw new IllegalArgumentException("alpha must be > 0");
        }
        if (xm <= 0.0) {
            throw new IllegalArgumentException("xm must be > 0");
        }
        if (rng == null) {
            throw new NullPointerException("rng must not be null");
        }
        this.alpha = alpha;
        this.xm = xm;
        this.rng = rng;
    }

    /**
     * Draw a single sample. Result is always >= xm.
     */
    public double sample() {
        double u = rng.nextDouble(); // in [0,1)
        if (u <= 0.0) {
            u = Double.MIN_VALUE; // avoid division by zero
        }
        return xm / Math.pow(u, 1.0 / alpha);
    }
}
