package com.benben.splendor.gamerole;

import com.benben.splendor.action.*;
import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.*;

import java.util.*;
import java.util.function.Predicate;

public class HumanPlayer extends Player{

    public HumanPlayer(String name) {
        super(name);
    }

    private HumanPlayer(String name, LinkedHashMap<Color, List<Card>> cards, List<Noble> nobles,
                        LinkedHashMap<Color, Integer> tokens) {
        this(name);
        _cards = cards;
        _nobles = nobles;
        _tokens = tokens;
    }

    @Override
    public ActionAndResponse askForAction(List<Player> opponents, Map<Color, Integer> remainingTokens,
                                          Map<CardsPosition, Card> visibleCards, List<Noble> nobles) {
        while(true) {
            try {
                int selection = UserInteractionUtil.askIntInput(GameInitUtil.SYSTEM_INPUT,
                        "Please choose your action:\n(1)Take tokens;\n(2)Buy card;\n"
                                + "(3)Hold card;\n(4)Buy hold card;\n(5)Pass\n",
                        (input) -> input >=1 && input <= 5);
                switch (selection){
                    case 1:
                        return new TakeTokenActionAndResponse(takeTokens());
                    case 2:
                        return new BuyCardActionAndResponse(selectCard(
                                "Please choose the card you want to buy ( 1-12 )",
                                (input) -> input >=1 && input <= 12));
                    case 3:
                        return new HoldCardActionAndResponse(
                                selectCard("Please choose the card you want to hold (1-12, use -1,-2,-3 for the folded top card from deck)",
                                        (input) -> input >=-3 && input <= 12 && input != 0));
                    case 4:
                        return new BuyHoldCardActionAndResponse(buyHoldCard());
                    case 5:
                        return new PassActionAndResponse();
                    default:
                        break;
                }
            } catch (InvalidInputException ex) {

            }
        }
    }

    @Override
    public int pickNoble(Map<Integer, Noble> nobles) {
        return 0;
    }

    @Override
    public Map<Color, Integer> askToReturnTokens() {
        printToken();
        Map<Color, Integer> returnTokens = new HashMap<>();
        UserInteractionUtil.askStringInput(GameInitUtil.SYSTEM_INPUT,
                "Please return some tokens to keep total number under 10 (White, Blue, Green, Red, Black), separate by \",\":\n",
                (input) -> {
                    try {
                        int[] inputTokens = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                        for (int i = 0; i < Color.COLOR_COUNT; i++) {
                            returnTokens.put(Color.getColorFromIndex(i), inputTokens[i]);
                        }
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                });
        return returnTokens;
    }

    private Map<Color, Integer> takeTokens() throws InvalidInputException {
        String userInput = UserInteractionUtil.askStringInputOnce(GameInitUtil.SYSTEM_INPUT,
                "Please choose the number of tokens you want to take(White, Blue, Green, Red, Black), separate by \",\":\n",
                (input) -> true);
        int[] tokens;
        try {
            tokens = Arrays.stream(userInput.split(",")).mapToInt(Integer::parseInt).toArray();
        } catch (Exception e) {
            UserInteractionUtil.OUT.println("Invalid Input");
            throw new InvalidInputException();
        }
        if (tokens.length != 5) {
            throw new InvalidInputException();
        }
        Map<Color, Integer> tokensMap = new HashMap<>();
        for (int i = 0; i < Color.COLOR_COUNT; i++) {
            if (tokens[i] > 0) {
                tokensMap.put(Color.getColorFromIndex(i), tokens[i]);
            }
        }
        return tokensMap;
    }

    private CardsPosition selectCard(String message, Predicate<Integer> predicate) throws InvalidInputException {
        int index = UserInteractionUtil.askIntInputOnce(GameInitUtil.SYSTEM_INPUT,
                message, predicate);
        CardsPosition cardsPosition =  CardsPosition.getPositionFromIndex(index - 1);
        if (cardsPosition == null) {
            throw new InvalidInputException();
        }
        return cardsPosition;
    }

    private int buyHoldCard() throws InvalidInputException {
        return UserInteractionUtil.askIntInputOnce(GameInitUtil.SYSTEM_INPUT,
                "Please choose the card you want to buy ( 1-3 )",
                (in) -> in >= 1 && in <= 3) - 1;
    }

    public void receiveTokens(int[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] != 0) {
                receiveTokens(Color.getColorFromIndex(i), tokens[i]);
            }
        }
    }

    @Override
    public HumanPlayer deepCopy() {
        return new HumanPlayer(this._name, this._cards, this._nobles, this._tokens);
    }
}
