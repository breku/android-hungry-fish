package com.hungryfish.model.scene;


import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.util.ConstantsUtil;
import com.hungryfish.util.SceneType;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

/**
 * User: Breku
 * Date: 08.10.13
 */
public class GameTypeScene extends BaseScene implements MenuScene.IOnMenuItemClickListener {

    private MenuScene menuScene;
    private final int CLASSIC_GAME = 0;
    private final int HALFMARATHON_GAME = 1;
    private final int MARATHON_GAME = 2;


    @Override
    public void createScene(Object... objects) {
        createBackground();
        createButtons();
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getBackgroundGameTypeTextureRegion(), vertexBufferObjectManager));
    }

    private void createButtons() {
        menuScene = new MenuScene(camera);
        menuScene.setPosition(0, 0);

        final IMenuItem classicGameItem = new ScaleMenuItemDecorator(new SpriteMenuItem(CLASSIC_GAME, ResourcesManager.getInstance().getPlayButtonTextureRegion(), vertexBufferObjectManager), 1.2f, 1);

        menuScene.addMenuItem(classicGameItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);

        classicGameItem.setPosition(400, 317);

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
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case CLASSIC_GAME:
                SceneManager.getInstance().loadGameScene();
                break;
            default:
                return false;
        }
        return false;
    }
}
