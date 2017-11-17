package fayeth.engine;

public class Expectation {

    public Satisfiability ifSat;
    public Satisfiability ifUnsat;
    
    public Expectation(Satisfiability ifSat, Satisfiability ifUnsat) {
        this.ifSat = ifSat;
        this.ifUnsat = ifUnsat;
    }
    
}
