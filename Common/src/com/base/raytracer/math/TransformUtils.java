package com.base.raytracer.math;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public final class TransformUtils {
    private static Matrix4 tempMat = new Matrix4();

    private TransformUtils() {
    }

    public static Matrix4 createTranslation(Vector3 translation) {
        Matrix4 result = tempMat.initIdentity();

        result.set(3, 0, translation.getX())
                .set(3, 1, translation.getY())
                .set(3, 2, translation.getZ());

        return result;
    }

    public static Matrix4 createScaling(Vector3 scale) {
        Matrix4 result = tempMat.initIdentity();

        result.set(0, 0, scale.getX())
                .set(1, 1, scale.getY())
                .set(2, 2, scale.getZ());

        return result;
    }

    public static Matrix4 createRotation(Vector3 axis, float angle) {
        assert axis != Vector3.ZERO;

        Matrix4 result = tempMat.initIdentity();

        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);

        Vector3 v = axis.normalize();

        result.set(0, 0, v.getX() * v.getX() * (1 - c) + c)
                .set(1, 0, v.getX() * v.getY() * (1 - c) - v.getZ() * s)
                .set(2, 0, v.getX() * v.getZ() * (1 - c) + v.getY() * s);

        result.set(0, 1, v.getY() * v.getX() * (1 - c) + v.getZ() * s)
                .set(1, 1, v.getY() * v.getY() * (1 - c) + c)
                .set(2, 1, v.getY() * v.getZ() * (1 - c) - v.getX() * s);

        result.set(0, 2, v.getX() * v.getZ() * (1 - c) - v.getY() * s)
                .set(1, 2, v.getY() * v.getZ() * (1 - c) + v.getX() * s)
                .set(2, 2, v.getZ() * v.getZ() * (1 - c) + c);

        return result;
    }

    public static Matrix4 createOrtho2d(float left, float right, float bottom, float top, float zNear, float zFar) {
        Matrix4 result = tempMat.initZero();

        result.set(0, 0, 2 / (right - left))
                .set(1, 1, 2 / (top - bottom))
                .set(2, 2, -2 / (zFar - zNear))
                .set(3, 0, -(right + left) / (right - left))
                .set(3, 1, -(top + bottom) / (top - bottom))
                .set(3, 2, -(zFar + zNear) / (zFar - zNear))
                .set(3, 3, 1);

        return result;
    }

    public static Matrix4 createFrustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        assert zFar > zNear;

        Matrix4 result = tempMat.initZero();

        result.set(0, 0, (2 * zNear) / (right - left))
                .set(1, 1, (2 * zNear) / (top - bottom))
                .set(2, 0, (right + left) / (right - left))
                .set(2, 1, (top + bottom) / (top - bottom))
                .set(2, 2, (zFar + zNear) / (zNear - zFar))
                .set(2, 3, -1)
                .set(3, 2, (-2 * zFar * zNear) / (zFar - zNear));

        return result;
    }

    public static Matrix4 createPerspective(float fovy, float aspect, float zNear, float zFar) {
        assert zFar > zNear;

        Matrix4 result = tempMat.initZero();

        float tanHalfFovy = (float) Math.tan(Math.toRadians(fovy) / 2);

        result.set(0, 0, 1 / (aspect * tanHalfFovy))
                .set(1, 1, 1 / tanHalfFovy)
                .set(2, 2, (zFar + zNear) / (zNear - zFar))
                .set(2, 3, -1)
                .set(3, 2, (-2 * zFar * zNear) / (zFar - zNear));

        return result;
    }

//    public static Matrix4 createLookAt(Vector3 eye, Vector3 center, Vector3 up)
//    {
//        Matrix4 result = tempMat.initIdentity();
//
//        final Vector3 f = center.subtract(eye).normalize();
//        final Vector3 s = f.cross(up).normalize();
//        final Vector3 u = s.cross(f);
//
//        result.set(0, 0, s.x)
//              .set(1, 0, s.y)
//              .set(2, 0, s.z);
//
//        result.set(0, 1, u.x)
//              .set(1, 1, u.y)
//              .set(2, 1, u.z);
//
//        result.set(0, 2, -f.x)
//              .set(1, 2, -f.y)
//              .set(2, 2, -f.z);
//
//        result.set(3, 0, -s.dot(eye))
//              .set(3, 1, -u.dot(eye))
//              .set(3, 2, f.dot(eye));
//
//        return result;
//    }

    public static Matrix4 createRotation(Quaternion q) {
        q = q.normalize();

        Matrix4 result = tempMat.initIdentity();

        double x2 = q.x * q.x;
        double y2 = q.y * q.y;
        double z2 = q.z * q.z;
        double xy = q.x * q.y;
        double xz = q.x * q.z;
        double yz = q.y * q.z;
        double wx = q.w * q.x;
        double wy = q.w * q.y;
        double wz = q.w * q.z;

        result.set(0, 0, 1.0f - 2.0f * (y2 + z2))
                .set(0, 1, 2.0f * (xy - wz))
                .set(0, 2, 2.0f * (xz + wy));

        result.set(1, 0, 2.0f * (xy + wz))
                .set(1, 1, 1.0f - 2.0f * (x2 + z2))
                .set(1, 2, 2.0f * (yz - wx));

        result.set(2, 0, 2.0f * (xz - wy))
                .set(2, 1, 2.0f * (yz + wx))
                .set(2, 2, 1.0f - 2.0f * (x2 + y2));

        return result;
    }
}
