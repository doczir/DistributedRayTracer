package com.base.raytracer.math;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Vector2 implements Serializable {
    public static final Vector2 ZERO   = new Vector2(0, 0);
    public static final Vector2 AXIS_X = new Vector2(1, 0);
    public static final Vector2 AXIS_Y = new Vector2(0, 1);

    public float x, y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public Vector2 copy() {
        return new Vector2(this);
    }

    public Vector2 add(float x, float y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 v) {
        return add(v.x, v.y);
    }

    public Vector2 subtract(float x, float y) {
        return add(-x, -y);
    }

    public Vector2 subtract(Vector2 v) {
        return add(-v.x, -v.y);
    }

    public Vector2 scale(float s) {
        return scale(s, s);
    }

    public Vector2 scale(float sx, float sy) {
        return new Vector2(x * sx, y * sy);
    }

    public float dot(Vector2 v) {
        return dot(v.x, v.y);
    }

    public float dot(float x, float y) {
        return this.x * x + this.y * y;
    }

    public Vector2 normalize() {
        float l = length();

        return new Vector2(x / l, y / l);
    }

    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        if (Float.compare(vector2.x, x) != 0) return false;
        if (Float.compare(vector2.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
