package fayeth.engine.ub.strategies.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fayeth.cnf.CNF;
import fayeth.engine.TestableInput;

public class RandomBrokenCNFInput implements TestableInput {
    final private Random random;

    RandomBrokenCNFInput(Random random) {
        this.random = random;
    }

    @Override
    public String asString() {
        final int numClauses = random.nextInt(2048);
        final List<List<Integer>> clauses = new ArrayList<>();
        for(int i = 0; i < numClauses; i++) {
            final int clauseSize = random.nextInt(15);
            final List<Integer> randomClause = random.ints(clauseSize, 0, 200)
                    .map(lit -> random.nextBoolean() ? lit * -1 : lit)
                    .boxed()
                    .collect(Collectors.toList());
            clauses.add(randomClause);

        }
        int numVars = random.nextInt(300);
        Set<Integer> vars = IntStream.range(-1, numVars).boxed().collect(Collectors.toSet());

        return new CNF(clauses, vars).asString();
    }

}
