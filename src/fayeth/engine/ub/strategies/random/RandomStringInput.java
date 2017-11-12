package fayeth.engine.ub.strategies.random;

import fayeth.cnf.CNF;
import fayeth.engine.TestableInput;

import java.util.*;
import java.util.stream.Collectors;

public class RandomStringInput implements TestableInput {
    final private Random random;

    RandomStringInput(Long seed) {
        this.random = new Random(seed);
    }

    RandomStringInput() {
        this.random = new Random();
    }

    @Override
    public String asString() {
        final int length = random.nextInt(2048);
        final StringBuilder sb = new StringBuilder();
        final byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        for(byte b : randomBytes) {
            sb.append(b);
        }
        return sb.toString();
    }

    @Override
    public CNF asCNF() {
       return null;
    }

}
