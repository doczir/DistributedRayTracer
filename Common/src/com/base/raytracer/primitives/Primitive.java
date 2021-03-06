package com.base.raytracer.primitives;

import com.base.raytracer.interfaces.Intersectable;
import com.base.raytracer.materials.Material;
import com.base.raytracer.math.AABB;
import com.base.raytracer.math.Transform;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public abstract class Primitive implements Intersectable, Serializable {

    protected Transform transform;
    protected Material  material;

    protected Primitive(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Transform getTransform() {
        return transform;
    }

    public abstract AABB getBoundingBox();
}
