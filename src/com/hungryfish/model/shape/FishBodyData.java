package com.hungryfish.model.shape;

import com.hungryfish.util.FishType;

/**
 * User: Breku
 * Date: 06.03.14
 */
public class FishBodyData {

    private boolean toRemove = false;
    private String name;
    private Integer spriteTag;
    private FishType fishType;
    private boolean isActiveBody = false;
    private boolean killed = false;

    private float fishValue;
    private float fishPower;
    private float fishSpeed;

    private Integer points = 0;


    public FishBodyData(String name, Integer spriteTag, FishType fishType) {
        this.name = name;
        this.spriteTag = spriteTag;
        this.fishType = fishType;

    }


    public void updateActualFishParameters(float fishValue, float fishPower, float fishSpeed) {
        this.fishPower = fishPower;
        this.fishSpeed = fishSpeed;
        this.fishValue = fishValue;
    }

    public boolean isActiveBody() {
        return isActiveBody;
    }

    public void setActiveBody(boolean activeBody) {
        isActiveBody = activeBody;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getSpriteTag() {
        return spriteTag;
    }

    public void setSpriteTag(Integer spriteTag) {
        this.spriteTag = spriteTag;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public FishType getFishType() {
        return fishType;
    }

    public void setFishType(FishType fishType) {
        this.fishType = fishType;
    }

    public float getFishValue() {
        return fishValue;
    }

    public float getFishPower() {
        return fishPower;
    }

    public float getFishSpeed() {
        return fishSpeed;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
