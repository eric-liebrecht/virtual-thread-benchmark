package vc.liebrecht.core;

import vc.liebrecht.config.BenchmarkConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BenchmarkOrchestrator {
    private final Map<ExecutorType, BenchmarkStatistics> _results;

    public BenchmarkOrchestrator() {
        _results = new HashMap<>();
    }

    public void runBenchmarks(BenchmarkConfig config) throws InterruptedException {
        BenchmarkStatistics statisticsPool = new BenchmarkStatistics(ExecutorType.ThreadPool);
        BenchmarkStatistics statisticsVirtual = new BenchmarkStatistics(ExecutorType.VirtualThreads);

        System.out.println("=== Benchmark gestartet ===");

        Benchmark bm = new Benchmark(config);

        for (int i = 0; i < config.getRuns(); i++) {
            // We need numProducers + 1 threads to cover the consumer
            ExecutorService pool = Executors.newFixedThreadPool(config.getProducers() + 1);
            long poolDuration = bm.run(pool);
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.MINUTES);
            statisticsPool.addDuration(poolDuration);

            ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor();
            long virtualDuration = bm.run(virtual);
            virtual.shutdown();
            virtual.awaitTermination(2, TimeUnit.MINUTES);
            statisticsVirtual.addDuration(virtualDuration);
            System.out.printf("\n=== %d. Durchlauf abgeschlossen ===", i + 1);
        }

        _results.put(ExecutorType.ThreadPool, statisticsPool);
        _results.put(ExecutorType.VirtualThreads, statisticsVirtual);

        System.out.println("=== Benchmark erfolgreich abgeschlossen ===\n");
        System.out.println(statisticsPool);
        System.out.println("-------------------------------------------\n");
        System.out.println(statisticsVirtual);
    }

    public Map<ExecutorType, BenchmarkStatistics> getResults() {
        return _results;
    }
}
