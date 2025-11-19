package vc.liebrecht.core;

public enum ExecutorType {
    VirtualThreads ("VirtualThreads"),
    ThreadPool ("ThreadPool");

    private final String _value;

    ExecutorType(String value) {
        _value = value;
    }

    @Override
    public String toString() {
        return _value;
    }
}
