package vc.liebrecht.config;

public class BenchmarkConfig {
    private final int _numProducers;
    private final int _numMessagesPerProducer;
    private final int _payloadSize;
    private final int _numRuns;

    public BenchmarkConfig(int numProducers, int numMessagesPerProducer, int payloadSize, int numRuns) {
        _numProducers = numProducers;
        _numMessagesPerProducer = numMessagesPerProducer;
        _payloadSize = payloadSize;
        _numRuns = numRuns;
    }

    public static BenchmarkConfig parse(String[] args) {
        int numProducers = 100;
        int numMessagesPerProducer = 100;
        int payloadSize = 1024;
        int numRuns = 5;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--numProducers":
                    numProducers = Integer.parseInt(args[++i]);
                    break;

                case "--numMessages":
                    numMessagesPerProducer = Integer.parseInt(args[++i]);
                    break;

                case "--payloadSize":
                    payloadSize = Integer.parseInt(args[++i]);
                    break;

                case "--numRuns":
                    numRuns = Integer.parseInt(args[++i]);
                    break;

                default:
                    System.err.println("Unknown argument: " + args[i]);
            }
        }

        return new BenchmarkConfig(numProducers, numMessagesPerProducer, payloadSize, numRuns);
    }

    public int getPayloadSize() {
        return _payloadSize;
    }

    public int getMessagesPerProducer() {
        return _numMessagesPerProducer;
    }

    public int getProducers() {
        return _numProducers;
    }

    public int getRuns() {
        return _numRuns;
    }
}