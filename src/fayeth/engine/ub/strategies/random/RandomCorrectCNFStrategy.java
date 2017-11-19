package fayeth.engine.ub.strategies.random;

import java.util.Random;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

public class RandomCorrectCNFStrategy implements Strategy<TestableInput> {
    private final Random random;

    public RandomCorrectCNFStrategy(Random random) {
        this.random = random;
    }

    @Override
    public TestableInput generateNextInput() {
        return new RandomCorrectCNFInput(random);
    }

    @Override
    public void recordOutcome(Outcome<TestableInput> outcome) {

    }
}
