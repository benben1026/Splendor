package com.benben.splendor.util;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Item;
import com.benben.splendor.gamerole.Player;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class UserInteractionUtil {

    public static final PrintStream SYSTEM_OUT;
    public static final Scanner SYSTEM_INPUT = new Scanner(System.in);
    public static final char TOP_LEFT = '\u250F';
    public static final char TOP_RIGHT = '\u2513';
    public static final char BOTTOM_LEFT = '\u2517';
    public static final char BOTTOM_RIGHT = '\u251B';
    public static final char HORIZONTAL = '\u2501';
    public static final char VERTICAL = '\u2503';


    static {
        try {
            SYSTEM_OUT = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPrintableCardUpperBorder(int length, Color color) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length - 2; i++) {
            output.append(HORIZONTAL);
        }
        return color.toPrintable() + TOP_LEFT + output.toString() + TOP_RIGHT + Color.ANSI_RESET;
    }

    public static String getPrintableCardLowerBorder(int length, Color color) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length - 2; i++) {
            output.append(HORIZONTAL);
        }
        return color.toPrintable() + BOTTOM_LEFT + output.toString() + BOTTOM_RIGHT + Color.ANSI_RESET;
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
        SYSTEM_OUT.println(String.join("\n", output));
    }

    public static void printItemsInOneRow(List<? extends Item> items) {
        List<Item> nonNullList = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (nonNullList.isEmpty()) {
            return;
        }
        String[] output = new String[nonNullList.get(0).getFullHeight()];
        Arrays.fill(output, "");

        for (Item item : items) {
            List<String> cardString;
            if (item == null) {
                String[] tmp = new String[nonNullList.get(0).getItemWidth()];
                Arrays.fill(tmp, "-");
                String row = String.join("", tmp);
                tmp = new String[nonNullList.get(0).getFullHeight()];
                Arrays.fill(tmp, row);
                cardString = Arrays.asList(tmp);
            } else {
                cardString = item.toListOfString();
            }
            for (int i = 0; i < cardString.size(); i++) {
                output[i] += "\t" + cardString.get(i);
            }
        }

        SYSTEM_OUT.println(String.join("\n", output));
    }

    public static void printPlayerCurrentStatus(Player player, boolean printHoldCards, List<Card> holdCards) {
        SYSTEM_OUT.println(String.format( "%s totalScore: %d", player.getName(), player.getTotalScore()));
        player.printToken();
        player.printCards();
        if (printHoldCards && !holdCards.isEmpty()) {
            printItemsInOneRow(holdCards);
        }
    }

    public static String getBorder(String colorCode) {
        return colorCode
                + UserInteractionUtil.VERTICAL
                + Color.ANSI_RESET;
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
        SYSTEM_OUT.print(message);
        try{
            input = scanner.nextInt();
        } catch (Exception e) {
            SYSTEM_OUT.println("Invalid input.");
            scanner.nextLine();
            throw new InvalidInputException();
        }
        if (!validator.test(input)) {
            SYSTEM_OUT.println("Invalid input.");
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
        SYSTEM_OUT.print(message);
        try{
            input = scanner.next();
        } catch (Exception e) {
            SYSTEM_OUT.println("Invalid input.");
            scanner.nextLine();
            throw new InvalidInputException();
        }
        if (!validator.test(input)) {
            SYSTEM_OUT.println("Invalid input.");
            throw new InvalidInputException();
        }
        return input;
    }
}
