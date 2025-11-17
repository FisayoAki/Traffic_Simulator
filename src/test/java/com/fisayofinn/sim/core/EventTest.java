package com.fisayofinn.sim.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Event class test for verifying that:
 * 1. The compareTo() method orders events correctly
 * 2. The toString() method returns the specified string
 * */

public class EventTest {
    @Test
    public void testEventComparison() {
        Event e1 = new Event(1.61, SimulationEventType.ON, 2);
        Event e2 = new Event(2.53, SimulationEventType.OFF, 3);
        Event e3 = new Event(2.53, SimulationEventType.ON, 1);
        Event e4 = new Event(3.02, SimulationEventType.OFF, 4);

        assertTrue(e1.compareTo(e2) < 0, "e1 should occur before e2");
        assertTrue(e2.compareTo(e4) < 0, "e2 should occur before e4");
        assertTrue(e1.compareTo(e4) < 0, "e1 should occur before e4");

        // Tied times use userID
        assertTrue(e3.compareTo(e2) < 0, "e3 should occur before e2 since same time but lower userID");
    }

    @Test
    public void testToString() {
        Event e = new Event(1.0, SimulationEventType.ON, 5);
        assertTrue(e.toString().contains("Event"));
    }
}
