package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Satisfiability;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleClausesInput implements FuncTestableInput {

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

    @Override
    public Expectation getExpectation() {
        return new Expectation(Satisfiability.SAT, Satisfiability.UNSAT);
    }

    @Override
    public String getGenesisFileName() {
        return cnf.getSourceFileName();
    }
}
