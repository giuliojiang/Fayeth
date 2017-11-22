package fayeth.engine.func.strategies;

import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.func.FuncCNFCollection;
import fayeth.engine.func.FuncTestableInput;

import java.util.Random;

public class AddPureLiteralStrategy implements Strategy<FuncTestableInput> {
    private final FuncCNFCollection formula;
    private final Random random;
    private int seenBefore = -1;

    public AddPureLiteralStrategy(Random random, FuncCNFCollection formula) {
        this.formula = formula;
        this.random = random;
    }

    @Override
    public FuncTestableInput generateNextInput() {
        int randInt = random.nextInt(formula.size());
        while (randInt == seenBefore) {
            randInt = random.nextInt(formula.size());
        }
        seenBefore = randInt;
        return new AddPureLiteralInput(random, formula.get(randInt));
    }

    @Override
    public void recordOutcome(Outcome<FuncTestableInput> outcome) {

    }
}
