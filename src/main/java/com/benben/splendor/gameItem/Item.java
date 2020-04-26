package com.benben.splendor.gameItem;

import com.benben.splendor.util.ColorUtil;

import java.util.Map;

public abstract class Item {
    int _score;
    Map<ColorUtil.Color, Integer> _price;

    public int getSore() {
        return _score;
    }
}
