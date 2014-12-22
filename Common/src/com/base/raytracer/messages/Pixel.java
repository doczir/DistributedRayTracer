package com.base.raytracer.messages;

import com.base.raytracer.math.HDRColor;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Pixel implements Serializable {
    private int      idx;
    private HDRColor val;

    public Pixel(int idx, HDRColor val) {
        this.idx = idx;
        this.val = val;
    }

    public int getIdx() {
        return idx;
    }

    public HDRColor getVal() {
        return val;
    }
}
