package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Color;
import java.util.List;
import java.util.Map;

public abstract class Role {

    Map<Color, Integer> _tokens;
    List<Card> _cards;
}
