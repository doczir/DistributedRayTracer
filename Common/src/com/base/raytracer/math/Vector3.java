package com.base.raytracer.math;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Vector3 {
    public static final Vector3 ZERO   = new Vector3(0, 0, 0);
    public static final Vector3 AXIS_X = new Vector3(1, 0, 0);
    public static final Vector3 AXIS_Y = new Vector3(0, 1, 0);
    public static final Vector3 AXIS_Z = new Vector3(0, 0, 1);

    public double x, y, z;

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(Vector2 v, double z) {
        this(v.getX(), v.getY(), z);
    }

    public Vector3(double x, Vector2 v) {
        this(x, v.getX(), v.getY());
    }

    public Vector3(Vector3 v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(double x, double y, double z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 add(Vector3 v) {
        return add(v.x, v.y, v.z);
    }

    public Vector3 subtract(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public Vector3 subtract(Vector3 v) {
        return add(-v.x, -v.y, -v.z);
    }

    public Vector3 scale(double sx, double sy, double sz) {
        return new Vector3(x * sx, y * sy, z * sz);
    }

    public Vector3 scale(double s) {
        return scale(s, s, s);
    }

    public Vector3 cross(Vector3 v) {
        return cross(v.x, v.y, v.z);
    }

    public Vector3 cross(double vx, double vy, double vz) {
        double x = this.x * vz - this.z * vy;
        double y = this.z * vx - this.x * vz;
        double z = this.x * vy - this.y * vx;

        return new Vector3(x, y, z);
    }

    public Vector3 normalize() {
        double l = length();

        return new Vector3(x / l, y / l, z / l);
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public double dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return (double) Math.sqrt(lengthSquared());
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public double getX() {
        return x;
    }

    public Vector3 setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Vector3 setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public Vector3 setZ(double z) {
        this.z = z;
        return this;
    }

    public Vector3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    /* Swizzling */
    public double getR() {
        return x;
    }

    public Vector3 setR(double r) {
        x = r;
        return this;
    }

    public double getG() {
        return y;
    }

    public Vector3 setG(double g) {
        y = g;
        return this;
    }

    public double getB() {
        return z;
    }

    public Vector3 setB(double b) {
        z = b;
        return this;
    }

    public Vector2 getXY() {
        return new Vector2(x, y);
    }

    public Vector2 getYZ() {
        return new Vector2(y, z);
    }

    public Vector2 getXZ() {
        return new Vector2(x, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 vector3 = (Vector3) o;

        if (Double.compare(vector3.x, x) != 0) return false;
        if (Double.compare(vector3.y, y) != 0) return false;
        if (Double.compare(vector3.z, z) != 0) return false;

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
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
