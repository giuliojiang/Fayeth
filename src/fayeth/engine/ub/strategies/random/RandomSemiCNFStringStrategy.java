package fayeth.engine.ub.strategies.random;

import java.util.Random;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

public class RandomSemiCNFStringStrategy implements Strategy<TestableInput> {

    private final Random random;

    public RandomSemiCNFStringStrategy(Random random) {
        this.random = random;
    }

    @Override
    public TestableInput generateNextInput() {
        return new RandomSemiCNFStringInput(random);
    }

    @Override
    public void recordOutcome(Outcome<TestableInput> outcome) {
        // Does nothing
    }

}
