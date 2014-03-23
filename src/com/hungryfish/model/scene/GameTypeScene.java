package com.hungryfish.model.scene;


import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.FishType;
import com.hungryfish.util.SceneType;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

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
    private Integer currentFishSpriteIndex;


    @Override
    public void createScene(Object... objects) {
        init();
        createBackground();
        createButtons();
        createFishGraphics();
    }

    private void createFishGraphics() {
        for (FishType fishType : FishType.values()) {
            Fish fish = new Fish(400,240,fishType);
            fish.setVisible(false);
            fishSpriteList.add(fish);
            attachChild(fish);
        }
        fishSpriteList.get(currentFishSpriteIndex).setVisible(true);
    }

    private void init() {
        fishSpriteList = new ArrayList<Fish>();
        currentFishSpriteIndex = 0;
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getBackgroundGameTypeTextureRegion(), vertexBufferObjectManager));
    }

    private void createButtons() {
        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);

        final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAY_BUTTON, ResourcesManager.getInstance().getPlayButtonTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem rightButtonMenuItem= new ScaleMenuItemDecorator(new SpriteMenuItem(RIGHT_BUTTON, ResourcesManager.getInstance().getRightArrowTextureRegion(), vertexBufferObjectManager), 1.2f, 1);
        final IMenuItem leftButtonMenuItem= new ScaleMenuItemDecorator(new SpriteMenuItem(LEFT_BUTTON, ResourcesManager.getInstance().getLeftArrowTextureRegion(), vertexBufferObjectManager), 1.2f, 1);

        menuScene.addMenuItem(playMenuItem);
        menuScene.addMenuItem(leftButtonMenuItem);
        menuScene.addMenuItem(rightButtonMenuItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        playMenuItem.setPosition(400, 100);
        leftButtonMenuItem.setPosition(200, 240);
        rightButtonMenuItem.setPosition(600, 240);

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
                break;
            case LEFT_BUTTON:
                changeFishLeft();
                break;
            default:
                return false;
        }
        return false;
    }

    private void changeFishLeft() {
        fishSpriteList.get(currentFishSpriteIndex).setVisible(false);
        if(currentFishSpriteIndex == 0){
            currentFishSpriteIndex = fishSpriteList.size()-1;
        }else {
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
