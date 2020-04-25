package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Role{

    public Player() {
        _tokens = new HashMap<>();
        _tokens.put(Color.GREEN, 0);
        _tokens.put(Color.BLUE, 0);
        _tokens.put(Color.BLACK, 0);
        _tokens.put(Color.RED, 0);
        _tokens.put(Color.WHITE, 0);
        _cards = new ArrayList<>();
    }
}
