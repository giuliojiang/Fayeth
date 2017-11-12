package fayeth.engine.ub.strategies.random;

import fayeth.cnf.CNF;
import fayeth.engine.TestableInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomBrokenCNFInput implements TestableInput {
    final private Random random;

    RandomBrokenCNFInput(Long seed) {
        this.random = new Random(seed);
    }

    RandomBrokenCNFInput() {
        this.random = new Random();
    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public CNF asCNF() {
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
        // Empty is fine
        Set<Integer> vars = new HashSet<>(random.nextInt(300));
        return new CNF(clauses, vars);
    }

}
