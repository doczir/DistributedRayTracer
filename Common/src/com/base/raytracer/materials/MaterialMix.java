package com.base.raytracer.materials;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class MaterialMix extends Material {

    private Material in1;
    private Material in2;
    private Operator op;

    public MaterialMix(Material in1, Material in2, Operator op) {
        this.in1 = in1;
        this.in2 = in2;
        this.op = op;
    }

    @Override
    public HDRColor getColor() {
        HDRColor color1 = in1.getColor();
        HDRColor color2 = in2.getColor();
        switch (op) {
            case ADD:
                return add(color1, color2);
            case MIX:
                return mix(color1, color2);
            case MUL:
                return mul(color1, color2);
            default:
                return new HDRColor();
        }
    }

    private HDRColor add(HDRColor c1, HDRColor c2) {
        return c1.add(c2);
    }

    private HDRColor mix(HDRColor c1, HDRColor c2) {
        return new HDRColor((c1.getR() + c2.getR()) / 2,
                (c1.getG() + c2.getG()) / 2,
                (c1.getB() + c2.getB()) / 2);
    }

    private HDRColor mul(HDRColor c1, HDRColor c2) {
        return new HDRColor(c1.getR() * c2.getR(),
                c1.getG() * c2.getG(),
                c1.getB() * c2.getB());
    }

    public static enum Operator {
        ADD,
        MIX,
        MUL
    }
}
