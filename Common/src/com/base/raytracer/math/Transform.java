package com.base.raytracer.math;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Transform implements Serializable {

    Vector3    pos;
    Quaternion rot;
    Vector3    scl;

    public Transform() {
        this(new Vector3(), new Quaternion(), new Vector3());
    }

    public Transform(Vector3 pos, Quaternion rot, Vector3 scl) {
        this.pos = pos;
        this.rot = rot;
        this.scl = scl;
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public Quaternion getRot() {
        return rot;
    }

    public void setRot(Quaternion rot) {
        this.rot = rot;
    }

    public Vector3 getScl() {
        return scl;
    }

    public void setScl(Vector3 scl) {
        this.scl = scl;
    }

    public Matrix4 getTransform() {
        Matrix4 translation = TransformUtils.createTranslation(pos);
        Matrix4 rotation = TransformUtils.createRotation(rot);
        Matrix4 scaling = TransformUtils.createScaling(scl);

        return translation.multiply(rotation.multiply(scaling));
    }
}
