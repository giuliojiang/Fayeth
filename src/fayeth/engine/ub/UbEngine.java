package fayeth.engine.ub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fayeth.engine.Engine;
import fayeth.engine.Outcome;
import fayeth.engine.OutputCollector;
import fayeth.engine.RandomFactory;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;
import fayeth.engine.ub.strategies.random.RandomBrokenCNFStrategy;
import fayeth.engine.ub.strategies.random.RandomCorrectCNFStrategy;
import fayeth.engine.ub.strategies.random.RandomSemiCNFStringStrategy;
import fayeth.engine.ub.strategies.random.RandomStringStrategy;
import fayeth.program.state.Args;
import fayeth.util.Log;

public class UbEngine implements Engine {

    private OutputCollector outputCollector;
    private Args arguments;
    private List<Strategy<TestableInput>> strategies = new ArrayList<>();

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
        for (Strategy<TestableInput> s : strategies) {
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
            runParallel();
        } else {
            runSequential();
        }
    }

    private void runParallel() {
        ExecutorService es = Executors.newCachedThreadPool();
        int limit = arguments.getLimit() > 0 ? arguments.getLimit() : 1000;
        long i = 0;
        while (true) {
            if (limit != 0 && i >= limit) {
                    break;
            }
            for (Strategy<TestableInput> strategy : strategies) {

                es.submit(() -> {
                    TestableInput input = strategy.generateNextInput();
                    UbTask task = new UbTask(input, arguments, strategy);
                    Outcome<TestableInput> outcome = null;
                    try {
                        outcome = task.run();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputCollector.collect(outcome);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.info("A task is complete. Outcome is " + outcome);
                });
                i++;
            }
        }
        try {
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            es.shutdownNow();
        }
    }

    private void runSequential() {
        try {
            int limit = arguments.getLimit();
            long i = 0;
            while (true) {
                for (Strategy<TestableInput> strategy : strategies) {
                    if (limit != 0 && i >= limit) {
                        return;
                    }
                    TestableInput input = strategy.generateNextInput();
                    UbTask task = new UbTask(input, arguments, strategy);
                    Outcome<TestableInput> outcome = task.run();
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
