package fayeth.engine.ub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fayeth.engine.*;
import fayeth.engine.ub.strategies.random.RandomCorrectCNFStrategy;
import fayeth.engine.ub.strategies.random.RandomBrokenCNFStrategy;
import fayeth.engine.ub.strategies.random.RandomStringStrategy;
import fayeth.program.state.Args;
import fayeth.util.Log;

public class UbEngine implements Engine {

    private Args arguments;
    private List<Strategy> strategies = new ArrayList<>();

    @Override
    public void setConfiguration(Args arguments) {
        Log.info("Setting up UbEngine with configuration " + arguments);
        this.arguments = arguments;
        RandomFactory randomFactory = new RandomFactory(arguments.getSeed());
        strategies.add(new RandomStringStrategy(randomFactory.newRandom()));
        strategies.add(new RandomBrokenCNFStrategy(randomFactory.newRandom()));
        strategies.add(new RandomCorrectCNFStrategy(randomFactory.newRandom()));
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
            for (long i = 0; limit == 0 || i < limit; i++) {
                for(Strategy strategy : strategies) {
                    TestableInput input = strategy.generateNextInput();
                    UbTask task = new UbTask(input, arguments, strategy);
                    Outcome outcome = task.run();
                    Log.info("A task is complete. Outcome is " + outcome);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
