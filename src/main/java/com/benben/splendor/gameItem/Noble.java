package com.benben.splendor.gameItem;

import com.benben.splendor.util.Color;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Noble extends Item{

    private static final int NOBLE_FULL_HEIGHT = 6;
    private static final int NOBLE_FOLDED_HEIGHT = 4;
    private static final int NOBLE_WIDTH = 9;

    public Noble(Map<Color, Integer> price) {
        super(price, 3);
    }

    public boolean affordable(Map<Color, Integer> cards) {
        return _price.entrySet().stream().allMatch(entry -> cards.get(entry.getKey()) >= entry.getValue());
    }

    @Override
    public int getFullHeight() {
        return NOBLE_FULL_HEIGHT;
    }

    @Override
    public int getItemWidth() {
        return NOBLE_WIDTH;
    }

    @Override
    public int getFoldedHeight() {
        return NOBLE_FOLDED_HEIGHT;
    }

    @Override
    public List<String> toListOfString() {
        String colorCode = Color.PURPLE.toPrintable();
        List<String> output = new ArrayList<>();
        output.add(UserInteractionUtil.getPrintableCardUpperBorder(9, Color.PURPLE));
        output.add(colorCode
                + UserInteractionUtil.VERTICAL
                + String.format("score=%d", _score)
                + UserInteractionUtil.VERTICAL
                + Color.ANSI_RESET);
        _price.forEach((color, count) -> output.add(UserInteractionUtil.getBorder(colorCode)
                + getPrintablePrice(color, count)
                + UserInteractionUtil.getBorder(colorCode)));
        for (int i = 0; i < 3 - _price.size(); i++) {
            output.add(UserInteractionUtil.getBorder(colorCode)
                    + "       "
                    + UserInteractionUtil.getBorder(colorCode));
        }
        output.add(UserInteractionUtil.getPrintableCardLowerBorder(9, Color.PURPLE));
        return output;
    }

    private String getPrintablePrice(Color color, int count) {
        return color.toPrintable() + " &:   " + count + Color.ANSI_RESET;
    }

    public Noble deepCopy() {
        return new Noble(this._price);
    }
}
