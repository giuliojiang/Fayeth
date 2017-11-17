package fayeth.engine.ub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fayeth.engine.*;
import fayeth.engine.ub.strategies.random.RandomCorrectCNFStrategy;
import fayeth.engine.ub.strategies.random.RandomBrokenCNFStrategy;
import fayeth.engine.ub.strategies.random.RandomSemiCNFStringStrategy;
import fayeth.engine.ub.strategies.random.RandomStringStrategy;
import fayeth.program.state.Args;
import fayeth.util.Log;

public class UbEngine implements Engine {

    private OutputCollector outputCollector;
    private Args arguments;
    private List<Strategy> strategies = new ArrayList<>();

    @Override
    public void setConfiguration(Args arguments) {
        Log.info("Setting up UbEngine with configuration " + arguments);
        this.arguments = arguments;
        RandomFactory randomFactory = new RandomFactory(arguments.getSeed());
        Log.info("Seed used is: " + randomFactory.getSeed());
        strategies.add(new RandomStringStrategy(randomFactory.newRandom()));
        strategies.add(new RandomSemiCNFStringStrategy(randomFactory.newRandom()));
        strategies.add(new RandomBrokenCNFStrategy(randomFactory.newRandom()));
        strategies.add(new RandomCorrectCNFStrategy(randomFactory.newRandom()));
        Log.info("Using the following strategies:");
        for (Strategy s : strategies) {
            Log.info("\t" + s.getClass().getSimpleName());
        }

        this.outputCollector = new OutputCollector(arguments);
    }
    
    @Override
    public void run() {
        if (arguments == null) {
            throw new RuntimeException("No configuration for UbEngine. Please call setConfiguration first");
        }
        
        if (arguments.isThreadingEnabled()) {
            // TODO add multithreaded supportc
            throw new RuntimeException("Multithreading is not supported yet. Please provide a fixed seed for reproducibility");
        } else {
            runSequential();
        }
    }

    private void runSequential() {
        try {
            int limit = arguments.getLimit();
            long i = 0;
            while (true) {
                for(Strategy strategy : strategies) {
                    if (limit != 0 && i >= limit) {
                        return;
                    }
                    TestableInput input = strategy.generateNextInput();
                    UbTask task = new UbTask(input, arguments, strategy);
                    Outcome outcome = task.run();
                    outputCollector.collect(outcome);
                    Log.info("A task is complete. Outcome is " + outcome);
                    i++;
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
