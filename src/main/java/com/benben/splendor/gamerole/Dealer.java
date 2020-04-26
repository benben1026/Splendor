package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.GameInitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dealer extends Role{

    private List<Noble> _nobles = new ArrayList<>();

    private List<Card> _invisibleCardsLevel1 = new ArrayList<>();
    private List<Card> _invisibleCardsLevel2 = new ArrayList<>();
    private List<Card> _invisibleCardsLevel3 = new ArrayList<>();

    private List<Card> _visibleCardsLevel1 = new ArrayList<>();
    private List<Card> _visibleCardsLevel2 = new ArrayList<>();
    private List<Card> _visibleCardsLevel3 = new ArrayList<>();


    public Dealer(int numOfPlayers) {
        super("Bank");
        GameInitUtil.initGame(numOfPlayers,_invisibleCardsLevel1, _invisibleCardsLevel2, _invisibleCardsLevel3, _nobles);

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

    private void initVisibleCards() {
        _visibleCardsLevel1 = _invisibleCardsLevel1;
        _visibleCardsLevel2 = _invisibleCardsLevel2;
        _visibleCardsLevel3 = _invisibleCardsLevel3;
    }

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
        return _visibleCardsLevel1;
    }

    public List<Card> getVisibleCardsLevel2() {
        return _visibleCardsLevel2;
    }

    public List<Card> getVisibleCardsLevel3() {
        return _visibleCardsLevel3;
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
        printOneRowOfCard(_visibleCardsLevel3);
    }
}
