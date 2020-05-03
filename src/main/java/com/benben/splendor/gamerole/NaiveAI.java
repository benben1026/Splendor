package com.benben.splendor.gamerole;

import com.benben.splendor.action.ActionAndResponse;
import com.benben.splendor.action.BuyCardActionAndResponse;
import com.benben.splendor.action.TakeTokenActionAndResponse;
import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.CardsPosition;
import com.benben.splendor.util.Color;

import java.util.*;
import java.util.stream.Collectors;

public class NaiveAI extends Player {
    private static final Random random = new Random(System.currentTimeMillis());

    public NaiveAI(String name) {
        super(name);
    }

    public NaiveAI(String name, LinkedHashMap<Color, List<Card>> cards, List<Noble> nobles,
                   LinkedHashMap<Color, Integer> tokens) {
        super(name, cards, nobles, tokens);
    }

    @Override
    public ActionAndResponse askForAction(List<Player> opponents, Map<Color, Integer> remainingTokens,
                                          Map<CardsPosition, Card> visibleCards, List<Noble> nobles) {
        LinkedHashMap<CardsPosition, Card> cardsOrderedByScore = visibleCards.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .sorted(Comparator.comparingInt(entry -> entry.getValue().getScore() * -1))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (Map.Entry<CardsPosition, Card> entry : cardsOrderedByScore.entrySet()) {
            if (entry.getValue().affordable(_tokens, this.getCardsByCount())) {
                return new BuyCardActionAndResponse(entry.getKey());
            }
        }

        LinkedHashMap<Integer, Map<Color, Integer>> tokenNeeds = visibleCards.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .sorted(Comparator.comparingInt(
                        entry -> getNumOfTokenNeed(entry.getValue()).values().stream().mapToInt(i -> i).sum()))
                .collect(Collectors.toMap(
                        entry -> getNumOfTokenNeed(entry.getValue()).values().stream().mapToInt(i -> i).sum(),
                        entry -> getNumOfTokenNeed(entry.getValue()),
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        Iterator<Map.Entry<Integer, Map<Color, Integer>>> iterator = tokenNeeds.entrySet().iterator();
        Map.Entry<Integer, Map<Color, Integer>> entry = iterator.next();
        Map<Color, Integer> output = entry.getValue();
        for (int i = 0; i < Color.COLOR_COUNT; i++) {
            if (!output.containsKey(Color.getColorFromIndex(i))) {
                output.put(Color.getColorFromIndex(i), 0);
            }
        }
        int quota = 3;
        for (Map.Entry<Color, Integer> pair : output.entrySet()) {
            if (pair.getValue() > 0 && quota > 0 && remainingTokens.get(pair.getKey()) > 0) {
                pair.setValue(1);
                quota --;
            } else if (pair.getValue() > 0) {
                pair.setValue(0);
            }
        }
        if (quota <= 0) {
            return new TakeTokenActionAndResponse(output);
        }
        int tryTime = 50;
        while (quota > 0 && tryTime > 0) {
            Color color = Color.getColorFromIndex(random.nextInt(Color.COLOR_COUNT));
            if (output.get(color) == 0 && remainingTokens.get(color) > 0) {
                output.put(color, 1);
                quota --;
            }
            tryTime --;
        }
        return new TakeTokenActionAndResponse(output);
    }

    @Override
    public int pickNoble(Map<Integer, Noble> nobles) {
        return (int)nobles.keySet().toArray()[0];
    }

    @Override
    public NaiveAI deepCopy() {
        return new NaiveAI(this.getName(), _cards, _nobles, _tokens);
    }

    @Override
    public Map<Color, Integer> askToReturnTokens() {
        Map<Color, Integer> tokenToReturn = new HashMap<>();
        while (_tokens.values().stream().mapToInt(i -> i).sum()
                - tokenToReturn.values().stream().mapToInt(i -> i).sum()> 10) {
            Color color = Color.getColorFromIndex(random.nextInt(Color.COLOR_COUNT));
            if (_tokens.get(color) > 0) {
                tokenToReturn.put(color, tokenToReturn.getOrDefault(color, 0) + 1);
            }
        }
        return tokenToReturn;
    }

    private Map<Color, Integer> getNumOfTokenNeed(Card card) {
        Map<Color, Integer> price = card.getPrice();
        for (Map.Entry<Color, Integer> pair : price.entrySet()) {
            int diff = pair.getValue()
                    - getCardsByCount().get(pair.getKey())
                    - _tokens.get(pair.getKey());
            pair.setValue(Math.max(diff, 0));
        }
        return price;
    }
}
