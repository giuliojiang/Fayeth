package fayeth.engine;

import java.util.Random;

public class RandomFactory {
    private Long seed;

    public RandomFactory(Long seed) {
        if(seed == null) {
            this.seed = System.currentTimeMillis();
        } else {
            this.seed = seed;
        }
    }

    public Random newRandom() {
        return new Random(seed++);
    }

    public Long getSeed() {
        return seed;
    }

}
