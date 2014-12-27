package com.base.raytracer.math;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class HDRColor implements Serializable {

    private float r, g, b;

    public HDRColor() {
        this(0, 0, 0);
    }

    public HDRColor(float r, float g, float b) {
//        checkArgument(r >= 0, "Red component must be greater than or equal to 0.");
//        checkArgument(g >= 0, "Green component must be greater than or equal to  0.");
//        checkArgument(b >= 0, "Blue component must be greater than or equal to  0.");
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float[] getValue() {
        return new float[]{r, g, b};
    }

    public float getMax() {
        return r > g && r > b ? r : g > b ? g : b;
    }

    public HDRColor add(HDRColor other) {
        return new HDRColor(r + other.r, g + other.g, b + other.b);
    }

    public HDRColor mult(HDRColor other) {
        return new HDRColor(r * other.r, g * other.g, b * other.b);
    }

    public HDRColor div(HDRColor other) {
        return new HDRColor(r / other.r, g / other.g, b / other.b);
    }

    public HDRColor scale(float v) {
        return new HDRColor(r * v, g * v, b * v);
    }

    public HDRColor add(float v) {
        return new HDRColor(r + v, g + v, b + v);
    }

    public HDRColor reinhart(float exposure) {
        HDRColor ret = scale(exposure);
        return ret.div(ret.add(1));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("r", r)
                .add("g", g)
                .add("b", b)
                .toString();
    }
}
