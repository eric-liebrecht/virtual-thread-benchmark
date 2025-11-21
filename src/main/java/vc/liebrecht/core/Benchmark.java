package vc.liebrecht.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.consumer.ConsumerFactory;
import vc.liebrecht.consumer.DefaultConsumerFactory;
import vc.liebrecht.producer.Producer;

/**
 * Creates a Benchmark for a specific {@code ExecutorService}.
 * <p>
 * This class executes a producer-consumer benchmark where multiple producer
 * threads
 * insert messages into a {@code BlockingQueue} and multiple consumer threads
 * retrieve
 * these messages. The duration of the benchmark is measured in nanoseconds.
 */
public class Benchmark {
	private final BenchmarkConfig _config;
	private final ConsumerFactory _consumerFactory;

	/**
	 * Constructs a new benchmark instance.
	 * <p>
	 * Creates a {@code DefaultConsumerFactory} based on the consumer type specified
	 * in the configuration.
	 *
	 * @param config The benchmark configuration with all necessary parameters
	 */
	public Benchmark(BenchmarkConfig config) {
		_config = config;
		_consumerFactory = new DefaultConsumerFactory(config.getConsumerType());
	}

	/**
	 * Constructs a new benchmark instance with a custom consumer factory.
	 * <p>
	 * This constructor allows dependency injection of a consumer factory, following
	 * the Dependency Inversion Principle.
	 *
	 * @param config          The benchmark configuration with all necessary
	 *                        parameters
	 * @param consumerFactory The factory to use for creating consumer instances
	 */
	public Benchmark(BenchmarkConfig config, ConsumerFactory consumerFactory) {
		_config = config;
		_consumerFactory = consumerFactory;
	}

	/**
	 * Starts the benchmark with the provided configuration and calculates the
	 * duration in nanoseconds.
	 * <p>
	 * Creates a {@code BlockingQueue} for messages, starts multiple consumer
	 * threads and multiple
	 * producer threads according to the configuration. The benchmark runs until all
	 * messages
	 * have been processed or a timeout of 10 minutes is reached.
	 *
	 * @param executor The {@code ExecutorService} to create the threads
	 * @return The duration of this benchmark run in nanoseconds
	 * @throws InterruptedException If the current thread is interrupted
	 */
	public long run(ExecutorService executor) throws InterruptedException {
		int totalMessages = _config.getProducers() * _config.getMessagesPerProducer();
		BlockingQueue<Message> queue = new ArrayBlockingQueue<>(totalMessages);
		CountDownLatch done = new CountDownLatch(_config.getConsumers());
		AtomicInteger received = new AtomicInteger(0);

		System.out.println("Starting " + _config.getConsumers() + " consumers");
		for (int i = 0; i < _config.getConsumers(); i++) {
			executor.submit(_consumerFactory.createConsumer(queue, totalMessages, done, received));
		}

		System.out.println("Starting " + _config.getProducers() + " producers");
		for (int i = 0; i < _config.getProducers(); i++) {
			executor.submit(new Producer(queue, _config.getMessagesPerProducer(), _config.getPayloadSize()));
		}

		long startTime = System.nanoTime();
		boolean completed = done.await(10, TimeUnit.MINUTES);
		long duration = System.nanoTime() - startTime;

		if (!completed) {
			System.err.println("WARNING: Benchmark timed out after 10 minutes. Not all messages were processed.");
		}

		return duration;
	}
}