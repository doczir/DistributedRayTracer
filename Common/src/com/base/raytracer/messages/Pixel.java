package com.base.raytracer.messages;

import com.base.raytracer.math.HDRColor;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Pixel implements Serializable {

    private UUID id;

    private int      idx;
    private HDRColor val;

    public Pixel(UUID id, int idx, HDRColor val) {
        this.id = id;

        this.idx = idx;
        this.val = val;
    }

    public UUID getId() {
        return id;
    }

    public int getIdx() {
        return idx;
    }

    public HDRColor getVal() {
        return val;
    }
}
