package com.hungryfish.model.shape;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.util.FishType;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.sensor.acceleration.AccelerationData;

/**
 * User: Breku
 * Date: 24.01.14
 */
public class Fish extends AnimatedSprite {

    /* The categories. */
    public static final short CATEGORY_BIT_PLAYER = 1;
    public static final short CATEGORY_BIT_ENEMY = 2;

    /* And what should collide with what. */
    public static final short MASK_BITS_PLAYER = CATEGORY_BIT_ENEMY;
    public static final short MASK_BITS_ENEMY = CATEGORY_BIT_PLAYER;

    private FishType fishType;
    private FixtureDef fixtureDef;
    public Body body;
    private boolean isEnemy;
    private boolean moving;


    public Fish(final float pX, final float pY, FishType fishType, PhysicsWorld physicsWorld, boolean isEnemy, String bodyUserData) {
        super(pX, pY, ResourcesManager.getInstance().getTextureFor(fishType), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.fishType = fishType;
        this.isEnemy = isEnemy;
        moving = false;

        if(isEnemy){
            this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0,false,CATEGORY_BIT_ENEMY,MASK_BITS_ENEMY,(short)0);
        }else {
            this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0,false,CATEGORY_BIT_PLAYER,MASK_BITS_PLAYER,(short)0);
        }



        createPhysics(physicsWorld, bodyUserData);
    }

    private void createPhysics(PhysicsWorld physicsWorld, String bodyUserData) {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, fixtureDef);
        body.setUserData(bodyUserData);
        body.setFixedRotation(true);
        body.setAwake(true);
        body.setActive(true);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));

    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void updatePosition(AccelerationData data) {

        float deadZone = 0.05f;
        if (data.getX() > deadZone) {
            setCurrentTileIndex(0);
        } else if (data.getX() < -deadZone) {
            setCurrentTileIndex(1);
        }
        body.setLinearVelocity(data.getX() * fishType.getFishSpeed(), data.getY() * fishType.getFishSpeed());
    }

    public boolean isMoving() {
        return moving;
    }

    public void swim() {
        moving = true;
        if (getCurrentTileIndex() == 0) {
            // RIGHT
            body.setLinearVelocity(fishType.getFishSpeed(), 0);
        } else {
            // LEFT
            body.setLinearVelocity(-1 * fishType.getFishSpeed(), 0);
        }
    }

    public void stop() {
        moving = false;
        body.setLinearVelocity(0, 0);
    }
}
