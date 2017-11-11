package fayeth.engine.ub;

import fayeth.subprocess.SubprocessListener;
import fayeth.util.Log;

public class UbOutputListener implements SubprocessListener {

    private UbTask ubTask;

    public UbOutputListener(UbTask ubTask) {
        this.ubTask = ubTask;
    }

    @Override
    public void onStdoutLine(String line) {
        Log.info("STDOUT:" + line);
    }

    @Override
    public void onStderrLine(String line) {
        Log.info("STDERR:" + line);
    }

    @Override
    public void onExit(int code) {
        Log.info("EXIT:" + code);
        ubTask.onCompletion();
    }

    @Override
    public void onTimeout() {
        Log.info("TIMEOUT");
        ubTask.onCompletion();
    }

    @Override
    public void onError(Throwable t) {
        Log.info("ERROR");
        ubTask.onCompletion();
    }

}
