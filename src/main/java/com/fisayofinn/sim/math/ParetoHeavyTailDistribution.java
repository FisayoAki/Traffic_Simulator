package com.fisayofinn.sim.math;

import java.util.Random;

/**
 * Simple implementation of a Pareto(alpha, xm) heavy-tailed distribution
 * using inverse transform sampling.
 */
public final class ParetoHeavyTailDistribution {

    private final double alpha; // Shape parameter
    private final double xm;    // Scale/minimum parameter
    private final Random rng;   // Random generator

    /**
     * @param alpha shape parameter (> 0)
     * @param xm    minimum value (> 0)
     * @param rng   random number generator
     */
    public ParetoHeavyTailDistribution(double alpha, double xm, Random rng) {
        if (alpha <= 0.0) throw new IllegalArgumentException("alpha must be > 0");
        if (xm <= 0.0)    throw new IllegalArgumentException("xm must be > 0");
        if (rng == null)  throw new NullPointerException("rng must not be null");

        this.alpha = alpha;
        this.xm = xm;
        this.rng = rng;
    }

    /**
     * Generates a single Pareto-distributed sample.
     * Formula: X = xm / U^(1/alpha)
     * U is uniform in (0,1).
     */
    public double sample() {
        double u = rng.nextDouble();       // uniform in [0,1)
        if (u <= 0.0) u = Double.MIN_VALUE; // guard against exact 0
        return xm / Math.pow(u, 1.0 / alpha);
    }
}
