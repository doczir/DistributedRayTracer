package com.base.raytracer.messages;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class PixelDone implements Serializable {

    private ArrayList<Pixel> pixels;

    public PixelDone(ArrayList<Pixel> pixels) {
        this.pixels = new ArrayList<>(pixels);
    }

    public void add(Pixel pixel) {
        pixels.add(pixel);
    }

    public ArrayList<Pixel> getPixels() {
        return pixels;
    }

}
