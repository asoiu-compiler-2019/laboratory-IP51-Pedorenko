package edu.pedorenko.interpreter.model.program;

import edu.pedorenko.interpreter.model.program.create_model.CreateModelModel;
import edu.pedorenko.interpreter.model.program.simulate.SimulateModel;
import edu.pedorenko.interpreter.model.program.stats.StatsModel;

public class ProgramModel {

    private CreateModelModel createModelModel;

    private SimulateModel simulateModel;

    private StatsModel statsModel;

    public ProgramModel(CreateModelModel createModelModel, SimulateModel simulateModel, StatsModel statsModel) {
        this.createModelModel = createModelModel;
        this.simulateModel = simulateModel;
        this.statsModel = statsModel;
    }

    public CreateModelModel getCreateModelModel() {
        return createModelModel;
    }

    public SimulateModel getSimulateModel() {
        return simulateModel;
    }

    public StatsModel getStatsModel() {
        return statsModel;
    }
}
