package fayeth.cnf;

import java.util.List;

public class CNFUtil {

    public static boolean clauseContainsVariable(List<Integer> clause, Integer variable) {
        if (variable == null) {
            return false;
        }
        
        int varIndex = Math.abs(variable);
        
        for (Integer cv : clause) {
            if (Math.abs(cv) == varIndex) {
                return true;
            }
        }
        return false;
    }
    
}
