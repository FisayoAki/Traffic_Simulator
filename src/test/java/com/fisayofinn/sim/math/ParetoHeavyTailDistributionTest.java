package com.fisayofinn.sim.math;

import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ParetoHeavyTailDistributionTest {

    // Deterministic stub for nextDouble()
    static final class StubRandom extends Random {
        private final double[] vals; private int i = 0;
        StubRandom(double... vals) { this.vals = vals; }
        @Override public double nextDouble() { return vals[i++ % vals.length]; }
    }

    @Test
    void samplesAreAtLeastXmAndReproducible() {
        var a = new ParetoHeavyTailDistribution(1.5, 0.5, new Random(42));
        var b = new ParetoHeavyTailDistribution(1.5, 0.5, new Random(42));
        for (int i = 0; i < 20; i++) {
            double xa = a.sample(), xb = b.sample();
            assertTrue(xa >= 0.5, "sample >= xm");
            assertEquals(xa, xb, 0.0, "same seed -> same sequence");
        }
    }

    @Test
    void inverseMatchesKnownUniforms() {
        var d = new ParetoHeavyTailDistribution(2.0, 1.0, new StubRandom(0.25, 0.5));
        assertEquals(2.0, d.sample(), 1e-12);            // 1/sqrt(0.25)
        assertEquals(Math.sqrt(2.0), d.sample(), 1e-12); // 1/sqrt(0.5)
    }
}
