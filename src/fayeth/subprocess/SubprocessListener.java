package fayeth.subprocess;

public interface SubprocessListener {

    void onStdoutLine(String line);

    void onStderrLine(String line);

    void onExit(int code);

    void onTimeout();

    void onError(Throwable t);

}
