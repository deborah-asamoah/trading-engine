package io.turntabl.project.orderprocessingapi.enums;

public enum OrderSide {
    BUY, SELL;

    public OrderSide inverse() {
        if (this == BUY)
            return SELL;
        return BUY;
    }
}
