package vc.liebrecht.config;

import vc.liebrecht.consumer.ConsumerType;

/**
 * Configuration class for benchmark parameters.
 * <p>
 * This class stores all necessary configuration parameters for the benchmark
 * system,
 * including the number of producers, number of consumers, consumer type,
 * messages per producer,
 * payload size, and number of runs.
 */
public class BenchmarkConfig {
	private final int _numProducers;
	private final int _numConsumers;
	private final ConsumerType _consumerType;
	private final int _numMessagesPerProducer;
	private final int _payloadSize;
	private final int _numRuns;

	/**
	 * Constructs a new benchmark configuration.
	 *
	 * @param numProducers           The number of producer threads
	 * @param numConsumers           The number of consumer threads
	 * @param consumerType           The type of consumer to use
	 * @param numMessagesPerProducer The number of messages each producer creates
	 * @param payloadSize            The size of each message payload in bytes
	 * @param numRuns                The number of benchmark runs to perform
	 */
	public BenchmarkConfig(int numProducers, int numConsumers, ConsumerType consumerType, int numMessagesPerProducer,
			int payloadSize, int numRuns) {
		_numProducers = numProducers;
		_numConsumers = numConsumers;
		_consumerType = consumerType;
		_numMessagesPerProducer = numMessagesPerProducer;
		_payloadSize = payloadSize;
		_numRuns = numRuns;
	}

	/**
	 * Parses command-line arguments and creates a benchmark configuration.
	 * <p>
	 * Supports the following command-line arguments:
	 * <ul>
	 * <li>{@code --numProducers}: Number of producer threads (default: 100)</li>
	 * <li>{@code --numConsumers}: Number of consumer threads (default: 1)</li>
	 * <li>{@code --consumerType}: Type of consumer - HEAVY or LIGHTWEIGHT (default:
	 * HEAVY)</li>
	 * <li>{@code --numMessages}: Number of messages per producer (default:
	 * 100)</li>
	 * <li>{@code --payloadSize}: Size of message payload in bytes (default:
	 * 1024)</li>
	 * <li>{@code --numRuns}: Number of benchmark runs (default: 5)</li>
	 * </ul>
	 * Unknown arguments are printed to {@code System.err} but do not cause the
	 * parsing to fail.
	 *
	 * @param args Command-line arguments to parse
	 * @return A new {@code BenchmarkConfig} instance with parsed or default values
	 */
	public static BenchmarkConfig parse(String[] args) {
		int numProducers = 100;
		int numConsumers = 1;
		ConsumerType consumerType = ConsumerType.HEAVY;
		int numMessagesPerProducer = 100;
		int payloadSize = 1024;
		int numRuns = 5;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "--numProducers":
					numProducers = Integer.parseInt(args[++i]);
					break;

				case "--numConsumers":
					numConsumers = Integer.parseInt(args[++i]);
					break;

				case "--consumerType":
					consumerType = ConsumerType.valueOf(args[++i].toUpperCase());
					break;

				case "--numMessages":
					numMessagesPerProducer = Integer.parseInt(args[++i]);
					break;

				case "--payloadSize":
					payloadSize = Integer.parseInt(args[++i]);
					break;

				case "--numRuns":
					numRuns = Integer.parseInt(args[++i]);
					break;

				default:
					System.err.println("Unknown argument: " + args[i]);
			}
		}

		return new BenchmarkConfig(numProducers, numConsumers, consumerType, numMessagesPerProducer, payloadSize,
				numRuns);
	}

	/**
	 * Returns the payload size in bytes.
	 *
	 * @return The payload size in bytes
	 */
	public int getPayloadSize() {
		return _payloadSize;
	}

	/**
	 * Returns the number of messages per producer.
	 *
	 * @return The number of messages per producer
	 */
	public int getMessagesPerProducer() {
		return _numMessagesPerProducer;
	}

	/**
	 * Returns the number of producers.
	 *
	 * @return The number of producers
	 */
	public int getProducers() {
		return _numProducers;
	}

	/**
	 * Returns the number of consumers.
	 *
	 * @return The number of consumers
	 */
	public int getConsumers() {
		return _numConsumers;
	}

	/**
	 * Returns the consumer type.
	 *
	 * @return The consumer type
	 */
	public ConsumerType getConsumerType() {
		return _consumerType;
	}

	/**
	 * Returns the number of benchmark runs.
	 *
	 * @return The number of runs
	 */
	public int getRuns() {
		return _numRuns;
	}

	/**
	 * Returns a formatted string representation of the benchmark configuration.
	 * <p>
	 * Contains all configuration parameters in a readable format with tab-separated
	 * values for alignment.
	 *
	 * @return A formatted string representation of the configuration
	 */
	@Override
	public String toString() {
		return """
				\t{
				    \tNumber of producers: \t%d
				    \tNumber of consumers: \t%d
				    \tConsumer type:       \t%s
				    \tMessages per producer:\t%d
				    \tPayload size:        \t%d byte
				    \tRuns:                \t%d
				\t}
				""".formatted(
				this.getProducers(),
				this.getConsumers(),
				this.getConsumerType(),
				this.getMessagesPerProducer(),
				this.getPayloadSize(),
				this.getRuns());
	}
}