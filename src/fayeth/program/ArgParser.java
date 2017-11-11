package fayeth.program;

import java.io.File;

import fayeth.program.state.Args;
import fayeth.program.state.Mode;

/**
 * Parses arguments from the command line
 * Arguments order:
 * 0 sut executable
 * 1 input path
 * 2 output path
 * 3 mode
 * 4 seed
 */
public class ArgParser {

    public static Args parse(String[] args) {
        String sutDir = parseSutDir(args);
        String inputPath = parseInputPath(args);
        String outputPath = parseOutputPath(args);
        Long seed = parseSeed(args);
        Mode mode = parseMode(args);
        
        return new Args(sutDir, inputPath, outputPath, seed, mode);
    }

    private static Mode parseMode(String[] args) {
        if (args.length > 3) {
            String modeStr = args[3];
            Mode mode = Mode.fromString(modeStr);
            if (mode == null) {
                throw new RuntimeException("Could not parse mode from ["+modeStr+"]");
            } else {
                return mode;
            }
        } else {
            throw new RuntimeException("Missing argument 3: mode [ub/func]");
        }
    }

    private static Long parseSeed(String[] args) {
        if (args.length > 4) {
            String seedStr = args[4];
            try {
                Long seedLng = Long.parseLong(seedStr);
                return seedLng;
            } catch (NumberFormatException e) {
                throw new RuntimeException("Could not parse seed from ["+seedStr+"]");
            }
        } else {
            return null;
        }
    }

    private static String parseOutputPath(String[] args) {
        if (args.length > 2) {
            String outputPath = args[2];
            File outputPathFile = new File(outputPath);
            if (outputPathFile.exists() && outputPathFile.isDirectory()) {
                // We check that the output directory is already present
                // We leave it to the python launchscript to make sure
                // that the output path is correctly initialized before
                // calling the java application
                return outputPath;
            } else {
                throw new RuntimeException("Path to output directory ["+outputPath+"] is not valid");
            }
        } else {
            throw new RuntimeException("Missing argument 2: path to output directory");
        }
    }

    private static String parseInputPath(String[] args) {
        if (args.length > 1) {
            String inputPath = args[1];
            File inputPathFile = new File(inputPath);
            if (inputPathFile.exists() && inputPathFile.isDirectory()) {
                return inputPath;
            } else {
                throw new RuntimeException("Path to input directory ["+inputPath+"] is not valid");
            }
        } else {
            throw new RuntimeException("Missing argument 1: path to input directory");
        }
    }

    private static String parseSutDir(String[] args) {
        if (args.length > 0) {
            String result = args[0];
            File sut = new File(result);
            if (sut.exists() && sut.isDirectory()) {
                return result;
            } else {
                throw new RuntimeException("Path to SUT ["+result+"] is not valid");
            }
        } else {
            throw new RuntimeException("Missing argument 0: path to SUT executable");
        }
    }
    
}
