package com.fisayofinn.sim.sources;

import com.fisayofinn.sim.math.ParetoHeavyTailDistribution;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class OnOffTrafficSourceTest {

    static class StubRandom extends Random {
        private final double[] v; private int i = 0;
        StubRandom(double... v){ this.v = v; }
        @Override public double nextDouble(){ return v[i++ % v.length]; }
    }

    @Test
    void usesCorrectSamplerPerState() {
        ParetoHeavyTailDistribution on  = new ParetoHeavyTailDistribution(2.0, 1.0, new StubRandom(0.25)); // 2.0
        ParetoHeavyTailDistribution off = new ParetoHeavyTailDistribution(2.0, 1.0, new StubRandom(0.5));  // 1.4142...
        OnOffTrafficSource s = new OnOffTrafficSource(1, on, off, true);

        assertTrue(s.isOn());
        assertEquals(2.0, s.nextDurationForCurrentState(), 1e-12);

        s.setOn(false);
        assertEquals(Math.sqrt(2.0), s.nextDurationForCurrentState(), 1e-12);
    }
}
