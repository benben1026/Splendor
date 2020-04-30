package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.CardsPosition;

import java.util.List;
import java.util.Random;

public class CardsManager {
    private static final int VISIBLE_CARDS_PER_LEVEL = 4;
    private static final int TOTAL_VISIBLE_CARDS = VISIBLE_CARDS_PER_LEVEL * 3;

    private final Random _random;
    private final List<Noble> _nobles;
    private final Card[] _visibleCards = new Card[TOTAL_VISIBLE_CARDS];
    private final List<Card> _level1Deck;
    private final List<Card> _level2Deck;
    private final List<Card> _level3Deck;

    public CardsManager(List<Card> level1Deck, List<Card> level2Deck, List<Card> level3Deck, List<Noble> nobles) {
        _random = new Random(System.currentTimeMillis());

        _nobles = nobles;
        _level1Deck = level1Deck;
        _level2Deck = level2Deck;
        _level3Deck = level3Deck;

        for (int i = 0; i < TOTAL_VISIBLE_CARDS; i++) {
            _visibleCards[i] = flipCard(i/4 + 1);
        }
    }

    public Noble takeNoble(int index) {
        return index < _nobles.size() ? _nobles.get(index) : null;
    }

    public Card getCard(CardsPosition position) {
        if (position != CardsPosition.LEVEL1_DECK_TOP
                && position != CardsPosition.LEVEL2_DECK_TOP
                && position != CardsPosition.LEVEL3_DECK_TOP) {
            return _visibleCards[position.getCardPositionIndex()];
        } else {
            return null;
        }
    }

    public Card getAndRemoveCard(CardsPosition position) {
        Card cardToReturn;
        if (position != CardsPosition.LEVEL1_DECK_TOP
                && position != CardsPosition.LEVEL2_DECK_TOP
                && position != CardsPosition.LEVEL3_DECK_TOP) {
            cardToReturn = _visibleCards[position.getCardPositionIndex()];
            _visibleCards[position.getCardPositionIndex()] = flipCard(position.getCardPositionIndex() / 4 + 1);
        } else {
            cardToReturn = flipCard(-1 * position.getCardPositionIndex());
        }
        return cardToReturn;
    }

    private Card flipCard(int level) {
        List<Card> tmp;
        switch (level) {
            case 1:
                tmp = _level1Deck;
                break;
            case 2:
                tmp = _level2Deck;
                break;
            case 3:
                tmp = _level3Deck;
                break;
            default:
                return null;
        }
        if (tmp.isEmpty()) {
            return null;
        }
        return tmp.remove(_random.nextInt(tmp.size()));
    }
}
