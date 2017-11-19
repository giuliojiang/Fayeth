package fayeth.engine;

public interface Task<Tinput> {

    Outcome<Tinput> run() throws Exception;
    
}
