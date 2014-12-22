package com.base.raytracer;

import com.base.raytracer.primitives.Primitive;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class HitInfo {

    public static final HitInfo NO_HIT = new HitInfo(false, 0, null);

    private boolean   intersect;
    private double    t;
    private Primitive primitive;

    public HitInfo(double t, Primitive primitive) {
        this(true, t, primitive);
    }

    private HitInfo(boolean intersect, double t, Primitive primitive) {
        this.intersect = intersect;
        this.t = t;
        this.primitive = primitive;
    }

    public Primitive getPrimitive() {
        return primitive;
    }

    public boolean isIntersect() {
        return intersect;
    }

    public double getT() {
        return t;
    }
}
