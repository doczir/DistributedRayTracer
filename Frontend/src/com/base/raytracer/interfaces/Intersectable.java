package com.base.raytracer.interfaces;

import com.base.raytracer.HitInfo;
import com.base.raytracer.primitives.Ray;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public interface Intersectable {

    public HitInfo intersect(Ray r);

}
