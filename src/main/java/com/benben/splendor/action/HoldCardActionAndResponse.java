package com.benben.splendor.action;

import com.benben.splendor.util.CardsPosition;

public class HoldCardActionAndResponse extends ActionAndResponse<CardsPosition> {

    public HoldCardActionAndResponse(CardsPosition response) {
        super(response);
    }

    @Override
    public Action getAction() {
        return Action.HOLD_CARD;
    }
}
