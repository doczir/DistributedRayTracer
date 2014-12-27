package com.base.raytracer;

import com.base.raytracer.math.Vector3;
import com.base.raytracer.primitives.Primitive;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class HitInfo {

    public static final HitInfo NO_HIT = new HitInfo(false, Double.MAX_VALUE, null, null, null, null);

    private boolean intersect;
    private double  t;
    private Vector3 hitPoint;
    private Vector3 normal;
    private Vector3 orientedNormal;

    private Primitive primitive;

    public HitInfo(double t, Primitive primitive, Vector3 hitPoint, Vector3 normal, Vector3 orientedNormal) {
        this(true, t, hitPoint, normal, orientedNormal, primitive);
    }

    private HitInfo(boolean intersect, double t, Vector3 hitPoint, Vector3 normal, Vector3 orientedNormal, Primitive primitive) {
        this.intersect = intersect;
        this.t = t;
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.orientedNormal = orientedNormal;
        this.primitive = primitive;
    }

    public Primitive getPrimitive() {
        return primitive;
    }

    public Vector3 getHitPoint() {
        return hitPoint;
    }

    public boolean isIntersect() {
        return intersect;
    }

    public double getT() {
        return t;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public Vector3 getOrientedNormal() {
        return orientedNormal;
    }
}
