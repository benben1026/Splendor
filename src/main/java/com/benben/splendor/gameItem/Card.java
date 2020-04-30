package com.benben.splendor.gameItem;

import com.benben.splendor.util.Color;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Card extends Item{
    public static final int CARD_FOLD_HEIGHT = 3;

    private final Color _color;

    public Card(Color color, int score, Map<Color, Integer> price) {
        super(price, score);
        _color = color;
        FULL_HEIGHT = 8;
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
    public List<String> toListOfString() {
        List<String> output = new ArrayList<>();
        output.add(UserInteractionUtil.getPrintableColor(_color)
                + UserInteractionUtil.getPrintableCardUpperBorder(9)
                + UserInteractionUtil.ANSI_RESET);
        output.add(UserInteractionUtil.getPrintableColor(_color)
                + UserInteractionUtil.VERTICAL
                + String.format("%7s", _color.toString())
                + UserInteractionUtil.VERTICAL
                + UserInteractionUtil.ANSI_RESET);
        output.add(UserInteractionUtil.getPrintableColor(_color)
                + UserInteractionUtil.VERTICAL
                + String.format("score=%d", _score)
                + UserInteractionUtil.VERTICAL
                + UserInteractionUtil.ANSI_RESET);
        _price.forEach((color, count) -> output.add(getBorder(_color) + getPrintableToken(color, count) + getBorder(_color)));
        for (int i = 0; i < 4 - _price.size(); i++) {
            output.add(getBorder(_color) + "       " + getBorder(_color));
        }
        output.add(UserInteractionUtil.getPrintableColor(_color)
                + UserInteractionUtil.getPrintableCardLowerBorder(9)
                + UserInteractionUtil.ANSI_RESET + UserInteractionUtil.ANSI_RESET);
        return output;
    }

    private String getPrintableToken(Color color, int count) {
        String colorString = UserInteractionUtil.getPrintableColor(color);
        return colorString + " @:   " + count + UserInteractionUtil.ANSI_RESET;
    }

    private String getBorder(Color color) {
        return UserInteractionUtil.getBorder(UserInteractionUtil.getPrintableColor(color));
    }

    public Color getColor() {
        return _color;
    }

    @Override
    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone card");
        }
    }
}
