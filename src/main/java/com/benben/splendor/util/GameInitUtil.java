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

    public static final Scanner SYSTEM_INPUT = new Scanner(System.in);
    private static Random random = new Random(System.currentTimeMillis());

    public static void initGame(
            int numOfPlayers,
            List<Card> cardsLevel1,
            List<Card> cardsLevel2,
            List<Card> cardsLevel3,
            List<Noble> nobles) {
        String path = "./src/main/resources/cards.json";
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(path)) {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            initGameForNoble((JSONArray) object.get("noble"), nobles, numOfPlayers);
            initGameForPlayers((JSONArray) object.get("deck"), cardsLevel1, cardsLevel2, cardsLevel3);
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }

    private static void initGameForNoble(JSONArray array, List<Noble> nobles, int numOfPlayers) {
        List<Noble> buffer = new ArrayList<>();
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            Map<ColorUtil.Color, Integer> priceMap = getPricesToMap((JSONObject) object.get("price"));

            Noble noble = new Noble(priceMap);
            buffer.add(noble);
        }
        // keep the nobles size as numOfPlayers+1
        while(numOfPlayers >= 0) {
            nobles.add(buffer.remove(random.nextInt(buffer.size())));
            numOfPlayers--;
        }
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

            JSONObject prices = (JSONObject) object.get("price");
            Map<ColorUtil.Color, Integer> priceMap = getPricesToMap(prices);
            Card card = new Card(color, score, priceMap);
            cards.add(card);
        }
    }

    private static LinkedHashMap<ColorUtil.Color, Integer> getPricesToMap(JSONObject prices) {
        LinkedHashMap<ColorUtil.Color, Integer> priceMap = new LinkedHashMap<>();
        for (int i = 0; i < ColorUtil.COLOR_COUNT; i++) {
            priceMap.put(ColorUtil.getColorFromIndex(i), 0);
        }
        for (String str : (Set<String>) prices.keySet()) {
            priceMap.put(ColorUtil.Color.valueOf(str), Integer.valueOf((String) prices.get(str)));
        }
        return priceMap;
    }
}
