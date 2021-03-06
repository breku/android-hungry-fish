package com.hungryfish.util;

/**
 * User: Breku
 * Date: 24.01.14
 */
public enum FishType implements Comparable<FishType> {
    GREEN(1, 1, 1.0f, 0),
    ORANGE(2, 1, 1.5f, 1),
    YELLOW(1, 2, 2.0f, 2),
    RED(2, 2, 2.5f, 3),
    PURPLE(3, 2, 3.0f, 4),
    BLACK(4, 3, 4.0f, 5);

    private float fishValue;
    private float fishPower;
    private float fishSpeed;
    private Integer fishLevel;
    private Integer fishPrice;

    private FishType(Integer fishValue, Integer fishPower, float fishSpeed, Integer fishLevel, Integer fishPrice) {
        this.fishValue = fishValue;
        this.fishPower = fishPower;
        this.fishSpeed = fishSpeed;
        this.fishLevel = fishLevel;
        this.fishPrice = fishPrice;
    }

    private FishType(Integer fishValue, Integer fishPower, float fishSpeed, Integer fishLevel) {
        this(fishValue, fishPower, fishSpeed, fishLevel, ConstantsUtil.PRICE_MULTIPLIER_COST * (fishLevel * fishLevel + 1));
    }

    public float getFishSpeed() {
        return fishSpeed;
    }

    public FishType next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public Float getFishPower() {
        return fishPower;
    }

    public Float getFishValue() {
        return fishValue;
    }


    public Integer getFishLevel() {
        return fishLevel;
    }

    public Integer getFishPrice() {
        return fishPrice;
    }
}
