package fayeth.engine.ub.strategies.random;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

import java.util.Random;

public class RandomBrokenCNFStrategy implements Strategy<TestableInput> {

    private final Random random;

    public RandomBrokenCNFStrategy(Random random) {
        this.random = random;
    }

    @Override
    public TestableInput generateNextInput() {
        return new RandomBrokenCNFInput(random);
    }

    @Override
    public void recordOutcome(Outcome<TestableInput> outcome) {
        // Does nothing
    }

}
