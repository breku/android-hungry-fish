package com.hungryfish.model.physics;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * User: Breku
 * Date: 26.02.14
 */
public class Shape {


    private String type;
    private List<Vector2> vertices;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Vector2> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vector2> vertices) {
        this.vertices = vertices;
    }
}
