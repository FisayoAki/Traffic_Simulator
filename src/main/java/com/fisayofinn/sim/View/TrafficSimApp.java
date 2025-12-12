package com.fisayofinn.sim.View;

import com.fisayofinn.sim.Model.AggregateTimeSeries;
import com.fisayofinn.sim.Controller.TrafficSimulationEngine;

import java.nio.file.Paths;
import java.util.Scanner;

public class TrafficSimApp {

    static void main() {
        Scanner in = new Scanner(System.in);

        boolean running = true; // Controls the main menu loop
        while (running) {
            System.out.println();
            System.out.println("=== On/Off Traffic Simulator ===");
            System.out.println("R - Run simulation");
            System.out.println("Q - Quit");
            System.out.print("Choice [R/Q]: ");

            String choice = in.nextLine().trim(); // Read user menu choice

            // Run a simulation
            if (choice.isEmpty() || choice.equalsIgnoreCase("r")) {

                // Read simulation parameters
                double endTime = readPositiveDouble(in);
                int sources = readPositiveInt(in);

                // Ask user if verbose logging should be enabled
                boolean verbose = readYesNo(in,
                        "Enable verbose event logging? [y/N]: "
                );

                // Create and execute the simulation
                TrafficSimulationEngine sim = new TrafficSimulationEngine(endTime, sources, verbose);
                sim.run();

                // Retrieve the recorded time-series results
                AggregateTimeSeries series = sim.getSeries();

                // Print the time series to the console
                System.out.println();
                System.out.println("time\tactive");
                for (int i = 0; i < series.size(); i++) {
                    System.out.println(series.times().get(i) + "\t" + series.values().get(i));
                }

                // Print summary statistics
                System.out.println();
                System.out.println("Finished at t=" + sim.getSimTime());
                System.out.println("Final active sources: " + sim.getActiveSources());
                System.out.println("Samples: " + series.size());
                System.out.println("Peak active: " + sim.getPeakActiveSources());
                System.out.println("Average active: " + sim.getAverageActiveSources());

                // Ask user if they want to export results to CSV
                boolean export = readYesNo(in,
                        "Export results to CSV file? [y/N]: "
                );
                if (export) {
                    System.out.print("Enter CSV file name: ");
                    String fileName = in.nextLine().trim();

                    // Only export if filename not empty
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

                // Quit program
            } else if (choice.equalsIgnoreCase("q") || choice.equalsIgnoreCase("quit")) {
                running = false;

                // Invalid input
            } else {
                System.out.println("Unknown choice: " + choice);
            }
        }

        System.out.println("Goodbye.");
    }

    // Reads a positive double from the console. Returns default 10.0 if the user just presses Enter
    private static double readPositiveDouble(Scanner in) {
        while (true) {
            System.out.print("Enter simulation end time (seconds) [default 10.0]: ");
            String line = in.nextLine().trim();

            if (line.isEmpty()) {
                return 10.0; // default end time
            }

            try {
                double v = Double.parseDouble(line);
                if (v > 0) {
                    return v; // valid, positive time
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a valid number, try again.");
            }
        }
    }

    //Reads a positive integer from the console. Returns default 3 if the user just presses Enter.
    private static int readPositiveInt(Scanner in) {
        while (true) {
            System.out.print("Enter number of sources [default 3]: ");
            String line = in.nextLine().trim();

            if (line.isEmpty()) {
                return 3; // default number of sources
            }

            try {
                int v = Integer.parseInt(line);
                if (v >= 1) {
                    return v; // valid source count
                } else {
                    System.out.println("Please enter an integer >= 1.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a valid integer, try again.");
            }
        }
    }

    // Reads a yes/no response. Returns false by default. Accepts: y, yes, n, no (case-insensitive)
    private static boolean readYesNo(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();

            if (line.isEmpty()) {
                return false; // default is "no"
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
