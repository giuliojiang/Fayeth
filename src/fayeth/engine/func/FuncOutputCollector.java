package fayeth.engine.func;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fayeth.engine.Expectation;
import fayeth.engine.Outcome;
import fayeth.program.state.Args;

public class FuncOutputCollector {

    private final Args arguments;
    private String outputDirPath;
    
    private Map<String, LinkedList<String>> fileNamesMap = new HashMap<>();
    private Map<String, Integer> nameCounterMap = new HashMap<>();

    public FuncOutputCollector(Args arguments) {
        this.arguments = arguments;
        this.outputDirPath = arguments.getOutputPath();
    }
    
    public void collect(Outcome<FuncTestableInput> outcome) throws IOException {
        FuncTestableInput input = outcome.getInput();
        Expectation expectation = input.getExpectation();
    }
    
}
