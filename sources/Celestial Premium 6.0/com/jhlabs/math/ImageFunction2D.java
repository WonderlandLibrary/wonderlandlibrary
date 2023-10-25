/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.math.Function2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

public class ImageFunction2D
implements Function2D {
    public static final int ZERO = 0;
    public static final int CLAMP = 1;
    public static final int WRAP = 2;
    protected int[] pixels;
    protected int width;
    protected int height;
    protected int edgeAction = 0;
    protected boolean alpha = false;

    public ImageFunction2D(BufferedImage image) {
        this(image, false);
    }

    public ImageFunction2D(BufferedImage image, boolean alpha) {
        this(image, 0, alpha);
    }

    public ImageFunction2D(BufferedImage image, int edgeAction, boolean alpha) {
        this.init(this.getRGB(image, 0, 0, image.getWidth(), image.getHeight(), null), image.getWidth(), image.getHeight(), edgeAction, alpha);
    }

    public ImageFunction2D(int[] pixels, int width, int height, int edgeAction, boolean alpha) {
        this.init(pixels, width, height, edgeAction, alpha);
    }

    public ImageFunction2D(Image image) {
        this(image, 0, false);
    }

    public ImageFunction2D(Image image, int edgeAction, boolean alpha) {
        PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, null, 0, -1);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            throw new RuntimeException("interrupted waiting for pixels!");
        }
        if ((pg.status() & 0x80) != 0) {
            throw new RuntimeException("image fetch aborted");
        }
        this.init((int[])pg.getPixels(), pg.getWidth(), pg.getHeight(), edgeAction, alpha);
    }

    public int[] getRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == 2 || type == 1) {
            return (int[])image.getRaster().getDataElements(x, y, width, height, pixels);
        }
        return image.getRGB(x, y, width, height, pixels, 0, width);
    }

    public void init(int[] pixels, int width, int height, int edgeAction, boolean alpha) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.edgeAction = edgeAction;
        this.alpha = alpha;
    }

    @Override
    public float evaluate(float x, float y) {
        int ix = (int)x;
        int iy = (int)y;
        if (this.edgeAction == 2) {
            ix = ImageMath.mod(ix, this.width);
            iy = ImageMath.mod(iy, this.height);
        } else if (ix < 0 || iy < 0 || ix >= this.width || iy >= this.height) {
            if (this.edgeAction == 0) {
                return 0.0f;
            }
            if (ix < 0) {
                ix = 0;
            } else if (ix >= this.width) {
                ix = this.width - 1;
            }
            if (iy < 0) {
                iy = 0;
            } else if (iy >= this.height) {
                iy = this.height - 1;
            }
        }
        return this.alpha ? (float)(this.pixels[iy * this.width + ix] >> 24 & 0xFF) / 255.0f : (float)PixelUtils.brightness(this.pixels[iy * this.width + ix]) / 255.0f;
    }

    public void setEdgeAction(int edgeAction) {
        this.edgeAction = edgeAction;
    }

    public int getEdgeAction() {
        return this.edgeAction;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getPixels() {
        return this.pixels;
    }
}

