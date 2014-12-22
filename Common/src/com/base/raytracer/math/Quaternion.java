package com.base.raytracer.math;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Quaternion {
    public double x;
    public double y;
    public double z;
    public double w;

    public Quaternion() {
        this(0, 0, 0, 1);
    }

    public Quaternion(Vector3 axis, double angle) {
        angle = Math.toRadians(angle) * 0.5f;
        axis = axis.normalize();

        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        x = axis.x * sinAngle;
        y = axis.y * sinAngle;
        z = axis.z * sinAngle;
        w = cosAngle;
    }

    public Quaternion(double pitch, double yaw, double roll) {
        pitch = Math.toRadians(pitch) * 0.5f;
        yaw = Math.toRadians(yaw) * 0.5f;
        roll = Math.toRadians(roll) * 0.5f;

        double sinP = Math.sin(pitch);
        double sinY = Math.sin(yaw);
        double sinR = Math.sin(roll);
        double cosP = Math.cos(pitch);
        double cosY = Math.cos(yaw);
        double cosR = Math.cos(roll);

        x = sinR * cosP * cosY - cosR * sinP * sinY;
        y = cosR * sinP * cosY + sinR * cosP * sinY;
        z = cosR * cosP * sinY - sinR * sinP * cosY;
        w = cosR * cosP * cosY + sinR * sinP * sinY;
    }

    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion add(double x, double y, double z, double w) {
        return new Quaternion(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion add(Quaternion q) {
        return add(q.x, q.y, q.z, q.w);
    }

    public Quaternion subtract(double x, double y, double z, double w) {
        return add(-x, -y, -z, -w);
    }

    public Quaternion subtract(Quaternion q) {
        return subtract(q.x, q.y, q.z, q.w);
    }

    public Quaternion normalize() {
        double length = length();

        return new Quaternion(x / length, y / length, z / length, w / length);
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion multiply(Quaternion q) {
        double nx = w * q.x + x * q.w + y * q.z - z * q.y;
        double ny = w * q.y + y * q.w + z * q.x - x * q.z;
        double nz = w * q.z + z * q.w + x * q.y - y * q.x;
        double nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return new Quaternion(nx, ny, nz, nw).normalize();
    }

    public Vector3 multiply(Vector3 v) {
        Vector3 vn = v.normalize();

        Quaternion q1 = conjugate();
        Quaternion qv = new Quaternion(vn.x, vn.y, vn.z, 1);

        qv = this.multiply(qv);
        qv = qv.multiply(q1);

        return new Vector3(qv.x, qv.y, qv.z);
    }

    public Quaternion copy() {
        return new Quaternion(x, y, z, w);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }
}
