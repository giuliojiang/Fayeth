package fayeth.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.TestableInput;

public class TempFile {

    private static final String TEMP_DIR = "/tmp/";
    private static final Random rand = new Random();
    
    public static synchronized File writeTemporaryFile(TestableInput input) throws IOException {
        File tempFile = createTemporaryFile();
        CNF cnf = input.asCNF();
        if (cnf != null) {
            cnf.toFile(tempFile);
        } else {
            PrintWriter pw = new PrintWriter(tempFile);
            pw.print(input.asString());
            pw.close();
        }
        return tempFile;
    }
    
    private static synchronized File createTemporaryFile() throws IOException {
        for (int i = 0; i < 100; i++) {
            String name = String.valueOf(rand.nextLong());
            File f = new File(TEMP_DIR + name);
            boolean success = f.createNewFile();
            if (success) {
                return f;
            }
        }
        throw new RuntimeException("Could not create a new temporary file in ["+TEMP_DIR+"]");
    }
    
}
