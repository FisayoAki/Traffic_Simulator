package com.fisayofinn.sim.core;

import com.fisayofinn.sim.math.ParetoHeavyTailDistribution;
import com.fisayofinn.sim.sources.OnOffTrafficSource;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class TrafficSimulationEngineTest {

    static class StubRandom extends Random {
        private final double[] v; private int i = 0;
        StubRandom(double... v){ this.v = v; }
        @Override public double nextDouble(){ return v[i++ % v.length]; }
    }

    @Test
    void endsBeforeFirstToggle() {
        TrafficSimulationEngine sim = new TrafficSimulationEngine(0.2, 3);
        sim.run();
        assertEquals(0.2, sim.getSimTime(), 1e-12);
        assertEquals(0, sim.getActiveSources());
    }

    @Test
    void processesDeterministicOnThenOff() {
        ParetoHeavyTailDistribution on  = new ParetoHeavyTailDistribution(2.0, 1.0, new StubRandom(0.25));
        ParetoHeavyTailDistribution off = new ParetoHeavyTailDistribution(2.0, 1.0, new StubRandom(0.5));
        OnOffTrafficSource s = new OnOffTrafficSource(1, on, off, false);

        ArrayList<OnOffTrafficSource> list = new ArrayList<>();
        list.add(s);

        SimulationEventQueue q = new SimulationEventQueue();
        TrafficSimulationEngine sim = new TrafficSimulationEngine(4.0, list, q);
        sim.run();

        assertEquals(4.0, sim.getSimTime(), 1e-12);
        assertTrue(sim.getSeries().size() >= 2); // ON then OFF recorded
        assertTrue(sim.getActiveSources() == 0 || sim.getActiveSources() == 1);
    }
}
