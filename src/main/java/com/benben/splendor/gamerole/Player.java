package com.benben.splendor.gamerole;

import com.benben.splendor.action.ActionAndResponse;
import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Item;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.CardsPosition;
import com.benben.splendor.util.Color;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Player extends Role{

    LinkedHashMap<Color, List<Card>> _cards;
    List<Noble> _nobles;

    public Player(String name) {
        super(name);
        _cards = new LinkedHashMap<>();
        for (int i = 0; i < Color.COLOR_COUNT; i++) {
            _cards.put(Color.getColorFromIndex(i), new ArrayList<>());
        }
        _nobles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            _cards.put(Color.getColorFromIndex(i), new ArrayList<>());
        }
    }

    /**
     * The System will notify the player when it's his turn by calling this method.
     */
    public abstract ActionAndResponse askForAction(List<Player> opponents,
                                                   Map<Color, Integer> remainingTokens,
                                                   Map<CardsPosition, Card> visibleCards,
                                                   List<Noble> nobles);

    public abstract int pickNoble(Map<Integer, Noble> nobles);

    public abstract <T extends Player> T deepCopy();

    /**
     * When a player's total tokens exceed 10, the dealer will ask the player to
     * return some tokens to keep the total count under 10.
     * @return A map indicates what tokens to return.
     */
    public abstract Map<Color, Integer> askToReturnTokens();

    public final boolean payWithTokens(Color color, int count) {
        if (_tokens.get(color) < count) {
            return false;
        }
        _tokens.put(color, _tokens.get(color) - count);
        return true;
    }

    public final void receiveCard(Card card) {
        _cards.get(card.getColor()).add(card);
    }

    public final void receiveNoble(Noble noble) {
        _nobles.add(noble);
    }

    public final int getTotalScore() {
        int totalScore = 0;
        for (Map.Entry<Color, List<Card>> entry : _cards.entrySet()) {
            totalScore += entry.getValue().stream().mapToInt(Item::getScore).sum();
        }
        return totalScore + _nobles.stream().mapToInt(Item::getScore).sum();
    }

    public final Map<Color, Integer> getCardsByCount() {
        return _cards.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }

    public void printCards() {
        String[] output = new String[Card.CARD_FOLDED_HEIGHT];
        for (int i = 0; i < Card.CARD_FOLDED_HEIGHT; i++) {
            output[i] = String.format("%3s", " ");
        }
        for (int i = 0; i < Color.COLOR_COUNT; i++) {
            Color color = Color.getColorFromIndex(i);
            output[0] += "    " + UserInteractionUtil.getPrintableCardUpperBorder(3, color);
            output[1] += "    " + color.toPrintable()
                    + UserInteractionUtil.VERTICAL
                    + _cards.get(color).size()
                    + UserInteractionUtil.VERTICAL
                    + Color.ANSI_RESET;
            output[2] += "    " + UserInteractionUtil.getPrintableCardLowerBorder(3, color);
        }
        UserInteractionUtil.SYSTEM_OUT.println(String.join("\n", output));
    }
}
