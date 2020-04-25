package java.com.benben.splendor.gameItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dealer extends Role{

    private Map<Color, Integer> _tokens;
    private List<Card> _inVisibleCards;
    private List<Card> _visibleCards;

    public Dealer(Map<Color, Integer> tokens, List<Card> cards) {
        super(tokens, cards);
        _tokens = tokens;
        _inVisibleCards = cards;
        _visibleCards = new ArrayList<>();
    }

    public boolean sell(Map<Color, Integer> tokens, Card card) {
        return false;
    }
}
