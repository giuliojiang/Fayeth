package fayeth.engine;

import fayeth.program.state.Args;

public interface Engine {

    void setConfiguration(Args arguments);
    
    void run();
    
}
