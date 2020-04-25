package com.benben.splendor;

import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.gamerole.CPU;
import com.benben.splendor.gamerole.Dealer;
import com.benben.splendor.gamerole.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Game {
    private final static Scanner SYSTEM_INPUT = new Scanner(System.in);

    private int _numOfPlayers;
    private int _numOfCPUs;
    private int _targetScore;

    private Dealer _dealer;
    private List<Player> _players;
    private int _currentPlayerIndex;

    private void start() {
        init();

        do {
            while (_currentPlayerIndex < _numOfPlayers + _numOfCPUs) {
                Player currentPlayer = _players.get(_currentPlayerIndex);
                if (currentPlayer instanceof CPU) {
                    cpuRound((CPU)currentPlayer);
                } else {
                    playerRound(currentPlayer);
                }
                _currentPlayerIndex ++;
            }
            _currentPlayerIndex = 0;
        } while (checkWinner() < 0);

        
    }

    private void init() {
        // Todo: load tokens and cards
        _dealer = new Dealer();
        _players = new ArrayList<>();
        for (int i = 0; i < _numOfPlayers; i++) {
            _players.add(new Player());
        }
        for (int i = 0; i < _numOfCPUs; i++) {
            _players.add(new CPU());
        }
        _currentPlayerIndex = 0;

    }

    private int checkWinner() {
        return 0;
    }

    private void playerRound(Player player) {
        askIntInput("Please choose your action:\n(1)Take tokens;\n(2)Buy card;\n(3)Hold card",
                (input) -> input >=1 && input <= 3);
    }

    private void cpuRound(CPU cpu) {

    }

    private static int askIntInput(String message, Predicate<Integer> ifValid) {
        int input;
        while (true) {
            System.out.print(message);
            try{
                input = SYSTEM_INPUT.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }
            if (ifValid.test(input)) {
                break;
            }
            System.out.println("Invalid input.");
        }
        return input;
    }


    public static void main(String[] args) {
        Game game = new Game();

        game._numOfCPUs = askIntInput("Please enter the number of CPUs (0-4):",
                (input) -> input >= 0 && input <= 4);

        if (game._numOfCPUs < 4) {
            String message = game._numOfCPUs == 3
                    ? String.format("Please enter the number players (0 - 1):")
                    : String.format("Please enter the number players (%d - %d):", 2 - game._numOfCPUs, 4 - game._numOfCPUs);
            game._numOfPlayers = askIntInput(message,
                    (input) -> input + game._numOfCPUs >= 2 && input + game._numOfCPUs <= 4);
        } else {
            game._numOfPlayers = 0;
        }

        game._targetScore = askIntInput("Please enter the target scores (15 or 21):",
                (input) -> input ==15 || input == 21);

        System.out.println(String.format("Your input is: number of players = %d, target score = %d", game._numOfPlayers, game._targetScore));
    }
}
