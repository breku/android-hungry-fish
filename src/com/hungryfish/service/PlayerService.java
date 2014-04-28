package com.hungryfish.service;

import com.hungryfish.util.FishType;

/**
 * User: Breku
 * Date: 26.03.14
 */
public class PlayerService extends BaseService {

    public boolean maxPropertyValue(int propertyNumber, FishType fishType) {
        switch (propertyNumber) {
            case 0:
                float fishSpeed = databaseHelper.getFishSpeed(fishType);
                if (fishSpeed >= fishType.getFishSpeed() * 2) {
                    return true;
                }
                return false;
            case 1:
                float fishPower = databaseHelper.getFishPower(fishType);
                if (fishPower >= fishType.getFishPower() * 2) {
                    return true;
                }
                return false;
            case 2:
                float fishValue = databaseHelper.getFishValue(fishType);
                if (fishValue >= fishType.getFishValue() * 2) {
                    return true;
                }
                return false;
            default:
                throw new UnsupportedOperationException("Wrong property number: " + propertyNumber);
        }
    }

    /**
     * Max property value = 2 * property
     * It's possible to upgrade the property 3 times. Every time you upgrade for 1/3
     *
     * @param propertyNumber 0 - speed
     *                       1 - power
     *                       2 - value
     * @param fishType
     * @return
     */
    public void increasePropertyFor(int propertyNumber, FishType fishType) {

        switch (propertyNumber) {
            case 0:
                float newSpeedFactor = fishType.getFishSpeed() / 3;
                databaseHelper.increaseSpeedFor(fishType, newSpeedFactor);
                break;
            case 1:
                float newPowerFactor = fishType.getFishPower() / 3;
                databaseHelper.increasePowerFor(fishType, newPowerFactor);
                break;
            case 2:
                float newFishValueFactor = fishType.getFishValue() / 3;
                databaseHelper.increaseValueFor(fishType, newFishValueFactor);
                break;
            default:
                throw new UnsupportedOperationException("Wrong property number: " + propertyNumber);
        }
    }
}
