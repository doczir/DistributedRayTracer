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

    public float x, y, z;

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(Vector2 v, float z) {
        this(v.getX(), v.getY(), z);
    }

    public Vector3(float x, Vector2 v) {
        this(x, v.getX(), v.getY());
    }

    public Vector3(Vector3 v) {
        this(v.getX(), v.getY(), v.getZ());
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 add(Vector3 v) {
        return add(v.x, v.y, v.z);
    }

    public Vector3 subtract(float x, float y, float z) {
        return add(-x, -y, -z);
    }

    public Vector3 subtract(Vector3 v) {
        return add(-v.x, -v.y, -v.z);
    }

    public Vector3 scale(float sx, float sy, float sz) {
        return new Vector3(x * sx, y * sy, z * sz);
    }

    public Vector3 scale(float s) {
        return scale(s, s, s);
    }

    public Vector3 cross(Vector3 v) {
        return cross(v.x, v.y, v.z);
    }

    public Vector3 cross(float vx, float vy, float vz) {
        float x = this.x * vz - this.z * vy;
        float y = this.z * vx - this.x * vz;
        float z = this.x * vy - this.y * vx;

        return new Vector3(x, y, z);
    }

    public Vector3 normalize() {
        float l = length();

        return new Vector3(x / l, y / l, z / l);
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public float dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public float getX() {
        return x;
    }

    public Vector3 setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public Vector3 setY(float y) {
        this.y = y;
        return this;
    }

    public float getZ() {
        return z;
    }

    public Vector3 setZ(float z) {
        this.z = z;
        return this;
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    /* Swizzling */
    public float getR() {
        return x;
    }

    public Vector3 setR(float r) {
        x = r;
        return this;
    }

    public float getG() {
        return y;
    }

    public Vector3 setG(float g) {
        y = g;
        return this;
    }

    public float getB() {
        return z;
    }

    public Vector3 setB(float b) {
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

        if (Float.compare(vector3.x, x) != 0) return false;
        if (Float.compare(vector3.y, y) != 0) return false;
        if (Float.compare(vector3.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
