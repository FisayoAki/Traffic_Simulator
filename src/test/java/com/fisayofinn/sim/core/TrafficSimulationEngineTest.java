package com.fisayofinn.sim.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrafficSimulationEngineTest {

    @Test
    public void singleSourceProducesSomeSamples() {
        TrafficSimulationEngine engine = new TrafficSimulationEngine(5.0, 1);
        engine.run();

        AggregateTimeSeries series = engine.getSeries();
        assertFalse(series.isEmpty(), "series should not be empty");

        double prev = -1.0;
        for (double t : series.times()) {
            assertTrue(t >= 0.0);
            assertTrue(t > prev);
            prev = t;
        }
    }

    @Test
    public void peakIsAtLeastAverage() {
        TrafficSimulationEngine engine = new TrafficSimulationEngine(10.0, 3);
        engine.run();

        int peak = engine.getPeakActiveSources();
        double avg = engine.getAverageActiveSources();

        assertTrue(peak >= avg);
    }
}
