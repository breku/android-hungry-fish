package com.hungryfish.handler;

import com.hungryfish.model.scene.GameScene;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.util.ConstantsUtil;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.List;

/**
 * User: Breku
 * Date: 07.03.14
 */
public class FishNumberUpdateHandler implements IUpdateHandler {

    private PhysicsWorld physicsWorld;
    private GameScene gameScene;

    public FishNumberUpdateHandler(PhysicsWorld physicsWorld, GameScene gameScene) {
        this.physicsWorld = physicsWorld;
        this.gameScene = gameScene;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {

        List<Fish> fishList = gameScene.getAllEnemyFishes();

        if (fishList.size() < ConstantsUtil.NUMBER_OF_ENEMIES) {
            Fish fish = gameScene.createEnemyFish();
            fish.swim();
        }

    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
