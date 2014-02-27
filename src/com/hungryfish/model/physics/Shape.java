package com.hungryfish.model.physics;

import com.badlogic.gdx.math.Vector2;

/**
 * User: Breku
 * Date: 26.02.14
 */
public class Shape {


    private String type;
    private Vector2[] vertices;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector2[] vertices) {
        this.vertices = vertices;
    }
}
