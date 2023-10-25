/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.annotation.Nullable;

public class SkinProcessor {
    private int[] pixels;
    private int width;
    private int height;

    @Nullable
    public BufferedImage process(BufferedImage image) {
        boolean isLegacy;
        if (image == null) {
            return null;
        }
        this.width = 64;
        this.height = 64;
        BufferedImage out = new BufferedImage(this.width, this.height, 2);
        Graphics outGraphics = out.getGraphics();
        outGraphics.drawImage(image, 0, 0, null);
        boolean bl = isLegacy = image.getHeight() == 32;
        if (isLegacy) {
            outGraphics.setColor(new Color(0, 0, 0, 0));
            outGraphics.fillRect(0, 32, 64, 32);
            outGraphics.drawImage(out, 24, 48, 20, 52, 4, 16, 8, 20, null);
            outGraphics.drawImage(out, 28, 48, 24, 52, 8, 16, 12, 20, null);
            outGraphics.drawImage(out, 20, 52, 16, 64, 8, 20, 12, 32, null);
            outGraphics.drawImage(out, 24, 52, 20, 64, 4, 20, 8, 32, null);
            outGraphics.drawImage(out, 28, 52, 24, 64, 0, 20, 4, 32, null);
            outGraphics.drawImage(out, 32, 52, 28, 64, 12, 20, 16, 32, null);
            outGraphics.drawImage(out, 40, 48, 36, 52, 44, 16, 48, 20, null);
            outGraphics.drawImage(out, 44, 48, 40, 52, 48, 16, 52, 20, null);
            outGraphics.drawImage(out, 36, 52, 32, 64, 48, 20, 52, 32, null);
            outGraphics.drawImage(out, 40, 52, 36, 64, 44, 20, 48, 32, null);
            outGraphics.drawImage(out, 44, 52, 40, 64, 40, 20, 44, 32, null);
            outGraphics.drawImage(out, 48, 52, 44, 64, 52, 20, 56, 32, null);
        }
        outGraphics.dispose();
        this.pixels = ((DataBufferInt)out.getRaster().getDataBuffer()).getData();
        this.setNoAlpha(0, 0, 32, 16);
        if (isLegacy) {
            this.doNotchTransparencyHack(32, 0, 64, 32);
        }
        this.setNoAlpha(0, 16, 64, 32);
        this.setNoAlpha(16, 48, 48, 64);
        return out;
    }

    private void doNotchTransparencyHack(int x0, int y0, int x1, int y1) {
        int y;
        int x;
        for (x = x0; x < x1; ++x) {
            for (y = y0; y < y1; ++y) {
                int pix = this.pixels[x + y * this.width];
                if ((pix >> 24 & 0xFF) >= 128) continue;
                return;
            }
        }
        for (x = x0; x < x1; ++x) {
            for (y = y0; y < y1; ++y) {
                int n = x + y * this.width;
                this.pixels[n] = this.pixels[n] & 0xFFFFFF;
            }
        }
    }

    private void setNoAlpha(int x0, int y0, int x1, int y1) {
        for (int x = x0; x < x1; ++x) {
            for (int y = y0; y < y1; ++y) {
                int n = x + y * this.width;
                this.pixels[n] = this.pixels[n] | 0xFF000000;
            }
        }
    }
}

