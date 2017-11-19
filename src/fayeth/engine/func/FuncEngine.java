package fayeth.engine.func;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fayeth.cnf.CNF;
import fayeth.engine.Engine;
import fayeth.engine.Outcome;
import fayeth.engine.RandomFactory;
import fayeth.engine.Strategy;
import fayeth.engine.func.strategies.ShuffleClausesStrategy;
import fayeth.engine.func.strategies.ShuffleLiteralsStrategy;
import fayeth.program.state.Args;
import fayeth.util.Log;

public class FuncEngine implements Engine {

    private FuncOutputCollector outputCollector;
    private Args arguments;
    private List<Strategy<FuncTestableInput>> strategies = new ArrayList<>();
    private List<CNF> initialFormulae;
    
    @Override
    public void setConfiguration(Args arguments) {
        Log.info("Setting up FuncEngine with configuration " + arguments);
        this.arguments = arguments;
        RandomFactory randomFactory = new RandomFactory(arguments.getSeed());
        Log.info("Seed used is: " + randomFactory.getSeed());

        // Parse existing CNFs from input directory
        File inputDirectory = new File(arguments.getInputPath());
        this.initialFormulae = Arrays.stream(inputDirectory.listFiles())
                .filter(f -> f.getName().endsWith(".cnf"))
                .map(CNF::fromFile).collect(Collectors.toList());

        strategies.add(new ShuffleLiteralsStrategy(randomFactory.newRandom(), initialFormulae));
        strategies.add(new ShuffleClausesStrategy(randomFactory.newRandom(), initialFormulae));

        Log.info("Using the following strategies:");
        for (Strategy<FuncTestableInput> s : strategies) {
            Log.info("\t" + s.getClass().getSimpleName());
        }

        this.outputCollector = new FuncOutputCollector(arguments);

    }

    @Override
    public void run() {
        if (arguments == null) {
            throw new RuntimeException("No configuration for FuncEngine. Please call setConfiguration first");
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
                for(Strategy<FuncTestableInput> strategy : strategies) {
                    if (limit != 0 && i >= limit) {
                        return;
                    }
                    FuncTestableInput input = strategy.generateNextInput();
                    FuncTask task = new FuncTask(input, arguments, strategy);
                    Outcome<FuncTestableInput> outcome = task.run();
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
