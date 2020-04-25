package java.com.benben.splendor.gameItem;

import java.util.Map;

public abstract class Item {
    int _score;
    Map<Color, Integer> _price;

    public boolean canBuy(Map<Color, Integer> input) {
        return _price.entrySet().stream().allMatch(
                entry -> input.getOrDefault(entry.getKey(), 0).equals(entry.getValue()));
    }
}
