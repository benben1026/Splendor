package com.benben.splendor.gameItem;

import com.benben.splendor.util.Color;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Item {
    int _score;
    Map<Color, Integer> _price;

    public int FULL_HEIGHT;

    /**
     * This abstract method should implement the way of print the item. Each String
     * in the list represents one line. The reason for doing this is when printing
     * multiple items in the console, they could be printed side by side. The caller
     * could reconstruct the format after calling this method.
     * @return List of String, each String represents one line.
     */
    public abstract List<String> toListOfString();

    public Item(Map<Color, Integer> price, int score) {
        _score = score;
        _price = new LinkedHashMap<>();
        for (Map.Entry<Color, Integer> tokenToCount : price.entrySet()) {
            if (tokenToCount.getValue() != 0) {
                _price.put(tokenToCount.getKey(), tokenToCount.getValue());
            }
        }
    }

    public int getScore() {
        return _score;
    }

    public Map<Color, Integer> getPrice() {
        return _price;
    }

}
