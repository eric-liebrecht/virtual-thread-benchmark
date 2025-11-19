package vc.liebrecht.producer;

import vc.liebrecht.core.Message;

import java.util.concurrent.BlockingQueue;

/**
 * A {@code Producer} creates messages with a certain payload size and pushes them into the {@code BlockingQueue}.
 */
public class Producer implements Runnable {
    private final BlockingQueue<Message> _queue;
    private final int _numMessages;
    private final byte[] _payload;

    public Producer(BlockingQueue<Message> q, int numMessages, int payloadSize) {
        _queue = q;
        _numMessages = numMessages;
        _payload = new byte[payloadSize];
    }

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