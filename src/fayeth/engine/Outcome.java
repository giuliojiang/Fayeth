package fayeth.engine;

import java.util.LinkedList;
import java.util.List;

public class Outcome {

    private TestableInput input;
    private Strategy strategy;
    private List<String> bugDescriptions;

    public Outcome() {
        this.bugDescriptions = new LinkedList<>();
    }

    public TestableInput getInput() {
        return input;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public List<String> getBugDescriptions() {
        return bugDescriptions;
    }

    public String getBugDescriptionsAsString() {
        return String.join(", ", bugDescriptions);
    }

    public void setInput(TestableInput input) {
        this.input = input;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void addBugDescription(String bugDescription) {
        bugDescriptions.add(bugDescription);
    }

    @Override
    public String toString() {
        return "Outcome [strategy=" + strategy.getClass().getName() + ", bugDescriptions=[" + getBugDescriptionsAsString() + "]]";
    }
    
    
    
}
