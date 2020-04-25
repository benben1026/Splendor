package java.com.benben.splendor.gamerole;

import java.com.benben.splendor.gameItem.Card;
import java.com.benben.splendor.gameItem.Color;
import java.util.List;
import java.util.Map;

public abstract class Role {

    private Map<Color, Integer> _tokens;
    private List<Card> _cards;

    public Role(Map<Color, Integer> tokens, List<Card> cards) {
        _tokens = tokens;
        _cards = cards;
    }
}
