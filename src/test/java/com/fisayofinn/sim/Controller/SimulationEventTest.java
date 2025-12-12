package com.fisayofinn.sim.Controller;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationEventTest {

    @Test
    public void compareToOrdersByTimeThenUserId() {
        SimulationEvent e1 = new SimulationEvent(1.0, SimulationEventType.ON, 2);
        SimulationEvent e2 = new SimulationEvent(1.0, SimulationEventType.ON, 1);
        SimulationEvent e3 = new SimulationEvent(0.5, SimulationEventType.ON, 3);

        List<SimulationEvent> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);
        list.add(e3);

        Collections.sort(list);

        assertEquals(e3, list.get(0));
        assertEquals(e2, list.get(1));
        assertEquals(e1, list.get(2));
    }
}
