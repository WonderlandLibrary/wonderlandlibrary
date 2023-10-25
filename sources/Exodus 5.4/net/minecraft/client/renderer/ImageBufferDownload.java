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
    private int imageWidth;
    private int imageHeight;
    private int[] imageData;

    private void setAreaOpaque(int n, int n2, int n3, int n4) {
        int n5 = n;
        while (n5 < n3) {
            int n6 = n2;
            while (n6 < n4) {
                int n7 = n5 + n6 * this.imageWidth;
                this.imageData[n7] = this.imageData[n7] | 0xFF000000;
                ++n6;
            }
            ++n5;
        }
    }

    @Override
    public void skinAvailable() {
    }

    private boolean hasTransparency(int n, int n2, int n3, int n4) {
        int n5 = n;
        while (n5 < n3) {
            int n6 = n2;
            while (n6 < n4) {
                int n7 = this.imageData[n5 + n6 * this.imageWidth];
                if ((n7 >> 24 & 0xFF) < 128) {
                    return true;
                }
                ++n6;
            }
            ++n5;
        }
        return false;
    }

    @Override
    public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        this.imageWidth = 64;
        this.imageHeight = 64;
        BufferedImage bufferedImage2 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = bufferedImage2.getGraphics();
        graphics.drawImage(bufferedImage, 0, 0, null);
        if (bufferedImage.getHeight() == 32) {
            graphics.drawImage(bufferedImage2, 24, 48, 20, 52, 4, 16, 8, 20, null);
            graphics.drawImage(bufferedImage2, 28, 48, 24, 52, 8, 16, 12, 20, null);
            graphics.drawImage(bufferedImage2, 20, 52, 16, 64, 8, 20, 12, 32, null);
            graphics.drawImage(bufferedImage2, 24, 52, 20, 64, 4, 20, 8, 32, null);
            graphics.drawImage(bufferedImage2, 28, 52, 24, 64, 0, 20, 4, 32, null);
            graphics.drawImage(bufferedImage2, 32, 52, 28, 64, 12, 20, 16, 32, null);
            graphics.drawImage(bufferedImage2, 40, 48, 36, 52, 44, 16, 48, 20, null);
            graphics.drawImage(bufferedImage2, 44, 48, 40, 52, 48, 16, 52, 20, null);
            graphics.drawImage(bufferedImage2, 36, 52, 32, 64, 48, 20, 52, 32, null);
            graphics.drawImage(bufferedImage2, 40, 52, 36, 64, 44, 20, 48, 32, null);
            graphics.drawImage(bufferedImage2, 44, 52, 40, 64, 40, 20, 44, 32, null);
            graphics.drawImage(bufferedImage2, 48, 52, 44, 64, 52, 20, 56, 32, null);
        }
        graphics.dispose();
        this.imageData = ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData();
        this.setAreaOpaque(0, 0, 32, 16);
        this.setAreaTransparent(32, 0, 64, 32);
        this.setAreaOpaque(0, 16, 64, 32);
        this.setAreaTransparent(0, 32, 16, 48);
        this.setAreaTransparent(16, 32, 40, 48);
        this.setAreaTransparent(40, 32, 56, 48);
        this.setAreaTransparent(0, 48, 16, 64);
        this.setAreaOpaque(16, 48, 48, 64);
        this.setAreaTransparent(48, 48, 64, 64);
        return bufferedImage2;
    }

    private void setAreaTransparent(int n, int n2, int n3, int n4) {
        if (!this.hasTransparency(n, n2, n3, n4)) {
            int n5 = n;
            while (n5 < n3) {
                int n6 = n2;
                while (n6 < n4) {
                    int n7 = n5 + n6 * this.imageWidth;
                    this.imageData[n7] = this.imageData[n7] & 0xFFFFFF;
                    ++n6;
                }
                ++n5;
            }
        }
    }
}

