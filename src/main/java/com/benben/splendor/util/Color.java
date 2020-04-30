package com.benben.splendor.util;

public enum Color {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    WHITE("\u001B[37m"),
    BLACK("\u001B[30m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m");

    public static final int COLOR_COUNT = 5;
    public static final String ANSI_RESET = "\u001B[0m";

    private final String _printableColor;

    Color(String printableColor) {
        _printableColor = printableColor;
    }

    public String toPrintable() {
        return _printableColor;
    }

    public static Color getColorFromIndex(int index) {
        switch (index) {
            case 0:
                return Color.WHITE;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.RED;
            case 4:
                return Color.BLACK;
            default:
                return null;
        }
    }
}
