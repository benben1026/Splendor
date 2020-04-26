package com.benben.splendor.gameItem;

import com.benben.splendor.util.ColorUtil;

import java.util.Map;

public class Noble extends Item{

    public Noble(Map<ColorUtil.Color, Integer> price) {
        _price = price;
        _score = 3;
    }
}
