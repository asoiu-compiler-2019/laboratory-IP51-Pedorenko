package edu.pedorenko.interpreter.model.program.create_model;

import java.util.ArrayList;
import java.util.List;

public class CreateObjectModel {

    private String objectIdentifier;

    private List<SetPlaceModel> setPlacesModel;

    private List<SetTransitionModel> setTransitionsModel;

    private List<SetArcModel> setArcsModel;

    private List<SetArcModel> setInArcModel = new ArrayList<>();

    private List<SetArcModel> setOutArcModel = new ArrayList<>();

    public CreateObjectModel(
            String objectIdentifier,
            List<SetPlaceModel> setPlacesModel,
            List<SetTransitionModel> setTransitionsModel,
            List<SetArcModel> setArcsModel) {

        this.objectIdentifier = objectIdentifier;
        this.setPlacesModel = setPlacesModel;
        this.setTransitionsModel = setTransitionsModel;
        this.setArcsModel = setArcsModel;
    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public List<SetPlaceModel> getSetPlacesModel() {
        return setPlacesModel;
    }

    public List<SetTransitionModel> getSetTransitionsModel() {
        return setTransitionsModel;
    }

    public List<SetArcModel> getSetArcsModel() {
        return setArcsModel;
    }

    public List<SetArcModel> getSetInArcModel() {
        return setInArcModel;
    }

    public List<SetArcModel> getSetOutArcModel() {
        return setOutArcModel;
    }

    public void addSetInArcModel(SetArcModel setArcModel) {
        setInArcModel.add(setArcModel);
    }

    public void addSetOutArcModel(SetArcModel setArcModel) {
        setOutArcModel.add(setArcModel);
    }
}
