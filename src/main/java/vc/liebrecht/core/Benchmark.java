package vc.liebrecht.core;

import vc.liebrecht.config.BenchmarkConfig;
import vc.liebrecht.consumer.Consumer;
import vc.liebrecht.producer.Producer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Creates a Benchmark for a specific {@code ExecutorService}.
 * <p>
 * This class executes a producer-consumer benchmark where multiple producer threads
 * insert messages into a {@code BlockingQueue} and a consumer thread retrieves these
 * messages. The duration of the benchmark is measured in nanoseconds.
 */
public class Benchmark {
    private final BenchmarkConfig _config;

    /**
     * Constructs a new benchmark instance.
     *
     * @param config The benchmark configuration with all necessary parameters
     */
    public Benchmark(BenchmarkConfig config) {
        _config = config;
    }

    /**
     * Starts the benchmark with the provided configuration and calculates the duration in nanoseconds.
     * <p>
     * Creates a {@code BlockingQueue} for messages, starts a consumer thread and multiple
     * producer threads according to the configuration. The benchmark runs until all messages
     * have been processed or a timeout of 10 minutes is reached.
     *
     * @param executor The {@code ExecutorService} to create the threads
     * @return The duration of this benchmark run in nanoseconds
     * @throws InterruptedException If the current thread is interrupted
     */
    public long run(ExecutorService executor) throws InterruptedException {
        int totalMessages = _config.getProducers() * _config.getMessagesPerProducer();
        BlockingQueue<Message> queue = new ArrayBlockingQueue<>(totalMessages);
        CountDownLatch done = new CountDownLatch(1);
        AtomicInteger received = new AtomicInteger(0);

        executor.submit(new Consumer(queue, totalMessages, done, received));

        for (int i = 0; i < _config.getProducers(); i++) {
            executor.submit(new Producer(queue, _config.getMessagesPerProducer(), _config.getPayloadSize()));
        }

        long startTime = System.nanoTime();
        done.await(10, TimeUnit.MINUTES);
        long duration = System.nanoTime() - startTime;

        return duration;
    }
}