package com.benben.splendor.action;

public class PassActionAndResponse extends ActionAndResponse{

    public PassActionAndResponse() {
        super(null);
    }

    @Override
    public Action getAction() {
        return Action.PASS;
    }
}
