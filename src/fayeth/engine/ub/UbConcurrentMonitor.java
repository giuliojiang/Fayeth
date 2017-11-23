package fayeth.engine.ub;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import fayeth.engine.Outcome;
import fayeth.engine.OutputCollector;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;
import fayeth.program.state.Args;
import fayeth.util.Log;

public class UbConcurrentMonitor {
    
    private final int limit;
    private int currJobIndex = 0;
    private final List<Strategy<TestableInput>> strategies;
    private final OutputCollector outputCollector;
    private final Args arguments;
    private final Random random;
    
    public UbConcurrentMonitor(int limit, List<Strategy<TestableInput>> strategies, OutputCollector outputCollector, 
            Args arguments, Random random) {
        this.limit = limit;
        this.strategies = strategies;
        this.outputCollector = outputCollector;
        this.arguments = arguments;
        this.random = random;
    }

    /**
     * Returns a new job to be executed.
     * Returns null if the limit has been reached.
     * @return
     */
    public synchronized UbConcurrentJob getJob() {
        if (limit != 0 && currJobIndex >= limit) {
            return null;
        }
        
        currJobIndex++;
        
        // Select a random strategy
        Strategy<TestableInput> aStrategy = strategies.get(random.nextInt(strategies.size()));
        
        return new UbConcurrentJob(aStrategy, this, arguments);
    }
    
    public synchronized void recordOutcome(Outcome<TestableInput> outcome) {
        try {
            outputCollector.collect(outcome);
            Log.info("A task is complete. Outcome is " + outcome);
        } catch (IOException e) {
            Log.error("UbConcurrentMonitor:recordOutcome: Exception when collecting an outcome", e);
        }
    }
    
    public static class UbConcurrentJob {
        
        private final Strategy<TestableInput> strategy;
        private final Args arguments;
        
        public UbConcurrentJob(Strategy<TestableInput> strategy, UbConcurrentMonitor monitor, Args arguments) {
            this.strategy = strategy;
            this.arguments = arguments;
        }

        public Strategy<TestableInput> getStrategy() {
            return strategy;
        }

        public Args getArguments() {
            return arguments;
        }

    }

}
