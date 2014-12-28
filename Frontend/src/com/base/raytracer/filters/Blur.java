package com.base.raytracer.filters;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.28.
 */
public class Blur implements Filter {

    float blurDistance;

    public Blur(float blurDistance) {
        this.blurDistance = blurDistance;
    }

    @Override
    public void apply(HDRColor[][] hdrRaster) {
        int blurDistanceInPixel = (int) (hdrRaster.length * blurDistance);

        for (int x = 0; x < hdrRaster.length; x++) {
            for (int y = 0; y < hdrRaster[x].length; y++) {
                HDRColor color = hdrRaster[mod((x + blurDistanceInPixel), hdrRaster.length)][mod((y + blurDistanceInPixel), hdrRaster[0].length)];
                color = color.add(hdrRaster[mod((x - blurDistanceInPixel), hdrRaster.length)][mod((y - blurDistanceInPixel), hdrRaster[0].length)]);
                color = color.add(hdrRaster[mod((x + blurDistanceInPixel), hdrRaster.length)][mod((y - blurDistanceInPixel), hdrRaster[0].length)]);
                color = color.add(hdrRaster[mod((x - blurDistanceInPixel), hdrRaster.length)][mod((y + blurDistanceInPixel), hdrRaster[0].length)]);

                color = color.scale(1f / 4f);
                hdrRaster[x][y] = color;
            }
        }
    }

    private int mod(int x, int n) {
        int r = x % n;
        if (r < 0) {
            r += n;
        }
        return r;
    }
}
