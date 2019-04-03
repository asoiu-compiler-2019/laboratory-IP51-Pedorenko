package edu.pedorenko.interpreter.model.program.create_model;

public class SetTransitionModel {

    private String identifier;

    private TransitionPropsModel transitionPropsModel;

    public SetTransitionModel(String identifier, TransitionPropsModel transitionPropsModel) {
        this.identifier = identifier;
        this.transitionPropsModel = transitionPropsModel;
    }

    public String getIdentifier() {
        return identifier;
    }

    public TransitionPropsModel getTransitionPropsModel() {
        return transitionPropsModel;
    }
}
