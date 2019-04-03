package edu.pedorenko.interpreter.model.program.create_model;

public class SetArcModel {

    private String identifierFrom;

    private String identifierTo;

    private int multiplicity;

    private boolean isInf;

    public SetArcModel(String identifierFrom, String identifierTo, int multiplicity, boolean isInf) {
        this.identifierFrom = identifierFrom;
        this.identifierTo = identifierTo;
        this.multiplicity = multiplicity;
        this.isInf = isInf;
    }

    public String getIdentifierFrom() {
        return identifierFrom;
    }

    public String getIdentifierTo() {
        return identifierTo;
    }

    public int getMultiplicity() {
        return multiplicity;
    }

    public boolean isInf() {
        return isInf;
    }
}
