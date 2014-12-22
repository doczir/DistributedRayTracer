package com.base.raytracer.primitives;

import com.base.raytracer.HitInfo;
import com.base.raytracer.math.AABB;
import com.base.raytracer.math.Vector3;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Sphere extends Primitive {

    private float r;

    public Sphere(Vector3 v0, float r) {
        this.transform.setPos(v0);
        this.r = r;
    }

    public float getRadius() {
        return r;
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
        Vector3 v0 = r.getV0();
        Vector3 c = getCenter();
        Vector3 vpc = c.copy().subtract(v0);


        return null;
    }
}
