package com.base.raytracer.primitives;

import com.base.raytracer.HitInfo;
import com.base.raytracer.math.AABB;
import com.base.raytracer.math.Transform;
import com.base.raytracer.math.Vector3;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Sphere extends Primitive {

    private static double EPSILON = 1e-4;

    private double rad;

    public Sphere(Vector3 v0, double rad) {
        this.transform = new Transform();
        this.transform.setPos(v0);
        this.rad = rad;
    }

    public double getRadius() {
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

        if (det < 0) return HitInfo.NO_HIT;
        else det = Math.sqrt(det);

        t = b - det;
        if (t > EPSILON)
            return new HitInfo(t);
        t = b + det;
        if (t > EPSILON)
            return new HitInfo(t);

        return HitInfo.NO_HIT;
    }
}
