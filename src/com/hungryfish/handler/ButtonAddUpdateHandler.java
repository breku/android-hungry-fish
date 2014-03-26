package com.hungryfish.handler;

import com.hungryfish.model.scene.GameTypeScene;
import com.hungryfish.model.shape.ButtonAdd;
import org.andengine.engine.handler.IUpdateHandler;

import java.util.List;

/**
 * User: Breku
 * Date: 26.03.14
 */
public class ButtonAddUpdateHandler implements IUpdateHandler {


    private List<ButtonAdd> buttonAddList;
    private GameTypeScene gameTypeScene;

    public ButtonAddUpdateHandler(List<ButtonAdd> buttonAddList, GameTypeScene gameTypeScene) {
        this.buttonAddList = buttonAddList;
        this.gameTypeScene = gameTypeScene;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        for (ButtonAdd buttonAdd : buttonAddList) {
            if (buttonAdd.isClicked()) {
                buttonAdd.setClicked(false);
                gameTypeScene.increaseProperty(buttonAdd.getPropertyNumber());
            }
        }

    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
