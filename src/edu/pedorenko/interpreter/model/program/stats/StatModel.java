package edu.pedorenko.interpreter.model.program.stats;

public class StatModel {

    private String identifier;

    private StatType statType;

    public StatModel(String identifier, StatType statType) {
        this.identifier = identifier;
        this.statType = statType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public StatType getStatType() {
        return statType;
    }
}
