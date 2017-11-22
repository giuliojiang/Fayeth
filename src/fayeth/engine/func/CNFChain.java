package fayeth.engine.func;

import fayeth.cnf.CNF;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
    
    public CNF getHighestCoverage() {
        if (generated.isEmpty()) {
            return base;
        }
        
        double maxCoverage = -1d;
        CNF candidate = null;
        for (CNF cnf : generated) {
            double aCoverage = cnf.getCoverage();
            if (aCoverage >= maxCoverage) {
                maxCoverage = aCoverage;
                candidate = cnf;
            }
        }
        return candidate;
    }

    public int getNumberGenerated() {
        return generated.size();
    }

    public CNF getRandom(Random random) {
        int idx = random.nextInt(generated.size() + 1);
        if (idx == generated.size()) {
            return base;
        } else {
            return generated.get(idx);
        }
    }
}
