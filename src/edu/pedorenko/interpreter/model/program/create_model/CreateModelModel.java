package edu.pedorenko.interpreter.model.program.create_model;

import java.util.List;

public class CreateModelModel {

    List<CreateObjectModel> createObjectModels;

    public CreateModelModel(List<CreateObjectModel> createObjectModels) {
        this.createObjectModels = createObjectModels;
    }

    public List<CreateObjectModel> getCreateObjectModels() {
        return createObjectModels;
    }
}
