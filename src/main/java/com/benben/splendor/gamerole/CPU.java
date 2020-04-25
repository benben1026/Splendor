package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Color;

import java.util.List;
import java.util.Map;

public class CPU extends Player{
    public CPU(Map<Color, Integer> tokens, List<Card> cards) {
        super(tokens, cards);
    }
}
