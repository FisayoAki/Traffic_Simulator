package com.fisayofinn.sim.Model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ParetoHeavyTailDistributionTest {

    @Test
    public void sampleIsAlwaysAtLeastXm() {
        Random rng = new Random(123);
        double xm = 0.5;
        ParetoHeavyTailDistribution dist =
                new ParetoHeavyTailDistribution(1.5, xm, rng);

        for (int i = 0; i < 200; i++) {
            double v = dist.sample();
            assertTrue(v >= xm, "Sample should be >= xm");
        }
    }

    @Test
    public void constructorRejectsBadParams() {
        Random rng = new Random();

        assertThrows(IllegalArgumentException.class,
                () -> new ParetoHeavyTailDistribution(0.0, 0.5, rng));

        assertThrows(IllegalArgumentException.class,
                () -> new ParetoHeavyTailDistribution(1.2, 0.0, rng));

        assertThrows(NullPointerException.class,
                () -> new ParetoHeavyTailDistribution(1.2, 0.5, null));
    }
}
