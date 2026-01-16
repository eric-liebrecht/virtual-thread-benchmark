package vc.liebrecht;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.consumer.ConsumerType;
import vc.liebrecht.engine.BenchmarkOrchestrator;
import vc.liebrecht.engine.BenchmarkResult;

/**
 * Main class for the Producer-Consumer-Benchmark.
 * <p>
 * This class serves as the entry point for the benchmark application. It parses
 * command-line arguments, creates a {@code BenchmarkOrchestrator}, and executes
 * the benchmarks with the configured parameters.
 */
public class Main {
	/**
	 * Entry point of the application.
	 * <p>
	 * Creates a {@code BenchmarkOrchestrator} and runs benchmarks with multiple
	 * predefined configurations. Each configuration is executed sequentially.
	 * After all benchmarks are completed, the results are written to a file named
	 * {@code results.txt}. If an error occurs, an error message is printed and the
	 * program exits with code 1.
	 *
	 * @param args Command-line arguments (currently unused, configurations are
	 *             hardcoded)
	 */
	public static void main(String[] args) {
		try {
			BenchmarkOrchestrator orchestrator = new BenchmarkOrchestrator();
			List<BenchmarkConfig> configs = List.of(

					// Lightweight Consumers – geringe Systemlast
					new BenchmarkConfig(1, 1, ConsumerType.LIGHTWEIGHT, 50, 16, 501),      // Config L1
					new BenchmarkConfig(10, 1, ConsumerType.LIGHTWEIGHT, 50, 16, 251),     // Config L2

					// Lightweight Consumers – mittlere Systemlast
					new BenchmarkConfig(1, 1, ConsumerType.LIGHTWEIGHT, 10_000, 16, 51),   // Config L3
					new BenchmarkConfig(50, 1, ConsumerType.LIGHTWEIGHT, 10_000, 16, 31),  // Config L4
					new BenchmarkConfig(500, 1, ConsumerType.LIGHTWEIGHT, 10_000, 16, 21), // Config L5

					// Lightweight Consumers – hohe Systemlast
					new BenchmarkConfig(1_000, 1, ConsumerType.LIGHTWEIGHT, 100_000, 16, 21), // Config L6

					// Lightweight Consumers – hohe Parallelität
					new BenchmarkConfig(100, 100, ConsumerType.LIGHTWEIGHT, 10_000, 16, 21),   // Config L7
					new BenchmarkConfig(500, 500, ConsumerType.LIGHTWEIGHT, 10_000, 16, 21),   // Config L8
					new BenchmarkConfig(1_000, 1_000, ConsumerType.LIGHTWEIGHT, 5_000, 16, 21),// Config L9

					// Heavy Consumers – geringe Systemlast
					new BenchmarkConfig(1, 1, ConsumerType.HEAVY, 500, 16_384, 101),  // Config H1
					new BenchmarkConfig(10, 1, ConsumerType.HEAVY, 500, 16_384, 51),  // Config H2

					// Heavy Consumers – mittlere Systemlast
					new BenchmarkConfig(1, 1, ConsumerType.HEAVY, 20_000, 16_384, 21), // Config H3
					new BenchmarkConfig(10, 1, ConsumerType.HEAVY, 20_000, 16_384, 21),// Config H4

					// Heavy Consumers – hohe Systemlast
					new BenchmarkConfig(1, 1, ConsumerType.HEAVY, 200_000, 16_384, 21), // Config H5
					new BenchmarkConfig(20, 1, ConsumerType.HEAVY, 200_000, 16_384, 21) // Config H6
			);

			for (BenchmarkConfig config : configs) {
				orchestrator.runBenchmarks(config);
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
				for (BenchmarkResult result : orchestrator.getResults()) {
					writer.write(result + "\n\n");
				}
			}

		} catch (Exception e) {
			System.err.println("Fehler beim Ausführen des Benchmarks:");
			e.printStackTrace();
			System.exit(1);
		}
	}
}