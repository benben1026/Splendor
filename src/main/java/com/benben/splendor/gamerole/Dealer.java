package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.GameInitUtil;

import java.util.List;
import java.util.Map;

public class Dealer extends Role{

    private List<Noble> nobles;

    private List<Card> _inVisibleCardsLevel1;
    private List<Card> _inVisibleCardsLevel2;
    private List<Card> _inVisibleCardsLevel3;

    private List<Card> _VisibleCardsLevel1;
    private List<Card> _VisibleCardsLevel2;
    public List<Card> _VisibleCardsLevel3;


    public Dealer(int numOfPlayers) {
        super("Bank");
        GameInitUtil gameInitUtil = new GameInitUtil();
        gameInitUtil.initGame(numOfPlayers);
        _tokens = gameInitUtil.getTokens();
        _inVisibleCardsLevel1 = gameInitUtil.getCardsLevel1();
        _inVisibleCardsLevel2 = gameInitUtil.getCardsLevel2();
        _inVisibleCardsLevel3 = gameInitUtil.getCardsLevel3();
        initVisibleCards();
        int tokenCount;
        if (numOfPlayers == 2 || numOfPlayers == 3) {
            tokenCount = numOfPlayers + 2;
        } else {
            tokenCount = 7;
        }
        _tokens.put(ColorUtil.Color.GREEN, tokenCount);
        _tokens.put(ColorUtil.Color.WHITE, tokenCount);
        _tokens.put(ColorUtil.Color.BLACK, tokenCount);
        _tokens.put(ColorUtil.Color.RED, tokenCount);
        _tokens.put(ColorUtil.Color.BLUE, tokenCount);
        _tokens.put(ColorUtil.Color.YELLOW, 5);
    }

    private void initVisibleCards() {}

    public boolean sell(Map<ColorUtil.Color, Integer> tokens, Card card) {
        return false;
    }

    public boolean requestToTakeTokens(int[] tokens) {
        if (tokens.length != 5) {
            return false;
        }
        int total = 0;
        boolean sameColor = false;
        for (int token : tokens) {
            total += token;
            if (token > 1) {
                sameColor = true;
            }
        }
        if (total > 3 || (sameColor && total > 2)) {
            return false;
        }
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] > _tokens.get(ColorUtil.getColorFromIndex(i))) {
                return false;
            }
            if (tokens[i] == 2 && _tokens.get(ColorUtil.getColorFromIndex(i)) < 4) {
                return false;
            }
        }
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] != 0) {
                _tokens.put(ColorUtil.getColorFromIndex(i), _tokens.get(ColorUtil.getColorFromIndex(i)) - tokens[i]);
            }
        }
        return true;
    }

    public List<Card> getVisibleCardsLevel1() {
        return _VisibleCardsLevel1;
    }

    public List<Card> getVisibleCardsLevel2() {
        return _VisibleCardsLevel2;
    }

    public List<Card> getVisibleCardsLevel3() {
        return _VisibleCardsLevel3;
    }

    void printOneRowOfCard(List<Card> cards) {
        String[] output = new String[Card.CARD_LENGTH];
        for (int i = 0; i < output.length; i++) {
            output[i] = "";
        }

        for (Card card : cards) {
            List<String> cardString = card.print();
            for (int i = 0; i < cardString.size(); i++) {
                output[i] += "\t" + cardString.get(i);
            }
        }

        System.out.println(String.join("\n", output));
    }

    @Override
    public void printCurrentStatus() {
        System.out.println(_name + ":");
        printToken();
        printOneRowOfCard(_VisibleCardsLevel3);
    }
}
