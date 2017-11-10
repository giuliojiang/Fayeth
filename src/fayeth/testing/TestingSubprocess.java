package fayeth.testing;

import fayeth.subprocess.Subprocess;
import fayeth.subprocess.SubprocessListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This executable shows how to launch a subprocess using the
 * Subprocess class and by defining an event listener to receive
 * output and signals.
 */
public class TestingSubprocess {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> cmd = Collections.singletonList("free");

        Subprocess sp = new Subprocess(cmd, 1, new SubprocessListener() {
            @Override
            public void onStdoutLine(String line) {
                System.out.println("STDOUT ["+line+"]");
            }

            @Override
            public void onStderrLine(String line) {
                System.out.println("STDERR ["+line+"]");
            }

            @Override
            public void onExit(int code) {
                System.out.println("EXIT ["+code+"]");
            }

            @Override
            public void onTimeout() {
                System.out.println("TIMEOUT");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("EXCEPTION");
                t.printStackTrace();
            }
        });
        sp.waitFor();
    }

}
