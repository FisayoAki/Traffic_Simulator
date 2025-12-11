package com.fisayofinn.sim;

import com.fisayofinn.sim.core.AggregateTimeSeries;
import com.fisayofinn.sim.core.TrafficSimulationEngine;

import java.nio.file.Paths;
import java.util.Scanner;

public class TrafficSimApp {

    static void main() {
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
                double endTime = readPositiveDouble(in
                );

                int sources = readPositiveInt(in
                );

                boolean verbose = readYesNo(in,
                        "Enable verbose event logging? [y/N]: "
                );

                TrafficSimulationEngine sim = new TrafficSimulationEngine(endTime, sources, verbose);
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

                boolean export = readYesNo(in,
                        "Export results to CSV file? [y/N]: "
                );
                if (export) {
                    System.out.print("Enter CSV file name: ");
                    String fileName = in.nextLine().trim();
                    if (!fileName.isEmpty()) {
                        try {
                            series.writeCsv(Paths.get(fileName));
                            System.out.println("Wrote CSV to " + fileName);
                        } catch (Exception e) {
                            System.out.println("Error writing CSV: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No file name given, not exporting.");
                    }
                }

            } else if (choice.equalsIgnoreCase("q") || choice.equalsIgnoreCase("quit")) {
                running = false;
            } else {
                System.out.println("Unknown choice: " + choice);
            }
        }

        System.out.println("Goodbye.");
    }

    private static double readPositiveDouble(Scanner in) {
        while (true) {
            System.out.print("Enter simulation end time (seconds) [default 10.0]: ");
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                return 10.0;
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

    private static int readPositiveInt(Scanner in) {
        while (true) {
            System.out.print("Enter number of sources [default 3]: ");
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                return 3;
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

    private static boolean readYesNo(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                return false;
            }
            if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
                return true;
            }
            if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }
}
