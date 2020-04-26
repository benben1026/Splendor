package com.benben.splendor.gameItem;

import com.benben.splendor.gamerole.Player;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Card extends Item{
    public static final int CARD_LENGTH = 8;

    private final ColorUtil.Color _color;

    public Card(ColorUtil.Color color, int score, Map<ColorUtil.Color, Integer> price) {
        _color = color;
        _score = score;
        _price = price;
    }

    public boolean affordable(Map<ColorUtil.Color, Integer> tokens, Map<ColorUtil.Color, Integer> cards) {
        int balance = 0;
        for (Map.Entry<ColorUtil.Color, Integer> singleColorCost : _price.entrySet()) {
            ColorUtil.Color color = singleColorCost.getKey();
            int owned = tokens.getOrDefault(color, 0) + cards.getOrDefault(color, 0);
            if (owned < singleColorCost.getValue()) {
                balance += singleColorCost.getValue() - owned;
            }
        }
        return balance <= tokens.getOrDefault(ColorUtil.Color.YELLOW, 0);
    }

    public List<String> print() {
        List<String> output = new ArrayList<>();
        output.add(UserInteractionUtil.getPrintableColor(_color) + "_________"  + UserInteractionUtil.ANSI_RESET);
        output.add(UserInteractionUtil.getPrintableColor(_color) + String.format("|%7s|", _color.toString()) + UserInteractionUtil.ANSI_RESET);
        output.add(UserInteractionUtil.getPrintableColor(_color) + String.format("|score=%d|", _score) + UserInteractionUtil.ANSI_RESET);
        _price.forEach((color, count) -> output.add(getBorder(_color) + getPrintableToken(color, count) + getBorder(_color)));
        for (int i = 0; i < 4 - _price.size(); i++) {
            output.add(getBorder(_color) + "       " + getBorder(_color));
        }
        output.add(UserInteractionUtil.getPrintableColor(_color) + "---------" + UserInteractionUtil.ANSI_RESET + UserInteractionUtil.ANSI_RESET);
        return output;
    }

    private String getPrintableToken(ColorUtil.Color color, int count) {
        String colorString = UserInteractionUtil.getPrintableColor(color);
        return colorString + " @:   " + count + UserInteractionUtil.ANSI_RESET;
    }

    private String getBorder(ColorUtil.Color color) {
        return UserInteractionUtil.getPrintableColor(color) + "|" + UserInteractionUtil.ANSI_RESET;
    }

    public ColorUtil.Color getColor() {
        return _color;
    }

    public int getScore() {
        return _score;
    }

    public Map<ColorUtil.Color, Integer> getPrice() {
        return _price;
    }
}
