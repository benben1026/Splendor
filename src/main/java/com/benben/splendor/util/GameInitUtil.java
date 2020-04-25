package com.benben.splendor.util;

import com.benben.splendor.gameItem.Card;
import com.benben.splendor.gameItem.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameInitUtil {

    private Map<Color, Integer> tokens;
    private List<Card> cardsLevel1;
    private List<Card> cardsLevel2;
    private List<Card> cardsLevel3;

    public void initGame(int numOfPlayers) {
        String path = "./src/main/resources/init.json";
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(path)) {
            Object object = jsonParser.parse(reader);

        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }

    public Map<Color, Integer> getTokens() {
        return tokens;
    }

    public List<Card> getCardsLevel1() {
        return cardsLevel1;
    }

    public List<Card> getCardsLevel2() {
        return cardsLevel2;
    }

    public List<Card> getCardsLevel3() {
        return cardsLevel3;
    }
}
