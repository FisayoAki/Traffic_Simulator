package com.fisayofinn.sim.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores (time, activeSources) samples.
 */
public class AggregateTimeSeries {
    private final ArrayList<Double> times = new ArrayList<>();
    private final ArrayList<Integer> values = new ArrayList<>();

    public void add(double time, int active) {
        times.add(time);
        values.add(active);
    }

    public List<Double> times() {
        return Collections.unmodifiableList(times);
    }

    public List<Integer> values() {
        return Collections.unmodifiableList(values);
    }

    public int size() {
        return times.size();
    }

    public boolean isEmpty() {
        return times.isEmpty();
    }

    /**
     * Write the series to a CSV file with header.
     */
    public void writeCsv(Path path) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(path)) {
            out.write("time,active");
            out.newLine();
            for (int i = 0; i < times.size(); i++) {
                out.write(times.get(i) + "," + values.get(i));
                out.newLine();
            }
        }
    }
}
