package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Satisfiability;
import fayeth.engine.func.CNFChain;
import fayeth.engine.func.FuncTestableInput;

public class ShuffleClausesInput implements FuncTestableInput {

    private final CNF cnf;
    private final CNF genesisFormula;

    public ShuffleClausesInput(CNFChain cnf, Random random) {
        CNF lastCNF = cnf.getLast();
        List<List<Integer>> shuffledClauses = new ArrayList<>(lastCNF.getClauses());
        Collections.shuffle(shuffledClauses, random);

        this.cnf = new CNF(shuffledClauses, lastCNF.getVariables());
        this.genesisFormula = cnf.getBase();
        cnf.addGeneratedCNF(this.cnf);
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
