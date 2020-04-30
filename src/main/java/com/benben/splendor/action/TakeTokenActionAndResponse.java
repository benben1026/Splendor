package com.benben.splendor.action;

import com.benben.splendor.util.Color;

import java.util.Map;

public class TakeTokenActionAndResponse extends ActionAndResponse<Map<Color, Integer>>{

    public TakeTokenActionAndResponse(Map<Color, Integer> response) {
        super(response);
    }

    @Override
    public Action getAction() {
        return Action.TAKE_TOKENS;
    }
}
