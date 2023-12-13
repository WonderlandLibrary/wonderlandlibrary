/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import org.newdawn.slick.opengl.LoadableImageData;

public class ImageIOImageData
implements LoadableImageData {
    private static final ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0);
    private static final ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 0}, false, false, 1, 0);
    private int depth;
    private int height;
    private int width;
    private int texWidth;
    private int texHeight;
    private boolean edging = true;

    public int getDepth() {
        return this.depth;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexHeight() {
        return this.texHeight;
    }

    public int getTexWidth() {
        return this.texWidth;
    }

    public int getWidth() {
        return this.width;
    }

    public ByteBuffer loadImage(InputStream fis) throws IOException {
        return this.loadImage(fis, true, null);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
        if (transparent != null) {
            forceAlpha = true;
        }
        BufferedImage bufferedImage = ImageIO.read(fis);
        return this.imageToByteBuffer(bufferedImage, flipped, forceAlpha, transparent);
    }

    public ByteBuffer imageToByteBuffer(BufferedImage image, boolean flipped, boolean forceAlpha, int[] transparent) {
        BufferedImage texImage;
        WritableRaster raster;
        boolean useAlpha;
        int texWidth;
        ByteBuffer imageBuffer = null;
        int texHeight = 2;
        for (texWidth = 2; texWidth < image.getWidth(); texWidth *= 2) {
        }
        while (texHeight < image.getHeight()) {
            texHeight *= 2;
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.texHeight = texHeight;
        this.texWidth = texWidth;
        boolean bl = useAlpha = image.getColorModel().hasAlpha() || forceAlpha;
        if (useAlpha) {
            this.depth = 32;
            raster = Raster.createInterleavedRaster(0, texWidth, texHeight, 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        } else {
            this.depth = 24;
            raster = Raster.createInterleavedRaster(0, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }
        Graphics2D g2 = (Graphics2D)texImage.getGraphics();
        if (useAlpha) {
            g2.setColor(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            g2.fillRect(0, 0, texWidth, texHeight);
        }
        if (flipped) {
            g2.scale(1.0, -1.0);
            g2.drawImage((Image)image, 0, -this.height, null);
        } else {
            g2.drawImage((Image)image, 0, 0, null);
        }
        if (this.edging) {
            if (this.height < texHeight - 1) {
                this.copyArea(texImage, 0, 0, this.width, 1, 0, texHeight - 1);
                this.copyArea(texImage, 0, this.height - 1, this.width, 1, 0, 1);
            }
            if (this.width < texWidth - 1) {
                this.copyArea(texImage, 0, 0, 1, this.height, texWidth - 1, 0);
                this.copyArea(texImage, this.width - 1, 0, 1, this.height, 1, 0);
            }
        }
        byte[] data = ((DataBufferByte)texImage.getRaster().getDataBuffer()).getData();
        if (transparent != null) {
            for (int i2 = 0; i2 < data.length; i2 += 4) {
                boolean match = true;
                for (int c2 = 0; c2 < 3; ++c2) {
                    int value;
                    int n2 = value = data[i2 + c2] < 0 ? 256 + data[i2 + c2] : data[i2 + c2];
                    if (value == transparent[c2]) continue;
                    match = false;
                }
                if (!match) continue;
                data[i2 + 3] = 0;
            }
        }
        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();
        g2.dispose();
        return imageBuffer;
    }

    public ByteBuffer getImageBufferData() {
        throw new RuntimeException("ImageIOImageData doesn't store it's image.");
    }

    private void copyArea(BufferedImage image, int x2, int y2, int width, int height, int dx, int dy) {
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        g2.drawImage((Image)image.getSubimage(x2, y2, width, height), x2 + dx, y2 + dy, null);
    }

    public void configureEdging(boolean edging) {
        this.edging = edging;
    }
}

