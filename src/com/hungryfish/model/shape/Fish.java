package com.hungryfish.model.shape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.extension.physics.box2d.util.triangulation.ITriangulationAlgoritm;
import org.andengine.input.sensor.acceleration.AccelerationData;

import java.util.List;

/**
 * User: Breku
 * Date: 24.01.14
 */
public class Fish extends AnimatedSprite {


    private FishType fishType;
    private FixtureDef fixtureDef;
    private Body currentBody;
    private boolean isEnemy;
    private Boolean movingLeft;
    private Integer fishTag;


    public Fish(float pX, float pY, FishType fishType) {
        super(pX, pY, ResourcesManager.getInstance().getTextureFor(fishType), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.fishType = fishType;
    }

    public Fish(final float pX, final float pY, FishType fishType, PhysicsWorld physicsWorld, boolean isEnemy, FishBodyData fishBodyData, Boolean movingLeft, Integer fishTag) {
        super(pX, pY, ResourcesManager.getInstance().getTextureFor(fishType), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.fishType = fishType;
        this.isEnemy = isEnemy;
        this.movingLeft = movingLeft;
        this.fishTag = fishTag;
        setTag(fishTag);


        if (isEnemy) {
            this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false,
                    ConstantsUtil.CATEGORY_BIT_ENEMY, ConstantsUtil.MASK_BITS_ENEMY, (short) 0);
        } else {
            this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false,
                    ConstantsUtil.CATEGORY_BIT_PLAYER, ConstantsUtil.MASK_BITS_PLAYER, (short) 0);
        }

        createPhysics(physicsWorld, fishBodyData);
    }

    private void createPhysics(PhysicsWorld physicsWorld, FishBodyData bodyUserData) {

        ITriangulationAlgoritm triangulationAlgoritm = new EarClippingTriangulator();

        if (isEnemy) {
            if (movingLeft) {
                List<Vector2> triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 1));
                shiftBodyPoints(triangles, -0.75f, -0.25f);
                scaleBodyPoints(triangles, 3.0f);
                currentBody = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.DynamicBody, fixtureDef);
            } else {
                List<Vector2> triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 0));
                shiftBodyPoints(triangles, -0.25f, -0.25f);
                scaleBodyPoints(triangles, 3.0f);
                currentBody = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.DynamicBody, fixtureDef);
            }

            currentBody.setUserData(bodyUserData);
            currentBody.setFixedRotation(true);
            currentBody.setAwake(false);
            currentBody.setActive(false);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, currentBody, true, false));

        }


        if (!isEnemy) {
            List<Vector2> triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 0));
            shiftBodyPoints(triangles, -0.25f, -0.25f);
            scaleBodyPoints(triangles, 3.0f);
            currentBody = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.DynamicBody, fixtureDef);


            triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 1));
            shiftBodyPoints(triangles, -0.25f, -0.25f);
            scaleBodyPoints(triangles, 3.0f);

            currentBody.setUserData(bodyUserData);
            currentBody.setFixedRotation(true);

            physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, currentBody, true, false));
        }

    }

    @Override
    public boolean collidesWith(IEntity pOtherEntity) {
        return super.collidesWith(pOtherEntity);
    }

    private void scaleBodyPoints(List<Vector2> triangles, float scale) {
        for (Vector2 point : triangles) {
            point.x = point.x * scale;
            point.y = point.y * scale;
        }
    }

    private void shiftBodyPoints(List<Vector2> vertices, float x, float y) {
        for (Vector2 point : vertices) {
            point.x = point.x + x;
            point.y = point.y + y;
        }

    }


    public boolean isEnemy() {
        return isEnemy;
    }

    public void updatePosition(AccelerationData data) {

        float deadZone = 0.05f;
        if (data.getX() > deadZone) {
            if (getCurrentTileIndex() == 1) {
                setCurrentTileIndex(0);
                currentBody.setTransform(currentBody.getPosition(), currentBody.getAngle() + (float) Math.toRadians(180));
            }
        } else if (data.getX() < -deadZone) {
            if (getCurrentTileIndex() == 0) {
                setCurrentTileIndex(1);
                currentBody.setTransform(currentBody.getPosition(), currentBody.getAngle() + (float) Math.toRadians(180));
            }
        }
        currentBody.setLinearVelocity(data.getX() * fishType.getFishSpeed(), data.getY() * fishType.getFishSpeed());

    }


    public void swim() {
        if (movingLeft) {
            currentBody.setLinearVelocity(-1 * fishType.getFishSpeed(), 0);
        } else {
            currentBody.setLinearVelocity(fishType.getFishSpeed(), 0);
        }
    }

    public Body getCurrentBody() {
        return currentBody;
    }

    public Integer getFishTag() {
        return fishTag;
    }

    public void switchSwimmingDirection() {
        if (getCurrentTileIndex() == 0) {
            setCurrentTileIndex(1);
        } else if (getCurrentTileIndex() == 1) {
            setCurrentTileIndex(0);
        }
        movingLeft = !movingLeft;
        currentBody.setTransform(currentBody.getPosition(), currentBody.getAngle() + (float) Math.toRadians(180));
    }

    public void stopSwimming() {
        currentBody.setLinearVelocity(0, 0);
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public FishType getFishType() {
        return fishType;
    }
}
