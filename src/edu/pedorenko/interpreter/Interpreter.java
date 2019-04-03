package edu.pedorenko.interpreter;

import edu.pedorenko.interpreter.PetriObj.ArcIn;
import edu.pedorenko.interpreter.PetriObj.ArcOut;
import edu.pedorenko.interpreter.PetriObj.ExceptionInvalidNetStructure;
import edu.pedorenko.interpreter.PetriObj.PetriNet;
import edu.pedorenko.interpreter.PetriObj.PetriObjModel;
import edu.pedorenko.interpreter.PetriObj.PetriP;
import edu.pedorenko.interpreter.PetriObj.PetriSim;
import edu.pedorenko.interpreter.PetriObj.PetriT;
import edu.pedorenko.interpreter.model.program.ProgramModel;
import edu.pedorenko.interpreter.model.program.create_model.CreateObjectModel;
import edu.pedorenko.interpreter.model.program.create_model.DistributionType;
import edu.pedorenko.interpreter.model.program.create_model.SetArcModel;
import edu.pedorenko.interpreter.model.program.create_model.SetPlaceModel;
import edu.pedorenko.interpreter.model.program.create_model.SetTransitionModel;
import edu.pedorenko.interpreter.model.program.create_model.TransitionPropsModel;
import edu.pedorenko.interpreter.model.program.stats.StatModel;
import edu.pedorenko.interpreter.semantic_analyzer.SemanticAnalyzer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private static ProgramModel programModel;

    private static PetriObjModel model;

    private static Map<String, PetriP> places = new HashMap<>();
    private static Map<String, PetriT> transitions = new HashMap<>();

    public static void main(String[] args) throws IOException, ExceptionInvalidNetStructure {

        String sourceFileName = args[0];
        String input = new String(Files.readAllBytes(Paths.get(sourceFileName)));

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(input);

        programModel = semanticAnalyzer.analyze();

        createModel();

        simulateModel();

        doStatistics();
    }

    private static void createModel() throws ExceptionInvalidNetStructure {

        ArrayList<PetriSim> petriObjects = new ArrayList<>();
        for (CreateObjectModel createObjectModel : programModel.getCreateModelModel().getCreateObjectModels()) {

            Map<String, PetriP> localPlaces = new HashMap<>();
            Map<String, PetriT> localTransitions = new HashMap<>();

            ArrayList<PetriP> d_P = new ArrayList<>();
            ArrayList<PetriT> d_T = new ArrayList<>();
            ArrayList<ArcIn> d_In = new ArrayList<>();
            ArrayList<ArcOut> d_Out = new ArrayList<>();

            String petriNetName = createObjectModel.getObjectIdentifier();

            for (SetPlaceModel setPlaceModel : createObjectModel.getSetPlacesModel()) {
                String placeName = setPlaceModel.getIdentifier();
                int placeMarking = setPlaceModel.getMarking();
                PetriP petriPlace = new PetriP(placeName, placeMarking);

                places.put(placeName, petriPlace);
                localPlaces.put(placeName, petriPlace);
                d_P.add(petriPlace);
            }

            for (SetTransitionModel setTransitionModel : createObjectModel.getSetTransitionsModel()) {

                String transitionName = setTransitionModel.getIdentifier();

                TransitionPropsModel transitionPropsModel = setTransitionModel.getTransitionPropsModel();

                float delay = transitionPropsModel.getDelay();
                float deviation = transitionPropsModel.getDeviation();
                int priority = transitionPropsModel.getPriority();
                float probability = transitionPropsModel.getProbability();
                DistributionType distributionType = transitionPropsModel.getDistribution();
                String distribution = null;
                switch (distributionType) {
                    case CONST:
                        distribution = null;
                        break;

                    case EXP:
                        distribution = "exp";
                        break;

                    case UNIFORM:
                        distribution = "unif";
                        break;

                    case NORM:
                        distribution = "norm";
                        break;
                }

                PetriT petriTransition = new PetriT(transitionName, delay, probability);
                petriTransition.setPriority(priority);
                petriTransition.setDistribution(distribution, delay);
                petriTransition.setParamDeviation(deviation);

                transitions.put(transitionName, petriTransition);
                localTransitions.put(transitionName, petriTransition);
                d_T.add(petriTransition);
            }

            for (SetArcModel setArcModel : createObjectModel.getSetInArcModel()) {

                String placeName = setArcModel.getIdentifierFrom();
                String transitionName = setArcModel.getIdentifierTo();
                int multiplicity = setArcModel.getMultiplicity();
                boolean isInf = setArcModel.isInf();

                if (localPlaces.containsKey(placeName)) {
                    if (localTransitions.containsKey(transitionName)) {

                        PetriP place = localPlaces.get(placeName);
                        PetriT transition = localTransitions.get(transitionName);

                        d_In.add(new ArcIn(place, transition, multiplicity, isInf));

                    } else {

                        PetriP place = localPlaces.get(placeName);
                        PetriT transition = transitions.get(transitionName);

                        PetriT hackTransition = createHackTransition(transition, d_T, localTransitions);

                        d_In.add(new ArcIn(place, hackTransition, multiplicity, isInf));

                    }

                } else {
                    if (localTransitions.containsKey(transitionName)) {

                        PetriP place = places.get(placeName);
                        PetriT transition = localTransitions.get(transitionName);

                        PetriP hackPlace = createHackPlace(place, d_P, localPlaces);

                        d_In.add(new ArcIn(hackPlace, transition, multiplicity, isInf));

                    } else {

                        PetriP place = places.get(placeName);
                        PetriT transition = transitions.get(transitionName);

                        PetriP hackPlace = createHackPlace(place, d_P, localPlaces);
                        PetriT hackTransition = createHackTransition(transition, d_T, localTransitions);

                        d_In.add(new ArcIn(hackPlace, hackTransition, multiplicity, isInf));
                    }
                }

            }

            for (SetArcModel setArcModel : createObjectModel.getSetOutArcModel()) {

                String transitionName = setArcModel.getIdentifierFrom();
                String placeName = setArcModel.getIdentifierTo();
                int multiplicity = setArcModel.getMultiplicity();
                if (localPlaces.containsKey(placeName)) {
                    if (localTransitions.containsKey(transitionName)) {

                        PetriT transition = localTransitions.get(transitionName);
                        PetriP place = localPlaces.get(placeName);

                        d_Out.add(new ArcOut(transition, place, multiplicity));

                    } else {

                        PetriT transition = transitions.get(transitionName);
                        PetriP place = localPlaces.get(placeName);

                        PetriT hackTransition = createHackTransition(transition, d_T, localTransitions);

                        d_Out.add(new ArcOut(hackTransition, place, multiplicity));

                    }

                } else {
                    if (localTransitions.containsKey(transitionName)) {


                        PetriT transition = localTransitions.get(transitionName);
                        PetriP place = places.get(placeName);

                        PetriP hackPlace = createHackPlace(place, d_P, localPlaces);

                        d_Out.add(new ArcOut(transition, hackPlace, multiplicity));

                    } else {

                        PetriT transition = transitions.get(transitionName);
                        PetriP place = places.get(placeName);

                        PetriT hackTransition = createHackTransition(transition, d_T, localTransitions);
                        PetriP hackPlace = createHackPlace(place, d_P, localPlaces);

                        d_Out.add(new ArcOut(hackTransition, hackPlace, multiplicity));
                    }
                }
            }

            PetriNet petriNet = new PetriNet(petriNetName, d_P, d_T, d_In, d_Out);

            PetriP.initNext();
            PetriT.initNext();
            ArcIn.initNext();
            ArcOut.initNext();

            PetriSim petriObject = new PetriSim(petriNet);

            petriObjects.add(petriObject);
        }

        model = new PetriObjModel(petriObjects);
        model.setTimeMod(programModel.getSimulateModel().getSimulationTime());
        PetriSim.setLimitArrayExtInputs(3);
    }

    private static PetriT createHackTransition(PetriT realTransition, List<PetriT> d_T, Map<String, PetriT> localTransitions) {
        PetriT hackTransition = new PetriT(realTransition.getName());
        d_T.add(realTransition);
        localTransitions.put(realTransition.getName(), realTransition);
        return hackTransition;
    }

    private static PetriP createHackPlace(PetriP realPlace, List<PetriP> d_P, Map<String, PetriP> localPlaces) {
        PetriP hackPlace = new PetriP(realPlace.getName());
        d_P.add(realPlace);
        localPlaces.put(realPlace.getName(), realPlace);
        return hackPlace;
    }

    private static void simulateModel() {

        List<Thread> threads = new ArrayList<>();
        model.getListObj().forEach((petriObject) -> {
            Thread petriObjectThread = new Thread(petriObject);
            threads.add(petriObjectThread);
            petriObjectThread.start();
        });

        threads.forEach((thread -> {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }));
    }

    private static void doStatistics() {

        if (!programModel.getStatsModel().getStatModels().isEmpty()) {

            System.out.println("======================================");
            System.out.println("=             STATISTICS             =");
            System.out.println("======================================");

            for (StatModel statModel : programModel.getStatsModel().getStatModels()) {

                String identifier = statModel.getIdentifier();
                if (places.containsKey(identifier)) {

                    PetriP position = places.get(identifier);
                    switch (statModel.getStatType()) {
                        case VALUE:
                            System.out.println(identifier + " value: " + position.getMark());
                            break;

                        case MAX:
                            System.out.println(identifier + " max: " + position.getObservedMax());
                            break;

                        case MIN:
                            System.out.println(identifier + " min: " + position.getObservedMin());
                            break;

                        case MEAN:
                            System.out.println(identifier + " mean: " + position.getMean());
                            break;
                    }

                } else {

                    PetriT transition = transitions.get(identifier);
                    switch (statModel.getStatType()) {
                        case MAX:
                            System.out.println(identifier + " max: " + transition.getObservedMax());
                            break;

                        case MIN:
                            System.out.println(identifier + " min: " + transition.getObservedMin());
                            break;

                        case MEAN:
                            System.out.println(identifier + " mean: " + transition.getMean());
                            break;
                    }
                }
            }
        }
    }
}
