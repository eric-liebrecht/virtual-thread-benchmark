package vc.liebrecht.engine;

import vc.liebrecht.config.BenchmarkConfig;

public class BenchmarkResult {
    private final BenchmarkConfig config;
    private final BenchmarkStatistics platformStats;
    private final BenchmarkStatistics virtualStats;

    public BenchmarkResult(BenchmarkConfig config,
                           BenchmarkStatistics platformStats,
                           BenchmarkStatistics virtualStats) {
        this.config = config;
        this.platformStats = platformStats;
        this.virtualStats = virtualStats;
    }

    public BenchmarkConfig getConfig() {
        return config;
    }

    public BenchmarkStatistics getPlatformStats() {
        return platformStats;
    }

    public BenchmarkStatistics getVirtualStats() {
        return virtualStats;
    }

    @Override
    public String toString() {
        return """
                BenchmarkResult {
                    config = %s
                    platformStats = %s
                    virtualStats = %s
                }
                """
                .formatted(
                        this.getConfig(),
                        this.getPlatformStats(),
                        this.getVirtualStats()
                );
    }
}