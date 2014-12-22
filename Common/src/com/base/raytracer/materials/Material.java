package com.base.raytracer.materials;

import com.base.raytracer.math.HDRColor;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public abstract class Material implements Serializable {
    public abstract HDRColor getColor();
}
