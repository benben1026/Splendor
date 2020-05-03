package com.benben.splendor.util;

import com.benben.splendor.gamerole.Player;

import java.util.List;

public class GameConfig {
    public final int _targetScore;
    public final int _timePerRoundSecond;
    public final int _totalBufferTimeMinutes;
    public final List<Player> _players;

    public GameConfig(int targetScore, int timePerRoundSecond, int totalBufferTimeMinutes, List<Player> players) {
        _targetScore = targetScore;
        _timePerRoundSecond = timePerRoundSecond;
        _totalBufferTimeMinutes = totalBufferTimeMinutes;
        _players = players;
    }
}
