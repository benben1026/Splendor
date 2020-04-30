package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.CardsPosition;
import com.benben.splendor.util.Color;
import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.*;

public class Dealer extends Role{
    private CardsManager _cardsManager;
    private List<List<Card>> _playerHoldingCards = new ArrayList<>();

    private Random _random;

    public Dealer(int numOfPlayers) {
        super("Bank");
        _random = new Random(System.currentTimeMillis());
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        List<Card> deck3 = new ArrayList<>();
        List<Noble> nobles = new ArrayList<>();
        GameInitUtil.loadCardsFromJson(numOfPlayers,deck1, deck2, deck3, nobles);
        _cardsManager = new CardsManager(deck1, deck2, deck3, nobles);

        for (int i = 0; i < numOfPlayers; i++) {
            _playerHoldingCards.add(new ArrayList<>());
        }

        int tokenCount;
        if (numOfPlayers == 2 || numOfPlayers == 3) {
            tokenCount = numOfPlayers + 2;
        } else {
            tokenCount = 7;
        }
        _tokens.put(Color.GREEN, tokenCount);
        _tokens.put(Color.WHITE, tokenCount);
        _tokens.put(Color.BLACK, tokenCount);
        _tokens.put(Color.RED, tokenCount);
        _tokens.put(Color.BLUE, tokenCount);
        _tokens.put(Color.YELLOW, 5);
    }

    private void initVisibleCards(List<Card> invisible, List<Card> visible, int num) {
        while(num > 0) {
            visible.add(getNextRandomCard(invisible));
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
                _visibleCardsLevel3.add(col, _topCardFromDeck3);
                _topCardFromDeck3 = getNextRandomCard(_invisibleCardsLevel3);
            } else if (row == 1) {
                card = _visibleCardsLevel2.remove(col);
                _visibleCardsLevel2.add(col, _topCardFromDeck2);
                _topCardFromDeck2 = getNextRandomCard(_invisibleCardsLevel2);
            } else if (row == 2) {
                card = _visibleCardsLevel1.remove(col);
                _visibleCardsLevel1.add(col, _topCardFromDeck1);
                _topCardFromDeck1 = getNextRandomCard(_invisibleCardsLevel1);
            } else {
                return null;
            }
            return card;
        } catch (Exception ex) {
            return null;
        }
    }

    private Card getNextRandomCard(List<Card> invisibleCardList) {
        if (invisibleCardList.isEmpty()) {
            return null;
        }
        return invisibleCardList.remove((int)_random.nextInt(invisibleCardList.size()));
    }

    public void validatePlayerTokenCounts(Player player) {
        while (true) {
            if (player.getTotalTokensCount() <= 10) {
                return;
            }
            Map<Color, Integer> tokenToReturn =
                    Optional.ofNullable(player.askToReturnTokens()).orElse(new HashMap<>());
            for (Map.Entry<Color, Integer> entry : tokenToReturn.entrySet()) {
                if (player.payWithTokens(entry.getKey(), entry.getValue())) {
                    this.receiveTokens(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void buyCard(Player player, Card cardToBuy) {
        Map<Color, Integer> cardsByCount = player.getCardsByCount();
        for (Map.Entry<Color, Integer> singleColorCost : cardToBuy.getPrice().entrySet()) {
            Color color = singleColorCost.getKey();
            int needTokenCount = singleColorCost.getValue() - cardsByCount.getOrDefault(color, 0);
            if (needTokenCount <= player.getTokens().getOrDefault(color, 0)) {
                // No need to use Star Token
                player.payWithTokens(color, needTokenCount);
                this.receiveTokens(color, needTokenCount);
            } else {
                // Need to use Star Token
                int diff = needTokenCount - player.getTokens().getOrDefault(color, 0);
                player.payWithTokens(color, needTokenCount - diff);
                player.payWithTokens(Color.YELLOW, diff);
                this.receiveTokens(Color.YELLOW, diff);
                this.receiveTokens(color, needTokenCount - diff);
            }
        }
        player.receiveCard(cardToBuy);
    }

    public boolean playerRequestToTakeTokens(Player player, Map<Color, Integer> tokens) {
        int total = tokens.values().stream().mapToInt(i -> i).sum();
        int max = tokens.values().stream().mapToInt(i -> i).max().getAsInt();
        if (max >= 2 && total > 2) {
            return false;
        }
        if (total > 3) {
            return false;
        }
        if (!tokens.entrySet().stream().allMatch((entry) -> _tokens.get(entry.getKey()) >= entry.getValue())) {
            return false;
        }
        for (Map.Entry<Color, Integer> entry : tokens.entrySet()) {
            player.receiveTokens(entry.getKey(), entry.getValue());
            this.spendTokens(entry.getKey(), entry.getValue());
        }
        return true;
    }

    public boolean playerRequestToBuyCard(Player player, CardsPosition cardPosition) {
        Card cardToBuy = _cardsManager.getCard(cardPosition);
        if (cardToBuy == null) {
            return false;
        }
        Map<Color, Integer> cardsByCount = player.getCardsByCount();
        if (!cardToBuy.affordable(player.getTokens(), cardsByCount)) {
            return false;
        }
        cardToBuy = _cardsManager.getAndRemoveCard(cardPosition);
        player.receiveCard(cardToBuy);
        return true;
    }

    public boolean playerRequestToHoldCard(Player player, int playerIndex, CardsPosition cardPosition) {
        if (_playerHoldingCards.get(playerIndex).size() >= 3) {
            return false;
        }
        Card cardToHold = _cardsManager.getAndRemoveCard(cardPosition);
        _playerHoldingCards.get(playerIndex).add(cardToHold);
        if (_tokens.get(Color.YELLOW) > 0) {
            player.receiveTokens(Color.YELLOW, 1);
            this.spendTokens(Color.YELLOW, 1);
            // Todo: check the total number of tokens does not exceed 10
        }
        return true;
    }

    public boolean playerRequestToBuyHoldCard(Player player, int playerIndex, int index) {
        if (_playerHoldingCards.get(playerIndex).size() == 0 || index > _playerHoldingCards.get(playerIndex).size()) {
            return false;
        }
        Card cardToBuy = _playerHoldingCards.get(playerIndex).get(index);
        Map<Color, Integer> cardsByCount = player.getCardsByCount();
        if (!cardToBuy.affordable(player.getTokens(), cardsByCount)) {
            return false;
        }
        player.receiveCard(cardToBuy);
        _playerHoldingCards.get(playerIndex).remove(index);
        return true;
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
