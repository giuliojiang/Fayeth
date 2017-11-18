package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Satisfiability;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleClausesInput implements FuncTestableInput {

    private final CNF cnf;
    private final CNF genesisFormula;

    public ShuffleClausesInput(CNF cnf, Random random) {
        List<List<Integer>> shuffledClauses = new ArrayList<>(cnf.getClauses());
        shuffledClauses.sort(((o1, o2) -> random.nextBoolean() ? -1 : 1));

        this.cnf = new CNF(shuffledClauses, cnf.getVariables());
        this.genesisFormula = cnf;
    }

    @Override
    public String asString() {
        return cnf.asString();
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
