package com.fisayofinn.sim;

import com.fisayofinn.sim.core.TrafficSimulationEngine;

import java.util.Scanner;

public class TrafficSimApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter simulation end time (seconds) [default 10.0]: ");
        String endInput = scanner.nextLine().trim();
        double endTime = endInput.isEmpty()
                ? 10.0
                : Double.parseDouble(endInput);

        System.out.print("Enter number of sources [default 3]: ");
        String srcInput = scanner.nextLine().trim();
        int sources = srcInput.isEmpty()
                ? 3
                : Integer.parseInt(srcInput);

        TrafficSimulationEngine sim = new TrafficSimulationEngine(endTime, sources);
        sim.run();

        System.out.println("Done at t=" + sim.getSimTime() +
                ", active=" + sim.getActiveSources() +
                ", points=" + sim.getSeries().size());
    }
}
