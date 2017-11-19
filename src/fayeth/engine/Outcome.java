package fayeth.engine;

import java.util.LinkedList;
import java.util.List;

public class Outcome<TInput> {

    protected TInput input;
    protected Strategy<TInput> strategy;
    protected List<String> bugDescriptions;

    public Outcome() {
        this.bugDescriptions = new LinkedList<>();
    }

    public TInput getInput() {
        return input;
    }

    public Strategy<TInput> getStrategy() {
        return strategy;
    }

    public List<String> getBugDescriptions() {
        return bugDescriptions;
    }

    public String getBugDescriptionsAsString() {
        return String.join(", ", bugDescriptions);
    }

    public void setInput(TInput input) {
        this.input = input;
    }

    public void setStrategy(Strategy<TInput> strategy) {
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
