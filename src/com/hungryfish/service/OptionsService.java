package com.hungryfish.service;

import com.hungryfish.util.FishType;

import java.text.DecimalFormat;

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
        String valueFormatted = new DecimalFormat("#.#").format(databaseHelper.getFishValue(fishType));
        return Float.valueOf(valueFormatted);
    }

    public float getFishSpeed(FishType fishType) {
        return databaseHelper.getFishSpeed(fishType);
    }

    public Integer getFishPriceFor(FishType fishType) {
        return databaseHelper.getFishPrice(fishType);
    }

    public void updateMoney(Float points) {
        databaseHelper.updateMoney(points);
    }

    public Float getMoney() {
        String moneyFormatted = new DecimalFormat("#.#").format(databaseHelper.getMoney());
        return Float.valueOf(moneyFormatted);
    }

    public void unlockFish(FishType fishType) {
        databaseHelper.unlockFish(fishType);
    }

    public void setMoney(Float money) {
        databaseHelper.setMoney(money);
    }
}
