package vc.liebrecht.core;

import vc.liebrecht.config.BenchmarkConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Orchestrates the execution of benchmarks for different executor types.
 * <p>
 * This class runs benchmarks for both {@code ExecutorType.ThreadPool} and
 * {@code ExecutorType.VirtualThreads} and collects the results in
 * {@code BenchmarkStatistics} objects. The results are printed after all runs
 * are completed.
 */
public class BenchmarkOrchestrator {
    private final Map<ExecutorType, BenchmarkStatistics> _results;

    /**
     * Constructs a new benchmark orchestrator.
     * <p>
     * Initializes an empty map for storing benchmark results.
     */
    public BenchmarkOrchestrator() {
        _results = new HashMap<>();
    }

    /**
     * Runs benchmarks for both executor types.
     * <p>
     * Executes the configured number of runs, where each run is performed with both
     * a thread pool and virtual threads. Results are collected in {@code BenchmarkStatistics}
     * objects and printed at the end.
     *
     * @param config The benchmark configuration with all necessary parameters
     * @throws InterruptedException If a thread is interrupted during execution
     */
    public void runBenchmarks(BenchmarkConfig config) throws InterruptedException {
        BenchmarkStatistics statisticsPool = new BenchmarkStatistics(ExecutorType.ThreadPool);
        BenchmarkStatistics statisticsVirtual = new BenchmarkStatistics(ExecutorType.VirtualThreads);

        System.out.println("=== Benchmark started ===");

        Benchmark bm = new Benchmark(config);

        for (int i = 0; i < config.getRuns(); i++) {
            boolean isDryRun = i == 0;
            // We need numProducers + 1 threads to cover the consumer
            ExecutorService pool = Executors.newFixedThreadPool(config.getProducers() + 1);
            long poolDuration = bm.run(pool);
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.MINUTES);
            if (!isDryRun) statisticsPool.addDuration(poolDuration);

            ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
            long virtualDuration = bm.run(virtual);
            virtual.shutdown();
            virtual.awaitTermination(2, TimeUnit.MINUTES);
            if (!isDryRun) statisticsVirtual.addDuration(virtualDuration);
            System.out.printf("\n=== %d. Run completed ===", i + 1);
        }

        _results.put(ExecutorType.ThreadPool, statisticsPool);
        _results.put(ExecutorType.VirtualThreads, statisticsVirtual);

        System.out.println("\n\n=== Benchmark completed successfully ===\n");

        System.out.println(config);
        System.out.println("-------------------------------------------\n");
        System.out.println(statisticsPool);
        System.out.println("-------------------------------------------\n");
        System.out.println(statisticsVirtual);
    }

    /**
     * Returns the collected benchmark results.
     * <p>
     * The map contains the corresponding {@code BenchmarkStatistics} for each
     * {@code ExecutorType} with all measured values.
     *
     * @return A map with benchmark results for each executor type
     */
    public Map<ExecutorType, BenchmarkStatistics> getResults() {
        return _results;
    }
}
