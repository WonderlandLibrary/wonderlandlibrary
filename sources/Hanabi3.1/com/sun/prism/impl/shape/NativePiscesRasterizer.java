package com.sun.prism.impl.shape;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Logging;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;
import java.nio.ByteBuffer;
import java.security.AccessController;

public class NativePiscesRasterizer implements ShapeRasterizer {
   private static MaskData emptyData = MaskData.create(new byte[1], 0, 0, 1, 1);
   private static final byte SEG_MOVETO = 0;
   private static final byte SEG_LINETO = 1;
   private static final byte SEG_QUADTO = 2;
   private static final byte SEG_CUBICTO = 3;
   private static final byte SEG_CLOSE = 4;
   private byte[] cachedMask;
   private ByteBuffer cachedBuffer;
   private MaskData cachedData;
   private int[] bounds = new int[4];
   private boolean lastAntialiasedShape;
   private boolean firstTimeAASetting = true;

   static native void init(int var0, int var1);

   static native void produceFillAlphas(float[] var0, byte[] var1, int var2, boolean var3, double var4, double var6, double var8, double var10, double var12, double var14, int[] var16, byte[] var17);

   static native void produceStrokeAlphas(float[] var0, byte[] var1, int var2, float var3, int var4, int var5, float var6, float[] var7, float var8, double var9, double var11, double var13, double var15, double var17, double var19, int[] var21, byte[] var22);

   public MaskData getMaskData(Shape var1, BasicStroke var2, RectBounds var3, BaseTransform var4, boolean var5, boolean var6) {
      if (this.firstTimeAASetting || this.lastAntialiasedShape != var6) {
         int var7 = var6 ? 3 : 0;
         init(var7, var7);
         this.firstTimeAASetting = false;
         this.lastAntialiasedShape = var6;
      }

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

      this.bounds[0] = (int)Math.floor((double)var3.getMinX());
      this.bounds[1] = (int)Math.floor((double)var3.getMinY());
      this.bounds[2] = (int)Math.ceil((double)var3.getMaxX());
      this.bounds[3] = (int)Math.ceil((double)var3.getMaxY());
      if (this.bounds[2] > this.bounds[0] && this.bounds[3] > this.bounds[1]) {
         Path2D var26 = var1 instanceof Path2D ? (Path2D)var1 : new Path2D(var1);
         double var8;
         double var10;
         double var12;
         double var14;
         double var16;
         double var18;
         if (var4 != null && !var4.isIdentity()) {
            var8 = var4.getMxx();
            var10 = var4.getMxy();
            var12 = var4.getMxt();
            var14 = var4.getMyx();
            var16 = var4.getMyy();
            var18 = var4.getMyt();
         } else {
            var16 = 1.0;
            var8 = 1.0;
            var14 = 0.0;
            var10 = 0.0;
            var18 = 0.0;
            var12 = 0.0;
         }

         int var20 = this.bounds[0];
         int var21 = this.bounds[1];
         int var22 = this.bounds[2] - var20;
         int var23 = this.bounds[3] - var21;
         if (var22 > 0 && var23 > 0) {
            if (this.cachedMask == null || var22 * var23 > this.cachedMask.length) {
               this.cachedMask = null;
               this.cachedBuffer = null;
               this.cachedData = new MaskData();
               int var24 = var22 * var23 + 4095 & -4096;
               this.cachedMask = new byte[var24];
               this.cachedBuffer = ByteBuffer.wrap(this.cachedMask);
            }

            try {
               if (var2 != null) {
                  produceStrokeAlphas(var26.getFloatCoordsNoClone(), var26.getCommandsNoClone(), var26.getNumCommands(), var2.getLineWidth(), var2.getEndCap(), var2.getLineJoin(), var2.getMiterLimit(), var2.getDashArray(), var2.getDashPhase(), var8, var10, var12, var14, var16, var18, this.bounds, this.cachedMask);
               } else {
                  produceFillAlphas(var26.getFloatCoordsNoClone(), var26.getCommandsNoClone(), var26.getNumCommands(), var26.getWindingRule() == 1, var8, var10, var12, var14, var16, var18, this.bounds, this.cachedMask);
               }
            } catch (Throwable var25) {
               if (PrismSettings.verbose) {
                  var25.printStackTrace();
               }

               Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + var25.toString());
               return emptyData;
            }

            var20 = this.bounds[0];
            var21 = this.bounds[1];
            var22 = this.bounds[2] - var20;
            var23 = this.bounds[3] - var21;
            if (var22 > 0 && var23 > 0) {
               this.cachedData.update(this.cachedBuffer, var20, var21, var22, var23);
               return this.cachedData;
            } else {
               return emptyData;
            }
         } else {
            return emptyData;
         }
      } else {
         return emptyData;
      }
   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = "prism_common";
         if (PrismSettings.verbose) {
            System.out.println("Loading Prism common native library ...");
         }

         NativeLibLoader.loadLibrary(var0);
         if (PrismSettings.verbose) {
            System.out.println("\tsucceeded.");
         }

         return null;
      });
   }
}
