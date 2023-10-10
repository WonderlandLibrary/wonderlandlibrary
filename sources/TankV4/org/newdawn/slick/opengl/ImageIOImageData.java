package org.newdawn.slick.opengl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public class ImageIOImageData implements LoadableImageData {
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

   public ByteBuffer loadImage(InputStream var1) throws IOException {
      return this.loadImage(var1, true, (int[])null);
   }

   public ByteBuffer loadImage(InputStream var1, boolean var2, int[] var3) throws IOException {
      return this.loadImage(var1, var2, false, var3);
   }

   public ByteBuffer loadImage(InputStream var1, boolean var2, boolean var3, int[] var4) throws IOException {
      if (var4 != null) {
         var3 = true;
      }

      BufferedImage var5 = ImageIO.read(var1);
      return this.imageToByteBuffer(var5, var2, var3, var4);
   }

   public ByteBuffer imageToByteBuffer(BufferedImage var1, boolean var2, boolean var3, int[] var4) {
      ByteBuffer var5 = null;
      int var8 = 2;

      int var9;
      for(var9 = 2; var8 < var1.getWidth(); var8 *= 2) {
      }

      while(var9 < var1.getHeight()) {
         var9 *= 2;
      }

      this.width = var1.getWidth();
      this.height = var1.getHeight();
      this.texHeight = var9;
      this.texWidth = var8;
      boolean var10 = var1.getColorModel().hasAlpha() || var3;
      WritableRaster var6;
      BufferedImage var7;
      if (var10) {
         this.depth = 32;
         var6 = Raster.createInterleavedRaster(0, var8, var9, 4, (Point)null);
         var7 = new BufferedImage(glAlphaColorModel, var6, false, new Hashtable());
      } else {
         this.depth = 24;
         var6 = Raster.createInterleavedRaster(0, var8, var9, 3, (Point)null);
         var7 = new BufferedImage(glColorModel, var6, false, new Hashtable());
      }

      Graphics2D var11 = (Graphics2D)var7.getGraphics();
      if (var10) {
         var11.setColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
         var11.fillRect(0, 0, var8, var9);
      }

      if (var2) {
         var11.scale(1.0D, -1.0D);
         var11.drawImage(var1, 0, -this.height, (ImageObserver)null);
      } else {
         var11.drawImage(var1, 0, 0, (ImageObserver)null);
      }

      if (this.edging) {
         if (this.height < var9 - 1) {
            this.copyArea(var7, 0, 0, this.width, 1, 0, var9 - 1);
            this.copyArea(var7, 0, this.height - 1, this.width, 1, 0, 1);
         }

         if (this.width < var8 - 1) {
            this.copyArea(var7, 0, 0, 1, this.height, var8 - 1, 0);
            this.copyArea(var7, this.width - 1, 0, 1, this.height, 1, 0);
         }
      }

      byte[] var12 = ((DataBufferByte)var7.getRaster().getDataBuffer()).getData();
      if (var4 != null) {
         for(int var13 = 0; var13 < var12.length; var13 += 4) {
            boolean var14 = true;

            for(int var15 = 0; var15 < 3; ++var15) {
               int var16 = var12[var13 + var15] < 0 ? 256 + var12[var13 + var15] : var12[var13 + var15];
               if (var16 != var4[var15]) {
                  var14 = false;
               }
            }

            if (var14) {
               var12[var13 + 3] = 0;
            }
         }
      }

      var5 = ByteBuffer.allocateDirect(var12.length);
      var5.order(ByteOrder.nativeOrder());
      var5.put(var12, 0, var12.length);
      var5.flip();
      var11.dispose();
      return var5;
   }

   public ByteBuffer getImageBufferData() {
      throw new RuntimeException("ImageIOImageData doesn't store it's image.");
   }

   private void copyArea(BufferedImage var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      Graphics2D var8 = (Graphics2D)var1.getGraphics();
      var8.drawImage(var1.getSubimage(var2, var3, var4, var5), var2 + var6, var3 + var7, (ImageObserver)null);
   }

   public void configureEdging(boolean var1) {
      this.edging = var1;
   }
}
