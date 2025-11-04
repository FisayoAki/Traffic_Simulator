package com.fisayofinn.sim.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimulationManagerTest {

    @Test
    void endsWithoutProcessingEventsWhenEndTimeIsBeforeFirstToggle() {
        double endTime = 0.2;      // strictly less than any first OFF->ON toggle time
        int sources = 3;           // any number; all start OFF in your constructor

        SimulationManager sim = new SimulationManager(endTime, sources);
        sim.simLoop();

        // Since first toggle for every source is > 0.2, nothing fires.
        assertEquals(endTime, sim.getSimTime(), 1e-12);
        assertEquals(0, sim.getActiveSources());
    }
}
