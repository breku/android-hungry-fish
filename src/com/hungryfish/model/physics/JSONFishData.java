package com.hungryfish.model.physics;

import java.util.List;

/**
 * User: Breku
 * Date: 26.02.14
 */
public class JSONFishData {

    private List<RigidBody> rigidBodies;
    private List<DynamicObject> dynamicObjects;

    public List<RigidBody> getRigidBodies() {
        return rigidBodies;
    }

    public void setRigidBodies(List<RigidBody> rigidBodies) {
        this.rigidBodies = rigidBodies;
    }

    public List<DynamicObject> getDynamicObjects() {
        return dynamicObjects;
    }

    public void setDynamicObjects(List<DynamicObject> dynamicObjects) {
        this.dynamicObjects = dynamicObjects;
    }
}
