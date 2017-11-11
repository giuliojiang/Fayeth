package fayeth.engine.ub.strategies.random;

import fayeth.cnf.CNF;
import fayeth.engine.TestableInput;

public class RandomTestableInput implements TestableInput {

    @Override
    public String asString() {
        // TODO properly implement with constructor...
        return "123";
    }

    @Override
    public CNF asCNF() {
        return null;
    }

}
