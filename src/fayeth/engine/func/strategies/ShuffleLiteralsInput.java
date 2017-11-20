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

public class ShuffleLiteralsInput implements FuncTestableInput {

    private final CNF formula;
    private final CNF genesisFormula;

    public ShuffleLiteralsInput(CNFChain cnfChain, Random random) {
        CNF lastFormula = cnfChain.getLast();
        List<List<Integer>> clauses = lastFormula.getClauses();
        List<List<Integer>> shuffledLiteralClauses = new ArrayList<>();
        for (List<Integer> clause : clauses) {
            List<Integer> shuffledClause = new ArrayList<>(clause);
            Collections.shuffle(shuffledClause, random);
            shuffledLiteralClauses.add(shuffledClause);
        }

        this.formula = new CNF(shuffledLiteralClauses, lastFormula.getVariables());
        cnfChain.addGeneratedCNF(this.formula);
        this.genesisFormula = cnfChain.getBase();
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
