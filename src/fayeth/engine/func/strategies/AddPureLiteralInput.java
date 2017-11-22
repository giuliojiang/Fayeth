package fayeth.engine.func.strategies;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Satisfiability;
import fayeth.engine.func.CNFChain;
import fayeth.engine.func.FuncTestableInput;

import java.util.*;

class AddPureLiteralInput implements FuncTestableInput {
    private final CNF cnf;
    private final CNF genesisFormula;

    AddPureLiteralInput(Random random, CNFChain cnfChain) {

        final CNF lastCNF = random.nextBoolean() ? cnfChain.getHighestCoverage() : cnfChain.getLast();
        final Integer newLit = cnfChain.getBase().getVariables().size() + 1;
        int randomNumClauses = random.nextInt(lastCNF.getClauses().size() + 1);

        final int sign = random.nextBoolean() ? 1 : -1;
        final List<List<Integer>> newClauses = new ArrayList<>(lastCNF.getClauses());
        for(int i = 0; i < randomNumClauses; i++) {
            int idx = random.nextInt(newClauses.size());
            newClauses.get(idx).add(sign * newLit);
        }
        System.out.println("Added literal "+newLit);
        Set<Integer> newVars = new HashSet<>(lastCNF.getVariables());
        newVars.add(newLit);
        this.cnf = new CNF(newClauses, newVars);
        this.genesisFormula = cnfChain.getBase();
        cnfChain.addGeneratedCNF(this.cnf);
    }

    @Override
    public Expectation getExpectation() {
        return new Expectation(Satisfiability.SAT, Satisfiability.UNSAT);
    }

    @Override
    public String getGenesisFileName() {
        return genesisFormula.getSourceFileName();
    }

    @Override
    public void recordCoverage(double d) {
        this.cnf.recordCoverage(d);
    }

    @Override
    public String asString() {
        return cnf.asString();
    }
}
