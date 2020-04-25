package com.benben.splendor;

import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.gamerole.Dealer;

import java.util.Collections;
import java.util.Scanner;

public class Game {
    private int _numOfPlayers;
    private int _targetScore;

    private void init() {
        // Todo: load tokens and cards
        Dealer dealer = new Dealer(Collections.emptyMap(), Collections.emptyList());

    }

    private void start() {

    }



    public static void main(String[] args) {
        Game game = new Game();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter the number players (2-4):");
            game._numOfPlayers = in.nextInt();
            if (game._numOfPlayers >= 2 && game._numOfPlayers <= 4) {
                break;
            }
            System.out.println("Invalid input.");
        }

        while (true) {
            System.out.print("Please enter the target scores (15 or 21):");
            game._targetScore = in.nextInt();
            if (game._targetScore == 15 || game._targetScore == 21) {
                break;
            }
            System.out.println("Invalid input.");
        }

        System.out.println(String.format("Your input is: number of players = %d, target score = %d", game._numOfPlayers, game._targetScore));
    }
}
