package vc.liebrecht.core;

/**
 * Represents a message with a byte-array as payload.
 */
public record Message(byte[] payload) {
}