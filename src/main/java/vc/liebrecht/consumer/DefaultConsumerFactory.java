package vc.liebrecht.consumer;

import vc.liebrecht.core.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation of {@code ConsumerFactory} that creates consumers based on
 * the configured consumer type.
 * <p>
 * This class implements the Factory Pattern and follows the Open/Closed Principle by
 * allowing new consumer types to be added through the {@code ConsumerType} enum without
 * modifying this factory class.
 */
public class DefaultConsumerFactory implements ConsumerFactory {
    private final ConsumerType _consumerType;

    /**
     * Constructs a new consumer factory with the specified consumer type.
     *
     * @param consumerType The type of consumer to create
     */
    public DefaultConsumerFactory(ConsumerType consumerType) {
        _consumerType = consumerType;
    }

    /**
     * Creates a new consumer instance based on the configured consumer type.
     *
     * @param queue The queue to retrieve messages from
     * @param totalMessages The total number of messages to receive
     * @param done The latch to signal when all messages have been received
     * @param received The atomic counter to track the number of received messages
     * @return A new {@code Runnable} consumer instance
     * @throws IllegalArgumentException If the consumer type is not supported
     */
    @Override
    public Runnable createConsumer(BlockingQueue<Message> queue, int totalMessages, CountDownLatch done, AtomicInteger received) {
        return switch (_consumerType) {
            case HEAVY -> new HeavyConsumer(queue, totalMessages, done, received);
            case LIGHTWEIGHT -> new LightweightConsumer(queue, totalMessages, done, received);
        };
    }
}