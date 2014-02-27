package com.hungryfish.model.physics;

import java.util.List;

/**
 * User: Breku
 * Date: 26.02.14
 */
public class Shape {


    private String type;
    private List<Vertice> vertices;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertice> vertices) {
        this.vertices = vertices;
    }
}
