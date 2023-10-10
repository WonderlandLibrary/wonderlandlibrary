package org.newdawn.slick.imageout;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class ImageIOWriter implements ImageWriter {
   public void saveImage(Image var1, String var2, OutputStream var3, boolean var4) throws IOException {
      int var5 = 4 * var1.getWidth() * var1.getHeight();
      if (!var4) {
         var5 = 3 * var1.getWidth() * var1.getHeight();
      }

      ByteBuffer var6 = ByteBuffer.allocate(var5);

      for(int var8 = 0; var8 < var1.getHeight(); ++var8) {
         for(int var9 = 0; var9 < var1.getWidth(); ++var9) {
            Color var7 = var1.getColor(var9, var8);
            var6.put((byte)((int)(var7.r * 255.0F)));
            var6.put((byte)((int)(var7.g * 255.0F)));
            var6.put((byte)((int)(var7.b * 255.0F)));
            if (var4) {
               var6.put((byte)((int)(var7.a * 255.0F)));
            }
         }
      }

      DataBufferByte var13 = new DataBufferByte(var6.array(), var5);
      ComponentColorModel var10;
      int[] var11;
      PixelInterleavedSampleModel var14;
      if (var4) {
         var11 = new int[]{0, 1, 2, 3};
         var14 = new PixelInterleavedSampleModel(0, var1.getWidth(), var1.getHeight(), 4, 4 * var1.getWidth(), var11);
         var10 = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 8}, true, false, 3, 0);
      } else {
         var11 = new int[]{0, 1, 2};
         var14 = new PixelInterleavedSampleModel(0, var1.getWidth(), var1.getHeight(), 3, 3 * var1.getWidth(), var11);
         var10 = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8, 0}, false, false, 1, 0);
      }

      WritableRaster var15 = Raster.createWritableRaster(var14, var13, new Point(0, 0));
      BufferedImage var12 = new BufferedImage(var10, var15, false, (Hashtable)null);
      ImageIO.write(var12, var2, var3);
   }
}
