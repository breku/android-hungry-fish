package com.hungryfish.handler;

/**
 * User: Breku
 * Date: 07.03.14
 */

import com.badlogic.gdx.physics.box2d.Body;
import com.hungryfish.model.scene.BaseScene;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.model.shape.FishBodyData;
import com.hungryfish.util.ConstantsUtil;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.Iterator;

/**
 * If the fish will swim outside the swimming area, it is removed
 */
public class FishBorderUpdateHandler implements IUpdateHandler {

    private PhysicsWorld physicsWorld;
    private BaseScene gameScene;

    public FishBorderUpdateHandler(PhysicsWorld physicsWorld, BaseScene gameScene) {
        this.physicsWorld = physicsWorld;
        this.gameScene = gameScene;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {

        Iterator<Body> bodyIterator = physicsWorld.getBodies();

        while (bodyIterator.hasNext()) {
            Body body = bodyIterator.next();
            if (body.getUserData() instanceof FishBodyData) {

                FishBodyData userData = (FishBodyData) body.getUserData();
                Fish fish = (Fish) gameScene.getChildByTag(userData.getSpriteTag());

                if (fish != null && fish.isEnemy()) {

                    if ((fish.getX() > ConstantsUtil.RIGHT_BORDER + 200 && !fish.isMovingLeft()) ||
                            (fish.getX() < ConstantsUtil.LEFT_BORDER - 200 && fish.isMovingLeft())) {

                        userData.setToRemove(true);
                    }
                }
            }
        }
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
