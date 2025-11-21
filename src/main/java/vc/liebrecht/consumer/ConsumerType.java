package vc.liebrecht.consumer;

/**
 * Enumeration for different consumer types used in the benchmark.
 * <p>
 * Defines the supported consumer types: {@code HEAVY} for CPU-intensive processing
 * and {@code LIGHTWEIGHT} for minimal processing.
 */
public enum ConsumerType {
    /**
     * Heavy consumer type that performs CPU-intensive operations.
     */
    HEAVY("Heavy"),

    /**
     * Lightweight consumer type that performs minimal processing.
     */
    LIGHTWEIGHT("Lightweight");

    private final String _displayName;

    /**
     * Constructs a consumer type.
     *
     * @param displayName The display name for this consumer type
     */
    ConsumerType(String displayName) {
        _displayName = displayName;
    }

    /**
     * Returns the display name of this consumer type.
     *
     * @return The display name of the consumer type
     */
    @Override
    public String toString() {
        return _displayName;
    }
}

