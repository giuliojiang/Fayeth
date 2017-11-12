package fayeth.util;

public class Log {
    
    private static final boolean ENABLE_INFO = true;
    
    public static void info(String s) {
        if (ENABLE_INFO) {
            System.err.println(s);
        }
    }
    
    public static void error(String s) {
        System.err.println(s);
    }

}
