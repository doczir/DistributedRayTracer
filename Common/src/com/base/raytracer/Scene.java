package com.base.raytracer;

import com.base.raytracer.math.HDRColor;
import com.base.raytracer.primitives.Primitive;
import com.base.raytracer.primitives.Ray;
import com.base.raytracer.primitives.Sphere;

import java.io.Serializable;
import java.util.List;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public abstract class Scene implements Serializable {

    private Camera   camera;
    private HDRColor ambient;
    private int      samples;
    private boolean  done;
    private float exposure;

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public abstract HitInfo intersect(Ray r);

    public abstract int getPrimitiveCount();

    public abstract void addPrimitive(Primitive p);

    public abstract void addLight(Sphere p);

    public abstract List<Primitive> getLights();

    public HDRColor getAmbient() {
        return ambient;
    }

    public void setAmbient(HDRColor ambient) {
        this.ambient = ambient;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }
}
