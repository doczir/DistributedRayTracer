package com.base.raytracer.math;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Vector4 {
    public double x, y, z, w;

    public Vector4() {
        this(0, 0, 0, 0);
    }

    public Vector4(Vector2 v, double z, double w) {
        this(v.getX(), v.getY(), z, w);
    }

    public Vector4(double x, Vector2 v, double w) {
        this(x, v.getX(), v.getY(), w);
    }

    public Vector4(double x, double y, Vector2 v) {
        this(x, y, v.getX(), v.getY());
    }

    public Vector4(Vector3 v, double w) {
        this(v.getX(), v.getY(), v.getZ(), w);
    }

    public Vector4(double x, Vector3 v) {
        this(x, v.getX(), v.getY(), v.getZ());
    }

    public Vector4(Vector4 v) {
        this(v.x, v.y, v.z, v.w);
    }

    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4 add(double x, double y, double z, double w) {
        return new Vector4(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Vector4 add(Vector4 v) {
        return add(v.x, v.y, v.z, v.w);
    }

    public Vector4 subtract(double x, double y, double z, double w) {
        return add(-x, -y, -z, -w);
    }

    public Vector4 subtract(Vector4 v) {
        return add(-v.x, -v.y, -v.z, -v.w);
    }

    public Vector4 scale(double s) {
        return scale(s, s, s, s);
    }

    public Vector4 scale(double sx, double sy, double sz, double sw) {
        return new Vector4(x * sx, y * sy, z * sz, w * sw);
    }

    public double dot(Vector4 v) {
        return x * v.x + y * v.y + z * v.z + w * v.w;
    }

    public double lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public double length() {
        return (double) Math.sqrt(lengthSquared());
    }

    public Vector4 normalize() {
        double l = length();

        return new Vector4(x / l, y / l, z / l, w / l);
    }

    public Vector4 negate() {
        return new Vector4(-x, -y, -z, -w);
    }

    public Vector4 multiply(Vector4 v) {
        return scale(v.x, v.y, v.z, v.w);
    }

    public Vector4 copy() {
        return new Vector4(this);
    }

    public double getX() {
        return x;
    }

    public Vector4 setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Vector4 setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public Vector4 setZ(double z) {
        this.z = z;
        return this;
    }

    public double getW() {
        return w;
    }

    public Vector4 setW(double w) {
        this.w = w;
        return this;
    }

    /* Swizzling */
    public double getR() {
        return x;
    }

    public Vector4 setR(double r) {
        x = r;
        return this;
    }

    public double getG() {
        return y;
    }

    public Vector4 setG(double g) {
        y = g;
        return this;
    }

    public double getB() {
        return z;
    }

    public Vector4 setB(double b) {
        z = b;
        return this;
    }

    public double getA() {
        return w;
    }

    public Vector4 setA(double a) {
        w = a;
        return this;
    }

    public Vector2 getXY() {
        return new Vector2(x, y);
    }

    public Vector2 getYZ() {
        return new Vector2(y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector4 vector4 = (Vector4) o;

        if (Double.compare(vector4.w, w) != 0) return false;
        if (Double.compare(vector4.x, x) != 0) return false;
        if (Double.compare(vector4.y, y) != 0) return false;
        if (Double.compare(vector4.z, z) != 0) return false;

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
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(w);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]";
    }
}
