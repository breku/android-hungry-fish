package com.hungryfish.util;

/**
 * User: Breku
 * Date: 24.01.14
 */
public enum FishType {
    YELLOW(2.0f),
    GREEN(1.0f),
    BLACK(1.0f),
    ORANGE(1.0f),
    PURPLE(1.0f),
    RED(1.0f);

    private float fishSpeed;

    private FishType(float fishSpeed) {
        this.fishSpeed = fishSpeed;
    }

    public float getFishSpeed() {
        return fishSpeed;
    }
}
