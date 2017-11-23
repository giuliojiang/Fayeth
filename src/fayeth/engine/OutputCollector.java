package fayeth.engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fayeth.program.state.Args;
import fayeth.util.FileUtil;

/**
 * The OutputCollector is the class that collects Bug reports to be
 * written to the output folder.
 * It is set to automatically remove entries when the output folder reaches
 * the maximum size, at which point it will try to remove entries
 * from those bug categories that have the highest number of bugs.
 * If an outcome has no Bugs (null field), it will not be written
 * to the output directory.
 *
 */
public class OutputCollector {

    private static final int MAX_SIZE = 20;
    
    private final String outputDirPath;
    
    /**
     * Records the outputs currently in the output directory.
     * Maps from Bug Description to a list of Files in the output
     * directory that have that bug description.
     */
    private final Map<String, List<File>> collectedOutputs = new HashMap<>();
    
    /**
     * Prefix of all files in the output directory
     */
    private final String filePrefix;
    
    /**
     * The current number of outputs collected in the output
     * directory
     */
    private int size = 0;
    
    /**
     * Counter to generate the next filename
     */
    private int currentIndex = 0;
    
    public OutputCollector(Args arguments) {
        this.outputDirPath = arguments.getOutputPath();
        this.filePrefix = arguments.getMode().toString();
    }
    
    // Private methods --------------------------------------------------------
    
    private String nextFilePath() {
        String result = outputDirPath + "/" + filePrefix + "_" + currentIndex + ".cnf";
        currentIndex++;
        return result;
    }
    
    private void purgeOne() {
        // Compute output class with most entries
        int maxSize = -1;
        String maxKey = null;
        for (String key : collectedOutputs.keySet()) {
            List<File> entries = collectedOutputs.get(key);
            int currSize = entries.size();
            if (currSize > maxSize) {
                maxSize = currSize;
                maxKey = key;
            }
        }
        
        // Remove an entry from the largest collection
        List<File> largestCollection = collectedOutputs.get(maxKey);
        File removedFile = largestCollection.remove(0);
        removedFile.delete();
        size--;
    }
    
    private void purge() {
        while (size >= MAX_SIZE) {
            purgeOne();
        }
    }
    
    // Public methods ---------------------------------------------------------
    
    public void collect(Outcome<TestableInput> outcome) throws IOException {
        // Add
        String bugDescription = outcome.getBugDescriptionsAsString();
        if (bugDescription == null) {
            // Do not write to output if there was no bug at all
            return;
        }
        String newFilePath = nextFilePath();
        synchronized(collectedOutputs) {
            if (!collectedOutputs.containsKey(bugDescription)) {
                collectedOutputs.put(bugDescription, new LinkedList<>());
            }
            File writtenFile = FileUtil.writeToFile(outcome.getInput(), newFilePath);
            collectedOutputs.get(bugDescription).add(writtenFile);
            size++;

            // Purge
            purge();
        }
    }
    
}
