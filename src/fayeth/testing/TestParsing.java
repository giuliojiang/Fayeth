package fayeth.testing;

import fayeth.cnf.CNF;

import java.io.IOException;

public class TestParsing {

    public static void main(String[] args) throws IOException {
        String fname = "src/TEST/test1.cnf";
        CNF cnf = CNF.fromFile(fname);
        System.out.println(cnf);
        cnf.toFile("test2.cnf");
    }
}
