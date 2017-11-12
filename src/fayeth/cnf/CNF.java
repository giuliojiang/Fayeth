package fayeth.cnf;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CNF {
    private final List<List<Integer>> clauses;
    private final Set<Integer> variables;
    private static final Pattern CNF_PATTERN = Pattern.compile("p cnf (\\d+) (\\d+)");

    public CNF(List<List<Integer>> clauses, Set<Integer> variables) {
        this.clauses = clauses;
        this.variables = variables;
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

    /**
     * Initialises a new CNF formula from a file
     * @param path path to the file that contains the formula
     * @return a CNF class representing the formula contained inside the file
     * @throws IOException if the file does not exist
     * @throws RuntimeException if the file is not in CNF format
     */
    public static CNF fromFile(String path) throws IOException {
        final File f = new File(path);
        if(!f.exists()) {
            throw new FileNotFoundException(path + " does not exist");
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
            throw new IllegalArgumentException("File did not contain a valid CNF formula");
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
        return new CNF(clauses, variables);
    }

    public void toFile(File f) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.append(String.format("p cnf %d %d\n", variables.size(), clauses.size()));
        for(List<Integer> clause : clauses) {
            for(Integer i : clause) {
                bw.append(i + " ");
            }
            bw.append("0\n");
        }
        bw.flush();
        bw.close();
    }
    
    /**
     * Creates a file from a CNF
     * @param file path to the output file
     * @throws IOException according to BufferedWriter
     */
    public void toFile(String path) throws IOException {
        toFile(new File(path));
    }
}
