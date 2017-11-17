package fayeth.engine;

public interface TestableInput {

    String asString();
    
    default Expectation getExpectation() {
        return null;
    }
    
}
