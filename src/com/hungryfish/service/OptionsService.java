package com.hungryfish.service;

import com.hungryfish.util.FishType;

/**
 * User: Breku
 * Date: 23.03.14
 */
public class OptionsService extends BaseService {


    public boolean isFishLocked(FishType fishType) {
        return databaseHelper.isFishLocked(fishType);
    }

    public Float getFishPower(FishType fishType) {
        return databaseHelper.getFishPower(fishType);
    }

    public Float getFishValue(FishType fishType) {
        return databaseHelper.getFishValue(fishType);
    }

    public float getFishSpeed(FishType fishType) {
        return databaseHelper.getFishSpeed(fishType);
    }

    public Integer getFishPriceFor(FishType fishType) {
        return databaseHelper.getFishPrice(fishType);
    }

    public void updateMoney(Integer points) {
        databaseHelper.updateMoney(points);
    }

    public Integer getMoney() {
        return databaseHelper.getMoney();
    }

    public void unlockFish(FishType fishType) {
        databaseHelper.unlockFish(fishType);
    }

    public void setMoney(Integer money) {
        databaseHelper.setMoney(money);
    }
}
