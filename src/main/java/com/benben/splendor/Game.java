package com.benben.splendor;

import com.benben.splendor.action.*;
import com.benben.splendor.gamerole.Dealer;
import com.benben.splendor.gamerole.Player;
import com.benben.splendor.util.Color;
import com.benben.splendor.util.GameConfig;
import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Game {

    private Dealer _dealer;
    private List<Player> _players;
    private int _currentPlayerIndex;

    private void start() {
        init();
        int round = 1;

        do {
            while (_currentPlayerIndex < _players.size()) {
                // Print game status
                UserInteractionUtil.printHeader(round);
                _dealer.printCurrentStatus();
                for (int i = 0; i < _players.size(); i++) {
                    UserInteractionUtil.printPlayerCurrentStatus(_players.get(i), i == _currentPlayerIndex,
                            _dealer.getPlayerHoldCards(_currentPlayerIndex));
                }
                Player currentPlayer = _players.get(_currentPlayerIndex);
                UserInteractionUtil.SYSTEM_OUT.println(String.format("It is %s's turn.", currentPlayer.getName()));

                while(!notifyPlayer()) {
                    continue;
                }

                _dealer.validatePlayerTokenCounts(currentPlayer);
                _dealer.validateEligibleForNoble(currentPlayer);
                _currentPlayerIndex ++;
            }
            _currentPlayerIndex = 0;
            round ++;
        } while (_dealer.checkWinner(_players) == null);
        
    }

    private void init() {
        GameConfig gameConfig = GameInitUtil.loadGameConfig();
        _dealer = new Dealer(gameConfig._players.size(), gameConfig._targetScore);
        _players = gameConfig._players;
        _currentPlayerIndex = 0;
    }

    private boolean notifyPlayer() {
        List<Player> opponents = new ArrayList<>();
        int i = _currentPlayerIndex + 1;
        while (i % _players.size() != _currentPlayerIndex) {
            i = i % _players.size();
            opponents.add(_players.get(i).deepCopy());
            i++;
        }

        Player currentPlayer = _players.get(_currentPlayerIndex);
        ActionAndResponse as = currentPlayer.askForAction(opponents,
                (Map<Color, Integer>)_dealer.getTokens().clone(), _dealer.getAllVisibleCardsCopy(),
                _dealer.getAllNobleCopy());
        switch (as.getAction()) {
            case TAKE_TOKENS:
                return _dealer.playerRequestToTakeTokens(currentPlayer, ((TakeTokenActionAndResponse)as).getResponse());
            case BUY_CARD:
                return _dealer.playerRequestToBuyCard(currentPlayer, ((BuyCardActionAndResponse) as).getResponse());
            case HOLD_CARD:
                return _dealer.playerRequestToHoldCard(currentPlayer, _currentPlayerIndex,
                        ((HoldCardActionAndResponse) as).getResponse());
            case BUY_HOLD_CARD:
                return _dealer.playerRequestToBuyHoldCard(currentPlayer, _currentPlayerIndex,
                        ((BuyHoldCardActionAndResponse) as).getResponse());
            case PASS:
                return true;
            default:
                return false;
        }
    }



    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
