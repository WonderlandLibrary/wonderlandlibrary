// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import java.awt.Graphics;
import java.awt.image.DataBufferInt;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;

public class ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;
    
    @Nullable
    @Override
    public BufferedImage parseUserSkin(final BufferedImage image) {
        if (image == null) {
            return null;
        }
        this.imageWidth = 64;
        this.imageHeight = 64;
        final int i = image.getWidth();
        final int j = image.getHeight();
        int k = 1;
        while (this.imageWidth < i || this.imageHeight < j) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
            k *= 2;
        }
        final BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        final Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        final boolean flag = image.getHeight() == 32 * k;
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
            this.setAreaTransparent(32 * k, 0 * k, 64 * k, 32 * k);
        }
        this.setAreaOpaque(0 * k, 16 * k, 64 * k, 32 * k);
        this.setAreaOpaque(16 * k, 48 * k, 48 * k, 64 * k);
        return bufferedimage;
    }
    
    @Override
    public BufferedImage cache() {
        return null;
    }
    
    @Override
    public void skinAvailable() {
    }
    
    private void setAreaTransparent(final int x, final int y, final int width, final int height) {
        for (int i = x; i < width; ++i) {
            for (int j = y; j < height; ++j) {
                final int k = this.imageData[i + j * this.imageWidth];
                if ((k >> 24 & 0xFF) < 128) {
                    return;
                }
            }
        }
        for (int l = x; l < width; ++l) {
            for (int i2 = y; i2 < height; ++i2) {
                final int[] imageData = this.imageData;
                final int n = l + i2 * this.imageWidth;
                imageData[n] &= 0xFFFFFF;
            }
        }
    }
    
    private void setAreaOpaque(final int x, final int y, final int width, final int height) {
        for (int i = x; i < width; ++i) {
            for (int j = y; j < height; ++j) {
                final int[] imageData = this.imageData;
                final int n = i + j * this.imageWidth;
                imageData[n] |= 0xFF000000;
            }
        }
    }
}
