package vc.liebrecht.domain;

/**
 * Enumeration for different executor types used in the benchmark.
 * <p>
 * Defines the two supported executor types: {@code VirtualThreads} for virtual threads
 * and {@code ThreadPool} for traditional thread pools.
 */
public enum ExecutorType {
    /**
     * Virtual threads executor type.
     */
    VirtualThreads ("VirtualThreads"),

    /**
     * Thread pool executor type.
     */
    ThreadPool ("ThreadPool");

    private final String _value;

    /**
     * Constructs an executor type.
     *
     * @param value The string value for this executor type
     */
    ExecutorType(String value) {
        _value = value;
    }

    /**
     * Returns the string value of this executor type.
     *
     * @return The string value of the executor type
     */
    @Override
    public String toString() {
        return _value;
    }
}
