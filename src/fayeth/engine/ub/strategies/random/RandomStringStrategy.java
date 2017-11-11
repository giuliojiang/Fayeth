package fayeth.engine.ub.strategies.random;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

public class RandomStringStrategy implements Strategy {

    @Override
    public TestableInput generateNextInput() {
        return new RandomTestableInput();
    }

    @Override
    public void recordOutcome(Outcome outcome) {
        // Does nothing
    }

}
