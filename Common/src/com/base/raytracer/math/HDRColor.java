package com.base.raytracer.math;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

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
        checkArgument(r >= 0, "Red component must be greater than or equal to 0.");
        checkArgument(g >= 0, "Green component must be greater than or equal to  0.");
        checkArgument(b >= 0, "Blue component must be greater than or equal to  0.");
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
}
