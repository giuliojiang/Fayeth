package fayeth.engine.func;

import fayeth.cnf.CNF;

import java.util.ArrayList;
import java.util.List;

public class FuncCNFCollection {

    private List<CNFChain> collection;

    private FuncCNFCollection() {
        this.collection = new ArrayList<>();
    }

    public FuncCNFCollection(List<CNF> cnfs) {
        this();
        for(CNF cnf: cnfs) {
            this.collection.add(new CNFChain(cnf));
        }
    }

    public void addBaseCNF(CNF cnf) {
        collection.add(new CNFChain(cnf));
    }

    public List<CNFChain> getCollection() {
        return collection;
    }

    public int size() {
        return collection.size();
    }

    public CNFChain get(int idx) {
        return collection.get(idx);
    }
}
