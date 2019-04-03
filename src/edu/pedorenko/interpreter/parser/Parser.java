package edu.pedorenko.interpreter.parser;

import edu.pedorenko.interpreter.lexer.Lexer;
import edu.pedorenko.interpreter.model.KeyWords;
import edu.pedorenko.interpreter.model.Token;
import edu.pedorenko.interpreter.model.TokenType;
import edu.pedorenko.interpreter.model.program.ProgramModel;
import edu.pedorenko.interpreter.model.program.create_model.CreateModelModel;
import edu.pedorenko.interpreter.model.program.create_model.CreateObjectModel;
import edu.pedorenko.interpreter.model.program.create_model.DistributionType;
import edu.pedorenko.interpreter.model.program.create_model.SetArcModel;
import edu.pedorenko.interpreter.model.program.create_model.SetPlaceModel;
import edu.pedorenko.interpreter.model.program.create_model.SetTransitionModel;
import edu.pedorenko.interpreter.model.program.create_model.TransitionPropsModel;
import edu.pedorenko.interpreter.model.program.simulate.SimulateModel;
import edu.pedorenko.interpreter.model.program.stats.StatModel;
import edu.pedorenko.interpreter.model.program.stats.StatType;
import edu.pedorenko.interpreter.model.program.stats.StatsModel;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private Lexer lexer;

    private Token currentToken;

    public Parser(String input) {
        lexer = new Lexer(input);
    }

    public ProgramModel parse() {
        currentToken = lexer.nextToken();
        CreateModelModel createModelModel = expectCreateModel();
        SimulateModel simulateModel = expectSimulate();
        StatsModel statsModel = expectStats();

        return new ProgramModel(createModelModel, simulateModel, statsModel);
    }

    private CreateModelModel expectCreateModel() {

        List<CreateObjectModel> createObjectModels = new ArrayList<>();
        createObjectModels.add(expectCreateObject());

        return new CreateModelModel(createObjectModels);
    }

    private CreateObjectModel expectCreateObject() {

        Token objectIdentifierToken = expect(TokenType.IDENTIFIER);
        String objectIdentifier = objectIdentifierToken.getValue();

        expect(TokenType.LEFT_BRACE);
        List<SetPlaceModel> setPlaceModels = expectSetPlaces();
        List<SetTransitionModel> setTransitionModels = expectSetTransitions();
        List<SetArcModel> setArcModels = expectSetArcs();
        expect(TokenType.RIGHT_BRACE);

        return new CreateObjectModel(objectIdentifier, setPlaceModels, setTransitionModels, setArcModels);
    }

    private List<SetPlaceModel> expectSetPlaces() {

        List<SetPlaceModel> setPlaceModels = new ArrayList<>();

        expect(TokenType.PLACES);
        do {

            Token placeIdentifierToken = expect(TokenType.IDENTIFIER);
            String placeIdentifier = placeIdentifierToken.getValue();

            if (accept(TokenType.LEFT_BRACKET)) {

                Token placeMarkingToken = expect(TokenType.INT);
                int placeMarking = parseInt(placeMarkingToken);

                expect(TokenType.RIGHT_BRACKET);

                setPlaceModels.add(new SetPlaceModel(placeIdentifier, placeMarking));
                continue;
            }

            setPlaceModels.add(new SetPlaceModel(placeIdentifier));

        } while (accept(TokenType.COMMA));

        return setPlaceModels;
    }

    private List<SetTransitionModel> expectSetTransitions() {

        List<SetTransitionModel> setTransitionModels = new ArrayList<>();

        expect(TokenType.TRANSITIONS);
        do {
            Token transitionIdentifierToken = expect(TokenType.IDENTIFIER);
            String transitionIdentifier = transitionIdentifierToken.getValue();
            TransitionPropsModel transitionPropsModel = new TransitionPropsModel();
            if (accept(TokenType.LEFT_BRACKET)) {
                transitionPropsModel = expectTransitionProps();
                expect(TokenType.RIGHT_BRACKET);
            }
            setTransitionModels.add(new SetTransitionModel(transitionIdentifier, transitionPropsModel));
        } while (accept(TokenType.COMMA));

        return setTransitionModels;
    }

    private TransitionPropsModel expectTransitionProps() {

        TransitionPropsModel transitionPropsModel = new TransitionPropsModel();

        boolean hadDelay = false;
        boolean hadDeviation = false;
        boolean hadDistribution = false;
        boolean hadProbability = false;
        boolean hadPriority = false;
        for (int i = 0; i < 5; ++i) {

            if (accept(TokenType.DELAY)) {

                if (hadDelay) {
                    throw new RuntimeException("Syntax error at " + currentToken + ". Delay already specified");
                }
                hadDelay = true;

                expect(TokenType.LEFT_BRACKET);

                Token delayToken = expectOneOf(KeyWords.numbers);
                float delay = parseFloat(delayToken);
                transitionPropsModel.setDelay(delay);

                expect(TokenType.RIGHT_BRACKET);

            } else if (accept(TokenType.DEVIATION)) {

                if (hadDeviation) {
                    throw new RuntimeException("Syntax error at " + currentToken + ". Deviation already specified");
                }
                hadDeviation = true;

                expect(TokenType.LEFT_BRACKET);

                Token deviationToken = expectOneOf(KeyWords.numbers);
                float deviation = parseFloat(deviationToken);
                transitionPropsModel.setDeviation(deviation);

                expect(TokenType.RIGHT_BRACKET);

            } else if (accept(TokenType.DISTRIBUTION)) {

                if (hadDistribution) {
                    throw new RuntimeException("Syntax error at " + currentToken + ". Distribution already specified");
                }
                hadDistribution = true;

                expect(TokenType.LEFT_BRACKET);

                Token distributionToken = expectOneOf(KeyWords.distributionTypes);
                switch (distributionToken.getTokenType()) {
                    case CONST:
                        transitionPropsModel.setDistribution(DistributionType.CONST);
                        break;

                    case EXP:
                        transitionPropsModel.setDistribution(DistributionType.EXP);
                        break;

                    case NORM:
                        transitionPropsModel.setDistribution(DistributionType.NORM);
                        break;

                    case UNIFORM:
                        transitionPropsModel.setDistribution(DistributionType.UNIFORM);
                        break;

                    default:
                        throw new RuntimeException("Syntax error at " + distributionToken + ". Unexpected distribution type.");
                }

                expect(TokenType.RIGHT_BRACKET);

            } else if (accept(TokenType.PROBABILITY)) {

                if (hadProbability) {
                    throw new RuntimeException("Syntax error at " + currentToken + ". Probability already specified");
                }

                hadProbability = true;

                expect(TokenType.LEFT_BRACKET);

                Token probabilityToken = expectOneOf(KeyWords.numbers);
                float probability = parseFloat(probabilityToken);
                transitionPropsModel.setProbability(probability);

                expect(TokenType.RIGHT_BRACKET);

            } else if (accept(TokenType.PRIORITY)) {

                if (hadPriority) {
                    throw new RuntimeException("Syntax error at " + currentToken + ". Priority already specified");
                }

                hadPriority = true;

                expect(TokenType.LEFT_BRACKET);

                Token priorityToken = expect(TokenType.INT);
                int priority = parseInt(priorityToken);
                transitionPropsModel.setPriority(priority);

                expect(TokenType.RIGHT_BRACKET);

            } else {
                break;
            }
        }

        return transitionPropsModel;
    }

    private List<SetArcModel> expectSetArcs() {

        List<SetArcModel> setArcModels = new ArrayList<>();

        expect(TokenType.ARCS);
        do {
            Token identifierFromToken = expect(TokenType.IDENTIFIER);

            int multiplicity = 1;
            if (accept(TokenType.LEFT_BRACKET)) {
                Token multiplicityToken = expect(TokenType.INT);
                multiplicity = parseInt(multiplicityToken);
                expect(TokenType.RIGHT_BRACKET);
            }
            expect(TokenType.CONNECTION);
            Token identifierToToken = expect(TokenType.IDENTIFIER);
            boolean isInf = accept(TokenType.INF);

            setArcModels.add(new SetArcModel(identifierFromToken.getValue(), identifierToToken.getValue(), multiplicity, isInf));

        } while (accept(TokenType.COMMA));

        return setArcModels;
    }

    private SimulateModel expectSimulate() {
        expect(TokenType.SIMULATE);
        Token simulationTimeToken = expect(TokenType.INT);

        int simulationTime = parseInt(simulationTimeToken);

        return new SimulateModel(simulationTime);
    }

    private StatsModel expectStats() {

        List<StatModel> statModels = new ArrayList<>();
        do {
            statModels.add(expectStat());
        } while (!currentToken.getTokenType().equals(TokenType.END_OF_INPUT));
        return new StatsModel(statModels);
    }

    private StatModel expectStat() {

        Token identifierToken = expect(TokenType.IDENTIFIER);
        Token reportTypeToken = expectOneOf(KeyWords.reportTypes);

        String identifier = identifierToken.getValue();
        StatType statType;
        switch (reportTypeToken.getTokenType()) {
            case VALUE:
                statType = StatType.VALUE;
                break;

            case MAX:
                statType = StatType.MAX;
                break;

            case MIN:
                statType = StatType.MIN;
                break;

            case MEAN:
                statType = StatType.MEAN;
                break;

            default:
                throw new RuntimeException("Syntax error at " + reportTypeToken + ". Unexpected report type.");
        }

        return new StatModel(identifier, statType);
    }

    private int parseInt(Token token) {
        try {
            return Integer.parseInt(token.getValue());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Syntax error at " + token + ". Malformed integer.");
        }
    }

    private float parseFloat(Token token) {
        try {
            return Float.parseFloat(token.getValue());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Syntax error at " + token + ". Malformed float.");
        }
    }

    private Token expect(TokenType tokenType) {

        Token expectedToken = currentToken;

        if (!accept(tokenType)) {
            throw new RuntimeException("Syntax error at " + currentToken);
        }

        return expectedToken;
    }

    private Token expectOneOf(List<TokenType> tokenTypes) {

        Token expectedToken = currentToken;

        for (TokenType tokenType : tokenTypes) {
            if (accept(tokenType)) {
                return expectedToken;
            }
        }
        throw new RuntimeException("Syntax error at " + currentToken);
    }

    private boolean accept(TokenType tokenType) {
        if (tokenType.equals(currentToken.getTokenType())) {
            currentToken = lexer.nextToken();
            return true;
        }
        return false;
    }
}
