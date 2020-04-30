package com.benben.splendor.action;

public class BuyHoldCardActionAndResponse extends ActionAndResponse<Integer>{
    public BuyHoldCardActionAndResponse(Integer response) {
        super(response);
    }

    @Override
    public Action getAction() {
        return Action.BUY_HOLD_CARD;
    }
}
