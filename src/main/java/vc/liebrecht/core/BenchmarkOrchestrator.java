package vc.liebrecht.core;

import vc.liebrecht.config.BenchmarkConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final List<BenchmarkResult> _results;

    /**
     * Constructs a new benchmark orchestrator.
     * <p>
     * Initializes an empty map for storing benchmark results.
     */
    public BenchmarkOrchestrator() {
        _results = new ArrayList<>();
    }

    /**
     * Runs benchmarks for both executor types.
     * <p>
     * Executes the configured number of runs, where each run is performed with both
     * a thread pool and virtual threads. The first run (index 0) is a dry run and its
     * results are not included in the statistics to account for JVM warm-up effects.
     * Results are collected in {@code BenchmarkStatistics} objects and printed at the end,
     * along with the configuration details.
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
            // We need numProducers + numConsumers threads to cover all producers and consumers
            ExecutorService pool = Executors.newFixedThreadPool(config.getProducers() + config.getConsumers());
            long poolDuration = bm.run(pool);
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.MINUTES);
            if (!isDryRun) statisticsPool.addDuration(poolDuration);

            ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
            long virtualDuration = bm.run(virtual);
            virtual.shutdown();
            virtual.awaitTermination(2, TimeUnit.MINUTES);
            if (!isDryRun) statisticsVirtual.addDuration(virtualDuration);
            System.out.format("=== %d. run completed ===\n", i + 1);
        }

        _results.add(new BenchmarkResult(config, statisticsPool, statisticsVirtual));

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
    public List<BenchmarkResult> getResults() {
        return _results;
    }
}
