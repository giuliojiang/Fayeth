package fayeth.cnf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fayeth.engine.TestableInput;
import fayeth.util.Log;

public class CNF implements TestableInput {
    private List<List<Integer>> clauses;
    private Set<Integer> variables;
    private File sourceFile;
    private double coverage;
    private static final Pattern CNF_PATTERN = Pattern.compile("p cnf (\\d+) (\\d+)");

    public CNF(List<List<Integer>> clauses, Set<Integer> variables, File sourceFile) {
        this.clauses = clauses;
        this.variables = variables;
        this.sourceFile = sourceFile;
    }
    
    public CNF(List<List<Integer>> clauses, Set<Integer> variables) {
        this(clauses, variables, null);
    }
    
    public CNF() {
        this(new ArrayList<>(), new HashSet<>(), null);
    }

    public Set<Integer> getVariables() {
        return variables;
    }

    public List<List<Integer>> getClauses() {
        return clauses;
    }

    @Override
    public String toString() {
        return "CNF{" +
                "clauses=" + clauses +
                ", variables=" + variables +
                '}';
    }

    public static CNF fromFile(File f) {
        
        try {
            if(!f.exists()) {
                throw new FileNotFoundException(f.getAbsolutePath() + " does not exist");
            }
            final BufferedReader bi = new BufferedReader(new FileReader(f));
            String line;

            // Skip all the initial comments that some queries have
            while((line = bi.readLine()) != null) {
                if(line.startsWith("c")) continue;
                if(line.startsWith("p")) {
                    break;
                }
            }
            if(line == null) {
                bi.close();
                throw new IllegalArgumentException("File " + f +  " did not contain a valid CNF formula");
            }
            Matcher m = CNF_PATTERN.matcher(line);
            if(!m.matches()) {
                bi.close();
                throw new IllegalArgumentException("File is not in CNF format: " + line);
            }
            final Integer numVariables = Integer.parseInt(m.group(1));
            final Integer numClauses = Integer.parseInt(m.group(2));
            final List<List<Integer>> clauses = new ArrayList<>(numClauses);
            final Set<Integer> variables = new HashSet<>(numVariables);

            while((line = bi.readLine()) != null) {
                if(line.startsWith("c")) continue;
                List<Integer> clause = new ArrayList<>();
                String[] splitSep = line.split("\\s");
                for(String s : splitSep) {
                    if(s.equals("0"))
                        break;
                    Integer l = Integer.parseInt(s);
                    clause.add(l);
                    variables.add(Math.abs(l));
                }
                clauses.add(clause);
            }
            bi.close();
            return new CNF(clauses, variables, f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Initialises a new CNF formula from a file
     * @param path path to the file that contains the formula
     * @return a CNF class representing the formula contained inside the file
     * @throws IOException if the file does not exist
     * @throws RuntimeException if the file is not in CNF format
     */
    public static CNF fromFile(String path) {
        return fromFile(new File(path));
    }
    
    public static CNF copyFrom(CNF other) {
        List<List<Integer>> newClauses = new ArrayList<>();
        for (List<Integer> aClause : other.clauses) {
            newClauses.add(new ArrayList<>(aClause));
        }
        
        Set<Integer> newVariables = new HashSet<>(other.variables);
        
        File newSourceFile = other.sourceFile;
        
        return new CNF(newClauses, newVariables, newSourceFile);
    }

    /**
     * Creates a file string representation from a CNF
     */
    @Override
    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("p cnf %d %d\n", variables.size(), clauses.size()));
        for(List<Integer> clause : clauses) {
            for(Integer i : clause) {
                sb.append(i + " ");
            }
            sb.append("0\n");
        }
        return sb.toString();
    }
    
    /**
     * Returns the name of the source file (no full path).
     * Includes the extension
     */
    public String getSourceFileName() {
        return sourceFile.getName();
    }

    public void recordCoverage(double coverage) {
        this.coverage = coverage;
    }

    public double getCoverage() {
        return coverage;
    }

    public void addClause(List<Integer> aClause) {
        this.clauses.add(aClause);
        for (Integer lit : aClause) {
            variables.add(Math.abs(lit));
        }
    }

    public void shuffleClauses(Random random) {
        Collections.shuffle(clauses, random);
    }

    public File getSourceFile() {
        return sourceFile;
    }
    
    /**
     * @return An unused variable.
     */
    public int nextNewVariable() {
        int newVar = variables.size() + 1;
        if (variables.contains(newVar)) {
            Log.error("Getting a new variable: this variable is contained in variables map. Did variables have skipped variables? It would not be valid CNF");
            for (int i = newVar; ; i++) {
                if (!variables.contains(i)) {
                    return i;
                }
            }
        }
        return newVar;
    }
    
    public List<Integer> getRandomClause(Random random) {
        return clauses.get(random.nextInt(clauses.size()));
    }

    public Integer getRandomVariable(Random random) {
        if (variables.isEmpty()) {
            return null;
        }
        
        // Variables range from 1 to size of the set
        return random.nextInt(variables.size()) + 1;
    }

    /**
     * Makes the CNF valid by finding gaps in the variable
     * names, and renaming all variables accordingly so that
     * there are no gaps.
     * It does not change the order in which variable indexes
     * appear, but some variables will get removed.
     */
    public void consolidateVariables() {
        Map<Integer, Integer> renamingMap = new HashMap<>();
        List<List<Integer>> newClauses = new ArrayList<>();
        Set<Integer> newVariables = new HashSet<>();
        
        // Create the renaming map
        int i = 1;
        for (Integer oldVariable : variables) {
            renamingMap.put(Math.abs(oldVariable), i);
            i++;
        }
        
        // Populate the new clauses and new variables
        for (List<Integer> oldClause : clauses) {
            List<Integer> newClause = new ArrayList<>();
            for (Integer aLit : oldClause) {
                int oldVar = Math.abs(aLit);
                Integer newVar = renamingMap.get(oldVar);
                if (newVar == null) {
                    throw new RuntimeException("CNF:consolidateVariables: no valid renaming entry for variable ["+oldVar+"]");
                }
                int newLit = aLit > 0 ? newVar : -newVar;
                newClause.add(newLit);
                newVariables.add(newVar);
            }
            newClauses.add(newClause);
        }
        
        // Set
        this.clauses = newClauses;
        this.variables = newVariables;
    }
}
