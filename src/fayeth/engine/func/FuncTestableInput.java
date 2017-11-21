package fayeth.engine.func;

import fayeth.engine.Expectation;
import fayeth.engine.TestableInput;

public interface FuncTestableInput extends TestableInput {

    Expectation getExpectation();
    
    String getGenesisFileName();

    void recordCoverage(double d);
    
}
