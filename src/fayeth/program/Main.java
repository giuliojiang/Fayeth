package fayeth.program;

import fayeth.engine.Engine;
import fayeth.engine.ub.UbEngine;
import fayeth.program.state.Args;
import fayeth.program.state.Mode;

public class Main {

    public static void main(String[] args) {
        // Parse the arguments
        Args arguments = ArgParser.parse(args);
        
        // Create the appropriate Engine
        Engine engine = createEngine(arguments);
        engine.setConfiguration(arguments);
        
        // Run the engine
        engine.run();
    }

    private static Engine createEngine(Args arguments) {
        // TODO modify to support creation of FuncEngine
        if (arguments.getMode() == Mode.UNDEF) {
            return new UbEngine();
        } else {
            throw new RuntimeException("Mode ["+arguments.getMode()+"] is currently unsupported");
        }
    }
    
}
