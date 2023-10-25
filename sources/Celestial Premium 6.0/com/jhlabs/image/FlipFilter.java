/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class FlipFilter
extends AbstractBufferedImageOp {
    public static final int FLIP_H = 1;
    public static final int FLIP_V = 2;
    public static final int FLIP_HV = 3;
    public static final int FLIP_90CW = 4;
    public static final int FLIP_90CCW = 5;
    public static final int FLIP_180 = 6;
    private int operation;
    private int width;
    private int height;
    private int newWidth;
    private int newHeight;

    public FlipFilter() {
        this(3);
    }

    public FlipFilter(int operation) {
        this.operation = operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return this.operation;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();
        int[] inPixels = this.getRGB(src, 0, 0, width, height, null);
        int x = 0;
        int y = 0;
        int w = width;
        int h = height;
        int newX = 0;
        int newY = 0;
        int newW = w;
        int newH = h;
        switch (this.operation) {
            case 1: {
                newX = width - (x + w);
                break;
            }
            case 2: {
                newY = height - (y + h);
                break;
            }
            case 3: {
                newW = h;
                newH = w;
                newX = y;
                newY = x;
                break;
            }
            case 4: {
                newW = h;
                newH = w;
                newX = height - (y + h);
                newY = x;
                break;
            }
            case 5: {
                newW = h;
                newH = w;
                newX = y;
                newY = width - (x + w);
                break;
            }
            case 6: {
                newX = width - (x + w);
                newY = height - (y + h);
            }
        }
        int[] newPixels = new int[newW * newH];
        for (int row = 0; row < h; ++row) {
            for (int col = 0; col < w; ++col) {
                int index = row * width + col;
                int newRow = row;
                int newCol = col;
                switch (this.operation) {
                    case 1: {
                        newCol = w - col - 1;
                        break;
                    }
                    case 2: {
                        newRow = h - row - 1;
                        break;
                    }
                    case 3: {
                        newRow = col;
                        newCol = row;
                        break;
                    }
                    case 4: {
                        newRow = col;
                        newCol = h - row - 1;
                        break;
                    }
                    case 5: {
                        newRow = w - col - 1;
                        newCol = row;
                        break;
                    }
                    case 6: {
                        newRow = h - row - 1;
                        newCol = w - col - 1;
                    }
                }
                int newIndex = newRow * newW + newCol;
                newPixels[newIndex] = inPixels[index];
            }
        }
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(newW, newH), dstCM.isAlphaPremultiplied(), null);
        }
        WritableRaster dstRaster = dst.getRaster();
        this.setRGB(dst, 0, 0, newW, newH, newPixels);
        return dst;
    }

    public String toString() {
        switch (this.operation) {
            case 1: {
                return "Flip Horizontal";
            }
            case 2: {
                return "Flip Vertical";
            }
            case 3: {
                return "Flip Diagonal";
            }
            case 4: {
                return "Rotate 90";
            }
            case 5: {
                return "Rotate -90";
            }
            case 6: {
                return "Rotate 180";
            }
        }
        return "Flip";
    }
}

