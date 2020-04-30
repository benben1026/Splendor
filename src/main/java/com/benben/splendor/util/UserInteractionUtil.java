package com.benben.splendor.util;

import com.benben.splendor.gameItem.Item;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
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

    public static final PrintStream OUT;
    public static final char TOP_LEFT = '\u250F';
    public static final char TOP_RIGHT = '\u2513';
    public static final char BOTTOM_LEFT = '\u2517';
    public static final char BOTTOM_RIGHT = '\u251B';
    public static final char HORIZONTAL = '\u2501';
    public static final char VERTICAL = '\u2503';


    static {
        try {
            OUT = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPrintableColor(Color color) {
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

    public static String getPrintableCardUpperBorder(int length) {
        String output = "";
        for (int i = 0; i < length - 2; i++) {
            output += HORIZONTAL;
        }
        return TOP_LEFT + output + TOP_RIGHT;
    }

    public static String getPrintableCardLowerBorder(int length) {
        String output = "";
        for (int i = 0; i < length - 2; i++) {
            output += HORIZONTAL;
        }
        return BOTTOM_LEFT + output + BOTTOM_RIGHT;
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

    public static void printItemsInOneRow(List<? extends Item> items) {
        String[] output = new String[items.get(0).FULL_HEIGHT];
        for (int i = 0; i < output.length; i++) {
            output[i] = "";
        }

        for (Item card : items) {
            List<String> cardString = card.toListOfString();
            for (int i = 0; i < cardString.size(); i++) {
                output[i] += "\t" + cardString.get(i);
            }
        }

        OUT.println(String.join("\n", output));
    }

    public static String getBorder(String colorCode) {
        return colorCode
                + UserInteractionUtil.VERTICAL
                + UserInteractionUtil.ANSI_RESET;
    }

    public static int askIntInput(Scanner scanner, String message, Predicate<Integer> validator) {
        int input;
        while (true) {
            try {
                input = askIntInputOnce(scanner, message, validator);
            } catch (InvalidInputException ex) {
                continue;
            }
            break;
        }
        return input;
    }

    public static int askIntInputOnce(Scanner scanner, String message, Predicate<Integer> validator)
            throws InvalidInputException {
        int input;
        System.out.print(message);
        try{
            input = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            throw new InvalidInputException();
        }
        if (!validator.test(input)) {
            System.out.println("Invalid input.");
            throw new InvalidInputException();
        }
        return input;
    }

    public static String askStringInput(Scanner scanner, String message, Predicate<String> validator) {
        String input;
        while (true) {
            try {
                input = askStringInputOnce(scanner, message, validator);
            } catch (InvalidInputException ex) {
                continue;
            }
            break;
        }
        return input;
    }

    public static String askStringInputOnce(Scanner scanner, String message, Predicate<String> validator)
            throws InvalidInputException {
        String input;
        System.out.print(message);
        try{
            input = scanner.next();
        } catch (Exception e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            throw new InvalidInputException();
        }
        if (!validator.test(input)) {
            System.out.println("Invalid input.");
            throw new InvalidInputException();
        }
        return input;
    }
}
