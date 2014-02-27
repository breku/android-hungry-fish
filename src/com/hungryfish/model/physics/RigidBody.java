package com.hungryfish.model.physics;

import java.util.List;
import java.util.Map;

/**
 * User: Breku
 * Date: 26.02.14
 */
public class RigidBody {

    private String name;
    private String imagePath;
    private Origin origin;
    private Map<String,Float> [][] polygons;
    private List<Circle> circles;
    private List<Shape> shapes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public void setCircles(List<Circle> circles) {
        this.circles = circles;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public Map<String, Float>[][] getPolygons() {
        return polygons;
    }

    public void setPolygons(Map<String, Float>[][] polygons) {
        this.polygons = polygons;
    }
}
