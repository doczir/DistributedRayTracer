package com.base.raytracer.materials;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class DiffuseMaterial extends Material {

    private HDRColor color;

    public DiffuseMaterial(HDRColor color) {
        this.color = color;
    }

    @Override
    public HDRColor getColor() {
        return color;
    }
}
