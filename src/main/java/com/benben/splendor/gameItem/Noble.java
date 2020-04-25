package com.benben.splendor.gameItem;

import java.util.Map;

public class Noble extends Item{

    public Noble(Map<Color, Integer> price) {
        _price = price;
        _score = 3;
    }
}
