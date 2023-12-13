package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Logging;
import com.sun.openpisces.AlphaConsumer;
import com.sun.openpisces.Renderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class OpenPiscesRasterizer implements ShapeRasterizer {
   private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
   private static Consumer savedConsumer;

   public MaskData getMaskData(Shape var1, BasicStroke var2, RectBounds var3, BaseTransform var4, boolean var5, boolean var6) {
      if (var2 != null && var2.getType() != 0) {
         var1 = var2.createStrokedShape(var1);
         var2 = null;
      }

      if (var3 == null) {
         if (var2 != null) {
            var1 = var2.createStrokedShape(var1);
            var2 = null;
         }

         var3 = new RectBounds();
         var3 = (RectBounds)var4.transform((BaseBounds)var1.getBounds(), (BaseBounds)var3);
      }

      Rectangle var7 = new Rectangle(var3);
      if (var7.isEmpty()) {
         return emptyData;
      } else {
         Renderer var8 = null;

         try {
            if (var1 instanceof Path2D) {
               var8 = OpenPiscesPrismUtils.setupRenderer((Path2D)var1, var2, var4, var7, var6);
            }

            if (var8 == null) {
               var8 = OpenPiscesPrismUtils.setupRenderer(var1, var2, var4, var7, var6);
            }

            int var9 = var8.getOutpixMinX();
            int var10 = var8.getOutpixMinY();
            int var11 = var8.getOutpixMaxX();
            int var12 = var8.getOutpixMaxY();
            int var13 = var11 - var9;
            int var14 = var12 - var10;
            if (var13 > 0 && var14 > 0) {
               Consumer var15 = savedConsumer;
               if (var15 == null || var13 * var14 > var15.getAlphaLength()) {
                  int var16 = var13 * var14 + 4095 & -4096;
                  savedConsumer = var15 = new Consumer(var16);
                  if (PrismSettings.verbose) {
                     System.out.println("new alphas");
                  }
               }

               var15.setBoundsNoClone(var9, var10, var13, var14);
               var8.produceAlphas(var15);
               return var15.getMaskData();
            } else {
               return emptyData;
            }
         } catch (Throwable var17) {
            if (PrismSettings.verbose) {
               var17.printStackTrace();
            }

            Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + var17.toString());
            return emptyData;
         }
      }
   }

   private static class Consumer implements AlphaConsumer {
      static byte[] savedAlphaMap;
      int x;
      int y;
      int width;
      int height;
      byte[] alphas;
      byte[] alphaMap;
      ByteBuffer alphabuffer;
      MaskData maskdata = new MaskData();

      public Consumer(int var1) {
         this.alphas = new byte[var1];
         this.alphabuffer = ByteBuffer.wrap(this.alphas);
      }

      public void setBoundsNoClone(int var1, int var2, int var3, int var4) {
         this.x = var1;
         this.y = var2;
         this.width = var3;
         this.height = var4;
         this.maskdata.update(this.alphabuffer, var1, var2, var3, var4);
      }

      public int getOriginX() {
         return this.x;
      }

      public int getOriginY() {
         return this.y;
      }

      public int getWidth() {
         return this.width;
      }

      public int getHeight() {
         return this.height;
      }

      public byte[] getAlphasNoClone() {
         return this.alphas;
      }

      public int getAlphaLength() {
         return this.alphas.length;
      }

      public MaskData getMaskData() {
         return this.maskdata;
      }

      public void setMaxAlpha(int var1) {
         byte[] var2 = savedAlphaMap;
         if (var2 == null || var2.length != var1 + 1) {
            var2 = new byte[var1 + 1];

            for(int var3 = 0; var3 <= var1; ++var3) {
               var2[var3] = (byte)((var3 * 255 + var1 / 2) / var1);
            }

            savedAlphaMap = var2;
         }

         this.alphaMap = var2;
      }

      public void setAndClearRelativeAlphas(int[] var1, int var2, int var3, int var4) {
         int var5 = this.width;
         int var6 = (var2 - this.y) * var5;
         byte[] var7 = this.alphas;
         byte[] var8 = this.alphaMap;
         int var9 = 0;

         for(int var10 = 0; var10 < var5; ++var10) {
            var9 += var1[var10];
            var1[var10] = 0;
            var7[var6 + var10] = var8[var9];
         }

      }

      public void setAndClearRelativeAlphas2(int[] var1, int var2, int var3, int var4) {
         if (var4 >= var3) {
            byte[] var5 = this.alphas;
            byte[] var6 = this.alphaMap;
            int var7 = var3 - this.x;
            int var8 = var4 - this.x;
            int var9 = this.width;
            int var10 = (var2 - this.y) * var9;

            int var11;
            for(var11 = 0; var11 < var7; ++var11) {
               var5[var10 + var11] = 0;
            }

            for(int var12 = 0; var11 <= var8; ++var11) {
               var12 += var1[var11];
               var1[var11] = 0;
               byte var13 = var6[var12];
               var5[var10 + var11] = var13;
            }

            for(var1[var11] = 0; var11 < var9; ++var11) {
               var5[var10 + var11] = 0;
            }
         } else {
            Arrays.fill(var1, 0);
         }

      }
   }
}
