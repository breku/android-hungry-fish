package com.hungryfish.service;

import com.hungryfish.util.FishType;

/**
 * User: Breku
 * Date: 26.03.14
 */
public class PlayerService extends BaseService {

    public boolean increasePropertyFor(int propertyNumber, FishType fishType) {

        switch (propertyNumber) {
            case 0:
                float fishSpeed = databaseHelper.getFishSpeed(fishType);
                if (fishSpeed < fishType.getFishSpeed() * 2) {
                    float newSpeedFactor = fishType.getFishSpeed() / 3;
                    databaseHelper.increaseSpeedFor(fishType, newSpeedFactor);
                    return true;
                }
                return false;
            case 1:
                float fishPower = databaseHelper.getFishPower(fishType);
                if (fishPower < fishType.getFishPower() * 2) {
                    float newPowerFactor = fishType.getFishPower() / 3;
                    databaseHelper.increasePowerFor(fishType, newPowerFactor);
                    return true;
                }
                return false;
            case 2:
                float fishValue = databaseHelper.getFishValue(fishType);
                if (fishValue < fishType.getFishValue() * 2) {
                    float newFishValueFactor = fishType.getFishValue() / 3;
                    databaseHelper.increaseValueFor(fishType, newFishValueFactor);
                    return true;
                }
                return false;
            default:
                throw new UnsupportedOperationException("Wrong property number: " + propertyNumber);
        }
    }
}
