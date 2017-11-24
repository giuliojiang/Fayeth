package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Outcome;
import fayeth.engine.Satisfiability;
import fayeth.engine.Strategy;
import fayeth.engine.func.CNFChain;
import fayeth.engine.func.FuncCNFCollection;
import fayeth.engine.func.FuncTestableInput;

public class AddUnitClausesStrategy implements Strategy<FuncTestableInput> {

    private final FuncCNFCollection collection;
    private final Random random;
    
    public AddUnitClausesStrategy(FuncCNFCollection collection, Random random) {
        this.collection = collection;
        this.random = random;
    }

    @Override
    public FuncTestableInput generateNextInput() {
        CNFChain chain = random.nextBoolean() ? collection.selectLeastUsed() : collection.selectRandom(random);
        CNF base;
        if (random.nextBoolean()) {
            // Get most successful from the chain
            base = chain.getHighestCoverage();
        } else {
            if (random.nextBoolean()) {
                // Get last from chain
                base = chain.getLast();
            } else {
                // Get random from chain
                base = chain.getRandom(random);
            }
        }
        
        return mutate(base, chain);
    }

    private FuncTestableInput mutate(CNF base, CNFChain chain) {
        CNF mutated = CNF.copyFrom(base);
        
        // Give 10 attempts at finding an existing clause which
        // is not already a unit clause
        List<Integer> targetClause = null;
        for (int i = 0; i < 10; i++) {
            List<Integer> candidateClause = mutated.getRandomClause(random);
            if (candidateClause.size() >= 2) {
                targetClause = candidateClause;
                break;
            }
        }
        targetClause = mutated.getRandomClause(random);
        
        // If a target clause was selected, apply the mutation
        if (targetClause != null) {
            // Get a new variable
            Integer targetVariable = mutated.nextNewVariable();

            // Randomly negate the literal
            if (random.nextBoolean()) {
                targetVariable = -targetVariable;
            }
            
            // Add the negated literal to the clause
            targetClause.add(-targetVariable);
            
            // Add the new unit clause
            List<Integer> newClause = new ArrayList<>();
            newClause.add(targetVariable);
            mutated.addClause(newClause);
        }
        
        // Shuffle the formula
        mutated.shuffleClauses(random);
        
        return new FuncTestableInput() {

            @Override
            public String asString() {
                return mutated.asString();
            }

            @Override
            public Expectation getExpectation() {
                return new Expectation(Satisfiability.SAT, Satisfiability.UNSAT);
            }

            @Override
            public String getGenesisFileName() {
                return mutated.getSourceFileName();
            }

            @Override
            public void recordCoverage(double coverage) {
                mutated.recordCoverage(coverage);
            }
            
        };
    }

    @Override
    public void recordOutcome(Outcome<FuncTestableInput> outcome) {
        // Does nothing
    }

}
