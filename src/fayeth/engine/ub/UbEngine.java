package fayeth.engine.ub;

import java.io.IOException;

import fayeth.engine.Engine;
import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;
import fayeth.engine.ub.strategies.random.RandomStringStrategy;
import fayeth.program.state.Args;
import fayeth.util.Log;

public class UbEngine implements Engine {

    private Args arguments;
    private Strategy strategy = new RandomStringStrategy();

    @Override
    public void setConfiguration(Args arguments) {
        Log.info("Setting up UbEngine with configuration " + arguments);
        this.arguments = arguments;
    }
    
    @Override
    public void run() {
        if (arguments == null) {
            throw new RuntimeException("No configuration for UbEngine. Please call setConfiguration first");
        }
        
        if (arguments.isMultithreadingAllowed()) {
            // TODO add multithreaded supportc
            throw new RuntimeException("Multithreading is not supported yet. Please provide a fixed seed for reproducibility");
        } else {
            runSequential();
        }
    }

    private void runSequential() {
        try {
            while (true) {
                TestableInput input = strategy.generateNextInput();
                UbTask task = new UbTask(input, arguments, strategy);
                Outcome outcome = task.run();
                Log.info("A task is complete. Outcome is " + outcome);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
