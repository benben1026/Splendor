package com.benben.splendor;

import com.benben.splendor.action.*;
import com.benben.splendor.gamerole.Dealer;
import com.benben.splendor.gamerole.Player;
import com.benben.splendor.util.GameConfig;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.*;

public class Game {

    private static final String CONFIG_FILE_PATH = "./src/main/resources/config.json";

    private Dealer _dealer;
    private List<Player> _players;
    private int _currentPlayerIndex;
    private GameConfig _gameConfig;
    private Map<String, Integer> _dashboard;

    private void start() throws InterruptedException {
        int gameCount = 0;
        while (gameCount < _gameConfig._totalNumOfGamesToPlay) {
            int round = 1;
            _players = _gameConfig.loadPlayers();
            _dealer = new Dealer(_players.size(), _gameConfig._targetScore);
            _currentPlayerIndex = 0;
            do {
                while (_currentPlayerIndex < _players.size()) {
                    // Print game status
                    Player currentPlayer = _players.get(_currentPlayerIndex);
                    if (_gameConfig._printSteps) {
                        UserInteractionUtil.printHeader(round);
                        _dealer.printCurrentStatus();
                        for (int i = 0; i < _players.size(); i++) {
                            UserInteractionUtil.printPlayerCurrentStatus(_players.get(i),
                                    i == _currentPlayerIndex, _dealer.getPlayerHoldCards(_currentPlayerIndex));
                        }
                        UserInteractionUtil.SYSTEM_OUT.println(String.format("It is %s's turn.", currentPlayer.getName()));
                    }

                    while(!notifyPlayer()) {
                        continue;
                    }
                    Thread.sleep(2);

                    _dealer.validatePlayerTokenCounts(currentPlayer);

                    if (_dealer.validateEligibleForNoble(currentPlayer) && _gameConfig._printSteps) {
                        UserInteractionUtil.SYSTEM_OUT.println(
                                String.format("%s got a noble card", currentPlayer.getName()));
                    }
                    _currentPlayerIndex ++;
                }
                _currentPlayerIndex = 0;
                round ++;
            } while (checkWinner() == null && round < 1000);
            gameCount ++;
        }
        UserInteractionUtil.SYSTEM_OUT.println(
                String.format("%d games played in total. The results are:", _gameConfig._totalNumOfGamesToPlay));
        UserInteractionUtil.SYSTEM_OUT.println(_dashboard);
        
    }

    private void init() {
        _gameConfig = new GameConfig(CONFIG_FILE_PATH);
        _dashboard = new HashMap<>();
        for (String name : _gameConfig.getPlayersName()) {
            _dashboard.put(name, 0);
        }
    }

    private boolean notifyPlayer() {
        boolean roundSuccess = false;
        String roundMessage = "";
        List<Player> opponents = new ArrayList<>();
        int i = _currentPlayerIndex + 1;
        while (i % _players.size() != _currentPlayerIndex) {
            i = i % _players.size();
            opponents.add(_players.get(i).deepCopy());
            i++;
        }

        Player currentPlayer = _players.get(_currentPlayerIndex);
        ActionAndResponse as = currentPlayer.askForAction(opponents,
                Collections.unmodifiableMap(_dealer.getTokens()),
                _dealer.getPositionToCardsMap(),
                Collections.unmodifiableList(_dealer.getPlayerHoldCards(_currentPlayerIndex)),
                _dealer.getUnmodifiableNobles());
        switch (as.getAction()) {
            case TAKE_TOKENS:
                roundMessage = String.format("%s takes tokens %s", currentPlayer.getName(), ((TakeTokenActionAndResponse)as).getResponse());
                roundSuccess = _dealer.playerRequestToTakeTokens(currentPlayer, ((TakeTokenActionAndResponse)as).getResponse());
                break;
            case BUY_CARD:
                roundMessage = String.format("%s buys card %s", currentPlayer.getName(), ((BuyCardActionAndResponse) as).getResponse().toString());
                roundSuccess = _dealer.playerRequestToBuyCard(currentPlayer, ((BuyCardActionAndResponse) as).getResponse());
                break;
            case HOLD_CARD:
                roundMessage = String.format("%s holds card", currentPlayer.getName());
                roundSuccess = _dealer.playerRequestToHoldCard(currentPlayer, _currentPlayerIndex,
                        ((HoldCardActionAndResponse) as).getResponse());
                break;
            case BUY_HOLD_CARD:
                roundMessage = String.format("%s buys hold card", currentPlayer.getName());
                roundSuccess = _dealer.playerRequestToBuyHoldCard(currentPlayer, _currentPlayerIndex,
                        ((BuyHoldCardActionAndResponse) as).getResponse());
                break;
            case PASS:
                roundMessage = String.format("%s skips this round", currentPlayer.getName());
                roundSuccess = true;
                break;
            default:
                roundSuccess = false;
        }
        if (_gameConfig._printSteps) {
            UserInteractionUtil.SYSTEM_OUT.println(roundMessage);
        }
        return roundSuccess;
    }

    private Player checkWinner() {
        Player player = _players.stream().max(Comparator.comparingInt(Player::getTotalScore)).get();
        if (_gameConfig._printSteps) {
            UserInteractionUtil.SYSTEM_OUT.println(
                    String.format("%s has the highest score: %d",player.getName(), player.getTotalScore()));
        }

        if (player.getTotalScore() >= _gameConfig._targetScore) {
            if (_gameConfig._printSteps) {
                UserInteractionUtil.SYSTEM_OUT.println(
                        String.format("Congratulations %s, you win the game with highest score: %d",
                                player.getName(), player.getTotalScore()));
                _players.forEach(p1 -> UserInteractionUtil.SYSTEM_OUT.println(
                        String.format("%s score = %d", p1.getName(), p1.getTotalScore())));
            }
            _dashboard.put(player.getName(), _dashboard.getOrDefault(player.getName(), 0) + 1);
            return player;
        }
        return null;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.init();

        try {
            game.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
