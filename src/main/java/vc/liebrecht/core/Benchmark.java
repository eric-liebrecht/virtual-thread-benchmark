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
 */
public class Benchmark {
    private final BenchmarkConfig _config;

    public Benchmark(BenchmarkConfig config) {
        _config = config;
    }

    /**
     * Starts the Benchmark with the provided configuration and calculates the duration in nanoseconds.
     *
     * @param executor The {@code ExecutorService} to create the threads
     * @return The duration of this benchmark run in nanoseconds
     * @throws InterruptedException If the current Thread is interrupted
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