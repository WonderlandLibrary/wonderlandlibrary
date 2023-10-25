/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;

public class LaplaceFilter
extends AbstractBufferedImageOp {
    private void brightness(int[] row) {
        for (int i = 0; i < row.length; ++i) {
            int rgb = row[i];
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            row[i] = (r + g + b) / 3;
        }
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int[] t;
        int y;
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int[] row1 = null;
        int[] row2 = null;
        int[] row3 = null;
        int[] pixels = new int[width];
        row1 = this.getRGB(src, 0, 0, width, 1, row1);
        row2 = this.getRGB(src, 0, 0, width, 1, row2);
        this.brightness(row1);
        this.brightness(row2);
        for (y = 0; y < height; ++y) {
            if (y < height - 1) {
                row3 = this.getRGB(src, 0, y + 1, width, 1, row3);
                this.brightness(row3);
            }
            pixels[width - 1] = -16777216;
            pixels[0] = -16777216;
            for (int x = 1; x < width - 1; ++x) {
                int r;
                int l1 = row2[x - 1];
                int l2 = row1[x];
                int l3 = row3[x];
                int l4 = row2[x + 1];
                int l = row2[x];
                int max = Math.max(Math.max(l1, l2), Math.max(l3, l4));
                int min = Math.min(Math.min(l1, l2), Math.min(l3, l4));
                int gradient = (int)(0.5f * (float)Math.max(max - l, l - min));
                pixels[x] = r = row1[x - 1] + row1[x] + row1[x + 1] + row2[x - 1] - 8 * row2[x] + row2[x + 1] + row3[x - 1] + row3[x] + row3[x + 1] > 0 ? gradient : 128 + gradient;
            }
            this.setRGB(dst, 0, y, width, 1, pixels);
            t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }
        row1 = this.getRGB(dst, 0, 0, width, 1, row1);
        row2 = this.getRGB(dst, 0, 0, width, 1, row2);
        for (y = 0; y < height; ++y) {
            if (y < height - 1) {
                row3 = this.getRGB(dst, 0, y + 1, width, 1, row3);
            }
            pixels[width - 1] = -16777216;
            pixels[0] = -16777216;
            for (int x = 1; x < width - 1; ++x) {
                int r = row2[x];
                r = r <= 128 && (row1[x - 1] > 128 || row1[x] > 128 || row1[x + 1] > 128 || row2[x - 1] > 128 || row2[x + 1] > 128 || row3[x - 1] > 128 || row3[x] > 128 || row3[x + 1] > 128) ? (r >= 128 ? r - 128 : r) : 0;
                pixels[x] = 0xFF000000 | r << 16 | r << 8 | r;
            }
            this.setRGB(dst, 0, y, width, 1, pixels);
            t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }
        return dst;
    }

    public String toString() {
        return "Edges/Laplace...";
    }
}

