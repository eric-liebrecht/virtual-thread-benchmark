package vc.liebrecht.consumer;

import vc.liebrecht.core.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory interface for creating consumer instances.
 * <p>
 * This interface follows the Dependency Inversion Principle by allowing the benchmark
 * to depend on an abstraction rather than concrete consumer implementations. New consumer
 * types can be added by implementing this interface without modifying existing code.
 */
public interface ConsumerFactory {
    /**
     * Creates a new consumer instance.
     *
     * @param queue The queue to retrieve messages from
     * @param totalMessages The total number of messages to receive
     * @param done The latch to signal when all messages have been received
     * @param received The atomic counter to track the number of received messages
     * @return A new {@code Runnable} consumer instance
     */
    Runnable createConsumer(BlockingQueue<Message> queue, int totalMessages, CountDownLatch done, AtomicInteger received);
}