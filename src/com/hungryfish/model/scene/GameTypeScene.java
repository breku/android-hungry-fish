package com.hungryfish.model.scene;


import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.service.OptionsService;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Breku
 * Date: 08.10.13
 */
public class GameTypeScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

    private MenuScene menuScene;
    private final int PLAY_BUTTON = 0;
    private final int LEFT_BUTTON = 1;
    private final int RIGHT_BUTTON = 2;

    private List<Fish> fishSpriteList;
    private List<Rectangle> fishPropertiesRectangleList;
    List<Line> lineList;
    private Integer currentFishSpriteIndex;
    private OptionsService optionsService;

    private final float MAX_FISH_SPEED = 8;
    private final float MAX_FISH_POWER = 6;
    private final float MAX_FISH_VALUE = 8;

    final int WHITE_RECTANGLE_WIDTH = 200;

    private Sprite lock;


    @Override
    public void createScene(Object... objects) {
        init();
        createBackground();
        createButtons();
        createFishGraphics();
        createFishProperties();
    }

    private void createFishProperties() {
        final int numberOfProperties = 3;


        String[] textPropertiesArray = new String[]{"Speed", "Power", "Value"};
        FishType fishType = fishSpriteList.get(currentFishSpriteIndex).getFishType();

        for (int i = 0; i < numberOfProperties; i++) {
            Text text = new Text(300, 200 - i * 30, ResourcesManager.getInstance().getBlackFont(), textPropertiesArray[i], vertexBufferObjectManager);
            Rectangle rectangleWhite = new Rectangle(500, 200 - i * 30, WHITE_RECTANGLE_WIDTH, 10, vertexBufferObjectManager);
            attachChild(rectangleWhite);
            attachChild(text);
        }


        float speedRectangleWidth = (float) WHITE_RECTANGLE_WIDTH * optionsService.getFishSpeed(fishType) / MAX_FISH_SPEED;
        float powerRectangleWidth = WHITE_RECTANGLE_WIDTH * optionsService.getFishPower(fishType) / MAX_FISH_POWER;
        float valueRectangleWidth = WHITE_RECTANGLE_WIDTH * optionsService.getFishValue(fishType) / MAX_FISH_VALUE;

        fishPropertiesRectangleList.add(new Rectangle(500-((WHITE_RECTANGLE_WIDTH -speedRectangleWidth)/2), 200, speedRectangleWidth, 10, vertexBufferObjectManager));
        fishPropertiesRectangleList.add(new Rectangle(500-((WHITE_RECTANGLE_WIDTH -powerRectangleWidth)/2), 170, powerRectangleWidth, 10, vertexBufferObjectManager));
        fishPropertiesRectangleList.add(new Rectangle(500-((WHITE_RECTANGLE_WIDTH -valueRectangleWidth)/2), 140, valueRectangleWidth, 10, vertexBufferObjectManager));

        for (Rectangle rectangle : fishPropertiesRectangleList) {
            rectangle.setColor(Color.GREEN);
            attachChild(rectangle);
        }


         updatePropertyLines();
    }


    private void updatePropertyLines(){
        FishType fishType = fishSpriteList.get(currentFishSpriteIndex).getFishType();
        for (Line line : lineList) {
            detachChild(line);
            line = null;
        }
        lineList.clear();

        float a = 500 - WHITE_RECTANGLE_WIDTH/2 + WHITE_RECTANGLE_WIDTH * fishType.getFishSpeed() * 2 / MAX_FISH_SPEED;
        lineList.add(new Line(a,195,a,205,vertexBufferObjectManager));


        float b = 500 - WHITE_RECTANGLE_WIDTH/2 + WHITE_RECTANGLE_WIDTH * fishType.getFishPower() * 2 / MAX_FISH_POWER;
        lineList.add(new Line(b,165,b,175,vertexBufferObjectManager));


        float c = 500 - WHITE_RECTANGLE_WIDTH/2 + WHITE_RECTANGLE_WIDTH * fishType.getFishValue() * 2 / MAX_FISH_VALUE;
        lineList.add(new Line(c,135,c,145,vertexBufferObjectManager));


        for (Line line : lineList) {
            line.setColor(Color.RED);
            attachChild(line);
        }

    }

    private void createFishGraphics() {
        for (FishType fishType : FishType.values()) {
            Fish fish = new Fish(400, 350, fishType);
            fish.setVisible(false);
            fishSpriteList.add(fish);
            attachChild(fish);
        }
        fishSpriteList.get(currentFishSpriteIndex).setVisible(true);

        lock = new Sprite(400, 350, ResourcesManager.getInstance().getLockTextureRegion(), vertexBufferObjectManager);
        lock.setVisible(false);
        attachChild(lock);
    }

    private void init() {
        fishSpriteList = new ArrayList<Fish>();
        fishPropertiesRectangleList = new ArrayList<Rectangle>();
        lineList = new ArrayList<Line>();
        currentFishSpriteIndex = 0;
        optionsService = new OptionsService();
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getBackgroundGameTypeTextureRegion(), vertexBufferObjectManager));
    }

    private void createButtons() {
        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAY_BUTTON, ResourcesManager.getInstance().getPlayButtonTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem rightButtonMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(RIGHT_BUTTON, ResourcesManager.getInstance().getRightArrowTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem leftButtonMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(LEFT_BUTTON, ResourcesManager.getInstance().getLeftArrowTextureRegion(), vertexBufferObjectManager), 1.2f, 1);

        menuScene.addMenuItem(playMenuItem);
        menuScene.addMenuItem(leftButtonMenuItem);
        menuScene.addMenuItem(rightButtonMenuItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(400, 100);
        leftButtonMenuItem.setPosition(200, 350);
        rightButtonMenuItem.setPosition(600, 350);

        menuScene.setOnMenuItemClickListener(this);

        setChildScene(menuScene);

    }


    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuSceneFrom(SceneType.GAMETYPE);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAMETYPE;
    }

    @Override
    public void disposeScene() {
        for (Sprite sprite : fishSpriteList) {
            detachChild(sprite);
            sprite = null;
        }
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case PLAY_BUTTON:
                SceneManager.getInstance().loadGameScene(fishSpriteList.get(currentFishSpriteIndex).getFishType());
                break;
            case RIGHT_BUTTON:
                changeFishRight();
                updateLock();
                updateProperties();
                break;
            case LEFT_BUTTON:
                changeFishLeft();
                updateLock();
                updateProperties();
                break;
            default:
                return false;
        }
        return false;
    }

    private void updateProperties() {

        FishType fishType = fishSpriteList.get(currentFishSpriteIndex).getFishType();
        float speedRectangleWidth = (float) WHITE_RECTANGLE_WIDTH * optionsService.getFishSpeed(fishType) / MAX_FISH_SPEED;
        float powerRectangleWidth = WHITE_RECTANGLE_WIDTH * optionsService.getFishPower(fishType) / MAX_FISH_POWER;
        float valueRectangleWidth = WHITE_RECTANGLE_WIDTH * optionsService.getFishValue(fishType) / MAX_FISH_VALUE;

        for (Rectangle rectangle : fishPropertiesRectangleList) {
            detachChild(rectangle);
            rectangle = null;
        }

        fishPropertiesRectangleList.clear();

        fishPropertiesRectangleList.add(new Rectangle(500-((WHITE_RECTANGLE_WIDTH -speedRectangleWidth)/2), 200, speedRectangleWidth, 10, vertexBufferObjectManager));
        fishPropertiesRectangleList.add(new Rectangle(500-((WHITE_RECTANGLE_WIDTH -powerRectangleWidth)/2), 170, powerRectangleWidth, 10, vertexBufferObjectManager));
        fishPropertiesRectangleList.add(new Rectangle(500-((WHITE_RECTANGLE_WIDTH -valueRectangleWidth)/2), 140, valueRectangleWidth, 10, vertexBufferObjectManager));

        for (Rectangle rectangle : fishPropertiesRectangleList) {
            rectangle.setColor(Color.GREEN);
            attachChild(rectangle);
        }
        updatePropertyLines();

    }

    private void updateLock() {
        Fish fish = fishSpriteList.get(currentFishSpriteIndex);
        if (optionsService.isFishLocked(fish.getFishType())) {
            lock.setVisible(true);
        } else {
            lock.setVisible(false);
        }
    }

    private void changeFishLeft() {
        fishSpriteList.get(currentFishSpriteIndex).setVisible(false);
        if (currentFishSpriteIndex == 0) {
            currentFishSpriteIndex = fishSpriteList.size() - 1;
        } else {
            currentFishSpriteIndex--;
        }
        fishSpriteList.get(currentFishSpriteIndex).setVisible(true);
    }

    private void changeFishRight() {
        fishSpriteList.get(currentFishSpriteIndex).setVisible(false);
        currentFishSpriteIndex = (currentFishSpriteIndex + 1) % fishSpriteList.size();
        fishSpriteList.get(currentFishSpriteIndex).setVisible(true);
    }
}
