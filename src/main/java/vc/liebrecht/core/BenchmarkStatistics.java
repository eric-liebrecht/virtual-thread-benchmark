package vc.liebrecht.core;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkStatistics {
    private final List<Long> _durationsNano;
    private final ExecutorType _executorType;

    public BenchmarkStatistics(final ExecutorType type) {
        _executorType = type;
        _durationsNano = new ArrayList<>();
    }

    public void addDuration(final long nano) {
        _durationsNano.add(nano);
    }

    public long totalDuration() {
        return _durationsNano.stream().reduce(0L, Long::sum);
    }

    public long minDurationMs() {
        return (long) (_durationsNano.stream().min(Long::compareTo).get() / 1_000_000.0);
    }

    public long maxDurationMs() {
        return (long) (_durationsNano.stream().max(Long::compareTo).get() / 1_000_000.0);
    }

    public double averageDurationMs() {
        return (this.totalDuration() / 1_000_000.0) / _durationsNano.size();
    }

    public double throughput() {
        return (double) _durationsNano.size() / (this.totalDuration() / 1_000_000_000.0);
    }

    public int getCount() {
        return _durationsNano.size();
    }

    @Override
    public String toString() {
        return """
        --- Benchmark Statistics: %s ---
        Total operations:      %d
        Total time:            %.3f ms
        Throughput:            %.2f ops/sec
        Avg duration:          %.2f ms
        Min duration:          %d ms
        Max duration:          %d ms
        """.formatted(
                _executorType,
                this.getCount(),
                this.totalDuration() / 1_000_000.0,
                this.throughput(),
                this.averageDurationMs(),
                this.minDurationMs(),
                this.maxDurationMs()
        );
    }
}