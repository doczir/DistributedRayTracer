package com.base.raytracer;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class HitInfo {
    private boolean intersect;
    private float   t;

    public HitInfo(boolean intersect, float t) {
        this.intersect = intersect;
        this.t = t;
    }

    public boolean isIntersect() {
        return intersect;
    }

    public float getT() {
        return t;
    }
}
