package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class Role {

    String _name;
    LinkedHashMap<ColorUtil.Color, Integer> _tokens;

    public Role(String name) {
        _name = name;
        _tokens = new LinkedHashMap<>();
        _tokens.put(ColorUtil.Color.WHITE, 0);
        _tokens.put(ColorUtil.Color.BLUE, 0);
        _tokens.put(ColorUtil.Color.GREEN, 0);
        _tokens.put(ColorUtil.Color.RED, 0);
        _tokens.put(ColorUtil.Color.BLACK, 0);
        _tokens.put(ColorUtil.Color.YELLOW, 0);
    }

    public abstract void printCurrentStatus(boolean myTurn);

    public String getName() {
        return _name;
    }

    public LinkedHashMap<ColorUtil.Color, Integer> getTokens() {
        return _tokens;
    }

    void printToken() {
        List<String> output = new ArrayList<>();
        output.add(UserInteractionUtil.ANSI_YELLOW + "#:" + _tokens.get(ColorUtil.Color.YELLOW) + UserInteractionUtil.ANSI_RESET);
        _tokens.forEach((color, count) -> {
            if (color != ColorUtil.Color.YELLOW)
                output.add(UserInteractionUtil.getPrintableColor(color) + "@:" + count + UserInteractionUtil.ANSI_RESET);
        });
        System.out.println(String.join("    ", output));
    }
}
