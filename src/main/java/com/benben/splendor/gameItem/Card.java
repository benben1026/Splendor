package com.benben.splendor.gameItem;

import java.util.Map;

public class Card extends Item{
    private final Color _color;

    public Card(Color color, int score, Map<Color, Integer> price) {
        _color = color;
        _score = score;
        _price = price;
    }

    public Color getColor() {
        return _color;
    }

    public int getScore() {
        return _score;
    }

    public Map<Color, Integer> getPrice() {
        return _price;
    }
}
