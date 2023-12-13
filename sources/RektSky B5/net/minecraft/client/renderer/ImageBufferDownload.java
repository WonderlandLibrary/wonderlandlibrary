/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import net.minecraft.client.renderer.IImageBuffer;

public class ImageBufferDownload
implements IImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;
    private static final String __OBFID = "CL_00000956";

    @Override
    public BufferedImage parseUserSkin(BufferedImage image) {
        if (image == null) {
            return null;
        }
        this.imageWidth = 64;
        this.imageHeight = 64;
        int i2 = image.getWidth();
        int j2 = image.getHeight();
        int k2 = 1;
        while (this.imageWidth < i2 || this.imageHeight < j2) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
            k2 *= 2;
        }
        BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        if (image.getHeight() == 32 * k2) {
            graphics.drawImage(bufferedimage, 24 * k2, 48 * k2, 20 * k2, 52 * k2, 4 * k2, 16 * k2, 8 * k2, 20 * k2, null);
            graphics.drawImage(bufferedimage, 28 * k2, 48 * k2, 24 * k2, 52 * k2, 8 * k2, 16 * k2, 12 * k2, 20 * k2, null);
            graphics.drawImage(bufferedimage, 20 * k2, 52 * k2, 16 * k2, 64 * k2, 8 * k2, 20 * k2, 12 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 24 * k2, 52 * k2, 20 * k2, 64 * k2, 4 * k2, 20 * k2, 8 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 28 * k2, 52 * k2, 24 * k2, 64 * k2, 0 * k2, 20 * k2, 4 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 32 * k2, 52 * k2, 28 * k2, 64 * k2, 12 * k2, 20 * k2, 16 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 40 * k2, 48 * k2, 36 * k2, 52 * k2, 44 * k2, 16 * k2, 48 * k2, 20 * k2, null);
            graphics.drawImage(bufferedimage, 44 * k2, 48 * k2, 40 * k2, 52 * k2, 48 * k2, 16 * k2, 52 * k2, 20 * k2, null);
            graphics.drawImage(bufferedimage, 36 * k2, 52 * k2, 32 * k2, 64 * k2, 48 * k2, 20 * k2, 52 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 40 * k2, 52 * k2, 36 * k2, 64 * k2, 44 * k2, 20 * k2, 48 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 44 * k2, 52 * k2, 40 * k2, 64 * k2, 40 * k2, 20 * k2, 44 * k2, 32 * k2, null);
            graphics.drawImage(bufferedimage, 48 * k2, 52 * k2, 44 * k2, 64 * k2, 52 * k2, 20 * k2, 56 * k2, 32 * k2, null);
        }
        graphics.dispose();
        this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
        this.setAreaOpaque(0, 0, 32 * k2, 16 * k2);
        this.setAreaTransparent(32 * k2, 0, 64 * k2, 32 * k2);
        this.setAreaOpaque(0, 16 * k2, 64 * k2, 32 * k2);
        this.setAreaTransparent(0, 32 * k2, 16 * k2, 48 * k2);
        this.setAreaTransparent(16 * k2, 32 * k2, 40 * k2, 48 * k2);
        this.setAreaTransparent(40 * k2, 32 * k2, 56 * k2, 48 * k2);
        this.setAreaTransparent(0, 48 * k2, 16 * k2, 64 * k2);
        this.setAreaOpaque(16 * k2, 48 * k2, 48 * k2, 64 * k2);
        this.setAreaTransparent(48 * k2, 48 * k2, 64 * k2, 64 * k2);
        return bufferedimage;
    }

    @Override
    public void skinAvailable() {
    }

    private void setAreaTransparent(int p_78434_1_, int p_78434_2_, int p_78434_3_, int p_78434_4_) {
        if (!this.hasTransparency(p_78434_1_, p_78434_2_, p_78434_3_, p_78434_4_)) {
            for (int i2 = p_78434_1_; i2 < p_78434_3_; ++i2) {
                for (int j2 = p_78434_2_; j2 < p_78434_4_; ++j2) {
                    int n2 = i2 + j2 * this.imageWidth;
                    this.imageData[n2] = this.imageData[n2] & 0xFFFFFF;
                }
            }
        }
    }

    private void setAreaOpaque(int p_78433_1_, int p_78433_2_, int p_78433_3_, int p_78433_4_) {
        for (int i2 = p_78433_1_; i2 < p_78433_3_; ++i2) {
            for (int j2 = p_78433_2_; j2 < p_78433_4_; ++j2) {
                int n2 = i2 + j2 * this.imageWidth;
                this.imageData[n2] = this.imageData[n2] | 0xFF000000;
            }
        }
    }

    private boolean hasTransparency(int p_78435_1_, int p_78435_2_, int p_78435_3_, int p_78435_4_) {
        for (int i2 = p_78435_1_; i2 < p_78435_3_; ++i2) {
            for (int j2 = p_78435_2_; j2 < p_78435_4_; ++j2) {
                int k2 = this.imageData[i2 + j2 * this.imageWidth];
                if ((k2 >> 24 & 0xFF) >= 128) continue;
                return true;
            }
        }
        return false;
    }
}

