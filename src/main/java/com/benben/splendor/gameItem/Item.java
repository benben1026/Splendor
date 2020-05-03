package com.benben.splendor.gameItem;

import com.benben.splendor.util.Color;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Item {
    final int _score;
    final Map<Color, Integer> _price;

    /**
     * This abstract method should implement the way of print the item. Each String
     * in the list represents one line. The reason for doing this is when printing
     * multiple items in the console, they could be printed side by side. The caller
     * could reconstruct the format after calling this method.
     * @return List of String, each String represents one line.
     */
    public abstract List<String> toListOfString();

    public abstract int getFullHeight();

    public abstract int getFoldedHeight();

    public abstract int getItemWidth();

    public Item(Map<Color, Integer> price, int score) {
        _score = score;
        _price = Collections.unmodifiableMap(
                price.entrySet().stream()
                        .filter(entry -> entry.getValue() > 0)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public int getScore() {
        return _score;
    }

    public Map<Color, Integer> getPrice() {
        return _price;
    }

}
