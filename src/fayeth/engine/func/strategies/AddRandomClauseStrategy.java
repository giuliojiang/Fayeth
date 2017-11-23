package fayeth.engine.func.strategies;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Outcome;
import fayeth.engine.Satisfiability;
import fayeth.engine.Strategy;
import fayeth.engine.func.CNFChain;
import fayeth.engine.func.FuncCNFCollection;
import fayeth.engine.func.FuncTestableInput;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AddRandomClauseStrategy implements Strategy<FuncTestableInput> {


    private final FuncCNFCollection formulae;
    private final Random random;
    private int lastSeen = -1;

    public AddRandomClauseStrategy(Random random, FuncCNFCollection formulae) {
        this.formulae = formulae;
        this.random = random;
    }

    @Override
    public FuncTestableInput generateNextInput() {
        int randInt = random.nextInt(formulae.size());
        while(randInt == lastSeen) {
            randInt = random.nextInt(formulae.size());
        }
        lastSeen = randInt;
        final CNFChain cnfChain = formulae.get(randInt);
        final CNF oldFormula = random.nextBoolean() ? cnfChain.getHighestCoverage() : cnfChain.getLast();
        final List<Integer> newClause = oldFormula.getVariables()
                                                  .stream()
                                                  .filter(l -> random.nextBoolean())
                                                  .map(l -> random.nextBoolean() ? -1 * l : l)
                                                  .collect(Collectors.toList());

        final CNF newFormula = CNF.copyFrom(oldFormula);
        newFormula.addClause(newClause);
        cnfChain.addGeneratedCNF(newFormula);
        return new FuncTestableInput() {
            @Override
            public Expectation getExpectation() {
                return new Expectation(Satisfiability.UNKNOWN, Satisfiability.UNSAT);
            }

            @Override
            public String getGenesisFileName() {
                return cnfChain.getBase().getSourceFileName();
            }

            @Override
            public void recordCoverage(double d) {
                newFormula.recordCoverage(d);
            }

            @Override
            public String asString() {
                if(newClause.isEmpty()) {
                    return oldFormula.asString();
                }
                return newFormula.asString();
            }
        };
    }

    @Override
    public void recordOutcome(Outcome<FuncTestableInput> outcome) {

    }
}
