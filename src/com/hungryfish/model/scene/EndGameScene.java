package com.hungryfish.model.scene;

import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

/**
 * User: Breku
 * Date: 04.10.13
 */
public class EndGameScene extends BaseScene implements IOnSceneTouchListener {

    /**
     * @param objects objects[0] - Integer score
     *                objects[1] - Number of eaten fishes
     *                objects[2] - FishType of player
     */
    public EndGameScene(Object... objects) {
        super(objects);
    }

    @Override
    public void createScene(Object... objects) {
        createBackground();
        createScores((Integer) objects[0], (Integer) objects[1], (FishType) objects[2]);
        setOnSceneTouchListener(this);
    }

    private void createScores(Integer points, Integer eatenFishes, FishType fishType) {


        // Eaten fihes
        attachChild(new Text(400, 400, ResourcesManager.getInstance().getWhiteFont(),
                "Eaten fishes: " + eatenFishes.toString(), vertexBufferObjectManager));

        // Points
        attachChild(new Text(400, 300, ResourcesManager.getInstance().getWhiteFont(),
                "Points: " + points.toString(), vertexBufferObjectManager));

        // Points
        String pointsString = "Money: " + points.toString() + " * " + fishType.getFishValue() + " = " + String.valueOf(points * fishType.getFishValue());
        attachChild(new Text(400, 200, ResourcesManager.getInstance().getWhiteFont(), pointsString, vertexBufferObjectManager));
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2,
                ResourcesManager.getInstance().getEndGameBackgroundTextureRegion(), vertexBufferObjectManager));
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.ENDGAME);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.ENDGAME;
    }

    @Override
    public void disposeScene() {
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()) {
            SceneManager.getInstance().loadMenuSceneFrom(SceneType.ENDGAME);
        }
        return false;
    }
}
