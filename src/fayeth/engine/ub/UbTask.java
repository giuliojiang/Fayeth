package fayeth.engine.ub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.Task;
import fayeth.engine.TestableInput;
import fayeth.program.state.Args;
import fayeth.subprocess.Subprocess;
import fayeth.util.FileUtil;

public class UbTask implements Task {

    private static final int SAT_TIMEOUT = 10; // seconds
    
    private final TestableInput testableInput;
    private final Args arguments;
    private final Strategy strategy;
    private Outcome outcome = new Outcome();
    
    public UbTask(TestableInput testableInput, Args arguments, Strategy strategy) {
        super();
        this.testableInput = testableInput;
        this.arguments = arguments;
        this.strategy = strategy;
    }
    
    public void onCompletion() {
        //outcome.setBugDescription(null);
    }
    
    public void onBugFound(String bugDescription) {
        outcome.addBugDescription(bugDescription);
    }

    @Override
    public Outcome run() throws IOException, InterruptedException {
        outcome.setInput(testableInput);
        outcome.setStrategy(strategy);
        
        // Initialize the event listener
        UbOutputListener listener = new UbOutputListener(this);
        
        // Write a temporary input file
        File tempFile = FileUtil.writeTemporaryFile(testableInput);
        
        // Run the SAT solver
        List<String> cmd = new ArrayList<>();
        cmd.add(Paths.get(arguments.getSutDir(), "runsat.sh").toAbsolutePath().toString());
        cmd.add(tempFile.getAbsolutePath());
        Subprocess sp = new Subprocess(cmd, SAT_TIMEOUT, listener);
        sp.waitFor();
        
        // Cleanup
        if (!arguments.isGcDisabled()) {
            tempFile.delete();
        }
        
        return outcome;
    }

}
