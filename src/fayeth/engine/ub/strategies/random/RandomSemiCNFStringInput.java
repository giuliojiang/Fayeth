package fayeth.engine.ub.strategies.random;

import fayeth.engine.TestableInput;

import java.util.Random;


public class RandomSemiCNFStringInput implements TestableInput {
    final private Random random;

    RandomSemiCNFStringInput(Random random) {
        this.random = random;
    }

    @Override
    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("p cnf %d %d\n", random.nextInt(1000), random.nextInt(1000)));
        final int length = random.nextInt(4096);

        final byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        for (byte b : randomBytes) {
            sb.append((char) b);
        }
        return sb.toString();
    }

}
