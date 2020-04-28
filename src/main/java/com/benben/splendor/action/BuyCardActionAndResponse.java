package com.benben.splendor.action;

import com.benben.splendor.util.CardsPosition;

public class BuyCardActionAndResponse extends ActionAndResponse<CardsPosition>{

    public BuyCardActionAndResponse(CardsPosition response) {
        super(response);
    }

    @Override
    public Action getAction() {
        return Action.BUY_CARD;
    }
}
