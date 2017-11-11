package fayeth.subprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Subprocess {

    private final Process process;
    private final boolean timeoutEnabled;
    private final Thread stdoutRunner;
    private final Thread stderrRunner;

    public Subprocess(List<String> cmd, int timeout, SubprocessListener eventListener) throws IOException {

        List<String> theCmd = new ArrayList<>();

        // Add `timeout` command
        if (timeout > 0) {
            theCmd.add("timeout");
            theCmd.add(String.valueOf(timeout));
        }
        this.timeoutEnabled = timeout > 0;

        // Turn off pipe buffering. Solves issues when using `timeout` to kill the subprocess
        // stdbuf -i0 -o0 -e0
        // See https://unix.stackexchange.com/a/194565 for details
        theCmd.add("stdbuf");
        theCmd.add("-i0");
        theCmd.add("-o0");
        theCmd.add("-e0");

        // Append rest of the command
        theCmd.addAll(cmd);

        // Execute command
        process = Runtime.getRuntime().exec(theCmd.toArray(new String[0]));

        // Create stdout loop thread
        this.stdoutRunner = new Thread(() -> {
            Scanner stdout = null;
            try {
                stdout = new Scanner(process.getInputStream());
                while (stdout.hasNextLine()) {
                    eventListener.onStdoutLine(stdout.nextLine());
                }
                int code = process.waitFor();
                if (timeoutEnabled && code == 124) {
                    eventListener.onTimeout();
                } else {
                    eventListener.onExit(code);
                }
            } catch (InterruptedException e) {
                eventListener.onError(e);
            } finally {
                if (stdout != null) {
                    stdout.close();
                }
            }
        });
        stdoutRunner.start();

        // Create stderr loop thread
        this.stderrRunner = new Thread(() -> {
            Scanner stderr = null;
            try {
                stderr = new Scanner(process.getErrorStream());
                while (stderr.hasNextLine()) {
                    eventListener.onStderrLine(stderr.nextLine());
                }
            }
            finally {
                if (stderr != null) {
                    stderr.close();
                }
            }
        });
        stderrRunner.start();
    }

    public void waitFor() throws InterruptedException {
        stdoutRunner.join();
        stderrRunner.join();
    }

}
