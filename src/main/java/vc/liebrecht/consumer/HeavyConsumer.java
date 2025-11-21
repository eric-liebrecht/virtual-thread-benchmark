package vc.liebrecht.consumer;

import vc.liebrecht.core.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A heavy consumer that takes messages from a {@code BlockingQueue} and processes them with CPU-intensive operations.
 * <p>
 * This class implements {@code Runnable} and can be executed in a separate thread.
 * It retrieves messages from the queue and processes each message by computing a SHA-256 hash
 * of the payload, simulating CPU-intensive work. The consumer continues until the specified
 * total number of messages has been received, then signals completion via a {@code CountDownLatch}.
 */
public class HeavyConsumer implements Runnable {
    private final BlockingQueue<Message> _queue;
    private final int _totalMessages;
    private final CountDownLatch _done;
    private final AtomicInteger _received;

    /**
     * Constructs a new consumer.
     *
     * @param q The queue to retrieve messages from
     * @param totalMessages The total number of messages to receive
     * @param done The latch to signal when all messages have been received
     * @param received The atomic counter to track the number of received messages
     */
    public HeavyConsumer(BlockingQueue<Message> q, int totalMessages, CountDownLatch done, AtomicInteger received) {
        _queue = q;
        _totalMessages = totalMessages;
        _done = done;
        _received = received;
    }

    /**
     * Executes the consumer task.
     * <p>
     * This method continuously retrieves {@link Message} objects from the queue and
     * simulates CPU load by hashing the message payload using SHA-256. For every
     * message taken from the queue, the internal counter for received messages is
     * incremented.
     *
     * <p>
     * The loop runs until the expected total number of messages has been processed.
     * If the thread is interrupted while waiting on the queue, the interrupt flag is
     * restored and the method proceeds to shutdown. Any failure to initialize the
     * SHA-256 digest results in a {@link RuntimeException}.
     *
     * <p>
     * Regardless of whether the loop completes normally or is interrupted, the
     * {@code CountDownLatch} is decremented in the {@code finally} block to signal
     * completion to the orchestrator.
     */
    @Override
    public void run() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            while (_received.get() < _totalMessages) {
                Message m = _queue.take();
                digest.update(m.payload());
                digest.digest();
                _received.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            _done.countDown();
        }
    }
}