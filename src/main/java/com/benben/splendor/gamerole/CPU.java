package com.benben.splendor.gamerole;

import com.benben.splendor.action.ActionAndResponse;
import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.util.CardsPosition;
import com.benben.splendor.util.Color;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CPU extends Player{
    public CPU(String name) {
        super(name);
    }

    @Override
    public ActionAndResponse askForAction(List<Player> opponents, Map<Color, Integer> remainingTokens, Map<CardsPosition, Card> visibleCards, List<Noble> nobles) {
        return null;
    }

    @Override
    public int pickNoble(Map<Integer, Noble> nobles) {
        return 0;
    }

    @Override
    public Map<Color, Integer> askToReturnTokens() {
        return Collections.emptyMap();
    }

    @Override
    public CPU deepCopy() {
        return new CPU(this._name);
    }
}
