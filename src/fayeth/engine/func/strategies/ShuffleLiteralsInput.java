package fayeth.engine.func.strategies;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.TestableInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ShuffleLiteralsInput implements TestableInput {

    private final Random random;
    private final CNF formula;

    public ShuffleLiteralsInput(CNF formula, Random random) {
        this.formula = formula;
        this.random = random;
    }

    @Override
    public String asString() {
        List<List<Integer>> clauses = formula.getClauses();
        List<List<Integer>> shuffledLiteralClauses = new ArrayList<>();
        for (List<Integer> clause : clauses) {
            List<Integer> shuffledClause = new ArrayList<>(clause);
            shuffledClause.sort((o1, o2) -> random.nextBoolean() ? -1 : 1);
            shuffledLiteralClauses.add(shuffledClause);
        }

        return new CNF(shuffledLiteralClauses, formula.getVariables()).asString();
    }

}
