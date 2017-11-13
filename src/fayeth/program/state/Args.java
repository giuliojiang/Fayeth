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

    public Mode getMode() {
        return mode;
    }
    
    public boolean isThreadingEnabled() {
        return !"1".equals(System.getenv("FAYETH_NOTHREADING"));
    }
    
    public boolean isGcDisabled() {
        return "1".equals(System.getenv("FAYETH_NOGC"));
    }
    
    public int getLimit() {
        return Integer.parseInt(System.getenv("FAYETH_LIMIT"));
    }

    @Override
    public String toString() {
        return "Args [sutDir=" + sutDir + ", inputPath=" + inputPath + ", outputPath=" + outputPath
                + ", seed=" + seed + ", mode=" + mode + "]";
    }

}
