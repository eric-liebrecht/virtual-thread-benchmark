package vc.liebrecht.engine;

import vc.liebrecht.config.BenchmarkConfig;

/**
 * Represents the result of a benchmark execution with statistics for both
 * executor types.
 * <p>
 * This record encapsulates the configuration used for the benchmark and the
 * statistics
 * collected for both platform threads (thread pool) and virtual threads. It
 * provides
 * a convenient way to store and retrieve benchmark results.
 *
 * @param config        The benchmark configuration that was used
 * @param platformStats Statistics for the thread pool executor
 * @param virtualStats  Statistics for the virtual thread executor
 */
public record BenchmarkResult(BenchmarkConfig config, BenchmarkStatistics platformStats,
		BenchmarkStatistics virtualStats) {

	/**
	 * Returns a formatted string representation of the benchmark result.
	 * <p>
	 * Contains the configuration and statistics for both executor types in a
	 * readable format.
	 *
	 * @return A formatted string representation of the benchmark result
	 */
	@Override
	public String toString() {
		return """
				BenchmarkResult {
				    config = %s
				    platformStats = %s
				    virtualStats = %s
				}
				""".formatted(this.config(), this.platformStats(), this.virtualStats());
	}
}