package com.benben.splendor.action;

public abstract class ActionAndResponse<RES> {
    private final RES _response;

    public ActionAndResponse(RES response) {
        _response = response;
    }

    public abstract Action getAction();

    public RES getResponse() {
        return _response;
    }
}
