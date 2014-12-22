package com.base.raytracer;

import com.base.raytracer.primitives.Primitive;
import com.base.raytracer.primitives.Ray;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public abstract class Scene implements Serializable {

    private Camera camera;

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public abstract HitInfo intersect(Ray r);

    public abstract void addPrimitive(Primitive p);
}
