package com.base.raytracer;

import com.base.raytracer.primitives.Primitive;
import com.base.raytracer.primitives.Ray;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public abstract class Scene implements Serializable {

    public abstract HitInfo intersect(Ray r);

    public abstract void addPrimitive(Primitive p);
}
