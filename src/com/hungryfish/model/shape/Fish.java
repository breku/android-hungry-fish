package com.hungryfish.model.shape;

import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.hungryfish.manager.ResourcesManager;
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

    /* The categories. */
    public static final short CATEGORY_BIT_PLAYER = 1;
    public static final short CATEGORY_BIT_ENEMY = 2;

    /* And what should collide with what. */
    public static final short MASK_BITS_PLAYER = CATEGORY_BIT_ENEMY;
    public static final short MASK_BITS_ENEMY = CATEGORY_BIT_PLAYER;

    private FishType fishType;
    private FixtureDef fixtureDef;
    private Body[] bodies;
    private Body currentBody;
    private boolean isEnemy;
    private Boolean movingLeft;
    private Integer fishTag;


    public Fish(final float pX, final float pY, FishType fishType, PhysicsWorld physicsWorld, boolean isEnemy, FishBodyData fishBodyData, Boolean movingLeft, Integer fishTag) {
        super(pX, pY, ResourcesManager.getInstance().getTextureFor(fishType), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.fishType = fishType;
        this.isEnemy = isEnemy;
        this.movingLeft = movingLeft;
        this.fishTag = fishTag;
        bodies = new Body[2];
        setTag(fishTag);


        if (isEnemy) {
            this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORY_BIT_ENEMY, MASK_BITS_ENEMY, (short) 0);
        } else {
            this.fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORY_BIT_PLAYER, MASK_BITS_PLAYER, (short) 0);
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
                currentBody = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.KinematicBody, fixtureDef);
            } else {
                List<Vector2> triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 0));
                shiftBodyPoints(triangles, -0.25f, -0.25f);
                scaleBodyPoints(triangles, 3.0f);
                currentBody = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.KinematicBody, fixtureDef);
            }

            currentBody.setUserData(bodyUserData);
            currentBody.setFixedRotation(true);
            currentBody.setActive(false);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, currentBody, true, false));
        }


        if (!isEnemy) {
            List<Vector2> triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 0));
            shiftBodyPoints(triangles, -0.25f, -0.25f);
            scaleBodyPoints(triangles, 3.0f);
            bodies[0] = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.DynamicBody, fixtureDef);

            triangles = triangulationAlgoritm.computeTriangles(ResourcesManager.getInstance().getVerticesFor(fishType, 1));
            shiftBodyPoints(triangles, -0.75f, -0.25f);
            scaleBodyPoints(triangles, 3.0f);
            bodies[1] = PhysicsFactory.createTrianglulatedBody(physicsWorld, this, triangles, BodyDef.BodyType.DynamicBody, fixtureDef);


            for (Body body : bodies) {
                body.setUserData(bodyUserData);
                body.setFixedRotation(true);
            }
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bodies[0], true, false));
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, bodies[1], true, false));

        }

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

    @Override
    public boolean collidesWith(IEntity pOtherEntity) {
        if (!this.isEnemy) {
            ResourcesManager.getInstance().getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ResourcesManager.getInstance().getActivity(), "BUM", 1).show();
                }
            });

        }
        return super.collidesWith(pOtherEntity);
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void updatePosition(AccelerationData data) {

        float deadZone = 0.05f;
        if (data.getX() > deadZone) {
            setCurrentTileIndex(0);
            bodies[0].setType(BodyDef.BodyType.DynamicBody);
            bodies[1].setType(BodyDef.BodyType.KinematicBody);
        } else if (data.getX() < -deadZone) {
            setCurrentTileIndex(1);
            bodies[0].setType(BodyDef.BodyType.KinematicBody);
            bodies[1].setType(BodyDef.BodyType.DynamicBody);
        }
        bodies[1].setLinearVelocity(data.getX() * fishType.getFishSpeed(), data.getY() * fishType.getFishSpeed());
        bodies[0].setLinearVelocity(data.getX() * fishType.getFishSpeed(), data.getY() * fishType.getFishSpeed());
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
}
