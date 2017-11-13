package fayeth.engine.ub.strategies.random;

import fayeth.cnf.CNF;
import fayeth.engine.TestableInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomCorrectCNFInput implements TestableInput {

    private final Random random;

    RandomCorrectCNFInput(Random random) {
        this.random = random;
    }

    @Override
    public String asString() {
        final int numClauses = random.nextInt(2048);
        final List<List<Integer>> clauses = new ArrayList<>();
        final int numVariables = random.nextInt(200);
        Set<Integer> variables = new HashSet<>(numVariables);
        for (int i = 0; i < numClauses; i++) {
            List<Integer> clause = IntStream
                    .range(1, numVariables)
                    .filter(lit -> random.nextBoolean())
                    .map(lit -> random.nextBoolean() ? lit * -1 : lit)
                    .boxed()
                    .peek(variables::add)
                    .collect(Collectors.toList());
            clauses.add(clause);
        }

        return new CNF(clauses, variables).asString();
    }
}
