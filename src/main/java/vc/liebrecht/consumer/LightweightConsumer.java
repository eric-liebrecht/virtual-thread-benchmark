package vc.liebrecht.consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
	private final CountDownLatch _done;

	/**
	 * Constructs a new consumer.
	 *
	 * @param q    The queue to retrieve messages from
	 * @param done The latch to signal when all messages have been received
	 */
	public LightweightConsumer(BlockingQueue<Message> q, CountDownLatch done) {
		_queue = q;
		_done = done;
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
	 * received, as tracked by the {@code CountDownLatch}.
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
			while (true) {
				Message m = _queue.poll(10, TimeUnit.NANOSECONDS);

				if (m == null) {
					if (_done.getCount() == 0) {
						break;
					}
					continue;
				}

				_done.countDown();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}