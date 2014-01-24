package com.hungryfish.model.shape;

import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.util.FishType;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.sensor.acceleration.AccelerationData;

/**
 * User: Breku
 * Date: 24.01.14
 */
public class Fish extends AnimatedSprite {

    private FishType fishType;

    public Fish(final float pX, final float pY, FishType fishType) {
        super(pX, pY, ResourcesManager.getInstance().getTextureFor(fishType), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.fishType = fishType;
    }

    public void updatePosition(AccelerationData data) {

        float deadZone = 0.05f;
        if (data.getX() > deadZone ) {
            setPosition(getX() + data.getX() * fishType.getFishSpeed(), getY() + data.getY() * fishType.getFishSpeed());
            setCurrentTileIndex(0);
        } else if (data.getX() < -deadZone){
            setPosition(getX() + data.getX() * fishType.getFishSpeed(), getY() + data.getY() * fishType.getFishSpeed());
            setCurrentTileIndex(1);
        }

    }

}
