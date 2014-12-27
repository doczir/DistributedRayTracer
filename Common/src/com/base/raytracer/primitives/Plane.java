package com.base.raytracer.primitives;

import com.base.raytracer.HitInfo;
import com.base.raytracer.materials.Material;
import com.base.raytracer.math.AABB;
import com.base.raytracer.math.Vector3;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.26.
 */
public class Plane extends Primitive {

    private static final double EPSILON = 1e-4;
    private final Vector3 p;
    private final Vector3 n;

    public Plane(Material material, Vector3 p, Vector3 n) {
        super(material);
        this.p = p;
        this.n = n;
    }

    @Override
    public AABB getBoundingBox() {
        return null;
    }

    @Override
    public HitInfo intersect(Ray r) {
        double ddotn = r.getDir().dot(n);
        if (Math.abs(ddotn) < EPSILON)
            return HitInfo.NO_HIT;

        double k = p.dot(n);
        double t = (k - r.getO().dot(n)) / ddotn;

        if (t < 0) return HitInfo.NO_HIT;

        return new HitInfo(t, this, r.getO().add(r.getDir().scale(t)), n, n);
    }
}
