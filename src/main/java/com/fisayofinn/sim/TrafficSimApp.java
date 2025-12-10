package com.fisayofinn.sim;

import com.fisayofinn.sim.core.AggregateTimeSeries;
import com.fisayofinn.sim.core.TrafficSimulationEngine;

import java.util.Scanner;

public class TrafficSimApp {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("=== On/Off Traffic Simulator ===");
            System.out.println("R - Run simulation");
            System.out.println("Q - Quit");
            System.out.print("Choice [R/Q]: ");

            String choice = in.nextLine().trim();

            if (choice.isEmpty() || choice.equalsIgnoreCase("r")) {
                double endTime = readPositiveDouble(in,
                        "Enter simulation end time (seconds) [default 10.0]: ",
                        10.0);

                int sources = readPositiveInt(in,
                        "Enter number of sources [default 3]: ",
                        3);

                TrafficSimulationEngine sim = new TrafficSimulationEngine(endTime, sources);
                sim.run();

                AggregateTimeSeries series = sim.getSeries();

                System.out.println();
                System.out.println("time\tactive");
                for (int i = 0; i < series.size(); i++) {
                    System.out.println(series.times().get(i) + "\t" + series.values().get(i));
                }

                System.out.println();
                System.out.println("Finished at t=" + sim.getSimTime());
                System.out.println("Final active sources: " + sim.getActiveSources());
                System.out.println("Samples: " + series.size());
                System.out.println("Peak active: " + sim.getPeakActiveSources());
                System.out.println("Average active: " + sim.getAverageActiveSources());

            } else if (choice.equalsIgnoreCase("q") || choice.equalsIgnoreCase("quit")) {
                running = false;
            } else {
                System.out.println("Unknown choice: " + choice);
            }
        }

        System.out.println("Goodbye.");
    }

    private static double readPositiveDouble(Scanner in, String prompt, double defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                return defaultValue;
            }
            try {
                double v = Double.parseDouble(line);
                if (v > 0) {
                    return v;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number, try again.");
            }
        }
    }

    private static int readPositiveInt(Scanner in, String prompt, int defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                return defaultValue;
            }
            try {
                int v = Integer.parseInt(line);
                if (v >= 1) {
                    return v;
                } else {
                    System.out.println("Please enter an integer >= 1.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a valid integer, try again.");
            }
        }
    }
}
