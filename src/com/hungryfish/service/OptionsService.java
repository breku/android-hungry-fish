package com.hungryfish.service;

import com.hungryfish.util.FishType;

/**
 * User: Breku
 * Date: 23.03.14
 */
public class OptionsService extends BaseService {


    DatabaseHelper databaseHelper = new DatabaseHelper(activity);

    public boolean isFishLocked(FishType fishType) {
        return databaseHelper.isFishLocked(fishType);
    }

    public int getFishPower(FishType fishType) {
        return databaseHelper.getFishPower(fishType);
    }

    public int getFishValue(FishType fishType) {
        return databaseHelper.getFishValue(fishType);
    }

    public float getFishSpeed(FishType fishType) {
        return databaseHelper.getFishSpeed(fishType);
    }

    public void updateMoney(Integer points) {
        databaseHelper.updateMoney(points);
    }

    public Integer getMoney() {
        return databaseHelper.getMoney();
    }
}
