package com.benben.splendor.gameItem;

import com.benben.splendor.util.Color;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Card extends Item{
    public static final int CARD_FOLDED_HEIGHT = 3;
    private static final int CARD_FULL_HEIGHT = 8;
    private static final int CARD_WIDTH = 9;

    private final Color _color;

    public Card(Color color, int score, Map<Color, Integer> price) {
        super(price, score);
        _color = color;
    }

    public boolean affordable(Map<Color, Integer> tokens, Map<Color, Integer> cards) {
        int balance = 0;
        for (Map.Entry<Color, Integer> singleColorCost : _price.entrySet()) {
            Color color = singleColorCost.getKey();
            int owned = tokens.getOrDefault(color, 0) + cards.getOrDefault(color, 0);
            if (owned < singleColorCost.getValue()) {
                balance += singleColorCost.getValue() - owned;
            }
        }
        return balance <= tokens.getOrDefault(Color.YELLOW, 0);
    }

    @Override
    public int getFullHeight() {
        return CARD_FULL_HEIGHT;
    }

    @Override
    public int getFoldedHeight() {
        return CARD_FOLDED_HEIGHT;
    }

    @Override
    public int getItemWidth() {
        return CARD_WIDTH;
    }

    @Override
    public List<String> toListOfString() {
        List<String> output = new ArrayList<>();
        output.add(UserInteractionUtil.getPrintableCardUpperBorder(9, _color));
        output.add(_color.toPrintable()
                + UserInteractionUtil.VERTICAL
                + String.format("%7s", _color.toString())
                + UserInteractionUtil.VERTICAL
                + Color.ANSI_RESET);
        output.add(_color.toPrintable()
                + UserInteractionUtil.VERTICAL
                + String.format("score=%d", _score)
                + UserInteractionUtil.VERTICAL
                + Color.ANSI_RESET);
        _price.forEach((color, count) -> output.add(getBorder(_color) + getPrintableToken(color, count) + getBorder(_color)));
        for (int i = 0; i < 4 - _price.size(); i++) {
            output.add(getBorder(_color) + "       " + getBorder(_color));
        }
        output.add(UserInteractionUtil.getPrintableCardLowerBorder(9, _color));
        return output;
    }

    private String getPrintableToken(Color color, int count) {
        String colorString = color.toPrintable();
        return colorString + " @:   " + count + Color.ANSI_RESET;
    }

    private String getBorder(Color color) {
        return UserInteractionUtil.getBorder(color.toPrintable());
    }

    public Color getColor() {
        return _color;
    }
}
