package vc.liebrecht.consumer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import vc.liebrecht.domain.Message;

/**
 * A heavy consumer that takes messages from a {@code BlockingQueue} and
 * processes them with CPU-intensive operations.
 * <p>
 * This class implements {@code Runnable} and can be executed in a separate
 * thread.
 * It retrieves messages from the queue and processes each message by computing
 * a SHA-256 hash
 * of the payload, simulating CPU-intensive work. The consumer continues until
 * the specified
 * total number of messages has been received, then signals completion via a
 * {@code CountDownLatch}.
 */
public class HeavyConsumer implements Runnable {
	private final BlockingQueue<Message> _queue;
	private final CountDownLatch _done;

	/**
	 * Constructs a new consumer.
	 *
	 * @param q    The queue to retrieve messages from
	 * @param done The latch to signal when all messages have been received
	 */
	public HeavyConsumer(BlockingQueue<Message> q, CountDownLatch done) {
		_queue = q;
		_done = done;
	}

	/**
	 * Executes the consumer task.
	 * <p>
	 * This method continuously retrieves {@link Message} objects from the queue and
	 * simulates CPU load by hashing the message payload using SHA-256. For every
	 * message processed, the {@code CountDownLatch} is decremented. The consumer
	 * continues processing messages until the {@code CountDownLatch} reaches zero,
	 * indicating that all messages have been processed.
	 *
	 * <p>
	 * The method uses a polling mechanism with a short timeout to check for new
	 * messages. If no message is available and the latch count is zero, the loop
	 * terminates. If the thread is interrupted while waiting on the queue, the
	 * interrupt flag is restored and the method proceeds to shutdown. Any failure
	 * to initialize the SHA-256 digest results in a {@link RuntimeException}.
	 */
	@Override
	public void run() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			while (true) {
				Message m = _queue.poll(10, TimeUnit.NANOSECONDS);

				if (m == null) {
					if (_done.getCount() == 0) {
						break;
					}
					continue;
				}

				digest.update(m.payload());
				digest.digest();

				_done.countDown();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}