package fayeth.engine.func;

import fayeth.cnf.CNF;

import java.util.LinkedList;
import java.util.List;

public class CNFChain {

    private final CNF base;
    private final LinkedList<CNF> generated;
    private static final int LIMIT = 250;

    public CNFChain(CNF base) {
        this.base = base;
        this.generated = new LinkedList<>();
    }

    public void addGeneratedCNF(CNF newCnf) {
        while (generated.size() >= LIMIT) {
            generated.removeFirst();
        }
        generated.addLast(newCnf);
    }

    public CNF getBase() {
        return base;
    }

    public List<CNF> getGenerated() {
        return generated;
    }

    public CNF getLast() {
        if(generated.isEmpty()) {
            return base;
        }
        return generated.peekLast();
    }
}
