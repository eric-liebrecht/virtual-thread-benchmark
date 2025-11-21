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
                    // 1) Small scale – LIGHTWEIGHT
                    new BenchmarkConfig(10, 2, ConsumerType.LIGHTWEIGHT, 500, 16, 21),

                    // 2) Small scale – HEAVY
                    new BenchmarkConfig(10, 1, ConsumerType.HEAVY, 500, 16, 21),

                    // 3) Mid scale – LIGHTWEIGHT
                    new BenchmarkConfig(500, 1, ConsumerType.LIGHTWEIGHT, 100, 16, 21),

                    // 4) Mid scale – HEAVY
                    new BenchmarkConfig(500, 1, ConsumerType.HEAVY, 100, 16, 21),

                    // 5) Large scale – LIGHTWEIGHT
                    new BenchmarkConfig(5_000, 1, ConsumerType.LIGHTWEIGHT, 50, 16, 21),

                    // 6) Large scale – HEAVY
                    new BenchmarkConfig(5_000, 1, ConsumerType.HEAVY, 50, 16, 21),

                    // 7) Multicore heavy workload: 8 consumers
                    new BenchmarkConfig(100, 8, ConsumerType.HEAVY, 200, 16, 21),

                    // 8) Higher load, 8 consumers – HEAVY
                    new BenchmarkConfig(500, 8, ConsumerType.HEAVY, 100, 16, 21),

                    // 9) I/O simulation – LIGHTWEIGHT + Sleep (2ms)
                    // (Sleep machst du im Consumer selbst)
                    new BenchmarkConfig(500, 1, ConsumerType.LIGHTWEIGHT, 200, 16, 21),

                    // 10) Extreme I/O simulation – LIGHTWEIGHT + Sleep (2ms)
                    new BenchmarkConfig(5_000, 1, ConsumerType.LIGHTWEIGHT, 100, 16, 21),

                    // 11) Large payload – HEAVY
                    new BenchmarkConfig(100, 1, ConsumerType.HEAVY, 100, 1_000_000, 21),

                    // 12) Large payload parallel – HEAVY + 8 consumers
                    new BenchmarkConfig(500, 8, ConsumerType.HEAVY, 100, 1_000_000, 21)
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