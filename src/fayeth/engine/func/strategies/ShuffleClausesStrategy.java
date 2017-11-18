package fayeth.engine.func.strategies;

import fayeth.cnf.CNF;
import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

import java.util.List;
import java.util.Random;

public class ShuffleClausesStrategy implements Strategy {

    private final Random random;
    private final List<CNF> formulae;
    private int seenBefore = -1;

    public ShuffleClausesStrategy(Random random, List<CNF> initialFormulae) {
        this.random = random;
        this.formulae = initialFormulae;
    }

    @Override
    public TestableInput generateNextInput() {
        int randInt = random.nextInt(formulae.size());
        while (randInt == seenBefore) {
            randInt = random.nextInt(formulae.size());
        }
        return new ShuffleClausesInput(formulae.get(randInt), random);
    }

    @Override
    public void recordOutcome(Outcome outcome) {
        // Do nothing
    }
}
