/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.ImageUtils;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.TransformFilter;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class CurlFilter
extends TransformFilter {
    private float angle = 0.0f;
    private float transition = 0.0f;
    private float width;
    private float height;
    private float radius;

    public CurlFilter() {
        this.setEdgeAction(0);
    }

    public void setTransition(float transition) {
        this.transition = transition;
    }

    public float getTransition() {
        return this.transition;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        this.width = src.getWidth();
        this.height = src.getHeight();
        int type = src.getType();
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
        float[] out = new float[4];
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
                int rgb = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
                int r = rgb >> 16 & 0xFF;
                int g = rgb >> 8 & 0xFF;
                int b = rgb & 0xFF;
                float shade = out[2];
                r = (int)((float)r * shade);
                g = (int)((float)g * shade);
                b = (int)((float)b * shade);
                rgb = rgb & 0xFF000000 | r << 16 | g << 8 | b;
                outPixels[x] = out[3] != 0.0f ? PixelUtils.combinePixels(rgb, inPixels[srcWidth * y + x], 1) : rgb;
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
                case 1: 
            }
            return pixels[ImageMath.clamp(y, 0, height - 1) * width + ImageMath.clamp(x, 0, width - 1)];
        }
        return pixels[y * width + x];
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        boolean offpage;
        float t = this.transition;
        float px = x;
        float py = y;
        float s = (float)Math.sin(this.angle);
        float c = (float)Math.cos(this.angle);
        float tx = t * this.width;
        tx = t * (float)Math.sqrt(this.width * this.width + this.height * this.height);
        float xoffset = c < 0.0f ? this.width : 0.0f;
        float yoffset = s < 0.0f ? this.height : 0.0f;
        float qx = (px -= xoffset) * c + (py -= yoffset) * s;
        float qy = -px * s + py * c;
        boolean outside = qx < tx;
        boolean unfolded = qx > tx * 2.0f;
        boolean oncurl = !outside && !unfolded;
        qx = qx > tx * 2.0f ? qx : 2.0f * tx - qx;
        px = qx * c - qy * s;
        py = qx * s + qy * c;
        py += yoffset;
        boolean bl = offpage = (px += xoffset) < 0.0f || py < 0.0f || px >= this.width || py >= this.height;
        if (offpage && oncurl) {
            px = x;
            py = y;
        }
        float shade = !offpage && oncurl ? 1.9f * (1.0f - (float)Math.cos(Math.exp((qx - tx) / this.radius))) : 0.0f;
        out[2] = 1.0f - shade;
        if (outside) {
            out[1] = -1.0f;
            out[0] = -1.0f;
        } else {
            out[0] = px;
            out[1] = py;
        }
        out[3] = !offpage && oncurl ? 1 : 0;
    }

    public String toString() {
        return "Distort/Curl...";
    }

    static class Sampler {
        private int edgeAction;
        private int width;
        private int height;
        private int[] inPixels;

        public Sampler(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            int type = image.getType();
            this.inPixels = ImageUtils.getRGB(image, 0, 0, width, height, null);
        }

        public int sample(float x, float y) {
            int se;
            int sw;
            int ne;
            int nw;
            int srcX = (int)Math.floor(x);
            int srcY = (int)Math.floor(y);
            float xWeight = x - (float)srcX;
            float yWeight = y - (float)srcY;
            if (srcX >= 0 && srcX < this.width - 1 && srcY >= 0 && srcY < this.height - 1) {
                int i = this.width * srcY + srcX;
                nw = this.inPixels[i];
                ne = this.inPixels[i + 1];
                sw = this.inPixels[i + this.width];
                se = this.inPixels[i + this.width + 1];
            } else {
                nw = this.getPixel(this.inPixels, srcX, srcY, this.width, this.height);
                ne = this.getPixel(this.inPixels, srcX + 1, srcY, this.width, this.height);
                sw = this.getPixel(this.inPixels, srcX, srcY + 1, this.width, this.height);
                se = this.getPixel(this.inPixels, srcX + 1, srcY + 1, this.width, this.height);
            }
            return ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
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
                    case 1: 
                }
                return pixels[ImageMath.clamp(y, 0, height - 1) * width + ImageMath.clamp(x, 0, width - 1)];
            }
            return pixels[y * width + x];
        }
    }
}

