package vc.liebrecht;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.core.BenchmarkOrchestrator;

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
     * Parses command-line arguments, creates a {@code BenchmarkOrchestrator}, and
     * runs the benchmarks. If an error occurs, an error message is printed and the
     * program exits with code 1.
     *
     * @param args Command-line arguments for benchmark configuration
     */
    public static void main(String[] args) {
        try {
            BenchmarkOrchestrator orchestrator = new BenchmarkOrchestrator();
            BenchmarkConfig config = BenchmarkConfig.parse(args);

            orchestrator.runBenchmarks(config);

        } catch (Exception e) {
            System.err.println("Fehler beim Ausführen des Benchmarks:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}