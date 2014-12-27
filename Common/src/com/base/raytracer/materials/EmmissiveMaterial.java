package com.base.raytracer.materials;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.23.
 */
public class EmmissiveMaterial extends Material {
    private HDRColor color;

    public EmmissiveMaterial(HDRColor color) {
        this.color = color;
    }

    @Override
    public HDRColor getColor() {
        return new HDRColor();
    }

    @Override
    public HDRColor getEmmission() {
        return color;
    }
}
