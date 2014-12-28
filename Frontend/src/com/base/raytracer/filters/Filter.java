package com.base.raytracer.filters;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.28.
 */
public interface Filter {


    public void apply(HDRColor[][] hdrRaster);
}
