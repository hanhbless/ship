package com.shipper.ship.utils;

public class GlobalSingleton {
    private static GlobalSingleton INSTANCE = new GlobalSingleton();

    public static GlobalSingleton getInstance() {
        return INSTANCE;
    }

    private GlobalSingleton() {
    }

    private boolean allowStateLoss = false;

    public boolean isAllowStateLoss() {
        return allowStateLoss;
    }

    public void setAllowStateLoss(boolean allowStateLoss) {
        this.allowStateLoss = allowStateLoss;
    }
}
