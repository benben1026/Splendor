package com.benben.splendor.gamerole;

import com.benben.splendor.util.Color;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Role {

    String _name;
    Map<Color, Integer> _tokens;

    public Role(String name) {
        _name = name;
        _tokens = new LinkedHashMap<>();
        _tokens.put(Color.WHITE, 0);
        _tokens.put(Color.BLUE, 0);
        _tokens.put(Color.GREEN, 0);
        _tokens.put(Color.RED, 0);
        _tokens.put(Color.BLACK, 0);
        _tokens.put(Color.YELLOW, 0);
    }

    public String getName() {
        return _name;
    }

    public Map<Color, Integer> getTokens() {
        return _tokens;
    }

    public final void receiveTokens(Color color, int count) {
        _tokens.put(color, _tokens.get(color) + count);
    }

    public final boolean spendTokens(Color color, int count) {
        if (_tokens.get(color) < count) {
            return false;
        }
        _tokens.put(color, _tokens.get(color) - count);
        return true;
    }

    public int getTotalTokensCount() {
        return _tokens.values().stream().mapToInt(i -> i).sum();
    }

    public void printToken() {
        List<String> output = new ArrayList<>();
        output.add(Color.YELLOW.toPrintable() + "*:" + _tokens.get(Color.YELLOW) + Color.ANSI_RESET);
        _tokens.forEach((color, count) -> {
            if (color != Color.YELLOW)
                output.add(color.toPrintable() + "@:" + count + Color.ANSI_RESET);
        });
        UserInteractionUtil.SYSTEM_OUT.println(String.join("    ", output));
    }
}
