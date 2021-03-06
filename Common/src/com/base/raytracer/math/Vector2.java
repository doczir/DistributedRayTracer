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

    public double x, y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public double length() {
        return (double) Math.sqrt(lengthSquared());
    }

    public Vector2 copy() {
        return new Vector2(this);
    }

    public Vector2 add(double x, double y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 add(Vector2 v) {
        return add(v.x, v.y);
    }

    public Vector2 subtract(double x, double y) {
        return add(-x, -y);
    }

    public Vector2 subtract(Vector2 v) {
        return add(-v.x, -v.y);
    }

    public Vector2 scale(double s) {
        return scale(s, s);
    }

    public Vector2 scale(double sx, double sy) {
        return new Vector2(x * sx, y * sy);
    }

    public double dot(Vector2 v) {
        return dot(v.x, v.y);
    }

    public double dot(double x, double y) {
        return this.x * x + this.y * y;
    }

    public Vector2 normalize() {
        double l = length();

        return new Vector2(x / l, y / l);
    }

    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2 = (Vector2) o;

        if (Double.compare(vector2.x, x) != 0) return false;
        if (Double.compare(vector2.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
