package fayeth.engine;

public interface Strategy {

    TestableInput generateNextInput();
    
    // Signal back to the Strategy an outcome of
    // a fuzz run using this strategy
    void recordOutcome(Outcome outcome);
    
}
