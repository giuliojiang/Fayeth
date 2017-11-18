package fayeth.engine.func.strategies;

import fayeth.cnf.CNF;
import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

import java.util.List;
import java.util.Random;

public class ShuffleLiteralsStrategy implements Strategy{

    private final List<CNF> formula;
    private Random random;
    private int seenBefore = -1;

    public ShuffleLiteralsStrategy(Random random, List<CNF> formula) {
        this.random = random;
        this.formula = formula;
    }

    @Override
    public TestableInput generateNextInput() {
        int randInt = random.nextInt(formula.size());
        while (randInt == seenBefore) {
            randInt = random.nextInt(formula.size());
        }
        return new ShuffleLiteralsInput(formula.get(randInt), random);
    }

    @Override
    public void recordOutcome(Outcome outcome) {
        // Do nothing
    }

}
