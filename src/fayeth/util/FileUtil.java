package fayeth.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import fayeth.engine.TestableInput;

public class FileUtil {

    private static final String TEMP_DIR = "/tmp/";
    private static final Random rand = new Random();

    public static synchronized void writeToFile(TestableInput input, File file) throws IOException {
        file.createNewFile();
        PrintWriter pw = new PrintWriter(file);
        pw.print(input.asString());
        pw.close();
    }
    
    public static synchronized File writeToFile(TestableInput input, String filePath) throws IOException {
        File file = new File(filePath);
        writeToFile(input, file);
        return file;
    }
    
    public static synchronized File writeTemporaryFile(TestableInput input) throws IOException {
        File tempFile = createTemporaryFile();
        writeToFile(input, tempFile);
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
        throw new RuntimeException("Could not create a new temporary file in [" + TEMP_DIR + "]");
    }

}
