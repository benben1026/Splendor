package com.benben.splendor.gamerole;

import com.benben.splendor.util.ColorUtil;
import com.benben.splendor.util.GameInitUtil;
import com.benben.splendor.util.InvalidInputException;
import com.benben.splendor.util.UserInteractionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class HumanPlayer extends Player{

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public void notifyTurn(Dealer dealer, List<Player> players) {
        while(true) {
            try {
                int selection = UserInteractionUtil.askIntInput(GameInitUtil.SYSTEM_INPUT,
                        "Please choose your action:\n(1)Take tokens;\n(2)Buy card;\n"
                                + "(3)Hold card;\n(4)Buy hold card;\n(5)Pass\n",
                        (input) -> input >=1 && input <= 5);
                switch (selection){
                    case 1:
                        takeTokens(dealer);
                        break;
                    case 2:
                        UserInteractionUtil.askIntInputOnce(GameInitUtil.SYSTEM_INPUT,
                                "Please choose the card you want to buy ( 1-12 )",
                                buyCard(dealer));
                        break;
                    case 3:
                        UserInteractionUtil.askIntInputOnce(GameInitUtil.SYSTEM_INPUT,
                                "Please choose the card you want to hold ( 1-12 )",
                                holdCard(dealer));
                        break;
                    case 4:
                        UserInteractionUtil.askIntInputOnce(GameInitUtil.SYSTEM_INPUT,
                                "Please choose the card you want to buy ( 1-3 )",
                                buyHoldCard(dealer));
                        break;
                    default:
                        break;
                }
            } catch (InvalidInputException ex) {
                continue;
            }
            break;
        }
    }

    @Override
    public Map<ColorUtil.Color, Integer> askToReturnTokens() {
        printToken();
        Map<ColorUtil.Color, Integer> returnTokens = new HashMap<>();
        UserInteractionUtil.askStringInput(GameInitUtil.SYSTEM_INPUT,
                "Please return some tokens to keep total number under 10 (White, Blue, Green, Red, Black), separate by \",\":\n",
                (input) -> {
                    try {
                        int[] inputTokens = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                        for (int i = 0; i < ColorUtil.COLOR_COUNT; i++) {
                            returnTokens.put(ColorUtil.getColorFromIndex(i), inputTokens[i]);
                        }
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                });
        return returnTokens;
    }

    private void takeTokens(Dealer dealer) throws InvalidInputException {
        UserInteractionUtil.askStringInputOnce(GameInitUtil.SYSTEM_INPUT,
                "Please choose the number of tokens you want to take(White, Blue, Green, Red, Black), separate by \",\":\n",
                (input) -> {
                    try {
                        int[] inputTokens = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
                        if (dealer.requestToTakeTokens(inputTokens)) {
                            receiveTokens(inputTokens);
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                });
    }

    private Predicate<Integer> buyCard(Dealer dealer) {
        return (input) -> dealer.requestToBuyCard(this, input - 1);
    }

    private Predicate<Integer> holdCard(Dealer dealer) {
        return (input) -> dealer.requestToHoldCard(this, input - 1);
    }

    private Predicate<Integer> buyHoldCard(Dealer dealer) {
        return (input) -> dealer.requestToBuyHoldCard(this, input - 1);
    }

    public void receiveTokens(int[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] != 0) {
                receiveTokens(ColorUtil.getColorFromIndex(i), tokens[i]);
            }
        }
    }
}
