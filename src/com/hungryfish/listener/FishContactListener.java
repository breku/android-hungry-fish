package com.hungryfish.listener;

import com.badlogic.gdx.physics.box2d.*;
import com.hungryfish.model.shape.FishBodyData;

/**
 * User: Breku
 * Date: 06.03.14
 */
public class FishContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        final Fixture x1 = contact.getFixtureA();
        final Fixture x2 = contact.getFixtureB();


        if (x1 != null && x2 != null &&
                x1.getBody().getUserData() instanceof FishBodyData &&
                x2.getBody().getUserData() instanceof FishBodyData) {

            FishBodyData x1Data = ((FishBodyData) x1.getBody().getUserData());
            FishBodyData x2Data = ((FishBodyData) x2.getBody().getUserData());


            if (x1Data.getName().equals("player") && x2Data.getName().contains("enemy")) {
                x2Data.setToRemove(true);
                x2Data.setKilled(true);
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
