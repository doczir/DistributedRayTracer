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

    public HDRColor(float v) {
        this(v, v, v);
    }

    public HDRColor(float r, float g, float b) {
//        checkArgument(r >= 0, "Red component must be greater than or equal to 0.");
//        checkArgument(g >= 0, "Green component must be greater than or equal to  0.");
//        checkArgument(b >= 0, "Blue component must be greater than or equal to  0.");
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static HDRColor lerp(HDRColor c1, HDRColor c2, float t) {
        return new HDRColor(
                c1.r * (1 - t) + c2.r * t,
                c1.g * (1 - t) + c2.g * t,
                c1.b * (1 - t) + c2.b * t
        );
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

    public HDRColor add(float v) {
        return new HDRColor(r + v, g + v, b + v);
    }

    public HDRColor sub(HDRColor other) {
        return new HDRColor(r - other.r, g - other.g, b - other.b);
    }

    public HDRColor sub(float v) {
        return new HDRColor(r - v, g - v, b - v);
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

    public HDRColor reinhart(float exposure) {
        HDRColor ret = scale(exposure);
        return ret.div(ret.add(1f));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("r", r)
                .add("g", g)
                .add("b", b)
                .toString();
    }

    public HDRColor clamp(float min, float max) {
        return new HDRColor(
                MathUtils.clamp(r, min, max),
                MathUtils.clamp(g, min, max),
                MathUtils.clamp(b, min, max)
        );
    }
}
