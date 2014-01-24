package com.hungryfish.model.scene;

import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.matcher.ClassTouchAreaMacher;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.pool.FishPool;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.engine.camera.BoundCamera;
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
    private AccelerationData accelerationData;
    private FishPool fishPool;

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
        createEnemy();
        createHUD();
    }

    private void createEnemy() {
        for(int i = 0 ;i<ConstantsUtil.NUMBER_OF_ENEMIES; i++){
            attachChild(fishPool.obtainPoolItem());
        }

    }

    private void createPlayer() {
        player = new Fish(ConstantsUtil.SCREEN_WIDTH,ConstantsUtil.SCREEN_HEIGHT, FishType.YELLOW);
        attachChild(player);
    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAME);
    }

    private void init(Object... objects) {
        clearUpdateHandlers();
        clearTouchAreas();

        engine.enableAccelerationSensor(activity, this);
        accelerationData = new AccelerationData();
        fishPool = new FishPool();
        fishPool.batchAllocatePoolItems(ConstantsUtil.POOL_SIZE);

        firstTimeCounter = 0;

    }


    private void createHUD() {
        gameHUD = new HUD();
        camera.setHUD(gameHUD);
        camera.setChaseEntity(player);
        ((BoundCamera)camera).setBounds(0,0,ConstantsUtil.SCREEN_WIDTH * 2,ConstantsUtil.SCREEN_HEIGHT * 2);
        ((BoundCamera)camera).setBoundsEnabled(true);

    }

    private void createBackground() {
        unregisterTouchAreas(new ClassTouchAreaMacher(Sprite.class));
        clearChildScene();

        int offSet = 1;
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2 + offSet, ConstantsUtil.SCREEN_HEIGHT * 3 / 2 - offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH * 3 / 2 - offSet, ConstantsUtil.SCREEN_HEIGHT * 3 / 2 - offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2 + offSet, ConstantsUtil.SCREEN_HEIGHT / 2+ offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH * 3 / 2 - offSet, ConstantsUtil.SCREEN_HEIGHT / 2 + offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));


    }




    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (firstTimeCounter++ == 1) {
            resourcesManager.getStartGameSound().play();
        }

        player.updatePosition(accelerationData);
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
        this.accelerationData = pAccelerationData;

    }
}
