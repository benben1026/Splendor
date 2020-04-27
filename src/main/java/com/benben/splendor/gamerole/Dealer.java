package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Card getCardFromIndex(int index) {
        int row = index / 4;
        int col = index % 4;
        try {
            if (row == 0) {
                return _visibleCardsLevel3.get(col);
            } else if (row == 1) {
                return _visibleCardsLevel2.get(col);
            } else if (row == 2) {
                return _visibleCardsLevel1.get(col);
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    private Card removeCardFromIndex(int index) {
        int row = index / 4;
        int col = index % 4;
        try {
            if (row == 0) {
                return _visibleCardsLevel3.remove(col);
            } else if (row == 1) {
                return _visibleCardsLevel2.remove(col);
            } else if (row == 2) {
                return _visibleCardsLevel1.remove(col);
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean requestToBuyHoldCard(Player player, int index) {
        if (player.getHoldCards().size() == 0 || index > player.getHoldCards().size()) {
            return false;
        }
        Card cardToBuy = player.getHoldCards().get(index);
        Map<ColorUtil.Color, Integer> cardsByCount = player.getCards().entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
        if (!cardToBuy.affordable(player.getTokens(), cardsByCount)) {
            return false;
        }
        buyCard(player, cardToBuy);
        player.getHoldCards().remove(index);
        return true;
    }

    public boolean requestToHoldCard(Player player, int index) {
        if (player.getHoldCards().size() >= 3) {
            return false;
        }
        player.addHoldCard(removeCardFromIndex(index));
        if (_tokens.get(ColorUtil.Color.YELLOW) > 0) {
            player.addToken(ColorUtil.Color.YELLOW, 1);
            _tokens.put(ColorUtil.Color.YELLOW, _tokens.get(ColorUtil.Color.YELLOW) - 1);
            // Todo: check the total number of tokens does not exceed 10
        }
        return true;
    }

    public boolean requestToBuyCard(Player player, int index) {
        Card cardToBuy = getCardFromIndex(index);
        if (cardToBuy == null) {
            return false;
        }
        Map<ColorUtil.Color, Integer> cardsByCount = player.getCards().entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
        if (!cardToBuy.affordable(player.getTokens(), cardsByCount)) {
            return false;
        }
        buyCard(player, cardToBuy);
        removeCardFromIndex(index);
        return true;
    }

    public void buyCard(Player player, Card card) {
        Map<ColorUtil.Color, Integer> cardsByCount = player.getCards().entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
        for (Map.Entry<ColorUtil.Color, Integer> singleColorCost : card.getPrice().entrySet()) {
            ColorUtil.Color color = singleColorCost.getKey();
            int needTokenCount = singleColorCost.getValue() - cardsByCount.getOrDefault(color, 0);
            if (needTokenCount <= player.getTokens().getOrDefault(color, 0)) {
                // No need to use Star Token
                player.getTokens().put(color, player.getTokens().getOrDefault(color, 0) - needTokenCount);
                this.addToken(color, needTokenCount);
            } else {
                // Need to use Star Token
                int diff = needTokenCount - player.getTokens().getOrDefault(color, 0);
                player.getTokens().put(ColorUtil.Color.YELLOW, player.getTokens().get(ColorUtil.Color.YELLOW) - diff);
                player.getTokens().put(color, 0);
                this.addToken(ColorUtil.Color.YELLOW, diff);
                this.addToken(color, needTokenCount - diff);
            }
        }
        player.takeCard(card);
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

    public void addToken(ColorUtil.Color color, int count) {
        _tokens.put(color, _tokens.get(color) + count);
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

    @Override
    public void printCurrentStatus(boolean myTurn) {
        System.out.println(_name + ":");
        UserInteractionUtil.printItemsInOneRow(_nobles);
        printToken();
        UserInteractionUtil.printItemsInOneRow(_visibleCardsLevel3);
        UserInteractionUtil.printItemsInOneRow(_visibleCardsLevel2);
        UserInteractionUtil.printItemsInOneRow(_visibleCardsLevel1);
    }
}
