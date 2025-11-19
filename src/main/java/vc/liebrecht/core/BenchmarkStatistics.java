package vc.liebrecht.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects and calculates statistics for benchmark runs.
 * <p>
 * This class stores the duration of each benchmark run in nanoseconds and provides
 * methods to calculate average, minimum, maximum, total duration, and throughput.
 */
public class BenchmarkStatistics {
    private final List<Long> _durationsNano;
    private final ExecutorType _executorType;

    /**
     * Constructs new benchmark statistics.
     *
     * @param type The executor type for which these statistics are collected
     */
    public BenchmarkStatistics(final ExecutorType type) {
        _executorType = type;
        _durationsNano = new ArrayList<>();
    }

    /**
     * Adds a new duration measurement.
     *
     * @param nano The duration of the benchmark run in nanoseconds
     */
    public void addDuration(final long nano) {
        _durationsNano.add(nano);
    }

    /**
     * Calculates the total duration of all benchmark runs.
     *
     * @return The total duration in nanoseconds
     */
    public long totalDuration() {
        return _durationsNano.stream().reduce(0L, Long::sum);
    }

    /**
     * Calculates the minimum duration of all benchmark runs.
     *
     * @return The minimum duration in milliseconds
     */
    public long minDurationMs() {
        return (long) (_durationsNano.stream().min(Long::compareTo).orElse(0L) / 1_000_000.0);
    }

    /**
     * Calculates the maximum duration of all benchmark runs.
     *
     * @return The maximum duration in milliseconds
     */
    public long maxDurationMs() {
        return (long) (_durationsNano.stream().max(Long::compareTo).orElse(0L) / 1_000_000.0);
    }

    /**
     * Calculates the average duration of all benchmark runs.
     *
     * @return The average duration in milliseconds
     */
    public double averageDurationMs() {
        return (this.totalDuration() / 1_000_000.0) / _durationsNano.size();
    }

    /**
     * Calculates the throughput of the benchmark runs.
     * <p>
     * The throughput indicates how many runs per second can be performed.
     *
     * @return The throughput in runs per second
     */
    public double throughput() {
        return (double) _durationsNano.size() / (this.totalDuration() / 1_000_000_000.0);
    }

    /**
     * Returns the number of collected benchmark runs.
     *
     * @return The number of runs
     */
    public int getCount() {
        return _durationsNano.size();
    }

    /**
     * Returns a formatted string representation of the benchmark statistics.
     * <p>
     * Contains all important metrics such as total duration, average, minimum,
     * maximum, and throughput in a readable format.
     *
     * @return A formatted string representation of the statistics
     */
    @Override
    public String toString() {
        return """
        --- Benchmark Statistics: %s ---
        Total operations:\t%d
        Total time:     \t%.3f ms
        Throughput:     \t%.2f ops/sec
        Avg duration:   \t%.2f ms
        Min duration:   \t%d ms
        Max duration:   \t%d ms
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