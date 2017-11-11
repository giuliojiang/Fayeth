package fayeth.program.state;

public class Args {

    private final String sutDir;
    private final String inputPath;
    private final String outputPath;
    private final Long seed;
    private final Mode mode;
    
    public Args(String sutExecutable, String inputPath, String outputPath, Long seed, Mode mode) {
        this.sutDir = sutExecutable;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.seed = seed;
        this.mode = mode;
    }

    public String getSutDir() {
        return sutDir;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public Long getSeed() {
        return seed;
    }

    public boolean isMultithreadingAllowed() {
        return seed == null;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "Args [sutDir=" + sutDir + ", inputPath=" + inputPath + ", outputPath=" + outputPath
                + ", seed=" + seed + ", mode=" + mode + "]";
    }

}
