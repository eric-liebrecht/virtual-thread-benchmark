package vc.liebrecht;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.consumer.ConsumerType;
import vc.liebrecht.core.BenchmarkOrchestrator;

import java.util.List;

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
     * If an error occurs, an error message is printed and the program exits with code 1.
     *
     * @param args Command-line arguments (currently unused, configurations are hardcoded)
     */
    public static void main(String[] args) {
        try {
            BenchmarkOrchestrator orchestrator = new BenchmarkOrchestrator();
            List<BenchmarkConfig> configs = List.of(
                    new BenchmarkConfig(10, 1, ConsumerType.HEAVY, 500, 16, 21),
                    new BenchmarkConfig(500, 1, ConsumerType.HEAVY, 100, 16, 21),
                    new BenchmarkConfig(5_000, 1, ConsumerType.HEAVY, 50, 16, 21)
            );

            for (BenchmarkConfig config : configs) {
                orchestrator.runBenchmarks(config);
            }

        } catch (Exception e) {
            System.err.println("Fehler beim Ausführen des Benchmarks:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}