package com.benben.splendor.util;

public enum CardsPosition {
    LEVEL1_1(0),
    LEVEL1_2(1),
    LEVEL1_3(2),
    LEVEL1_4(3),
    LEVEL2_1(4),
    LEVEL2_2(5),
    LEVEL2_3(6),
    LEVEL2_4(7),
    LEVEL3_1(8),
    LEVEL3_2(9),
    LEVEL3_3(10),
    LEVEL3_4(11),
    LEVEL1_DECK_TOP(-1),
    LEVEL2_DECK_TOP(-2),
    LEVEL3_DECK_TOP(-3);

    private final int _cardIndex;

    CardsPosition(int index) {
        _cardIndex = index;
    }

    public int getCardPositionIndex() {
        return _cardIndex;
    }

    public static CardsPosition getPositionFromIndex(int index) {
        switch (index) {
            case 0:
                return LEVEL1_1;
            case 1:
                return LEVEL1_2;
            case 2:
                return LEVEL1_3;
            case 3:
                return LEVEL1_4;
            case 4:
                return LEVEL2_1;
            case 5:
                return LEVEL2_2;
            case 6:
                return LEVEL2_3;
            case 7:
                return LEVEL2_4;
            case 8:
                return LEVEL3_1;
            case 9:
                return LEVEL3_2;
            case 10:
                return LEVEL3_3;
            case 11:
                return LEVEL3_4;
            case -1:
                return LEVEL1_DECK_TOP;
            case -2:
                return LEVEL2_DECK_TOP;
            case -3:
                return LEVEL3_DECK_TOP;
            default:
                return null;
        }
    }
}
