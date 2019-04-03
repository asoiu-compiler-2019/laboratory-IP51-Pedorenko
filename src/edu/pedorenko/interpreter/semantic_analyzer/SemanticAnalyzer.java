package edu.pedorenko.interpreter.semantic_analyzer;

import edu.pedorenko.interpreter.model.program.ProgramModel;
import edu.pedorenko.interpreter.model.program.create_model.CreateObjectModel;
import edu.pedorenko.interpreter.model.program.create_model.SetArcModel;
import edu.pedorenko.interpreter.model.program.create_model.SetPlaceModel;
import edu.pedorenko.interpreter.model.program.create_model.SetTransitionModel;
import edu.pedorenko.interpreter.model.program.stats.StatModel;
import edu.pedorenko.interpreter.model.program.stats.StatType;
import edu.pedorenko.interpreter.parser.Parser;
import java.util.HashSet;
import java.util.Set;

public class SemanticAnalyzer {

    private String input;

    private ProgramModel programModel;

    private Set<String> allIdentifiers = new HashSet<>();
    private Set<String> placeIdentifiers = new HashSet<>();
    private Set<String> transitionIdentifiers = new HashSet<>();
    private Set<String> arcs = new HashSet<>();

    public SemanticAnalyzer(String input) {
        this.input = input;
    }

    public ProgramModel analyze() {
        Parser parser = new Parser(input);
        programModel = parser.parse();

        validateCreate();
        validateStatsIdentifiers();
        return programModel;
    }

    private void validateCreate() {

        for (CreateObjectModel createObjectModel : programModel.getCreateModelModel().getCreateObjectModels()) {
            validateIdentifiersUniqueness(createObjectModel);
            validateArcs(createObjectModel);
        }

    }

    private void validateIdentifiersUniqueness(CreateObjectModel createObjectModel) {

        String objectIdentifier = createObjectModel.getObjectIdentifier();
        if (allIdentifiers.contains(objectIdentifier)) {
            throw new RuntimeException("Semantics error: object identifier " + objectIdentifier + " is already used. " +
                    "Rename it please.");
        }

        allIdentifiers.add(objectIdentifier);

        for (SetPlaceModel setPlaceModel : createObjectModel.getSetPlacesModel()) {

            String placeIdentifier = setPlaceModel.getIdentifier();

            if (allIdentifiers.contains(placeIdentifier)) {
                throw new RuntimeException("Semantics error: place identifier " + placeIdentifier + " is already used. " +
                        "Rename it please.");
            }

            allIdentifiers.add(placeIdentifier);
            placeIdentifiers.add(placeIdentifier);
        }

        for (SetTransitionModel setTransitionModel : createObjectModel.getSetTransitionsModel()) {

            String transitionIdentifier = setTransitionModel.getIdentifier();

            if (allIdentifiers.contains(transitionIdentifier)) {
                throw new RuntimeException("Semantics error: transition identifier " + transitionIdentifier + " is already used. " +
                        "Rename it please.");
            }

            allIdentifiers.add(transitionIdentifier);
            transitionIdentifiers.add(transitionIdentifier);
        }
    }

    private void validateArcs(CreateObjectModel createObjectModel) {

        for (SetArcModel setArcModel : createObjectModel.getSetArcsModel()) {

            String identifierFrom = setArcModel.getIdentifierFrom();
            String identifierTo = setArcModel.getIdentifierTo();
            String arc = identifierFrom + " -> " + identifierTo;

            boolean isFromPlace = isPlaceIdentifierInArc(identifierFrom, arc);
            boolean isToPlace = isPlaceIdentifierInArc(identifierTo, arc);

            if (isFromPlace == isToPlace) {
                throw new RuntimeException("Semantics error at arc " + arc + ": " +
                        "arc can be only from place to transition or from transition to place");
            }

            if (setArcModel.isInf() && isToPlace) {
                throw new RuntimeException("Semantics error at arc " + arc + ": " +
                        "informative arc can be only between from place to transition");
            }

            if (arcs.contains(arc)) {
                throw new RuntimeException("Semantics error: arc " + arc + " is already defined");
            }

            if (isFromPlace) {
                createObjectModel.addSetInArcModel(setArcModel);
            } else {
                createObjectModel.addSetOutArcModel(setArcModel);
            }

            arcs.add(arc);
        }
    }

    private boolean isPlaceIdentifierInArc(String identifier, String arc) {

        if (placeIdentifiers.contains(identifier)) {
            return true;
        } else if (transitionIdentifiers.contains(identifier)) {
            return false;
        } else {
            throw new RuntimeException("Semantics error: identifier " + identifier + " is not found for arc " + arc);
        }
    }

    private void validateStatsIdentifiers() {
        for (StatModel statModel : programModel.getStatsModel().getStatModels()) {

            String identifier = statModel.getIdentifier();
            if (!allIdentifiers.contains(identifier)) {
                throw new RuntimeException("Semantics error: identifier " + identifier + " is not found for statistics");
            }

            if (statModel.getStatType().equals(StatType.VALUE) && !placeIdentifiers.contains(identifier)) {
                throw new RuntimeException("Semantics error: value statistics is only possible for place");
            }
        }
    }
}
