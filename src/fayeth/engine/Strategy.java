package fayeth.engine;

public interface Strategy<Tinput> {

    Tinput generateNextInput();
    
    // Signal back to the Strategy an outcome of
    // a fuzz run using this strategy
    void recordOutcome(Outcome<Tinput> outcome);
    
}
