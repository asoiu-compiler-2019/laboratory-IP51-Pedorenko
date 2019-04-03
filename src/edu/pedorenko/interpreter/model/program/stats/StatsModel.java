package edu.pedorenko.interpreter.model.program.stats;

import java.util.List;

public class StatsModel {

    private List<StatModel> statModels;

    public StatsModel(List<StatModel> statModels) {
        this.statModels = statModels;
    }

    public List<StatModel> getStatModels() {
        return statModels;
    }
}
