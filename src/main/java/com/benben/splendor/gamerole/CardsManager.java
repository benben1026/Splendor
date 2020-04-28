package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.CardsPosition;
import com.benben.splendor.util.Color;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<Noble> getNobles() {
        return _nobles;
    }

    public List<Card> getVisibleCards() {
        return Arrays.asList(_visibleCards);
    }

    public Noble getAndRemoveNoble(int index) {
        return index < _nobles.size() ? _nobles.remove(index) : null;
    }

    public Map<Integer, Noble> getAffordableNobleList(Map<Color, Integer> playerCards) {
        Map<Integer, Noble> affordableNobles = new HashMap<>();
        for (int i = 0; i < _nobles.size(); i++) {
            if (_nobles.get(i).affordable(playerCards)) {
                //todo: fix clone
                affordableNobles.put(i, _nobles.get(i).deepCopy());
                //affordableNobles.put(i, _nobles.get(i));
            }
        }
        return affordableNobles;
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

    public Map<CardsPosition, Card> getAllVisibleCardsCopy() {
        Map<CardsPosition, Card> cards = new HashMap<>();
        for (int i = 0; i < _visibleCards.length; i++) {
            cards.put(CardsPosition.getPositionFromIndex(i),
                    _visibleCards[i] == null ? null : _visibleCards[i].deepCopy());
            //todo: fix clone
            //cards.put(CardsPosition.getPositionFromIndex(i), _visibleCards[i]);
        }
        return cards;
    }

    public List<Noble> getAllNobleCopy() {
        // Todo: fix clone
        return _nobles.stream().map(Noble::deepCopy).collect(Collectors.toList());
        //return new ArrayList<>(_nobles);
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
