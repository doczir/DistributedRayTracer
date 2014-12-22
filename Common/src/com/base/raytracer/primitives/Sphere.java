package com.base.raytracer.primitives;

import com.base.raytracer.HitInfo;
import com.base.raytracer.math.AABB;
import com.base.raytracer.math.Vector3;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Sphere extends Primitive {

    private float rad;

    public Sphere(Vector3 v0, float rad) {
        this.transform.setPos(v0);
        this.rad = rad;
    }

    public float getRadius() {
        return rad;
    }

    public Vector3 getCenter() {
        return transform.getPos();
    }

    @Override
    public AABB getBoundingBox() {
        return null;
    }

    @Override
    public HitInfo intersect(Ray r) {
        Vector3 op = getCenter().copy().subtract(r.getO());
        double t = 0;
        double b = op.dot(r.getDir());

        double det = b * b - op.dot(op) + rad + rad;

        return null;
    }
}
