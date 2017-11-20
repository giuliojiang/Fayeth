package fayeth.engine.func.strategies;

import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.func.FuncCNFCollection;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleLiteralsStrategy implements Strategy<FuncTestableInput> {

    private final FuncCNFCollection formula;
    private Random random;
    private int seenBefore = -1;

    public ShuffleLiteralsStrategy(Random random, FuncCNFCollection formula) {
        this.random = random;
        this.formula = formula;
    }

    @Override
    public FuncTestableInput generateNextInput() {
        int randInt = random.nextInt(formula.size());
        while (randInt == seenBefore) {
            randInt = random.nextInt(formula.size());
        }
        return new ShuffleLiteralsInput(formula.get(randInt), random);
    }

    @Override
    public void recordOutcome(Outcome<FuncTestableInput> outcome) {
        // Do nothing
    }

}
