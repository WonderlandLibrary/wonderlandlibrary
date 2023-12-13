/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.imageout;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.imageout.ImageWriter;

public class ImageIOWriter
implements ImageWriter {
    public void saveImage(Image image, String format, OutputStream output, boolean hasAlpha) throws IOException {
        ComponentColorModel cm;
        PixelInterleavedSampleModel sampleModel;
        int[] offsets;
        int len = 4 * image.getWidth() * image.getHeight();
        if (!hasAlpha) {
            len = 3 * image.getWidth() * image.getHeight();
        }
        ByteBuffer out = ByteBuffer.allocate(len);
        for (int y2 = 0; y2 < image.getHeight(); ++y2) {
            for (int x2 = 0; x2 < image.getWidth(); ++x2) {
                Color c2 = image.getColor(x2, y2);
                out.put((byte)(c2.r * 255.0f));
                out.put((byte)(c2.g * 255.0f));
                out.put((byte)(c2.b * 255.0f));
                if (!hasAlpha) continue;
                out.put((byte)(c2.a * 255.0f));
            }
        }
        DataBufferByte dataBuffer = new DataBufferByte(out.array(), len);
        if (hasAlpha) {
            offsets = new int[]{0, 1, 2, 3};
            sampleModel = new PixelInterleavedSampleModel(0, image.getWidth(), image.getHeight(), 4, 4 * image.getWidth(), offsets);
            cm = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0);
        } else {
            offsets = new int[]{0, 1, 2};
            sampleModel = new PixelInterleavedSampleModel(0, image.getWidth(), image.getHeight(), 3, 3 * image.getWidth(), offsets);
            cm = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 0}, false, false, 1, 0);
        }
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));
        BufferedImage img = new BufferedImage(cm, raster, false, null);
        ImageIO.write((RenderedImage)img, format, output);
    }
}

