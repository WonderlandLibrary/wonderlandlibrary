/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.IImageBuffer;

public class ImageBufferDownload
implements IImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    @Override
    @Nullable
    public BufferedImage parseUserSkin(BufferedImage image) {
        boolean flag;
        if (image == null) {
            return null;
        }
        this.imageWidth = 64;
        this.imageHeight = 64;
        int i = image.getWidth();
        int j = image.getHeight();
        int k = 1;
        while (this.imageWidth < i || this.imageHeight < j) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
            k *= 2;
        }
        BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        boolean bl = flag = image.getHeight() == 32 * k;
        if (flag) {
            graphics.setColor(new Color(0, 0, 0, 0));
            graphics.fillRect(0 * k, 32 * k, 64 * k, 32 * k);
            graphics.drawImage(bufferedimage, 24 * k, 48 * k, 20 * k, 52 * k, 4 * k, 16 * k, 8 * k, 20 * k, null);
            graphics.drawImage(bufferedimage, 28 * k, 48 * k, 24 * k, 52 * k, 8 * k, 16 * k, 12 * k, 20 * k, null);
            graphics.drawImage(bufferedimage, 20 * k, 52 * k, 16 * k, 64 * k, 8 * k, 20 * k, 12 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 24 * k, 52 * k, 20 * k, 64 * k, 4 * k, 20 * k, 8 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 28 * k, 52 * k, 24 * k, 64 * k, 0 * k, 20 * k, 4 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 32 * k, 52 * k, 28 * k, 64 * k, 12 * k, 20 * k, 16 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 40 * k, 48 * k, 36 * k, 52 * k, 44 * k, 16 * k, 48 * k, 20 * k, null);
            graphics.drawImage(bufferedimage, 44 * k, 48 * k, 40 * k, 52 * k, 48 * k, 16 * k, 52 * k, 20 * k, null);
            graphics.drawImage(bufferedimage, 36 * k, 52 * k, 32 * k, 64 * k, 48 * k, 20 * k, 52 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 40 * k, 52 * k, 36 * k, 64 * k, 44 * k, 20 * k, 48 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 44 * k, 52 * k, 40 * k, 64 * k, 40 * k, 20 * k, 44 * k, 32 * k, null);
            graphics.drawImage(bufferedimage, 48 * k, 52 * k, 44 * k, 64 * k, 52 * k, 20 * k, 56 * k, 32 * k, null);
        }
        graphics.dispose();
        this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
        this.setAreaOpaque(0 * k, 0 * k, 32 * k, 16 * k);
        if (flag) {
            this.doTransparencyHack(32 * k, 0 * k, 64 * k, 32 * k);
        }
        this.setAreaOpaque(0 * k, 16 * k, 64 * k, 32 * k);
        this.setAreaOpaque(16 * k, 48 * k, 48 * k, 64 * k);
        return bufferedimage;
    }

    @Override
    public void skinAvailable() {
    }

    private void doTransparencyHack(int p_189559_1_, int p_189559_2_, int p_189559_3_, int p_189559_4_) {
        for (int i = p_189559_1_; i < p_189559_3_; ++i) {
            for (int j = p_189559_2_; j < p_189559_4_; ++j) {
                int k = this.imageData[i + j * this.imageWidth];
                if ((k >> 24 & 0xFF) >= 128) continue;
                return;
            }
        }
        for (int l = p_189559_1_; l < p_189559_3_; ++l) {
            for (int i1 = p_189559_2_; i1 < p_189559_4_; ++i1) {
                int n = l + i1 * this.imageWidth;
                this.imageData[n] = this.imageData[n] & 0xFFFFFF;
            }
        }
    }

    private void setAreaOpaque(int x, int y, int width, int height) {
        for (int i = x; i < width; ++i) {
            for (int j = y; j < height; ++j) {
                int n = i + j * this.imageWidth;
                this.imageData[n] = this.imageData[n] | 0xFF000000;
            }
        }
    }
}

