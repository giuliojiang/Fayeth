package fayeth.engine.ub.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fayeth.cnf.CNF;
import fayeth.engine.Outcome;
import fayeth.engine.Strategy;
import fayeth.engine.TestableInput;

/**
 * Generates valid 2CNFs up to 5CNFs.
 *
 */
public class SquareCNFStrategy implements Strategy<TestableInput> {

    private final Random random;

    public SquareCNFStrategy(Random random) {
        this.random = random;
    }

    @Override
    public TestableInput generateNextInput() {
        int clauseSize = random.nextInt(4) + 2;
        int numberVariables = random.nextInt(200) + 10;
        int numberClauses = 2 * numberVariables;
        
        CNF cnf = new CNF();
        
        for (int i = 0; i < numberClauses; i++) {
            List<Integer> aClause = new ArrayList<>();
            for (int j = 0; j < clauseSize; j++) {
                int aLiteral = random.nextInt(numberVariables) + 1;
                if (random.nextBoolean()) {
                    aLiteral = -aLiteral;
                }
                aClause.add(aLiteral);
            }
            cnf.addClause(aClause);
        }
        
        cnf.consolidateVariables();
        
        return new TestableInput() {

            @Override
            public String asString() {
                return cnf.asString();
            }
            
        };
    }

    @Override
    public void recordOutcome(Outcome<TestableInput> outcome) {
        
    }

}
