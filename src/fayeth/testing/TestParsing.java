package fayeth.testing;

import java.io.File;
import java.io.IOException;

import fayeth.cnf.CNF;
import fayeth.util.Log;
import fayeth.util.TempFile;

public class TestParsing {

    public static void main(String[] args) throws IOException {
        String fname = "src/TEST/test1.cnf";
        CNF cnf = CNF.fromFile(fname);
        System.out.println(cnf);
        File f = TempFile.writeTemporaryFile(cnf);
        Log.info("Written to " + f.getAbsolutePath());
    }
}
