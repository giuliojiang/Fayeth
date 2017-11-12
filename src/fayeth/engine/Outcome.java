package fayeth.engine;

public class Outcome {

    private TestableInput input;
    private Strategy strategy;
    private String bugDescription;
    
    public TestableInput getInput() {
        return input;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setInput(TestableInput input) {
        this.input = input;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    @Override
    public String toString() {
        return "Outcome [input=" + input.asString() + ", strategy=" + strategy.getClass().getName() + ", bugDescription=" + bugDescription + "]";
    }
    
    
    
}
