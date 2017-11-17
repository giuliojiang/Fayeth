package fayeth.subprocess;

import fayeth.util.Log;

public interface SubprocessListener {

    void onStdoutLine(String line);

    void onStderrLine(String line);

    void onExit(int code);

    void onTimeout();

    default void onError(Throwable t) {
        Log.error("Error in ["+this.getClass().getSimpleName()+"]", t);
    }

}
