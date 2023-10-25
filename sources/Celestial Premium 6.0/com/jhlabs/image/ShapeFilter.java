/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class ShapeFilter
extends WholeImageFilter {
    public static final int LINEAR = 0;
    public static final int CIRCLE_UP = 1;
    public static final int CIRCLE_DOWN = 2;
    public static final int SMOOTH = 3;
    private float factor = 1.0f;
    protected Colormap colormap = new LinearColormap();
    private boolean useAlpha = true;
    private boolean invert = false;
    private boolean merge = false;
    private int type;
    private static final int one = 41;
    private static final int sqrt2 = (int)(41.0 * Math.sqrt(2.0));
    private static final int sqrt5 = (int)(41.0 * Math.sqrt(5.0));

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public float getFactor() {
        return this.factor;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    public void setUseAlpha(boolean useAlpha) {
        this.useAlpha = useAlpha;
    }

    public boolean getUseAlpha() {
        return this.useAlpha;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public boolean getInvert() {
        return this.invert;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public boolean getMerge() {
        return this.merge;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int[] map = new int[width * height];
        this.makeMap(inPixels, map, width, height);
        int max = this.distanceMap(map, width, height);
        this.applyMap(map, inPixels, width, height, max);
        return inPixels;
    }

    public int distanceMap(int[] map, int width, int height) {
        int v;
        int offset;
        int x;
        int y;
        int xmax = width - 3;
        int ymax = height - 3;
        int max = 0;
        for (y = 0; y < height; ++y) {
            for (x = 0; x < width; ++x) {
                offset = x + y * width;
                if (map[offset] <= 0 || (v = x < 2 || x > xmax || y < 2 || y > ymax ? this.setEdgeValue(x, y, map, width, offset, xmax, ymax) : this.setValue(map, width, offset)) <= max) continue;
                max = v;
            }
        }
        for (y = height - 1; y >= 0; --y) {
            for (x = width - 1; x >= 0; --x) {
                offset = x + y * width;
                if (map[offset] <= 0 || (v = x < 2 || x > xmax || y < 2 || y > ymax ? this.setEdgeValue(x, y, map, width, offset, xmax, ymax) : this.setValue(map, width, offset)) <= max) continue;
                max = v;
            }
        }
        return max;
    }

    private void makeMap(int[] pixels, int[] map, int width, int height) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int offset = x + y * width;
                int b = this.useAlpha ? pixels[offset] >> 24 & 0xFF : PixelUtils.brightness(pixels[offset]);
                map[offset] = b * 41 / 10;
            }
        }
    }

    private void applyMap(int[] map, int[] pixels, int width, int height, int max) {
        if (max == 0) {
            max = 1;
        }
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int offset = x + y * width;
                int m = map[offset];
                float v = 0.0f;
                int sa = 0;
                int sr = 0;
                int sg = 0;
                int sb = 0;
                if (m == 0) {
                    sb = 0;
                    sg = 0;
                    sr = 0;
                    sa = 0;
                    sa = pixels[offset] >> 24 & 0xFF;
                } else {
                    v = ImageMath.clamp(this.factor * (float)m / (float)max, 0.0f, 1.0f);
                    switch (this.type) {
                        case 1: {
                            v = ImageMath.circleUp(v);
                            break;
                        }
                        case 2: {
                            v = ImageMath.circleDown(v);
                            break;
                        }
                        case 3: {
                            v = ImageMath.smoothStep(0.0f, 1.0f, v);
                        }
                    }
                    if (this.colormap == null) {
                        sg = sb = (int)(v * 255.0f);
                        sr = sb;
                    } else {
                        int c = this.colormap.getColor(v);
                        sr = c >> 16 & 0xFF;
                        sg = c >> 8 & 0xFF;
                        sb = c & 0xFF;
                    }
                    int n = sa = this.useAlpha ? pixels[offset] >> 24 & 0xFF : PixelUtils.brightness(pixels[offset]);
                    if (this.invert) {
                        sr = 255 - sr;
                        sg = 255 - sg;
                        sb = 255 - sb;
                    }
                }
                if (this.merge) {
                    int transp = 255;
                    int col = pixels[offset];
                    int a = (col & 0xFF000000) >> 24;
                    int r = (col & 0xFF0000) >> 16;
                    int g = (col & 0xFF00) >> 8;
                    int b = col & 0xFF;
                    r = sr * r / transp;
                    g = sg * g / transp;
                    b = sb * b / transp;
                    if (r < 0) {
                        r = 0;
                    }
                    if (r > 255) {
                        r = 255;
                    }
                    if (g < 0) {
                        g = 0;
                    }
                    if (g > 255) {
                        g = 255;
                    }
                    if (b < 0) {
                        b = 0;
                    }
                    if (b > 255) {
                        b = 255;
                    }
                    pixels[offset] = a << 24 | r << 16 | g << 8 | b;
                    continue;
                }
                pixels[offset] = sa << 24 | sr << 16 | sg << 8 | sb;
            }
        }
    }

    private int setEdgeValue(int x, int y, int[] map, int width, int offset, int xmax, int ymax) {
        int r1 = offset - width - width - 2;
        int r2 = r1 + width;
        int r3 = r2 + width;
        int r4 = r3 + width;
        int r5 = r4 + width;
        if (y == 0 || x == 0 || y == ymax + 2 || x == xmax + 2) {
            map[offset] = 41;
            return 41;
        }
        int v = map[r3 + 1] + 41;
        v = map[r2 + 2] + 41;
        int min = v;
        if (v < min) {
            min = v;
        }
        if ((v = map[r3 + 3] + 41) < min) {
            min = v;
        }
        if ((v = map[r4 + 2] + 41) < min) {
            min = v;
        }
        if ((v = map[r2 + 1] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r2 + 3] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r4 + 1] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r4 + 3] + sqrt2) < min) {
            min = v;
        }
        if (y == 1 || x == 1 || y == ymax + 1 || x == xmax + 1) {
            map[offset] = min;
            return map[offset];
        }
        v = map[r1 + 1] + sqrt5;
        if (v < min) {
            min = v;
        }
        if ((v = map[r1 + 3] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r2 + 4] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r4 + 4] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r5 + 3] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r5 + 1] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r4] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r2] + sqrt5) < min) {
            min = v;
        }
        map[offset] = min;
        return map[offset];
    }

    private int setValue(int[] map, int width, int offset) {
        int r1 = offset - width - width - 2;
        int r2 = r1 + width;
        int r3 = r2 + width;
        int r4 = r3 + width;
        int r5 = r4 + width;
        int v = map[r3 + 1] + 41;
        v = map[r2 + 2] + 41;
        int min = v;
        if (v < min) {
            min = v;
        }
        if ((v = map[r3 + 3] + 41) < min) {
            min = v;
        }
        if ((v = map[r4 + 2] + 41) < min) {
            min = v;
        }
        if ((v = map[r2 + 1] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r2 + 3] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r4 + 1] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r4 + 3] + sqrt2) < min) {
            min = v;
        }
        if ((v = map[r1 + 1] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r1 + 3] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r2 + 4] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r4 + 4] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r5 + 3] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r5 + 1] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r4] + sqrt5) < min) {
            min = v;
        }
        if ((v = map[r2] + sqrt5) < min) {
            min = v;
        }
        map[offset] = min;
        return map[offset];
    }

    public String toString() {
        return "Stylize/Shapeburst...";
    }
}

