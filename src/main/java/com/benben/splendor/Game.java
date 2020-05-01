package com.benben.splendor;

import com.benben.splendor.action.*;
import com.benben.splendor.gamerole.CPU;
import com.benben.splendor.gamerole.Dealer;
import com.benben.splendor.gamerole.HumanPlayer;
import com.benben.splendor.gamerole.Player;
import com.benben.splendor.util.Color;
import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Game {

    private int _numOfHumanPlayers;
    private int _numOfCPUs;
    private int _targetScore;

    private Dealer _dealer;
    private List<Player> _players;
    private int _currentPlayerIndex;

    private void start() {
        init();
        int round = 1;

        do {
            while (_currentPlayerIndex < _numOfHumanPlayers + _numOfCPUs) {
                // Print game status
                UserInteractionUtil.printHeader(round);
                _dealer.printCurrentStatus();
                for (int i = 0; i < _players.size(); i++) {
                    //_players.get(i).printCurrentStatus(i == _currentPlayerIndex);
                    UserInteractionUtil.printPlayerCurrentStatus(_players.get(i), i == _currentPlayerIndex,
                            _dealer.getPlayerHoldCards(_currentPlayerIndex));
                }
                Player currentPlayer = _players.get(_currentPlayerIndex);
                System.out.println("It is " + currentPlayer.getName() + "'s turn.");

                while(!notifyPlayer()) {
                    continue;
                }

                _dealer.validatePlayerTokenCounts(currentPlayer);
                _dealer.validateEligibleForNoble(currentPlayer);
                _currentPlayerIndex ++;
            }
            _currentPlayerIndex = 0;
            round ++;
        } while (checkWinner() == null);
        
    }

    private void init() {
        _dealer = new Dealer(_numOfHumanPlayers + _numOfCPUs);
        _players = new ArrayList<>();
        for (int i = 0; i < _numOfHumanPlayers; i++) {
            _players.add(new HumanPlayer("Player" + (i + 1)));
        }
        for (int i = 0; i < _numOfCPUs; i++) {
            _players.add(new CPU("CPU" + (i + 1)));
        }
        _currentPlayerIndex = 0;
    }

    private boolean notifyPlayer() {
        List<Player> opponents = new ArrayList<>();
        int i = _currentPlayerIndex + 1;
        while (i % _players.size() != _currentPlayerIndex) {
            i = i % _players.size();
            // todo: fix clone
            opponents.add(_players.get(i).deepCopy());
            //opponents.add(_players.get(i));
            i++;
        }
        // Todo: pass in cards and nobles
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

    private Player checkWinner() {
        Player player = _players.stream().max(Comparator.comparingInt(Player::getTotalScore)).get();
        if (player == null) {
            return null;
        }
        System.out.println(player.getName() + " has the highest score: " + player.getTotalScore());

        if (player.getTotalScore() >= _targetScore) {
            System.out.println("Congratulations " + player.getName() + ", you win the game with highest score: "
                    + player.getTotalScore());
            _players.forEach(p1 -> System.out.println(p1.getName() + " score = " + p1.getTotalScore()));
            return player;
        }
        return null;
    }

    public static void main(String[] args) {
        Game game = new Game();

        game._numOfCPUs = UserInteractionUtil.askIntInput(GameInitUtil.SYSTEM_INPUT,
                "Please enter the number of CPUs (0-4):",
                (input) -> input >= 0 && input <= 4);

        if (game._numOfCPUs < 4) {
            String message = game._numOfCPUs == 3
                    ? String.format("Please enter the number players (0-1):")
                    : String.format("Please enter the number players (%d-%d):", 2 - game._numOfCPUs, 4 - game._numOfCPUs);
            game._numOfHumanPlayers = UserInteractionUtil.askIntInput(GameInitUtil.SYSTEM_INPUT, message,
                    (input) -> input + game._numOfCPUs >= 2 && input + game._numOfCPUs <= 4);
        } else {
            game._numOfHumanPlayers = 0;
        }

        game._targetScore = UserInteractionUtil.askIntInput(GameInitUtil.SYSTEM_INPUT,
                "Please enter the target scores (15 or 21):",
                (input) -> input ==15 || input == 21);
        game.start();
    }
}
