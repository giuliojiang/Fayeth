package fayeth.engine.func;

import fayeth.subprocess.SubprocessListener;
import fayeth.util.Log;

public class FuncOutputListener implements SubprocessListener {

    private final FuncTask task;
    
    public FuncOutputListener(FuncTask task) {
        this.task = task;
    }

    @Override
    public void onStdoutLine(String line) {
        Log.info("FUNCOUTPUT STDOUT ["+line+"]");
    }

    @Override
    public void onStderrLine(String line) {
        Log.info("FUNCOUTPUT STDERR ["+line+"]");
    }

    @Override
    public void onExit(int code) {
        task.onCompletion();
    }

    @Override
    public void onTimeout() {
        task.onCompletion();
    }

}
