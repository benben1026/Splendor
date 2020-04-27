package com.benben.splendor.util;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class GameInitUtil {

    public static void initGame(
            int numOfPlayers,
            List<Card> cardsLevel1,
            List<Card> cardsLevel2,
            List<Card> cardsLevel3,
            List<Noble> nobles) {
        String path = "./src/main/resources/init.json";
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(path)) {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            initGameForNoble((JSONArray) object.get("0"), nobles);
            initGameForPlayers((JSONArray) object.get("1"), cardsLevel1, cardsLevel2, cardsLevel3);
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }

    private static void initGameForNoble(JSONArray array, List<Noble> nobles) {
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            Map<ColorUtil.Color, Integer> priceMap = new HashMap<>();
            convertPricesToMap((JSONObject) object.get("price"), priceMap);

            Noble noble = new Noble(priceMap);
            nobles.add(noble);
        }
        // need to remove per players num
    }

    private static void initGameForPlayers(JSONArray array, List<Card> cardsLevel1, List<Card> cardsLevel2, List<Card> cardsLevel3) {
        initCardsForPlayer((JSONArray) ((JSONObject) array.get(0)).get("level1"), cardsLevel1);
        initCardsForPlayer((JSONArray) ((JSONObject) array.get(1)).get("level2"), cardsLevel2);
        initCardsForPlayer((JSONArray) ((JSONObject) array.get(2)).get("level3"), cardsLevel3);
    }

    private static void initCardsForPlayer(JSONArray array, List<Card> cards) {
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            ColorUtil.Color color = ColorUtil.Color.valueOf((String) object.get("color"));
            Integer score = Integer.valueOf((String) object.get("score"));

            Map<ColorUtil.Color, Integer> pricesMap = new HashMap<>();
            JSONObject prices = (JSONObject) object.get("price");
            convertPricesToMap(prices, pricesMap);
            Card card = new Card(color, score, pricesMap);
            cards.add(card);
        }
    }

    private static void convertPricesToMap(JSONObject prices, Map<ColorUtil.Color, Integer> pricesMap) {
        for (String str : (Set<String>) prices.keySet()) {
            pricesMap.put(ColorUtil.Color.valueOf(str), Integer.valueOf((String) prices.get(str)));
        }
    }
}
