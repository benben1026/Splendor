package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Player extends Role{

    LinkedHashMap<ColorUtil.Color, List<Card>> _cards;
    List<Card> _holdCards;
    List<Noble> _nobles;

    public Player(String name) {
        super(name);
        _holdCards = new ArrayList<>();
        _cards = new LinkedHashMap<>();
        for (int i = 0; i < ColorUtil.COLOR_COUNT; i++) {
            _cards.put(ColorUtil.getColorFromIndex(i), new ArrayList<>());
        }
        _nobles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            _cards.put(ColorUtil.getColorFromIndex(i), new ArrayList<>());
        }
    }

    public List<Card> getHoldCards() {
        return _holdCards;
    }

    public LinkedHashMap<ColorUtil.Color, List<Card>> getCards() {
        return _cards;
    }

    public void addHoldCard(Card card) {
        _holdCards.add(card);
    }

    @Override
    public void printCurrentStatus(boolean myTurn) {
        System.out.println(_name + "  totalScore:" + getTotalScore());
        printToken();
        printCards();
        if (myTurn) {
            printHoldCards();
        }
    }

    public void takeTokens(int[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] != 0) {
                _tokens.put(ColorUtil.getColorFromIndex(i), _tokens.get(ColorUtil.getColorFromIndex(i)) + tokens[i]);
            }
        }
    }

    public boolean validateNumOfTokens() {
        int totalCount = 0;
        for (Map.Entry<ColorUtil.Color, Integer> entry : _tokens.entrySet()) {
            totalCount += entry.getValue();
        }
        return totalCount <= 10;
    }

    public void takeCard(Card card) {
        _cards.get(card.getColor()).add(card);
    }

    private void printCards() {
        String[] output = new String[Card.CARD_FOLD_HEIGHT];
        for (int i = 0; i < Card.CARD_FOLD_HEIGHT; i++) {
            output[i] = String.format("%3s", " ");
        }
        for (int i = 0; i < ColorUtil.COLOR_COUNT; i++) {
            ColorUtil.Color color = ColorUtil.getColorFromIndex(i);
            output[0] += "    " + UserInteractionUtil.getPrintableColor(color) + "___" + UserInteractionUtil.ANSI_RESET;
            output[1] += "    " + UserInteractionUtil.getPrintableColor(color) + "|" + _cards.get(color).size() + "|" + UserInteractionUtil.ANSI_RESET;
            output[2] += "    " + UserInteractionUtil.getPrintableColor(color) + "___" + UserInteractionUtil.ANSI_RESET;
        }
        System.out.println(String.join("\n", output));
    }

    private void printHoldCards() {
        if (_holdCards.isEmpty()) {
            return;
        }
        System.out.println("Hold Cards");
        UserInteractionUtil.printCardsInOneRow(_holdCards);
    }

    private int getTotalScore() {
        int totalScore = 0;
        for (Noble noble : _nobles) {
            totalScore += noble.getSore();
        }
        for (Map.Entry<ColorUtil.Color, List<Card>> entry : _cards.entrySet()) {
            for (Card card : entry.getValue()) {
                totalScore += card.getScore();
            }
        }
        return totalScore;
    }
}
