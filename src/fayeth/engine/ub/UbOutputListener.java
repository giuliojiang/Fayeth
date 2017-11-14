package fayeth.engine.ub;

import fayeth.subprocess.SubprocessListener;
import fayeth.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UbOutputListener implements SubprocessListener {

    private UbTask ubTask;
    private boolean hasFailed = false;
    public UbOutputListener(UbTask ubTask) {
        this.ubTask = ubTask;
    }

    @Override
    public void onStdoutLine(String line) {
        // ASAN error
        Pattern pattern = Pattern.compile(".*ERROR: AddressSanitizer: ([a-zA-Z_0-9-]+) .*");
        Matcher matcher = pattern.matcher(line);
        if(matcher.matches()) {
            hasFailed = true;
            ubTask.onBugFound(String.format("AddressSanitizer: %s", matcher.group(1)));
            return;
        }
        // ASAN allocation failure
        pattern = Pattern.compile(".*AddressSanitizer's (.*?)");
        matcher = pattern.matcher(line);
        if(matcher.matches()) {
            hasFailed = true;
            ubTask.onBugFound(String.format("AddressSanitizer: Failure: %s", matcher.group(1)));
            return;
        }
        // UBSAN error
        pattern = Pattern.compile(".*?: runtime error: (.*?)");
        matcher = pattern.matcher(line);
        if(matcher.matches()) {
            hasFailed = true;
            ubTask.onBugFound(String.format("UBSanitizer: %s", matcher.group(1)));
            return;
        }
        if(!hasFailed) {
            Log.info("STDOUT:" + line);
        }
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
