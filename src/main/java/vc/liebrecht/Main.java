package vc.liebrecht;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.consumer.ConsumerType;
import vc.liebrecht.core.BenchmarkOrchestrator;
import vc.liebrecht.core.BenchmarkResult;

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
	 * If an error occurs, an error message is printed and the program exits with
	 * code 1.
	 *
	 * @param args Command-line arguments (currently unused, configurations are
	 *             hardcoded)
	 */
	public static void main(String[] args) {
		try {
			BenchmarkOrchestrator orchestrator = new BenchmarkOrchestrator();
			List<BenchmarkConfig> configs = List.of(
					new BenchmarkConfig(1, 1, ConsumerType.LIGHTWEIGHT, 50, 16, 1001),
					new BenchmarkConfig(10, 1, ConsumerType.LIGHTWEIGHT, 50, 16, 501),
					new BenchmarkConfig(1, 1, ConsumerType.LIGHTWEIGHT, 10_000, 16, 101),
					new BenchmarkConfig(50, 1, ConsumerType.LIGHTWEIGHT, 10_000, 16, 51),
					new BenchmarkConfig(500, 1, ConsumerType.LIGHTWEIGHT, 10_000, 16, 21));

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