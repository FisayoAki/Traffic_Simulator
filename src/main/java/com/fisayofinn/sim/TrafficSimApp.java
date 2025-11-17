package com.fisayofinn.sim;

import com.fisayofinn.sim.core.TrafficSimulationEngine;

public class TrafficSimApp {
    public static void main(String[] args) {
        double endTime = (args.length > 0) ? Double.parseDouble(args[0]) : 10.0;
        int sources    = (args.length > 1) ? Integer.parseInt(args[1]) : 3;

        TrafficSimulationEngine sim = new TrafficSimulationEngine(endTime, sources);
        sim.run();

        System.out.println("Done at t=" + sim.getSimTime() +
                ", active=" + sim.getActiveSources() +
                ", points=" + sim.getSeries().size());
    }
}
