package com.base.raytracer.materials;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.27.
 */
public class GlassMaterial extends Material {

    private HDRColor color;
    private double   ior;

    public GlassMaterial(HDRColor color, float ior) {
        this.color = color;
        this.ior = ior;
    }

    @Override
    public HDRColor getColor() {
        return color;
    }

    @Override
    public HDRColor getEmmission() {
        return new HDRColor();
    }

    public double getIor() {
        return ior;
    }
}
