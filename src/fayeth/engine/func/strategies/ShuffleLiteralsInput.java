package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Satisfiability;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleLiteralsInput implements FuncTestableInput {

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

    @Override
    public Expectation getExpectation() {
        return new Expectation(Satisfiability.SAT, Satisfiability.UNSAT);
    }

    @Override
    public String getGenesisFileName() {
        return formula.getSourceFileName();
    }
}
