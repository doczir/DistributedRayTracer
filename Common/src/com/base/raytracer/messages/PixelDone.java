package com.base.raytracer.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class PixelDone implements Serializable {

    private List<Pixel> pixels;

    public PixelDone(List<Pixel> pixels) {
        this.pixels = new ArrayList<>(pixels);
    }

    public void add(Pixel pixel) {
        pixels.add(pixel);
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

}
