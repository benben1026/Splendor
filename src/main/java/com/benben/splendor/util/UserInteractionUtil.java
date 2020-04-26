package com.benben.splendor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public final class UserInteractionUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static String getPrintableColor(ColorUtil.Color color) {
        switch (color) {
            case YELLOW:
                return ANSI_YELLOW;
            case BLUE:
                return ANSI_BLUE;
            case RED:
                return ANSI_RED;
            case WHITE:
                return ANSI_WHITE;
            case GREEN:
                return ANSI_GREEN;
            case BLACK:
                return ANSI_BLACK;
            default:
                return "Invalid Color";
        }
    }

    public static void printHeader(int round) {
        List<String> output = new ArrayList<>();
        StringBuilder sb;
        for (int i = 0; i < 2; i++) {
            sb = new StringBuilder();
            for (int j = 0; j < 60; j++) {
                sb.append("#");
            }
            output.add(sb.toString());
        }
        sb = new StringBuilder();
        for (int j = 0; j < 25; j++) {
            sb.append("#");
        }
        sb.append(String.format(" Round %2d ", round));
        for (int j = 0; j < 25; j++) {
            sb.append("#");
        }
        output.add(sb.toString());
        for (int i = 0; i < 2; i++) {
            sb = new StringBuilder();
            for (int j = 0; j < 60; j++) {
                sb.append("#");
            }
            output.add(sb.toString());
        }
        System.out.println(String.join("\n", output));
    }

    public static int askIntInput(Scanner scanner, String message, Predicate<Integer> ifValid) {
        int input;
        while (true) {
            System.out.print(message);
            try{
                input = scanner.nextInt();
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

    public static String askStringInput(Scanner scanner, String message, Predicate<String> ifValid) {
        String input;
        while (true) {
            System.out.print(message);
            try{
                input = scanner.next();
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
}
