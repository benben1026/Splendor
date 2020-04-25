package com.benben.splendor.gamerole;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Color;
import com.benben.splendor.util.GameInitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dealer extends Role{

    private List<Card> _inVisibleCardsLevel1;
    private List<Card> _inVisibleCardsLevel2;
    private List<Card> _inVisibleCardsLevel3;

    private List<Card> _VisibleCardsLevel1;
    private List<Card> _VisibleCardsLevel2;
    private List<Card> _VisibleCardsLevel3;


    public Dealer(int numOfPlayers) {
        GameInitUtil gameInitUtil = new GameInitUtil();
        gameInitUtil.initGame(numOfPlayers);
        _tokens = gameInitUtil.getTokens();
        _inVisibleCardsLevel1 = gameInitUtil.getCardsLevel1();
        _inVisibleCardsLevel2 = gameInitUtil.getCardsLevel2();
        _inVisibleCardsLevel3 = gameInitUtil.getCardsLevel3();
        initVisibleCards();
    }

    private void initVisibleCards() {}

    public boolean sell(Map<Color, Integer> tokens, Card card) {
        return false;
    }
}
