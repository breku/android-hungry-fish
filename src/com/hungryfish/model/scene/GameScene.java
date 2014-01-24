package com.hungryfish.model.scene;

import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.matcher.ClassTouchAreaMacher;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class GameScene extends BaseScene implements IAccelerationListener{

    private HUD gameHUD;

    private Integer firstTimeCounter;
    private Fish player;

    /**
     * @param objects objects[0] - levelDifficulty
     *                objects[1] - mathParameter
     */
    public GameScene(Object... objects) {
        super(objects);
    }


    @Override
    public void createScene(Object... objects) {
        init(objects);
        createBackground();
        createPlayer();
        createHUD();
    }

    private void createPlayer() {
        player = new Fish(400,200, FishType.YELLOW);
        attachChild(player);
    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAME);
    }

    private void init(Object... objects) {
        clearUpdateHandlers();
        clearTouchAreas();

        engine.enableAccelerationSensor(activity,this);

        firstTimeCounter = 0;

    }


    private void createHUD() {
        gameHUD = new HUD();
        camera.setHUD(gameHUD);
    }

    private void createBackground() {
        unregisterTouchAreas(new ClassTouchAreaMacher(Sprite.class));
        clearChildScene();
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (firstTimeCounter++ == 1) {
            resourcesManager.getStartGameSound().play();
        }

        super.onManagedUpdate(pSecondsElapsed);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAME;
    }

    @Override
    public void disposeScene() {
        gameHUD.clearChildScene();
        camera.setHUD(null);
        camera.setCenter(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2);
        camera.setChaseEntity(null);
        engine.disableAccelerationSensor(activity);
    }

    @Override
    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
    }

    @Override
    public void onAccelerationChanged(AccelerationData pAccelerationData) {
        player.updatePosition(pAccelerationData);
    }
}
