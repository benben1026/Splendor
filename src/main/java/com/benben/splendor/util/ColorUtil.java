package com.benben.splendor.util;

public final class ColorUtil {
    public enum Color{
        RED,
        GREEN,
        BLUE,
        WHITE,
        BLACK,
        YELLOW
    }

    public static final int COLOR_COUNT = 5;

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
