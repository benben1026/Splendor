package com.benben.splendor.util;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Noble;
import com.benben.splendor.gamerole.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public final class GameUtil {

    private static final Random random = new Random(System.currentTimeMillis());

    public static GameConfig loadGameConfig() {
        String path = "./src/main/resources/config.json";
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(path)) {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            int targetScore = ((Long) object.get("targetScore")).intValue();
            int timePerRoundSecond = ((Long) object.get("timePerRoundSecond")).intValue();
            int totalBufferTimeMinutes = ((Long) object.get("totalBufferTimeMinutes")).intValue();
            List<Player> players = new ArrayList<>();
            JSONObject playersJsonObject = (JSONObject)object.get("players");
            for (Object key : playersJsonObject.keySet()) {
                Class<?> clazz = Class.forName((String) playersJsonObject.get(key));
                Constructor<?> constructor = clazz.getConstructor(String.class);
                players.add((Player)constructor.newInstance(key));
            }
            return new GameConfig(targetScore, timePerRoundSecond, totalBufferTimeMinutes, players);
        } catch (IOException | ParseException e1) {
            throw new RuntimeException("Failed to load config file.", e1);
        } catch (Exception e2) {
            throw new RuntimeException("Failed to load player class", e2);
        }
    }

    public static void loadCardsFromJson(
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
            throw new RuntimeException("Failed to load cards file.", exception);
        }
    }

    private static void initGameForNoble(JSONArray array, List<Noble> nobles, int numOfPlayers) {
        List<Noble> buffer = new ArrayList<>();
        for (Object object : array) {
            Map<Color, Integer> priceMap = getPricesToMap((JSONObject) ((JSONObject)object).get("price"));

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
        for (Object object : array) {
            Color color = Color.valueOf((String) ((JSONObject)object).get("color"));
            int score = Integer.parseInt((String) ((JSONObject) object).get("score"));
            JSONObject prices = (JSONObject) ((JSONObject)object).get("price");
            Map<Color, Integer> priceMap = getPricesToMap(prices);
            Card card = new Card(color, score, priceMap);
            cards.add(card);
        }
    }

    private static LinkedHashMap<Color, Integer> getPricesToMap(JSONObject prices) {
        LinkedHashMap<Color, Integer> priceMap = new LinkedHashMap<>();
        for (int i = 0; i < Color.COLOR_COUNT; i++) {
            priceMap.put(Color.getColorFromIndex(i), 0);
        }
        for (String str : (Set<String>) prices.keySet()) {
            priceMap.put(Color.valueOf(str), Integer.valueOf((String) prices.get(str)));
        }
        return priceMap;
    }
}
