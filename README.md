# Telecom Traffic Simulator (Java)

This is a simple console-based Java program that simulates telecom-style traffic using many ON/OFF sources.  
Each source uses a heavy-tailed Pareto distribution for ON and OFF times, and when you add a lot of them together the traffic becomes bursty and roughly self-similar.  
The aim of the project is to demonstrate discrete-event simulation and generate data suitable for plotting and analysis.

---

## Features

- Console-only Java application
- User can choose simulation time, number of sources, and Pareto parameters
- ON/OFF sources with heavy-tailed durations
- Discrete-event simulation (time jumps to next event)
- Aggregated time-series output
- Optional verbose logging
- CSV export for plotting (Excel, Python, etc.)
- Input validation and simple error handling

---

## Requirements

- Java JDK 11 or newer
- A terminal
- (Optional) JUnit if running the test classes

---

## How to Compile and Run

If your `.java` files are in `src/`:

```bash
javac -d out $(find src -name "*.java")
java -cp out TrafficSimApp
```

If using packages, adjust the run command accordingly:

```bash
java -cp out sim.TrafficSimApp
```

The program will ask:

1. Simulation duration
2. Number of sources
3. Pareto α
4. Pareto xₘ
5. Verbose mode

Press Enter to use defaults if provided.

At the end it can export a CSV file containing:

```
time,active
0.0,42
0.5,47
...
```

---

## Example Parameters for Bursty Output

Good values for seeing clear burstiness:

- Simulation time: 5000
- Number of sources: 100
- Pareto α: 1.5
- Pareto xₘ: 1.0

Plotting the CSV in Excel over the whole duration and on a zoomed-in window usually shows the expected bursty/self-similar pattern.

---

## Main Classes (Summary)

- **TrafficSimApp** – User interaction and starting the simulation
- **TrafficSimulationEngine** – Runs the event loop, processes events
- **OnOffTrafficSource** – Represents a single ON/OFF source
- **ParetoHeavyTailDistribution** – Samples from the Pareto distribution
- **SimulationEvent / SimulationEventType** – Stores ON/OFF transition events
- **SimulationEventQueue** – Priority queue for events
- **AggregateTimeSeries** – Records traffic samples and writes CSV

---

## Notes / Limitations

- No GUI (intentionally a console application)
- The model is simplified: only ON/OFF behaviour and aggregate counts
- More advanced analysis (like estimating the Hurst parameter) would need external tools  

