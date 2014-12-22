package com.base.raytracer;

import com.base.raytracer.math.Vector2;
import com.base.raytracer.math.Vector3;
import com.base.raytracer.primitives.Ray;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Camera implements Serializable {

    private final int halfWidth;
    private final int halfHeight;
    private final double ar;
    Vector3 eyePos, planePos, right, up;

    public Camera(double fov, int width, int height, Vector3 eye, Vector3 target, Vector3 planeUp) {
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
        this.ar = (double) width / height;
        this.eyePos = eye.copy();
        this.planePos = eye.add(target.subtract(eye).normalize());

        Vector3 fwd = planePos.subtract(eye).normalize();
        double planeHalfTan = Math.tan((fov * (Math.PI / 180)) / 2);

        right = fwd.cross(planeUp).normalize().scale(planeHalfTan);
        up = right.cross(fwd).normalize().scale(planeHalfTan);
    }


    public Ray getRay(Vector2 pos) {
        Vector2 posOnPlane = new Vector2(
                (pos.x - halfWidth) / halfWidth,
                (pos.y - halfHeight) / halfHeight);

        Vector3 planeIntersection = planePos.add(right.scale(posOnPlane.x).scale(ar)).add(up.scale(posOnPlane.y));

        return new Ray(planeIntersection.subtract(eyePos).normalize(), eyePos);
    }
}
