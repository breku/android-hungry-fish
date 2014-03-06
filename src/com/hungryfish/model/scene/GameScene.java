package com.hungryfish.model.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.hungryfish.handler.CollisionUpdateHandler;
import com.hungryfish.listener.FishContactListener;
import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.matcher.ClassTouchAreaMacher;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.model.shape.FishBodyData;
import com.hungryfish.pool.FishPool;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.debugdraw.Box2dDebugRenderer;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Breku
 * Date: 21.09.13
 */
public class GameScene extends BaseScene implements IAccelerationListener {

    private HUD gameHUD;

    private Integer firstTimeCounter;
    private Fish player;
    private AccelerationData accelerationData;
    private ContactListener fishContactListener;
    private FishPool fishPool;

    private PhysicsWorld physicsWorld;

    private IUpdateHandler collisionUpdateHandler;


    /**
     * @param objects objects[0] - levelDifficulty
     *                objects[1] - mathParameter
     */
    public GameScene(Object... objects) {
        super(objects);
    }


    @Override
    public void createScene(Object... objects) {
        clearEverything();
        initPhysics();
        init(objects);
        createBackground();
        createPlayer();
        createEnemy();
        createHUD();
        activateCollisions();
//        createDebugRenderer();
    }

    private void activateCollisions() {
        fishContactListener = new FishContactListener();
        physicsWorld.setContactListener(fishContactListener);

        collisionUpdateHandler = new CollisionUpdateHandler(physicsWorld,this);
        registerUpdateHandler(collisionUpdateHandler);
    }

    private void createDebugRenderer() {
        Box2dDebugRenderer box2dDebugRenderer = new Box2dDebugRenderer(physicsWorld,vertexBufferObjectManager);
        attachChild(box2dDebugRenderer);
    }

    private void clearEverything() {
        clearUpdateHandlers();
        clearTouchAreas();
    }

    private void createEnemy() {
        for (int i = 0; i < ConstantsUtil.NUMBER_OF_ENEMIES; i++) {
            attachChild(fishPool.obtainPoolItem());
        }

    }

    private void createPlayer() {
        FishBodyData fishBodyData = new FishBodyData("player",ConstantsUtil.TAG_SPRITE_PLAYER);
        player = new Fish(ConstantsUtil.SCREEN_WIDTH, ConstantsUtil.SCREEN_HEIGHT, FishType.YELLOW, physicsWorld, false, fishBodyData,null,ConstantsUtil.TAG_SPRITE_PLAYER);

        attachChild(player);
    }

    private void initPhysics() {
        physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
        registerUpdateHandler(physicsWorld);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAME);
    }

    private void init(Object... objects) {


        engine.enableAccelerationSensor(activity, this);
        accelerationData = new AccelerationData();
        fishPool = new FishPool(physicsWorld);
        fishPool.batchAllocatePoolItems(ConstantsUtil.POOL_SIZE);

        firstTimeCounter = 0;

    }


    private void createHUD() {
        gameHUD = new HUD();
        camera.setHUD(gameHUD);
        camera.setChaseEntity(player);
        ((BoundCamera) camera).setBounds(0, 0, ConstantsUtil.SCREEN_WIDTH * 2, ConstantsUtil.SCREEN_HEIGHT * 2);
        ((BoundCamera) camera).setBoundsEnabled(true);

    }

    private void createBackground() {
        unregisterTouchAreas(new ClassTouchAreaMacher(Sprite.class));
        clearChildScene();

        int offSet = 1;
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2 + offSet, ConstantsUtil.SCREEN_HEIGHT * 3 / 2 - offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH * 3 / 2 - offSet, ConstantsUtil.SCREEN_HEIGHT * 3 / 2 - offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2 + offSet, ConstantsUtil.SCREEN_HEIGHT / 2 + offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH * 3 / 2 - offSet, ConstantsUtil.SCREEN_HEIGHT / 2 + offSet,
                ResourcesManager.getInstance().getBackgroundGameTextureRegion(), vertexBufferObjectManager));


    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if (firstTimeCounter++ == 1) {
            resourcesManager.getStartGameSound().play();

            List<Fish> enemyFishes = getAllEnemyFishes();
            for (Fish enemyFish : enemyFishes) {
                enemyFish.swim();
            }
        }

        player.updatePosition(accelerationData);

    }

    private List<Fish> getAllEnemyFishes() {
        List<Fish> result = new ArrayList<Fish>();
        for (IEntity child : mChildren) {
            if (child instanceof Fish) {
                if (((Fish) child).isEnemy()) {
                    result.add(((Fish) child));
                }
            }
        }
        return result;

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
