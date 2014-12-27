package com.base.raytracer.materials;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.27.
 */
public class GlossyMaterial extends Material {

    private HDRColor color;

    public GlossyMaterial(HDRColor color) {
        this.color = color;
    }

    @Override
    public HDRColor getColor() {
        return color;
    }

    @Override
    public HDRColor getEmmission() {
        return new HDRColor();
    }
}
