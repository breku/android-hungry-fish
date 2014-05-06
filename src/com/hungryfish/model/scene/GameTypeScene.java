package com.hungryfish.model.scene;


import android.widget.Toast;
import com.hungryfish.handler.ButtonAddUpdateHandler;
import com.hungryfish.manager.ResourcesManager;
import com.hungryfish.manager.SceneManager;
import com.hungryfish.model.shape.ButtonAdd;
import com.hungryfish.model.shape.Fish;
import com.hungryfish.service.OptionsService;
import com.hungryfish.service.PlayerService;
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
import org.andengine.input.touch.TouchEvent;
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
    private List<Line> lineList;
    private List<Text> propertyCostTextList;
    private List<ButtonAdd> buttonAddList;

    private Integer currentFishSpriteIndex;

    private OptionsService optionsService;
    private PlayerService playerService;

    private final float MAX_FISH_SPEED = 8;
    private final float MAX_FISH_POWER = 6;
    private final float MAX_FISH_VALUE = 8;

    final int WHITE_RECTANGLE_WIDTH = 200;

    final int TOP_PROPERTY_HEIGHT = 250;
    final int PROPERTY_STRIDE = 50;
    final int NUMBER_OF_PROPERITES = 3;

    private Toast currentToast;

    private Sprite lock;
    private Sprite buttonBuy;

    private Text fishPriceText;
    private Text moneyText;


    @Override
    public void createScene(Object... objects) {
        clearEverything();
        init();
        createBackground();
        createButtonsLeftRight();
        createFishGraphics();
        createFishProperties();
        createMoneyCaptions();
        createButtonsAdd();
        createButtonBuy();
    }

    private void createButtonBuy() {
        buttonBuy = new Sprite(150, 200, ResourcesManager.getInstance().getBuyButtonTextureRegion(), vertexBufferObjectManager) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionUp() && buttonBuy.isVisible()) {
                    Integer fishPrice = optionsService.getFishPriceFor(getCurrentFishType());
                    Float money = optionsService.getMoney();
                    if (money >= fishPrice) {
                        optionsService.unlockFish(getCurrentFishType());
                        optionsService.setMoney(money - fishPrice);
                        lock.setVisible(false);
                        fishPriceText.setVisible(false);
                        buttonBuy.setVisible(false);
                        showFishBoughtText();
                        refreshMoney();
                    } else {
                        showWarningNotEnoughMoney();
                    }
                    return true;
                }
                return false;
            }
        };
        buttonBuy.setVisible(false);
        registerTouchArea(buttonBuy);
        attachChild(buttonBuy);
    }

    private void clearEverything() {
        clearTouchAreas();
        clearEntityModifiers();
        clearUpdateHandlers();
    }

    private void createButtonsAdd() {
        for (int i = 0; i < NUMBER_OF_PROPERITES; i++) {
            int x = 650;
            int y = TOP_PROPERTY_HEIGHT - i * PROPERTY_STRIDE;
            ButtonAdd button = new ButtonAdd(x, y, i);
            createPropertyCost(x, y);
            buttonAddList.add(button);
            registerTouchArea(button);
            attachChild(button);
        }

        registerUpdateHandler(new ButtonAddUpdateHandler(buttonAddList, this));

    }

    private void createPropertyCost(int x, int y) {
        Integer propertyUpgradeCost = getPropertyCost();
        Text text = new Text(x + 70, y, ResourcesManager.getInstance().getBlackFont(), "0123456789 $", vertexBufferObjectManager);
        text.setText(propertyUpgradeCost + " $");
        attachChild(text);
        propertyCostTextList.add(text);
    }

    private Integer getPropertyCost() {
        int fishLevel = getCurrentFishType().getFishLevel();

        return ConstantsUtil.PROPERTY_MULTIPLIER_COST * (int) Math.pow(fishLevel + 1, 3);
    }

    private void createMoneyCaptions() {
        String money = "Money:  " + optionsService.getMoney() + " $";
        moneyText = new Text(400, 440, ResourcesManager.getInstance().getBlackFont(), money, vertexBufferObjectManager);
        attachChild(moneyText);
    }

    private void createFishProperties() {
        String[] textPropertiesArray = new String[]{"Speed", "Power", "Money Profit"};

        for (int i = 0; i < NUMBER_OF_PROPERITES; i++) {
            Text text = new Text(300, TOP_PROPERTY_HEIGHT - i * PROPERTY_STRIDE, ResourcesManager.getInstance().getBlackFont(), textPropertiesArray[i], vertexBufferObjectManager);
            Rectangle rectangleWhite = new Rectangle(500, TOP_PROPERTY_HEIGHT - i * PROPERTY_STRIDE, WHITE_RECTANGLE_WIDTH, 10, vertexBufferObjectManager);
            attachChild(rectangleWhite);
            attachChild(text);
        }

        updateProperties();
        updatePropertyLines();
    }


    private void updatePropertyLines() {
        FishType fishType = getCurrentFishType();
        for (Line line : lineList) {
            line.setVisible(false);
        }
        lineList.clear();

        float a = 500 - WHITE_RECTANGLE_WIDTH / 2 + WHITE_RECTANGLE_WIDTH * fishType.getFishSpeed() * 2 / MAX_FISH_SPEED;
        lineList.add(new Line(a, TOP_PROPERTY_HEIGHT - 5, a, TOP_PROPERTY_HEIGHT + 5, vertexBufferObjectManager));


        float b = 500 - WHITE_RECTANGLE_WIDTH / 2 + WHITE_RECTANGLE_WIDTH * fishType.getFishPower() * 2 / MAX_FISH_POWER;
        lineList.add(new Line(b, TOP_PROPERTY_HEIGHT - PROPERTY_STRIDE - 5, b, TOP_PROPERTY_HEIGHT - PROPERTY_STRIDE + 5, vertexBufferObjectManager));


        float c = 500 - WHITE_RECTANGLE_WIDTH / 2 + WHITE_RECTANGLE_WIDTH * fishType.getFishValue() * 2 / MAX_FISH_VALUE;
        lineList.add(new Line(c, TOP_PROPERTY_HEIGHT - 2 * PROPERTY_STRIDE - 5, c, TOP_PROPERTY_HEIGHT - 2 * PROPERTY_STRIDE + 5, vertexBufferObjectManager));


        for (Line line : lineList) {
            line.setLineWidth(2.0f);
            line.setColor(Color.RED);
            attachChild(line);
        }

    }

    private void createFishGraphics() {

        for (int i = 0; i < FishType.values().length; i++) {

            Fish fish = new Fish(400, 350, FishType.values()[i]);
            fish.setVisible(false);
            fishSpriteList.add(fish);
            attachChild(fish);

            if (FishType.values()[i].getFishLevel() == 0) {
                fish.setVisible(true);
                currentFishSpriteIndex = i;
            }


        }

        lock = new Sprite(400, 350, ResourcesManager.getInstance().getLockTextureRegion(), vertexBufferObjectManager);
        lock.setVisible(false);

        Integer fishPrice = optionsService.getFishPriceFor(getCurrentFishType());
        fishPriceText = new Text(400, 300, ResourcesManager.getInstance().getBlackFont(), "Cost:  " + fishPrice + " $", vertexBufferObjectManager);
        fishPriceText.setVisible(false);
        attachChild(fishPriceText);

        attachChild(lock);
    }

    private void init() {
        fishSpriteList = new ArrayList<Fish>();
        fishPropertiesRectangleList = new ArrayList<Rectangle>();
        lineList = new ArrayList<Line>();
        buttonAddList = new ArrayList<ButtonAdd>();
        propertyCostTextList = new ArrayList<Text>();
        currentFishSpriteIndex = 0;
        optionsService = new OptionsService();
        playerService = new PlayerService();
    }

    private void createBackground() {
        attachChild(new Sprite(ConstantsUtil.SCREEN_WIDTH / 2, ConstantsUtil.SCREEN_HEIGHT / 2, resourcesManager.getBackgroundGameTypeTextureRegion(), vertexBufferObjectManager));
    }

    private void createButtonsLeftRight() {
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
            sprite.setVisible(false);
        }
        fishSpriteList.clear();
    }

    @Override
    public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
        switch (pMenuItem.getID()) {
            case PLAY_BUTTON:
                if (!optionsService.isFishLocked(getCurrentFishType())) {
                    SceneManager.getInstance().loadGameScene(getCurrentFishType());
                }
                break;
            case RIGHT_BUTTON:
                changeFishRight();
                updateLock();
                updateProperties();
                updatePropertyLines();
                updateFishPrice();
                break;
            case LEFT_BUTTON:
                changeFishLeft();
                updateLock();
                updateProperties();
                updatePropertyLines();
                updateFishPrice();
                break;
            default:
                return false;
        }
        return false;
    }

    private void updateProperties() {

        FishType fishType = getCurrentFishType();
        float speedRectangleWidth = (float) WHITE_RECTANGLE_WIDTH * optionsService.getFishSpeed(fishType) / MAX_FISH_SPEED;
        float powerRectangleWidth = WHITE_RECTANGLE_WIDTH * optionsService.getFishPower(fishType) / MAX_FISH_POWER;
        float valueRectangleWidth = WHITE_RECTANGLE_WIDTH * optionsService.getFishValue(fishType) / MAX_FISH_VALUE;

        for (Rectangle rectangle : fishPropertiesRectangleList) {
            rectangle.setVisible(false);
        }

        fishPropertiesRectangleList.clear();

        fishPropertiesRectangleList.add(new Rectangle(500 - ((WHITE_RECTANGLE_WIDTH - speedRectangleWidth) / 2), TOP_PROPERTY_HEIGHT, speedRectangleWidth, 10, vertexBufferObjectManager));
        fishPropertiesRectangleList.add(new Rectangle(500 - ((WHITE_RECTANGLE_WIDTH - powerRectangleWidth) / 2), TOP_PROPERTY_HEIGHT - PROPERTY_STRIDE, powerRectangleWidth, 10, vertexBufferObjectManager));
        fishPropertiesRectangleList.add(new Rectangle(500 - ((WHITE_RECTANGLE_WIDTH - valueRectangleWidth) / 2), TOP_PROPERTY_HEIGHT - 2 * PROPERTY_STRIDE, valueRectangleWidth, 10, vertexBufferObjectManager));

        for (Rectangle rectangle : fishPropertiesRectangleList) {
            rectangle.setColor(Color.GREEN);
            attachChild(rectangle);
        }

    }

    private void updateLock() {
        Fish fish = fishSpriteList.get(currentFishSpriteIndex);
        if (optionsService.isFishLocked(fish.getFishType())) {
            lock.setVisible(true);
            fishPriceText.setVisible(true);
            buttonBuy.setVisible(true);
        } else {
            lock.setVisible(false);
            fishPriceText.setVisible(false);
            buttonBuy.setVisible(false);
        }
    }

    private void updateFishPrice() {
        Integer fishPrice = optionsService.getFishPriceFor(getCurrentFishType());
        fishPriceText.setText("Cost:  " + fishPrice + " $");
        for (Text text : propertyCostTextList) {
            text.setText(getPropertyCost() + " $");
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

        updateButtonsTouchArea();
    }


    private void changeFishRight() {
        fishSpriteList.get(currentFishSpriteIndex).setVisible(false);
        currentFishSpriteIndex = (currentFishSpriteIndex + 1) % fishSpriteList.size();
        fishSpriteList.get(currentFishSpriteIndex).setVisible(true);
        updateButtonsTouchArea();
    }


    private void updateButtonsTouchArea() {
        for (ButtonAdd buttonAdd : buttonAddList) {
            unregisterTouchArea(buttonAdd);
        }

        if (!optionsService.isFishLocked(getCurrentFishType())) {
            for (ButtonAdd buttonAdd : buttonAddList) {
                registerTouchArea(buttonAdd);
            }
        }


    }


    public void increaseProperty(int propertyNumber) {
        if (playerService.maxPropertyValue(propertyNumber, getCurrentFishType())) {
            showWarningMaxProperty(propertyNumber);
            return;
        }

        Float money = optionsService.getMoney();
        Integer propertyCost = getPropertyCost();
        if (money >= propertyCost) {
            playerService.increasePropertyFor(propertyNumber, getCurrentFishType());
            updateProperties();
            showPropertyUpgraded(propertyNumber);
            optionsService.setMoney(money - propertyCost);
            refreshMoney();
        } else {
            showWarningNotEnoughMoney();
        }
    }

    private void refreshMoney() {
        String money = "Money:  " + optionsService.getMoney() + " $";
        moneyText.setText(money);
    }

    private void showPropertyUpgraded(int propertyNumber) {
        final String finalPropertyName = getPropertyName(propertyNumber);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentToast != null) {
                    currentToast.cancel();
                }
                currentToast = Toast.makeText(activity, finalPropertyName + " upgraded", 1);
                currentToast.show();
            }
        });
    }

    private String getPropertyName(int propertyNumber) {
        String propertyName = null;
        switch (propertyNumber) {
            case 0:
                propertyName = "Speed";
                break;
            case 1:
                propertyName = "Power";
                break;
            case 2:
                propertyName = "Money Profit";
                break;
        }
        return propertyName;
    }


    private void showWarningMaxProperty(int propertyNumber) {
        final String finalPropertyName = getPropertyName(propertyNumber);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentToast != null) {
                    currentToast.cancel();
                }
                currentToast = Toast.makeText(activity, finalPropertyName + " has max value", 1);
                currentToast.show();
            }
        });
    }

    private void showWarningNotEnoughMoney() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentToast != null) {
                    currentToast.cancel();
                }
                currentToast = Toast.makeText(activity, "Not enough money", 1);
                currentToast.show();
            }
        });
    }

    private void showFishBoughtText() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentToast != null) {
                    currentToast.cancel();
                }
                currentToast = Toast.makeText(activity, "Fish bought", 1);
                currentToast.show();
            }
        });
    }

    private FishType getCurrentFishType() {
        return fishSpriteList.get(currentFishSpriteIndex).getFishType();
    }

}
