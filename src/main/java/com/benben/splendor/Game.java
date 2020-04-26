package com.benben.splendor;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gamerole.CPU;
import com.benben.splendor.gamerole.Dealer;
import com.benben.splendor.gamerole.Player;
import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.*;

public class Game {
    private final static Scanner SYSTEM_INPUT = new Scanner(System.in);

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
                _dealer.printCurrentStatus(false);
                for (int i = 0; i < _players.size(); i++) {
                    _players.get(i).printCurrentStatus(i == _currentPlayerIndex);
                }
                Player currentPlayer = _players.get(_currentPlayerIndex);
                System.out.println("It is " + currentPlayer.getName() + "'s turn.");
                if (currentPlayer instanceof CPU) {
                    cpuRound((CPU)currentPlayer);
                } else {
                    playerRound(currentPlayer);
                }
                _currentPlayerIndex ++;
            }
            _currentPlayerIndex = 0;
            round ++;
        } while (checkWinner() < 0);
        
    }

    private void init() {
        _dealer = new Dealer(_numOfHumanPlayers + _numOfCPUs);
        _players = new ArrayList<>();
        for (int i = 0; i < _numOfHumanPlayers; i++) {
            _players.add(new Player("Player" + (i + 1)));
        }
        for (int i = 0; i < _numOfCPUs; i++) {
            _players.add(new CPU("CPU" + (i + 1)));
        }
        _currentPlayerIndex = 0;

    }

    private int checkWinner() {
        return -1;
    }

    private void playerRound(Player player) {
        int selection = UserInteractionUtil.askIntInput(SYSTEM_INPUT,
                "Please choose your action:\n(1)Take tokens;\n(2)Buy card;\n"
                        + "(3)Hold card;\n(4)Buy hold card;\n(5)Pass\n",
                (input) -> input >=1 && input <= 5);
        switch (selection){
            case 1:
                takeTokens(player);
                break;
            case 2:
                buyCard(player);
                break;
            case 3:
                holdCard(player);
                break;
            case 4:
                buyHoldCard(player);
                break;
            default:
                break;
        }
    }

    private void cpuRound(CPU cpu) {

    }

    private void takeTokens(Player player) {
        UserInteractionUtil.askStringInput(SYSTEM_INPUT, "Please choose the number of tokens you want to take(White, Blue, Green, Red, Black), separate by \",\":\n",
                (input) -> {
                    try {
                        int[] inputTokens = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                        if (_dealer.requestToTakeTokens(inputTokens)) {
                            player.takeTokens(inputTokens);
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                });
        // Todo: Check the total number of tokens to be less than 10.
//        if (player.validateNumOfTokens()) {
//            return;
//        }
    }

    private void buyCard(Player player) {
        UserInteractionUtil.askIntInput(SYSTEM_INPUT, "Please choose the card you want to buy ( 1-12 )",
                (input) -> _dealer.requestToBuyCard(player, input - 1));
    }

    private void holdCard(Player player) {
        UserInteractionUtil.askIntInput(SYSTEM_INPUT, "Please choose the card you want to hold ( 1-12 )",
                (input) -> _dealer.requestToHoldCard(player, input - 1));
    }

    private void buyHoldCard(Player player) {
        UserInteractionUtil.askIntInput(SYSTEM_INPUT, "Please choose the card you want to buy ( 1-3 )",
                (input) -> _dealer.requestToBuyHoldCard(player, input - 1));
    }


    public static void main(String[] args) {
        Game game = new Game();

        game._numOfCPUs = UserInteractionUtil.askIntInput(SYSTEM_INPUT, "Please enter the number of CPUs (0-4):",
                (input) -> input >= 0 && input <= 4);

        if (game._numOfCPUs < 4) {
            String message = game._numOfCPUs == 3
                    ? String.format("Please enter the number players (0-1):")
                    : String.format("Please enter the number players (%d-%d):", 2 - game._numOfCPUs, 4 - game._numOfCPUs);
            game._numOfHumanPlayers = UserInteractionUtil.askIntInput(SYSTEM_INPUT, message,
                    (input) -> input + game._numOfCPUs >= 2 && input + game._numOfCPUs <= 4);
        } else {
            game._numOfHumanPlayers = 0;
        }

        game._targetScore = UserInteractionUtil.askIntInput(SYSTEM_INPUT, "Please enter the target scores (15 or 21):",
                (input) -> input ==15 || input == 21);

        System.out.println(String.format("Your input is: number of players = %d, target score = %d", game._numOfHumanPlayers, game._targetScore));
        game.start();
    }
}
