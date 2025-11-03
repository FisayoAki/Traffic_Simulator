package com.fisayofinn.sim.math;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class HeavyTailTest {
    @Test
    void samplesAreAtLeastXmAndReproducible() {
        HeavyTail a = new HeavyTail(1.5, 0.5, new Random(42));
        HeavyTail b = new HeavyTail(1.5, 0.5, new Random(42));

        for (int i = 0; i < 100; i++) {
            double xa = a.sample();
            double xb = b.sample();
            assertTrue(xa >= 0.5, "Pareto sample must be >= xm");
            assertEquals(xa, xb, 0.0, "Same seed => same sequence");
        }
    }
}
