/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public abstract class TransformFilter
extends AbstractBufferedImageOp {
    public static final int ZERO = 0;
    public static final int CLAMP = 1;
    public static final int WRAP = 2;
    public static final int RGB_CLAMP = 3;
    public static final int NEAREST_NEIGHBOUR = 0;
    public static final int BILINEAR = 1;
    protected int edgeAction = 3;
    protected int interpolation = 1;
    protected Rectangle transformedSpace;
    protected Rectangle originalSpace;

    public void setEdgeAction(int edgeAction) {
        this.edgeAction = edgeAction;
    }

    public int getEdgeAction() {
        return this.edgeAction;
    }

    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    public int getInterpolation() {
        return this.interpolation;
    }

    protected abstract void transformInverse(int var1, int var2, float[] var3);

    protected void transformSpace(Rectangle rect) {
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();
        this.originalSpace = new Rectangle(0, 0, width, height);
        this.transformedSpace = new Rectangle(0, 0, width, height);
        this.transformSpace(this.transformedSpace);
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(this.transformedSpace.width, this.transformedSpace.height), dstCM.isAlphaPremultiplied(), null);
        }
        WritableRaster dstRaster = dst.getRaster();
        int[] inPixels = this.getRGB(src, 0, 0, width, height, null);
        if (this.interpolation == 0) {
            return this.filterPixelsNN(dst, width, height, inPixels, this.transformedSpace);
        }
        int srcWidth = width;
        int srcHeight = height;
        int srcWidth1 = width - 1;
        int srcHeight1 = height - 1;
        int outWidth = this.transformedSpace.width;
        int outHeight = this.transformedSpace.height;
        boolean index = false;
        int[] outPixels = new int[outWidth];
        int outX = this.transformedSpace.x;
        int outY = this.transformedSpace.y;
        float[] out = new float[2];
        for (int y = 0; y < outHeight; ++y) {
            for (int x = 0; x < outWidth; ++x) {
                int se;
                int sw;
                int ne;
                int nw;
                this.transformInverse(outX + x, outY + y, out);
                int srcX = (int)Math.floor(out[0]);
                int srcY = (int)Math.floor(out[1]);
                float xWeight = out[0] - (float)srcX;
                float yWeight = out[1] - (float)srcY;
                if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {
                    int i = srcWidth * srcY + srcX;
                    nw = inPixels[i];
                    ne = inPixels[i + 1];
                    sw = inPixels[i + srcWidth];
                    se = inPixels[i + srcWidth + 1];
                } else {
                    nw = this.getPixel(inPixels, srcX, srcY, srcWidth, srcHeight);
                    ne = this.getPixel(inPixels, srcX + 1, srcY, srcWidth, srcHeight);
                    sw = this.getPixel(inPixels, srcX, srcY + 1, srcWidth, srcHeight);
                    se = this.getPixel(inPixels, srcX + 1, srcY + 1, srcWidth, srcHeight);
                }
                outPixels[x] = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
            }
            this.setRGB(dst, 0, y, this.transformedSpace.width, 1, outPixels);
        }
        return dst;
    }

    private final int getPixel(int[] pixels, int x, int y, int width, int height) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            switch (this.edgeAction) {
                default: {
                    return 0;
                }
                case 2: {
                    return pixels[ImageMath.mod(y, height) * width + ImageMath.mod(x, width)];
                }
                case 1: {
                    return pixels[ImageMath.clamp(y, 0, height - 1) * width + ImageMath.clamp(x, 0, width - 1)];
                }
                case 3: 
            }
            return pixels[ImageMath.clamp(y, 0, height - 1) * width + ImageMath.clamp(x, 0, width - 1)] & 0xFFFFFF;
        }
        return pixels[y * width + x];
    }

    protected BufferedImage filterPixelsNN(BufferedImage dst, int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int srcWidth = width;
        int srcHeight = height;
        int outWidth = transformedSpace.width;
        int outHeight = transformedSpace.height;
        int[] outPixels = new int[outWidth];
        int outX = transformedSpace.x;
        int outY = transformedSpace.y;
        int[] rgb = new int[4];
        float[] out = new float[2];
        for (int y = 0; y < outHeight; ++y) {
            for (int x = 0; x < outWidth; ++x) {
                this.transformInverse(outX + x, outY + y, out);
                int srcX = (int)out[0];
                int srcY = (int)out[1];
                if (out[0] < 0.0f || srcX >= srcWidth || out[1] < 0.0f || srcY >= srcHeight) {
                    int p;
                    switch (this.edgeAction) {
                        default: {
                            p = 0;
                            break;
                        }
                        case 2: {
                            p = inPixels[ImageMath.mod(srcY, srcHeight) * srcWidth + ImageMath.mod(srcX, srcWidth)];
                            break;
                        }
                        case 1: {
                            p = inPixels[ImageMath.clamp(srcY, 0, srcHeight - 1) * srcWidth + ImageMath.clamp(srcX, 0, srcWidth - 1)];
                            break;
                        }
                        case 3: {
                            p = inPixels[ImageMath.clamp(srcY, 0, srcHeight - 1) * srcWidth + ImageMath.clamp(srcX, 0, srcWidth - 1)] & 0xFFFFFF;
                        }
                    }
                    outPixels[x] = p;
                    continue;
                }
                int i = srcWidth * srcY + srcX;
                rgb[0] = inPixels[i];
                outPixels[x] = inPixels[i];
            }
            this.setRGB(dst, 0, y, transformedSpace.width, 1, outPixels);
        }
        return dst;
    }
}

