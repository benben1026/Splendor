package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.*;

public class Dealer extends Role{

    private List<Noble> _nobles = new ArrayList<>();

    private List<Card> _invisibleCardsLevel1 = new ArrayList<>();
    private List<Card> _invisibleCardsLevel2 = new ArrayList<>();
    private List<Card> _invisibleCardsLevel3 = new ArrayList<>();

    private List<Card> _visibleCardsLevel1 = new ArrayList<>();
    private List<Card> _visibleCardsLevel2 = new ArrayList<>();
    private List<Card> _visibleCardsLevel3 = new ArrayList<>();

    private Random _random;

    public Dealer(int numOfPlayers) {
        super("Bank");
        _random = new Random(System.currentTimeMillis());
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
        getRandomCardsFromInvisibleCards(_invisibleCardsLevel1, _visibleCardsLevel1, 4);
        getRandomCardsFromInvisibleCards(_invisibleCardsLevel2, _visibleCardsLevel2, 4);
        getRandomCardsFromInvisibleCards(_invisibleCardsLevel3, _visibleCardsLevel3, 4);
    }

    private void getRandomCardsFromInvisibleCards(List<Card> invisible, List<Card> visible, int num) {
        while(num > 0) {
            int index = (int) _random.nextInt(invisible.size());
            visible.add(invisible.remove(index));
            num --;
        }
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
        Card card;
        try {
            if (row == 0) {
                card = _visibleCardsLevel3.remove(col);
                getRandomCardFromInvisibleList(_invisibleCardsLevel3, _visibleCardsLevel3, col);
            } else if (row == 1) {
                card = _visibleCardsLevel2.remove(col);
                getRandomCardFromInvisibleList(_invisibleCardsLevel2, _visibleCardsLevel2, col);
            } else if (row == 2) {
                card = _visibleCardsLevel1.remove(col);
                getRandomCardFromInvisibleList(_invisibleCardsLevel1, _visibleCardsLevel1, col);
            } else {
                return null;
            }
            return card;
        } catch (Exception ex) {
            return null;
        }
    }

    private void getRandomCardFromInvisibleList(List<Card> invisibleCardList, List<Card> visibleCardList, int index) {
        if (invisibleCardList.isEmpty()) {
            return;
        }
        int removeIndex = _random.nextInt(invisibleCardList.size());
        visibleCardList.add(index, invisibleCardList.remove(removeIndex));
    }

    public void validatePlayerTokenCounts(Player player) {
        while (true) {
            if (player.getTotalTokensCount() <= 10) {
                return;
            }
            Map<ColorUtil.Color, Integer> tokenToReturn =
                    Optional.ofNullable(player.askToReturnTokens()).orElse(new HashMap<>());
            for (Map.Entry<ColorUtil.Color, Integer> entry : tokenToReturn.entrySet()) {
                if (player.payWithTokens(entry.getKey(), entry.getValue())) {
                    this.receiveTokens(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public boolean requestToBuyHoldCard(Player player, int index) {
        if (player.getHoldCards().size() == 0 || index > player.getHoldCards().size()) {
            return false;
        }
        Card cardToBuy = player.getHoldCards().get(index);
        Map<ColorUtil.Color, Integer> cardsByCount = player.getCardsByCount();
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
        player.receiveHoldCard(removeCardFromIndex(index));
        if (_tokens.get(ColorUtil.Color.YELLOW) > 0) {
            player.receiveTokens(ColorUtil.Color.YELLOW, 1);
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
        Map<ColorUtil.Color, Integer> cardsByCount = player.getCardsByCount();
        if (!cardToBuy.affordable(player.getTokens(), cardsByCount)) {
            return false;
        }
        buyCard(player, cardToBuy);
        removeCardFromIndex(index);
        return true;
    }

    public void buyCard(Player player, Card cardToBuy) {
        Map<ColorUtil.Color, Integer> cardsByCount = player.getCardsByCount();
        for (Map.Entry<ColorUtil.Color, Integer> singleColorCost : cardToBuy.getPrice().entrySet()) {
            ColorUtil.Color color = singleColorCost.getKey();
            int needTokenCount = singleColorCost.getValue() - cardsByCount.getOrDefault(color, 0);
            if (needTokenCount <= player.getTokens().getOrDefault(color, 0)) {
                // No need to use Star Token
                player.payWithTokens(color, needTokenCount);
                this.receiveTokens(color, needTokenCount);
            } else {
                // Need to use Star Token
                int diff = needTokenCount - player.getTokens().getOrDefault(color, 0);
                player.payWithTokens(color, needTokenCount - diff);
                player.payWithTokens(ColorUtil.Color.YELLOW, diff);
                this.receiveTokens(ColorUtil.Color.YELLOW, diff);
                this.receiveTokens(color, needTokenCount - diff);
            }
        }
        player.receiveCard(cardToBuy);
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
