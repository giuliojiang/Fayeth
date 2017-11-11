package fayeth.program.state;

public enum Mode {
    UNDEF, FUNC;
    
    public static Mode fromString(String s) {
        if ("ub".equals(s)) {
            return UNDEF;
        }
        
        if ("func".equals(s)) {
            return FUNC;
        }
        
        return null;
    }
}
