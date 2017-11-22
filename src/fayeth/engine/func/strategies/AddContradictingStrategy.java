package fayeth.engine.func.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fayeth.cnf.CNF;
import fayeth.engine.Expectation;
import fayeth.engine.Outcome;
import fayeth.engine.Satisfiability;
import fayeth.engine.Strategy;
import fayeth.engine.func.CNFChain;
import fayeth.engine.func.FuncCNFCollection;
import fayeth.engine.func.FuncTestableInput;

public class AddContradictingStrategy implements Strategy<FuncTestableInput> {
    
    private final FuncCNFCollection collection;
    private final Random random;
    private final List<AdditionalClauses> additionalClauses = new ArrayList<>();
      
    public AddContradictingStrategy(FuncCNFCollection collection, Random random) {
        this.collection = collection;
        this.random = random;
        initAdditionalClauses();
    }

    private void initAdditionalClauses() {
        /*
            1 2 0
            -2 -1 0
            1 -2 0
            -1 2 0
         */
        AdditionalClauses adc = new AdditionalClauses(random);
        adc.addClause(1, 2);
        adc.addClause(-2, -1);
        adc.addClause(1, -2);
        adc.addClause(-1, 2);
        additionalClauses.add(adc);
        
        /*
            1 2 0
            -2 3 0
            1 -2 0
            -1 2 0
            -1 -2 -3 0
         */
        adc = new AdditionalClauses(random);
        adc.addClause(1, 2);
        adc.addClause(-2, 3);
        adc.addClause(1, -2);
        adc.addClause(-1, 2);
        adc.addClause(-1, -2, -3);
        additionalClauses.add(adc);
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
        
        // Select a random mutator
        AdditionalClauses mutator = additionalClauses.get(random.nextInt(additionalClauses.size()));
        
        // Get the next new variable
        int nextNewVar = mutated.nextNewVariable();
        
        // Rename each variable that needs renaming
        mutator.initPreparation();
        for (int toBeRenamed : mutator.getUnrenamedVariables()) {
            mutator.rename(toBeRenamed, nextNewVar);
            nextNewVar++;
        }
        
        // Inject
        mutator.inject(mutated);
        
        // Add to the chain
        chain.addGeneratedCNF(mutated);
       
        // Return the object
        return new FuncTestableInput() {

            @Override
            public String asString() {
                return mutated.asString();
            }

            @Override
            public Expectation getExpectation() {
                return new Expectation(Satisfiability.UNSAT, Satisfiability.UNSAT);
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
    
    private static class AdditionalClauses {
        
        // The original generator clauses
        private List<List<Integer>> generatorClauses = new ArrayList<>();
        // The original variables
        private Set<Integer> generatorVariables = new HashSet<>();
        
        // Mapping of names
        private Map<Integer, Integer> renamingMap = new HashMap<>();
        
        private final Random random;
        
        // Constructor
        private AdditionalClauses(Random random) {
            this.random = random;
        }
        
        // Adds a clause to the generator clauses
        public void addClause(int... literals) {
            List<Integer> newClause = new ArrayList<>();
            for (int lit : literals) {
                newClause.add(lit);
                generatorVariables.add(Math.abs(lit));
            }
            generatorClauses.add(newClause);
        }
        
        public Set<Integer> getUnrenamedVariables() {
            return generatorVariables;
        }
        
        // First method to call before starting a new injection
        public void initPreparation() {
            renamingMap.clear();
        }
        
        // Rename variable from into variable to
        public void rename(int from, int to) {
            from = Math.abs(from);
            to = Math.abs(to);
            
            renamingMap.put(from, to);
            renamingMap.put(-from, -to);
        }
        
        // Injects the current additional clauses into the given CNF
        public void inject(CNF cnf) {
            List<List<Integer>> newClauses = new ArrayList<>();
            
            for (List<Integer> aClause : generatorClauses) {
                List<Integer> newClause = new ArrayList<>();
                for (Integer aLiteral : aClause) {
                    if (!renamingMap.containsKey(aLiteral)) {
                        throw new RuntimeException("Could not find renaming map for literal ["+aLiteral+"]. A complete renaming map is necessary");
                    }
                    Integer newLiteral = renamingMap.get(aLiteral);
                    newClause.add(newLiteral);
                }
                newClauses.add(newClause);
            }
            
            for (List<Integer> aClause : newClauses) {
                cnf.addClause(aClause);
            }
            
            cnf.shuffleClauses(random);
        }
        
    }

}
