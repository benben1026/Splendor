package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Item;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Player extends Role{

    private LinkedHashMap<ColorUtil.Color, List<Card>> _cards;
    private List<Card> _holdCards;
    private List<Noble> _nobles;

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

    /**
     * The System will notify the player when it's his turn by calling this method.
     * @param dealer Dealer
     * @param players You could access the information of all the players.
     */
    // Todo: Pass a copy of other players information to prevent undesired modifications
    //       the current player.
    public abstract void notifyTurn(Dealer dealer, List<Player> players);

    /**
     * When a player's total tokens exceed 10, the dealer will ask the player to
     * return some tokens to keep the total count under 10.
     * @return A map indicates what tokens to return.
     */
    public abstract Map<ColorUtil.Color, Integer> askToReturnTokens();

    public final List<Card> getHoldCards() {
        return _holdCards;
    }

    public final boolean payWithTokens(ColorUtil.Color color, int count) {
        if (_tokens.get(color) < count) {
            return false;
        }
        _tokens.put(color, _tokens.get(color) - count);
        return true;
    }

    public final void receiveCard(Card card) {
        _cards.get(card.getColor()).add(card);
    }

    public final void receiveHoldCard(Card card) {
        _holdCards.add(card);
    }

    public final int getTotalScore() {
        int totalScore = 0;
        for (Map.Entry<ColorUtil.Color, List<Card>> entry : _cards.entrySet()) {
            totalScore += entry.getValue().stream().mapToInt(Item::getScore).sum();
        }
        return totalScore + _nobles.stream().mapToInt(Item::getScore).sum();
    }

    public final Map<ColorUtil.Color, Integer> getCardsByCount() {
        return _cards.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }

    @Override
    public Player clone() {
        try {
            return (Player) super.clone();
        } catch (CloneNotSupportedException e) {
            UserInteractionUtil.OUT.println("One player object cannot be deep copied");
            return null;
        }
    }

    @Override
    public final void printCurrentStatus(boolean myTurn) {
        System.out.println(_name + "  totalScore:" + getTotalScore());
        printToken();
        printCards();
        if (myTurn) {
            printHoldCards();
        }
    }

    private void printCards() {
        String[] output = new String[Card.CARD_FOLD_HEIGHT];
        for (int i = 0; i < Card.CARD_FOLD_HEIGHT; i++) {
            output[i] = String.format("%3s", " ");
        }
        for (int i = 0; i < ColorUtil.COLOR_COUNT; i++) {
            ColorUtil.Color color = ColorUtil.getColorFromIndex(i);
            output[0] += "    " + UserInteractionUtil.getPrintableColor(color)
                    + UserInteractionUtil.getPrintableCardUpperBorder(3)
                    + UserInteractionUtil.ANSI_RESET;
            output[1] += "    " + UserInteractionUtil.getPrintableColor(color)
                    + UserInteractionUtil.VERTICAL
                    + _cards.get(color).size()
                    + UserInteractionUtil.VERTICAL
                    + UserInteractionUtil.ANSI_RESET;
            output[2] += "    " + UserInteractionUtil.getPrintableColor(color)
                    + UserInteractionUtil.getPrintableCardLowerBorder(3)
                    + UserInteractionUtil.ANSI_RESET;
        }
        System.out.println(String.join("\n", output));
    }

    private void printHoldCards() {
        if (_holdCards.isEmpty()) {
            return;
        }
        System.out.println("Hold Cards");
        UserInteractionUtil.printItemsInOneRow(_holdCards);
    }
}
