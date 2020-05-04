package com.benben.splendor.util;

import com.benben.splendor.gamerole.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class GameConfig {
    public final int _targetScore;
    public final int _timePerRoundSecond;
    public final int _totalBufferTimeMinutes;

    public final boolean _printSteps;
    public final int _totalNumOfGamesToPlay;

    private final Map<String, String> _playerNameToClassPath;

    public GameConfig(String configFilePath) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(configFilePath)) {
            JSONObject object = (JSONObject) jsonParser.parse(reader);
            _targetScore = ((Long) object.get("targetScore")).intValue();
            _timePerRoundSecond = ((Long) object.get("timePerRoundSecond")).intValue();
            _totalBufferTimeMinutes = ((Long) object.get("totalBufferTimeMinutes")).intValue();
            _printSteps = ((Boolean) object.get("printSteps"));
            _totalNumOfGamesToPlay = ((Long) object.get("totalNumOfGamesToPlay")).intValue();
            _playerNameToClassPath = new LinkedHashMap<>();
            JSONObject playersJsonObject = (JSONObject)object.get("players");
            for (Object key : playersJsonObject.keySet()) {
                _playerNameToClassPath.put((String)key, (String) playersJsonObject.get(key));
            }
        } catch (IOException | ParseException e1) {
            throw new RuntimeException("Failed to load config file.", e1);
        }
    }

    public List<String> getPlayersName() {
        return _playerNameToClassPath.keySet().stream().collect(Collectors.toList());
    }

    public List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            for (Map.Entry<String, String> entry : _playerNameToClassPath.entrySet()) {
                Class<?> clazz = Class.forName(entry.getValue());
                    Constructor<?> constructor = clazz.getConstructor(String.class);
                    players.add((Player)constructor.newInstance(entry.getKey()));
            }
            return players;
        } catch (Exception exception) {
            throw new RuntimeException("Failed to load player class", exception);
        }
    }

}
