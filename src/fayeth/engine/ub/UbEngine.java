package fayeth.engine.ub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fayeth.engine.Engine;
import fayeth.engine.Outcome;
import fayeth.engine.OutputCollector;
import fayeth.engine.RandomFactory;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;
import fayeth.engine.ub.strategies.SquareCNFStrategy;
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
    private RandomFactory randomFactory;

    @Override
    public void setConfiguration(Args arguments) {
        Log.info("Setting up UbEngine with configuration " + arguments);
        this.arguments = arguments;
        this.randomFactory = new RandomFactory(arguments.getSeed());
        Log.info("Seed used is: " + randomFactory.getSeed());
        strategies.add(new RandomStringStrategy(randomFactory.newRandom()));
        strategies.add(new RandomSemiCNFStringStrategy(randomFactory.newRandom()));
        strategies.add(new RandomBrokenCNFStrategy(randomFactory.newRandom()));
        strategies.add(new RandomCorrectCNFStrategy(randomFactory.newRandom()));
        strategies.add(new SquareCNFStrategy(randomFactory.newRandom()));
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
            runMultithreaded();
        } else {
            runSequential();
        }
    }

    /**
     * Runs tasks in parallel.
     * When running in multithreaded mode, no reproducibility even with a fixed seed is guaranteed
     */
    private void runMultithreaded() {
        Log.info("Fayeth is starting with multithreading enabled. No reproducibility of results is guaranteed");
        
        int cores = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();
        UbConcurrentMonitor monitor = new UbConcurrentMonitor(
                arguments.getLimit(), strategies, outputCollector, arguments, randomFactory.newRandom());
        for (int i = 0; i < cores; i++) {
            UbConcurrentRunner aRunner = new UbConcurrentRunner(monitor, i);
            Thread aThread = new Thread(aRunner);
            threads.add(aThread);
            aThread.start();
        }
        try {
            for (Thread aThread : threads) {
                aThread.join();
            }
        } catch (InterruptedException e) {
            Log.error("UbEngine:runMultithreaded: Exception when waiting for threads to join", e);
        }
    }

    private void runSequential() {
        try {
            int limit = arguments.getLimit();
            long i = 0;
            while (true) {
                for(Strategy<TestableInput> strategy : strategies) {
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
