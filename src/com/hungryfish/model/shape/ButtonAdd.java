package com.hungryfish.model.shape;

import com.hungryfish.manager.ResourcesManager;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

/**
 * User: Breku
 * Date: 26.03.14
 */
public class ButtonAdd extends Sprite {

    private boolean clicked;
    private int propertyNumber;

    public ButtonAdd(final float pX, final float pY, int propertyNumber) {
        super(pX, pY, ResourcesManager.getInstance().getButtonAddTextureRegion(), ResourcesManager.getInstance().getVertexBufferObjectManager());
        this.propertyNumber = propertyNumber;
        this.clicked = false;
    }

    //TODO Fish locked - turn button off

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
            clicked = true;
            return true;
        }
        return false;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }

    public int getPropertyNumber() {
        return propertyNumber;
    }
}
