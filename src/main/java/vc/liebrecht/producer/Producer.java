package vc.liebrecht.producer;

import vc.liebrecht.core.Message;

import java.util.concurrent.BlockingQueue;

/**
 * A {@code Producer} creates messages with a certain payload size and pushes them into the {@code BlockingQueue}.
 * <p>
 * This class implements {@code Runnable} and can be executed in a separate thread.
 * It creates a specified number of messages with a fixed payload size and inserts
 * them into the provided queue.
 */
public class Producer implements Runnable {
    private final BlockingQueue<Message> _queue;
    private final int _numMessages;
    private final byte[] _payload;

    /**
     * Constructs a new producer.
     *
     * @param q The queue to insert messages into
     * @param numMessages The number of messages to create
     * @param payloadSize The size of each message payload in bytes
     */
    public Producer(BlockingQueue<Message> q, int numMessages, int payloadSize) {
        _queue = q;
        _numMessages = numMessages;
        _payload = new byte[payloadSize];
    }

    /**
     * Executes the producer task.
     * <p>
     * Creates the specified number of messages and inserts them into the queue.
     * If the thread is interrupted, the interrupt flag is set and the method returns.
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < _numMessages; i++) {
                _queue.put(new Message(_payload));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}