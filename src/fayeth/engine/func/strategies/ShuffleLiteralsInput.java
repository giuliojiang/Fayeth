package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Satisfiability;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleLiteralsInput implements FuncTestableInput {

    private final CNF formula;
    private final CNF genesisFormula;

    public ShuffleLiteralsInput(CNF formula, Random random) {
        List<List<Integer>> clauses = formula.getClauses();
        List<List<Integer>> shuffledLiteralClauses = new ArrayList<>();
        for (List<Integer> clause : clauses) {
            List<Integer> shuffledClause = new ArrayList<>(clause);
            shuffledClause.sort((o1, o2) -> random.nextBoolean() ? -1 : 1);
            shuffledLiteralClauses.add(shuffledClause);
        }

        this.formula = new CNF(shuffledLiteralClauses, formula.getVariables());
        this.genesisFormula = formula;
    }

    @Override
    public String asString() {
        return formula.asString();
    }

    @Override
    public Expectation getExpectation() {
        return new Expectation(Satisfiability.SAT, Satisfiability.UNSAT);
    }

    @Override
    public String getGenesisFileName() {
        return genesisFormula.getSourceFileName();
    }
}
