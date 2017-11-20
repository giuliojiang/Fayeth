package fayeth.engine.func;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.Task;
import fayeth.program.state.Args;
import fayeth.subprocess.Subprocess;
import fayeth.subprocess.SubprocessListener;
import fayeth.util.FileUtil;
import fayeth.util.Log;

public class FuncTask implements Task<FuncTestableInput> {

private static final int SAT_TIMEOUT = 5; // seconds
    
    private final FuncTestableInput testableInput;
    private final Args arguments;
    private final Strategy<FuncTestableInput> strategy;
    private Outcome<FuncTestableInput> outcome = new Outcome<>();
    
    public FuncTask(FuncTestableInput testableInput, Args arguments, Strategy<FuncTestableInput> strategy) {
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
    public Outcome<FuncTestableInput> run() throws IOException, InterruptedException {
        outcome.setInput(testableInput);
        outcome.setStrategy(strategy);
        
        // Initialize the event listener
        FuncOutputListener listener = new FuncOutputListener(this);
        
        // Write a temporary input file
        File tempFile = FileUtil.writeTemporaryFile(testableInput);
        
        // Run the SAT solver
        List<String> cmd = new ArrayList<>();
        cmd.add(Paths.get(arguments.getSutDir(), "runsat.sh").toAbsolutePath().toString());
        cmd.add(tempFile.getAbsolutePath());
        Subprocess sp = new Subprocess(cmd, SAT_TIMEOUT, listener);
        sp.waitFor();
        
        // Run GCOV
        Log.info("Working directory is " + System.getProperty("user.dir"));

        double[] gcovValue = new double[1];
        List<String> gcovCmd = new ArrayList<>();
        gcovCmd.add("gcov");
        gcovCmd.add("--no-output");
        File cwd = new File(".");
        File[] cwdFiles = cwd.listFiles();
        for (File f : cwdFiles) {
            if (f.getName().endsWith(".c")) {
                gcovCmd.add(f.getAbsolutePath());
            }
        }
        Subprocess gcovSp = new Subprocess(gcovCmd, 0, new GcovSubprocessListener(gcovValue));
        gcovSp.waitFor();
        Log.info("Final coverage value is: " + gcovValue[0]);
        testableInput.recordCoverage(gcovValue[0]);
        
        // Clean the GCOV generated reports
        new Subprocess(Arrays.asList("rm", "-rf", "*.gcno", "*.gcda"), 0, new NullProcessListener()).waitFor();
        
        // Cleanup
        if (!arguments.isGcDisabled()) {
            tempFile.delete();
        }
        
        return outcome;
    }
    
    private static class GcovSubprocessListener implements SubprocessListener {

        // Having the array is a simple trick to pass the gcovValue by
        // reference.
        private double[] gcovValue;

        public GcovSubprocessListener(double[] gcovValue) {
            this.gcovValue = gcovValue;
        }

        @Override
        public void onStdoutLine(String line) {
            String[] colonSplit = line.split(":");
            if (colonSplit.length != 2) {
                return;
            }
            
            String colonLast = colonSplit[1];
            String[] percentSplit = colonLast.split("%");
            if (percentSplit.length != 2) {
                return;
            }
            
            String percentFirst = percentSplit[0];
            try {
                double finalPercentage = Double.parseDouble(percentFirst);
                gcovValue[0] = finalPercentage;
            } catch (NumberFormatException e) {
                // Do nothing
            }
        }

        @Override
        public void onStderrLine(String line) {
        }

        @Override
        public void onExit(int code) {
        }

        @Override
        public void onTimeout() {
            
        }
        
    }
    
    private static class NullProcessListener implements SubprocessListener {

        @Override
        public void onStdoutLine(String line) {

        }

        @Override
        public void onStderrLine(String line) {
            
        }

        @Override
        public void onExit(int code) {
            
        }

        @Override
        public void onTimeout() {
            
        }
        
        
    }

}
