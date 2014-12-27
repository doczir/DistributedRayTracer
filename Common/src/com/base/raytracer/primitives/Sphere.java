package com.base.raytracer.primitives;

import com.base.raytracer.HitInfo;
import com.base.raytracer.materials.Material;
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

    public Sphere(Material material, Vector3 v0, double rad) {
        super(material);
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
        Vector3 op = getCenter().subtract(r.getO());
        double t;
        double b = op.dot(r.getDir());

        double det = b * b - op.dot(op) + rad * rad;

        if (det < 0) return HitInfo.NO_HIT;
        else det = Math.sqrt(det);

        t = b - det;
        if (t > EPSILON) {
            return calcHitInfo(r, t);
        }
        t = b + det;
        if (t > EPSILON) {
            return calcHitInfo(r, t);
        }

        return HitInfo.NO_HIT;
    }

    private HitInfo calcHitInfo(Ray r, double t) {
        Vector3 hitPoint = r.getO().add(r.getDir().normalize().scale(t));
        Vector3 normal = hitPoint.subtract(getCenter()).normalize();
        Vector3 orientedNormal = normal.dot(r.getDir()) < 0 ? normal : normal.scale(-1);
        return new HitInfo(t, this, hitPoint, normal, orientedNormal);
    }
}
