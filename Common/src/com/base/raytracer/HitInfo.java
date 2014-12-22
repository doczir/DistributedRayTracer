package com.base.raytracer;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class HitInfo {

    public static final HitInfo NO_HIT = new HitInfo(false, 0);

    private boolean intersect;
    private double t;

    public HitInfo(double t) {
        this(true, t);
    }

    private HitInfo(boolean intersect, double t) {
        this.intersect = intersect;
        this.t = t;
    }

    public boolean isIntersect() {
        return intersect;
    }

    public double getT() {
        return t;
    }
}
