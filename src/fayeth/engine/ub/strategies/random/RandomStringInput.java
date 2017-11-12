package fayeth.engine.ub.strategies.random;

import fayeth.engine.TestableInput;

import java.util.Random;


public class RandomStringInput implements TestableInput {
    final private Random random;

    RandomStringInput(Random random) {
        this.random = random;
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

}
