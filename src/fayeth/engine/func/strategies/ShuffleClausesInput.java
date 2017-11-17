package fayeth.engine.func.strategies;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.TestableInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShuffleClausesInput implements TestableInput {

    private final Random random;
    private final CNF cnf;

    public ShuffleClausesInput(CNF cnf, Random random) {
        this.cnf = cnf;
        this.random = random;
    }

    @Override
    public String asString() {
        List<List<Integer>> shuffledClauses = new ArrayList<>(cnf.getClauses());
        shuffledClauses.sort(((o1, o2) -> random.nextBoolean() ? -1 : 1));

        return new CNF(shuffledClauses, cnf.getVariables()).asString();
    }

}
