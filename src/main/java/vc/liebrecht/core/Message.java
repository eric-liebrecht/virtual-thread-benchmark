package vc.liebrecht.core;

/**
 * Represents a message with a byte-array as payload.
 * <p>
 * This record class is used to transfer messages between producer and consumer.
 * The payload can have any size.
 *
 * @param payload The byte array containing the message payload
 */
public record Message(byte[] payload) {
}