package fayeth.engine.ub;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;
import fayeth.engine.ub.UbConcurrentMonitor.UbConcurrentJob;
import fayeth.util.Log;

public class UbConcurrentRunner implements Runnable {

    private final UbConcurrentMonitor monitor;
    private final int runnerNumber;
    
    public UbConcurrentRunner(UbConcurrentMonitor monitor, int runnerNumber) {
        this.monitor = monitor;
        this.runnerNumber = runnerNumber;
    }
    
    @Override
    public void run() {
        Log.info("UbConcurrentRunner:run: Runner number ["+runnerNumber+"] starting");
        
        while (true) {
            UbConcurrentJob aJob = monitor.getJob();
            if (aJob == null) {
                return;
            }
            
            try {
                Strategy<TestableInput> aStrategy = aJob.getStrategy();
                TestableInput input = aStrategy.generateNextInput();
                UbTask task = new UbTask(input, aJob.getArguments(), aStrategy);
                Outcome<TestableInput> outcome = task.run();
                monitor.recordOutcome(outcome);
            } catch (Exception e) {
                Log.error("UbConcurrentRunner:run: Runner number ["+runnerNumber+"] got an exception", e);
            }
        }
    }

}
