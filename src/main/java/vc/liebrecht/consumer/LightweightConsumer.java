package vc.liebrecht.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import vc.liebrecht.core.Message;

/**
 * A {@code LightweightConsumer} takes messages from a {@code BlockingQueue} and
 * reads them until no more messages are left.
 * <p>
 * This class implements {@code Runnable} and can be executed in a separate
 * thread.
 * It retrieves messages from the queue until the specified total number of
 * messages
 * has been received, then signals completion via a {@code CountDownLatch}.
 */
public class LightweightConsumer implements Runnable {
	private final BlockingQueue<Message> _queue;
	private final int _totalMessages;
	private final CountDownLatch _done;
	private final AtomicInteger _received;

	/**
	 * Constructs a new consumer.
	 *
	 * @param q             The queue to retrieve messages from
	 * @param totalMessages The total number of messages to receive
	 * @param done          The latch to signal when all messages have been received
	 * @param received      The atomic counter to track the number of received
	 *                      messages
	 */
	public LightweightConsumer(BlockingQueue<Message> q, int totalMessages, CountDownLatch done,
			AtomicInteger received) {
		_queue = q;
		_totalMessages = totalMessages;
		_done = done;
		_received = received;
	}

	/**
	 * Executes the consumer task.
	 * <p>
	 * This method continuously retrieves {@link Message} objects from the queue
	 * without
	 * performing any processing on them. The messages are simply taken from the
	 * queue
	 * and discarded. The loop runs until the expected total number of messages has
	 * been
	 * received, as tracked by the shared atomic counter.
	 *
	 * <p>
	 * If the thread is interrupted while waiting on the queue, the interrupt flag
	 * is
	 * restored and the method proceeds to shutdown.
	 *
	 * <p>
	 * Regardless of whether the loop completes normally or is interrupted, the
	 * {@code CountDownLatch} is decremented in the {@code finally} block to signal
	 * completion to the orchestrator.
	 */
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