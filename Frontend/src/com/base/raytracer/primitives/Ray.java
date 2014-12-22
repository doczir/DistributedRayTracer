package com.base.raytracer.primitives;

import com.base.raytracer.math.Vector3;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Ray {

    Vector3 dir;
    Vector3 v0;

    public Ray(Vector3 dir, Vector3 v0) {
        this.dir = dir;
        this.v0 = v0;
    }

    public Vector3 getDir() {
        return dir;
    }

    public Vector3 getV0() {
        return v0;
    }
}
