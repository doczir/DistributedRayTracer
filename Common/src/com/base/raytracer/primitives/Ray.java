package com.base.raytracer.primitives;

import com.base.raytracer.math.Vector3;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Ray {

    private Vector3 dir;
    private Vector3 o;

    public Ray(Vector3 dir, Vector3 o) {
        this.dir = dir;
        this.o = o;
    }

    public Vector3 getDir() {
        return dir;
    }

    public Vector3 getO() {
        return o;
    }
}
