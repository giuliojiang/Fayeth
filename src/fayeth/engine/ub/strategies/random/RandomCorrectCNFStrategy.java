package fayeth.engine.ub.strategies.random;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

import java.util.Random;

public class RandomCorrectCNFStrategy implements Strategy {
    private final Random random;

    public RandomCorrectCNFStrategy(Random random) {
        this.random = random;
    }

    @Override
    public TestableInput generateNextInput() {
        return new RandomCorrectCNFInput(random);
    }

    @Override
    public void recordOutcome(Outcome outcome) {

    }
}
