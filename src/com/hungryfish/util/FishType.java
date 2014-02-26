package com.hungryfish.util;

/**
 * User: Breku
 * Date: 24.01.14
 */
public enum FishType {
    BLACK(6.0f),
    PURPLE(5.0f),
    RED(4.0f),
    YELLOW(3.0f),
    ORANGE(2.0f),
    GREEN(1.0f);


    private float fishSpeed;

    private FishType(float fishSpeed) {
        this.fishSpeed = fishSpeed;
    }

    public float getFishSpeed() {
        return fishSpeed;
    }

    public FishType next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
