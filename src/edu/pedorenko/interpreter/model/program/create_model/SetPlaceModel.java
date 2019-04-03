package edu.pedorenko.interpreter.model.program.create_model;

public class SetPlaceModel {

    private String identifier;

    private int marking;

    public SetPlaceModel(String identifier, int marking) {
        this.identifier = identifier;
        this.marking = marking;
    }

    public SetPlaceModel(String identifier) {
        this.identifier = identifier;
        this.marking = 0;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getMarking() {
        return marking;
    }
}
