package edu.pedorenko.interpreter.model.program.create_model;

public class TransitionPropsModel {

    private float delay = 0;

    private float deviation = 0;

    private DistributionType distribution = DistributionType.CONST;

    private float probability = 1;

    private int priority = 0;

    public TransitionPropsModel() {
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getDeviation() {
        return deviation;
    }

    public float getProbability() {
        return probability;
    }

    public int getPriority() {
        return priority;
    }

    public void setDeviation(float deviation) {
        this.deviation = deviation;
    }

    public DistributionType getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionType distribution) {
        this.distribution = distribution;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
