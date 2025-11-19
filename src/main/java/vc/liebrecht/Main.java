package vc.liebrecht;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.core.BenchmarkOrchestrator;

/**
 * Main class for the Producer-Consumer-Benchmark.
 */
public class Main {
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