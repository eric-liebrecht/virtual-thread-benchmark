package vc.liebrecht.consumer;

import vc.liebrecht.core.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@code Consumer} takes messages from a {@code BlockingQueue} and reads them until no more messages are left.
 */
public class Consumer implements Runnable {
    private final BlockingQueue<Message> _queue;
    private final int _totalMessages;
    private final CountDownLatch _done;
    private final AtomicInteger _received;

    public Consumer(BlockingQueue<Message> q, int totalMessages, CountDownLatch done, AtomicInteger received) {
        _queue = q;
        _totalMessages = totalMessages;
        _done = done;
        _received = received;
    }

    @Override
    public void run() {
        try {
            while (_received.get() < _totalMessages) {
                _queue.take();
                _received.incrementAndGet();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            _done.countDown();
        }
    }
}