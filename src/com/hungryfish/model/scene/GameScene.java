package com.hungryfish.model.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.hungryfish.handler.CollisionUpdateHandler;
import com.hungryfish.handler.FishBorderUpdateHandler;
import com.hungryfish.handler.FishNumberUpdateHandler;
import com.hungryfish.listener.FishContactListener;
import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.matcher.ClassTouchAreaMacher;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.model.shape.FishBodyData;
import com.hungryfish.pool.FishPool;
import com.hungryfish.service.HighScoreService;
import com.hungryfish.service.OptionsService;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.debugdraw.Box2dDebugRenderer;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.Iterator;
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
    private IUpdateHandler fishRemovingUpdateHandler;
    private IUpdateHandler fishNumberUpdateHandler;

    private Text numberOfEatenFishesTextStatic;
    private Text pointsTextStatic;
    private Text pointsTextDynamic;
    private Text canEatText;
    private Text numberOfEatenFishesTextDynamic;
    private Text timerTextStatic;
    private Text timerTextDynamic;

    private Integer numberOfEeatenFishes;
    private Integer points;

    private List<Fish> possibleFishToEat;
    private boolean endGame;
    private boolean endGameLose;

    private HighScoreService highScoreService;
    private OptionsService optionsService;


    // objects[0] - FishType
    public GameScene(Object... objects) {
        super(objects);
    }


    @Override
    public void createScene(Object... objects) {
        clearEverything();
        initPhysics();
        init(objects);
        createBackground();
        createPlayer((FishType) objects[0]);
        createEnemy();
        createTerrain();
        activateCollisions();
        createHUD();
        updatePossibleFishToEat();

//        createDebugRenderer();
    }

    private void createTerrain() {

        List<Line> lineList = new ArrayList<Line>();

        // top, bottom, left, right
        lineList.add(new Line(ConstantsUtil.LEFT_BORDER, ConstantsUtil.TOP_BORDER, ConstantsUtil.RIGHT_BORDER, ConstantsUtil.TOP_BORDER, vertexBufferObjectManager));
        lineList.add(new Line(ConstantsUtil.LEFT_BORDER, ConstantsUtil.BOTTOM_BORDER, ConstantsUtil.RIGHT_BORDER, ConstantsUtil.BOTTOM_BORDER, vertexBufferObjectManager));
        lineList.add(new Line(ConstantsUtil.LEFT_BORDER, ConstantsUtil.BOTTOM_BORDER, ConstantsUtil.LEFT_BORDER, ConstantsUtil.TOP_BORDER, vertexBufferObjectManager));
        lineList.add(new Line(ConstantsUtil.RIGHT_BORDER, ConstantsUtil.BOTTOM_BORDER, ConstantsUtil.RIGHT_BORDER, ConstantsUtil.TOP_BORDER, vertexBufferObjectManager));


        List<Body> bodyList = new ArrayList<Body>();


        FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0, false,
                ConstantsUtil.CATEGORY_BIT_WALL, ConstantsUtil.MASK_BITS_WALL, (short) 0);

        // top, bottom, left, right
        for (Line line : lineList) {
            bodyList.add(PhysicsFactory.createLineBody(physicsWorld, line, fixtureDef));
        }

        Iterator<Line> lineIterator = lineList.iterator();
        Iterator<Body> bodyIterator = bodyList.iterator();
        while (bodyIterator.hasNext() && lineIterator.hasNext()) {
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(lineIterator.next(), bodyIterator.next()));
        }

        for (Line line : lineList) {
            line.setVisible(false);
            attachChild(line);
        }
    }

    private void activateCollisions() {
        fishContactListener = new FishContactListener();
        physicsWorld.setContactListener(fishContactListener);

        collisionUpdateHandler = new CollisionUpdateHandler(physicsWorld, this);
        registerUpdateHandler(collisionUpdateHandler);

        fishRemovingUpdateHandler = new FishBorderUpdateHandler(physicsWorld, this);
        registerUpdateHandler(fishRemovingUpdateHandler);

        fishNumberUpdateHandler = new FishNumberUpdateHandler(physicsWorld, this);
        registerUpdateHandler(fishNumberUpdateHandler);
    }

    private void createDebugRenderer() {
        Box2dDebugRenderer box2dDebugRenderer = new Box2dDebugRenderer(physicsWorld, vertexBufferObjectManager);
        attachChild(box2dDebugRenderer);
    }

    private void clearEverything() {
        clearUpdateHandlers();
        clearTouchAreas();
    }

    private void createEnemy() {
        for (int i = 0; i < ConstantsUtil.NUMBER_OF_ENEMIES; i++) {
            createEnemyFish();
        }

    }

    public Fish createEnemyFish() {
        Fish fish = fishPool.obtainPoolItem();
        attachChild(fish);
        return fish;
    }

    private void createPlayer(FishType fishType) {
        FishBodyData fishBodyData = new FishBodyData("player", ConstantsUtil.TAG_SPRITE_PLAYER, fishType);
        fishBodyData.updateActualFishParameters(optionsService.getFishValue(fishType), optionsService.getFishPower(fishType), optionsService.getFishSpeed(fishType));
        player = new Fish(ConstantsUtil.SCREEN_WIDTH, ConstantsUtil.SCREEN_HEIGHT, fishType, physicsWorld, false, fishBodyData, null, ConstantsUtil.TAG_SPRITE_PLAYER);
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

        highScoreService = new HighScoreService();
        optionsService = new OptionsService();
        possibleFishToEat = new ArrayList<Fish>();

        for (int i = 0; i < FishType.values().length; i++) {
            Fish fish = new Fish(520 + i * 50, 460, FishType.values()[i]);
            fish.setScale(0.5f);
            fish.setAlpha(0.3f);
            possibleFishToEat.add(fish);

        }

        firstTimeCounter = 0;
        numberOfEeatenFishes = 0;
        points = 0;

        endGame = false;
        endGameLose = false;

        timerTextStatic = new Text(30, 460, ResourcesManager.getInstance().getBlackFont(), "Time:", vertexBufferObjectManager);
        timerTextDynamic = new Text(80, 460, ResourcesManager.getInstance().getBlackFont(), "0123456789", vertexBufferObjectManager);
        numberOfEatenFishesTextStatic = new Text(160, 460, ResourcesManager.getInstance().getBlackFont(), "Eaten fishes:", vertexBufferObjectManager);
        numberOfEatenFishesTextDynamic = new Text(240, 460, ResourcesManager.getInstance().getBlackFont(), "0123456789", vertexBufferObjectManager, DrawType.DYNAMIC);
        pointsTextStatic = new Text(300, 460, ResourcesManager.getInstance().getBlackFont(), "Points:", vertexBufferObjectManager);
        pointsTextDynamic = new Text(350, 460, ResourcesManager.getInstance().getBlackFont(), "0123456789", vertexBufferObjectManager);
        canEatText = new Text(440, 460, ResourcesManager.getInstance().getBlackFont(), "You can eat:", vertexBufferObjectManager);

        numberOfEatenFishesTextDynamic.setText("0");
        timerTextDynamic.setText(String.valueOf(ConstantsUtil.GAME_TIME));
        pointsTextDynamic.setText("0");

    }


    private void createHUD() {
        gameHUD = new HUD();
        Rectangle gameHudRectangle = new Rectangle(400, 460, 800, 40, vertexBufferObjectManager);
        gameHudRectangle.setColor(Color.WHITE);
        gameHUD.attachChild(gameHudRectangle);

        // Timer
        gameHUD.attachChild(timerTextStatic);
        gameHUD.attachChild(timerTextDynamic);

        // Number of eaten fishes
        gameHUD.attachChild(numberOfEatenFishesTextStatic);
        gameHUD.attachChild(numberOfEatenFishesTextDynamic);

        // Points
        gameHUD.attachChild(pointsTextStatic);
        gameHUD.attachChild(pointsTextDynamic);

        // Fishes that you can eat
        gameHUD.attachChild(canEatText);
        for (Fish fish : possibleFishToEat) {
            gameHUD.attachChild(fish);
        }
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
            timerTextDynamic.setText(String.valueOf(ConstantsUtil.GAME_TIME));
            List<Fish> enemyFishes = getAllEnemyFishes();
            for (Fish enemyFish : enemyFishes) {
                enemyFish.swim();
            }
            updateTime();
        }

        updateNumberOfEatenFishesText();
        updatePoints();

        checkForEndGame();


        player.updatePosition(accelerationData);

    }

    private void checkForEndGame() {
        if (endGame) {
            highScoreService.addScore(points);
            Float fishValue = optionsService.getFishValue(player.getFishType());
            optionsService.updateMoney(points * fishValue);
            SceneManager.getInstance().loadEndGameScene(points, numberOfEeatenFishes, player.getFishType(), true);
        }

        if (endGameLose) {
            highScoreService.addScore(points);
            Float fishValue = optionsService.getFishValue(player.getFishType());
            optionsService.updateMoney(points * fishValue);
            SceneManager.getInstance().loadEndGameScene(points, numberOfEeatenFishes, player.getFishType(), false);
        }
    }

    private void updateTime() {
        registerUpdateHandler(new TimerHandler(1.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                Integer prevTime = Integer.valueOf(timerTextDynamic.getText().toString());
                if (prevTime == 1) {
                    endGame = true;
                }
                String nextTime = String.valueOf(--prevTime);
                timerTextDynamic.setText(nextTime);

            }
        }));
    }

    private void updatePoints() {
        if (Integer.valueOf(pointsTextDynamic.getText().toString()) != points) {
            pointsTextDynamic.setText(String.valueOf(points));
        }
    }

    public void addOneEnemy() {
        numberOfEeatenFishes += 1;
    }

    public void addPoints(FishType fishType) {
        points = points + fishType.getFishLevel() + 1;
        ((FishBodyData) player.getCurrentBody().getUserData()).setPoints(points);
    }

    private void updateNumberOfEatenFishesText() {
        if (Integer.valueOf(numberOfEatenFishesTextDynamic.getText().toString()) != numberOfEeatenFishes) {
            numberOfEatenFishesTextDynamic.setText(String.valueOf(numberOfEeatenFishes));
        }
    }

    public void updatePossibleFishToEat() {

        for (Fish fish : possibleFishToEat) {
            int enemyLevel = fish.getFishType().getFishLevel();
            float fishPower = ((FishBodyData) player.getCurrentBody().getUserData()).getFishPower();
            if (points * fishPower >= Math.pow(enemyLevel, 3) * ConstantsUtil.UNLOCK_LEVEL_MULTIPLIER) {
                fish.setAlpha(1.0f);
            }
        }
    }


    public List<Fish> getAllEnemyFishes() {
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

    public void setEndGameLose(boolean endGameLose) {
        this.endGameLose = endGameLose;
    }
}
