package fayeth.engine.func;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import fayeth.engine.Expectation;
import fayeth.engine.Outcome;
import fayeth.engine.Satisfiability;
import fayeth.program.state.Args;

public class FuncOutputCollector {

    private static final int FILE_LIMIT = 50;
    
    private String outputDirPath;
    
    private Map<String, Integer> nameCounterMap = new HashMap<>();

    public FuncOutputCollector(Args arguments) {
        this.outputDirPath = arguments.getOutputPath();
    }
    
    // Public -----------------------------------------------------------------
    
    public void collect(Outcome<FuncTestableInput> outcome) throws IOException {
        FuncTestableInput input = outcome.getInput();
        Expectation expectation = input.getExpectation();
        String newName = makeNewName(input);
        String cnfPath = outputDirPath + "/" + newName + ".cnf";
        String txtPath = outputDirPath + "/" + newName + ".txt";
        writeCnf(cnfPath, input);
        writeTxt(txtPath, expectation);
    }


    // Private ----------------------------------------------------------------
    
    private String findFileName(FuncTestableInput input) {
        String fullName = input.getGenesisFileName();
        return fullName.substring(0, fullName.lastIndexOf('.'));
    }
    
    private int makeNextNumber(String inputName) {
        if (!nameCounterMap.containsKey(inputName)) {
            nameCounterMap.put(inputName, 0);
        }
        int oldValue = nameCounterMap.get(inputName);
        int newValue = (oldValue + 1) % FILE_LIMIT;
        nameCounterMap.put(inputName, newValue);
        return oldValue;
    }
    
    private String makeNewName(FuncTestableInput input) {
        String inputName = findFileName(input);
        int nextNumber = makeNextNumber(inputName);
        return inputName + "_" + String.format("%02d", nextNumber);
    }
    
    private void writeCnf(String cnfPath, FuncTestableInput input) throws FileNotFoundException {
        File f = new File(cnfPath);
        if (f.exists()) {
            f.delete();
        }
        
        PrintWriter pw = new PrintWriter(f);
        pw.write(input.asString());
        pw.close();
    }
    
    private void writeTxt(String txtPath, Expectation expectation) throws FileNotFoundException {
        File f = new File(txtPath);
        if (f.exists()) {
            f.delete();
        }
        
        PrintWriter pw = new PrintWriter(f);
        pw.println(Satisfiability.SAT + "->" + expectation.ifSat);
        pw.print(Satisfiability.UNSAT + "->" + expectation.ifUnsat);
        pw.close();
    }
    
}
