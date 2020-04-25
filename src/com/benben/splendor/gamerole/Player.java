package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Color;
import java.util.List;
import java.util.Map;

public class Player extends Role{

    private Map<Color, Integer> _tokens;
    private List<Card> _cards;

    public Player(Map<Color, Integer> tokens, List<Card> cards) {
        super(tokens, cards);
        _tokens = tokens;
        _cards = cards;
    }
}
