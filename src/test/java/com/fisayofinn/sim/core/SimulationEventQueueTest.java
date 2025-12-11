package com.fisayofinn.sim.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationEventQueueTest {

    @Test
    public void queueReturnsEventsInOrder() {
        SimulationEventQueue q = new SimulationEventQueue();

        SimulationEvent e1 = new SimulationEvent(1.0, SimulationEventType.ON, 2);
        SimulationEvent e2 = new SimulationEvent(1.0, SimulationEventType.ON, 1);
        SimulationEvent e3 = new SimulationEvent(0.5, SimulationEventType.ON, 3);

        q.addEvent(e1);
        q.addEvent(e2);
        q.addEvent(e3);

        assertEquals(e3, q.retrieveEvent());
        assertEquals(e2, q.retrieveEvent());
        assertEquals(e1, q.retrieveEvent());
        assertTrue(q.isEmpty());
    }
}
