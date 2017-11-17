package fayeth.engine.func.strategies;

import fayeth.engine.Expectation;
import fayeth.engine.Outcome;
import fayeth.engine.Satisfiability;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

public class NullStrategy implements Strategy {

    @Override
    public TestableInput generateNextInput() {
        return new TestableInput() {
            
            @Override
            public String asString() {
                return "blah";
            }

            @Override
            public Expectation getExpectation() {
                // TODO Auto-generated method stub
                return new Expectation(Satisfiability.UNKNOWN, Satisfiability.UNSAT);
            }
            
        };
    }

    @Override
    public void recordOutcome(Outcome outcome) {
        // TODO Auto-generated method stub
        
    }

}
