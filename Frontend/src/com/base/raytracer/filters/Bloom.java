package com.base.raytracer.filters;

import com.base.raytracer.math.HDRColor;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.28.
 */
public class Bloom implements Filter {

    private final float bloomIntensity;
    private final float originalIntensity;
    private final float bloomSaturation;
    private final float originalSaturation;
    private       float threshold;

    public Bloom(float threshold, float bloomIntensity, float originalIntensity, float bloomSaturation, float originalSaturation) {
        this.threshold = threshold;
        this.bloomIntensity = bloomIntensity;
        this.originalIntensity = originalIntensity;
        this.bloomSaturation = bloomSaturation;
        this.originalSaturation = originalSaturation;
    }

    @Override
    public void apply(HDRColor[][] hdrRaster) {
        HDRColor[][] brightAreas = brightPass(hdrRaster);

        Blur blur = new Blur(0.002f);

        blur.apply(brightAreas);
        blur.apply(brightAreas);
        blur.apply(brightAreas);
        blur.apply(brightAreas);
        blur.apply(brightAreas);
        blur.apply(brightAreas);

        float max = getMaxLum(hdrRaster);

        for (int x = 0; x < hdrRaster.length; x++) {
            for (int y = 0; y < hdrRaster[x].length; y++) {
                HDRColor bloomColor = brightAreas[x][y];
                HDRColor originalColor = hdrRaster[x][y];

                bloomColor = adjustSaturation(bloomColor, bloomSaturation).scale(bloomIntensity);
                originalColor = adjustSaturation(originalColor, originalSaturation).scale(originalIntensity);

                originalColor = originalColor.mult(new HDRColor(max).sub(bloomColor).scale(1 / max));

                hdrRaster[x][y] = originalColor.add(bloomColor);
            }
        }
    }

    private HDRColor adjustSaturation(HDRColor color, float saturation) {
        float grey = color.getR() * 0.3f + color.getG() * 0.59f + color.getB() * 0.11f;

        return HDRColor.lerp(new HDRColor(grey), color, saturation);
    }

    private HDRColor[][] brightPass(HDRColor[][] hdrRaster) {
        final HDRColor[][] ret = new HDRColor[hdrRaster.length][hdrRaster[0].length];

        float max = getMaxLum(hdrRaster);

        for (int x = 0; x < hdrRaster.length; x++) {
            for (int y = 0; y < hdrRaster[x].length; y++) {
                HDRColor color = hdrRaster[x][y];

                ret[x][y] = color.sub(threshold * max).scale(1.0f / ((1 - threshold) * max)).clamp(0, max);
            }
        }
        return ret;
    }

    private float getMaxLum(HDRColor[][] hdrRaster) {
        float max = 0;

        for (int x = 0; x < hdrRaster.length; x++) {
            for (int y = 0; y < hdrRaster[x].length; y++) {
                float tmp = hdrRaster[x][y].getMax();
                if (tmp > max)
                    max = tmp;
            }
        }
        return max;
    }


}
