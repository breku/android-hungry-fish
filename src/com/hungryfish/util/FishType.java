package com.hungryfish.util;

/**
 * User: Breku
 * Date: 24.01.14
 */
public enum FishType implements Comparable<FishType>{
    BLACK(4, 3, 4.0f,6),
    PURPLE(3, 2, 3.0f,5),
    RED(2, 2, 2.5f,4),
    YELLOW(1, 2, 2.0f,3),
    ORANGE(2, 1, 1.5f,2),
    GREEN(1, 1, 1.0f,1);


    private Integer fishValue;
    private Integer fishPower;
    private float fishSpeed;
    private Integer fishLevel;

    private FishType(Integer fishValue, Integer fishPower, float fishSpeed, Integer fishLevel) {
        this.fishValue = fishValue;
        this.fishPower = fishPower;
        this.fishSpeed = fishSpeed;
        this.fishLevel = fishLevel;
    }

    public float getFishSpeed() {
        return fishSpeed;
    }

    public FishType next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public Integer getFishPower() {
        return fishPower;
    }

    public Integer getFishValue() {
        return fishValue;
    }


    public Integer getFishLevel() {
        return fishLevel;
    }
}
