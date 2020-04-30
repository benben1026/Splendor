package com.benben.splendor.gameItem;

import com.benben.splendor.util.Color;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Noble extends Item{

    public Noble(Map<Color, Integer> price) {
        super(price, 3);
        FULL_HEIGHT = 6;
    }

    public boolean affordable(Map<Color, Integer> cards) {
        return _price.entrySet().stream().allMatch(entry -> cards.get(entry.getKey()) >= entry.getValue());
    }

    @Override
    public List<String> toListOfString() {
        String colorCode = UserInteractionUtil.ANSI_PURPLE;
        List<String> output = new ArrayList<>();
        output.add(colorCode
                + UserInteractionUtil.getPrintableCardUpperBorder(9)
                + UserInteractionUtil.ANSI_RESET);
        output.add(colorCode
                + UserInteractionUtil.VERTICAL
                + String.format("score=%d", _score)
                + UserInteractionUtil.VERTICAL
                + UserInteractionUtil.ANSI_RESET);
        _price.forEach((color, count) -> output.add(UserInteractionUtil.getBorder(colorCode)
                + getPrintablePrice(color, count)
                + UserInteractionUtil.getBorder(colorCode)));
        for (int i = 0; i < 3 - _price.size(); i++) {
            output.add(UserInteractionUtil.getBorder(colorCode)
                    + "       "
                    + UserInteractionUtil.getBorder(colorCode));
        }
        output.add(colorCode
                + UserInteractionUtil.getPrintableCardLowerBorder(9)
                + UserInteractionUtil.ANSI_RESET + UserInteractionUtil.ANSI_RESET);
        return output;
    }

    private String getPrintablePrice(Color color, int count) {
        String colorString = UserInteractionUtil.getPrintableColor(color);
        return colorString + " &:   " + count + UserInteractionUtil.ANSI_RESET;
    }

    @Override
    public Noble clone() {
        try {
            return (Noble) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone noble");
        }
    }
}
