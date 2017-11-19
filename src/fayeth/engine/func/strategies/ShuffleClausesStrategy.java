package fayeth.engine.func.strategies;

import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleClausesStrategy implements Strategy<FuncTestableInput> {

    private final Random random;
    private final List<CNF> formulae;
    private int seenBefore = -1;

    public ShuffleClausesStrategy(Random random, List<CNF> initialFormulae) {
        this.random = random;
        this.formulae = initialFormulae;
    }

    @Override
    public FuncTestableInput generateNextInput() {
        int randInt = random.nextInt(formulae.size());
        while (randInt == seenBefore) {
            randInt = random.nextInt(formulae.size());
        }
        return new ShuffleClausesInput(formulae.get(randInt), random);
    }

    @Override
    public void recordOutcome(Outcome<FuncTestableInput> outcome) {
        // Do nothing
    }
}
