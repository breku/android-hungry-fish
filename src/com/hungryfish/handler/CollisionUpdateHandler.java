package com.hungryfish.handler;

import com.badlogic.gdx.physics.box2d.Body;
import com.hungryfish.model.scene.GameScene;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.model.shape.FishBodyData;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.Iterator;

/**
 * User: Breku
 * Date: 06.03.14
 */
public class CollisionUpdateHandler implements IUpdateHandler {

    private PhysicsWorld physicsWorld;
    private GameScene gameScene;

    public CollisionUpdateHandler(PhysicsWorld physicsWorld, GameScene gameScene) {
        this.physicsWorld = physicsWorld;
        this.gameScene = gameScene;
    }


    @Override
    public void onUpdate(float pSecondsElapsed) {
        Iterator<Body> bodyIterator = physicsWorld.getBodies();

        while (bodyIterator.hasNext()) {
            Body body = bodyIterator.next();

            // TODO check why body is sometimes null
            if (body != null && body.getUserData() instanceof FishBodyData) {

                FishBodyData userData = (FishBodyData) body.getUserData();
                if (userData.isToRemove()) {

                    Fish fish = (Fish) gameScene.getChildByTag(userData.getSpriteTag());


                    if (userData.isKilled()) {
                        gameScene.addOneEnemy();
                        gameScene.addPoints(fish.getFishType());
                    }

                    fish.detachSelf();
                    fish.dispose();
                    physicsWorld.destroyBody(body);
                }

            }
        }
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
