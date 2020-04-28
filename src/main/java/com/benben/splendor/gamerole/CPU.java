package com.benben.splendor.gamerole;

import com.benben.splendor.util.ColorUtil;

import java.util.List;
import java.util.Map;

public class CPU extends Player{
    public CPU(String name) {
        super(name);
    }

    @Override
    public void notifyTurn(Dealer dealer, List<Player> players) {

    }

    @Override
    public Map<ColorUtil.Color, Integer> askToReturnTokens() {
        return null;
    }
}
