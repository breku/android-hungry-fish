package com.hungryfish.util;

/**
 * User: Breku
 * Date: 24.01.14
 */
public enum FishType {
    BLACK(4, 3, 4.0f),
    PURPLE(3, 2, 3.0f),
    RED(2, 2, 2.5f),
    YELLOW(1, 2, 2.0f),
    ORANGE(2, 1, 1.5f),
    GREEN(1, 1, 1.0f);


    private Integer fishValue;
    private Integer fishPower;
    private float fishSpeed;

    private FishType(Integer fishValue, Integer fishPower, float fishSpeed) {
        this.fishValue = fishValue;
        this.fishPower = fishPower;
        this.fishSpeed = fishSpeed;
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
}
