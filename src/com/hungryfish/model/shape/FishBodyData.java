package com.hungryfish.model.shape;

/**
 * User: Breku
 * Date: 06.03.14
 */
public class FishBodyData {

    private boolean toRemove = false;
    private String name;
    private Integer spriteTag;

    public FishBodyData(String name, Integer spriteTag) {
        this.name = name;
        this.spriteTag = spriteTag;
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
}
