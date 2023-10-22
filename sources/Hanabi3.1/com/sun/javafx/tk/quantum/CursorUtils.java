package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Size;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.prism.Image;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.geometry.Dimension2D;

final class CursorUtils {
   private CursorUtils() {
   }

   public static Cursor getPlatformCursor(CursorFrame var0) {
      Cursor var1 = (Cursor)var0.getPlatformCursor(Cursor.class);
      if (var1 != null) {
         return var1;
      } else {
         Cursor var2 = createPlatformCursor(var0);
         var0.setPlatforCursor(Cursor.class, var2);
         return var2;
      }
   }

   public static Dimension2D getBestCursorSize(int var0, int var1) {
      Size var2 = Cursor.getBestSize(var0, var1);
      return new Dimension2D((double)var2.width, (double)var2.height);
   }

   private static Cursor createPlatformCursor(CursorFrame var0) {
      Application var1 = Application.GetApplication();
      switch (var0.getCursorType()) {
         case CROSSHAIR:
            return var1.createCursor(3);
         case TEXT:
            return var1.createCursor(2);
         case WAIT:
            return var1.createCursor(14);
         case DEFAULT:
            return var1.createCursor(1);
         case OPEN_HAND:
            return var1.createCursor(5);
         case CLOSED_HAND:
            return var1.createCursor(4);
         case HAND:
            return var1.createCursor(6);
         case H_RESIZE:
            return var1.createCursor(11);
         case V_RESIZE:
            return var1.createCursor(12);
         case MOVE:
            return var1.createCursor(19);
         case DISAPPEAR:
            return var1.createCursor(13);
         case SW_RESIZE:
            return var1.createCursor(15);
         case SE_RESIZE:
            return var1.createCursor(16);
         case NW_RESIZE:
            return var1.createCursor(17);
         case NE_RESIZE:
            return var1.createCursor(18);
         case N_RESIZE:
         case S_RESIZE:
            return var1.createCursor(12);
         case W_RESIZE:
         case E_RESIZE:
            return var1.createCursor(11);
         case NONE:
            return var1.createCursor(-1);
         case IMAGE:
            return createPlatformImageCursor((ImageCursorFrame)var0);
         default:
            System.err.println("unhandled Cursor: " + var0.getCursorType());
            return var1.createCursor(1);
      }
   }

   private static Cursor createPlatformImageCursor(ImageCursorFrame var0) {
      return createPlatformImageCursor(var0.getPlatformImage(), (float)var0.getHotspotX(), (float)var0.getHotspotY());
   }

   private static Cursor createPlatformImageCursor(Object var0, float var1, float var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("QuantumToolkit.createImageCursor: no image");
      } else {
         assert var0 instanceof Image;

         Image var3 = (Image)var0;
         int var4 = var3.getHeight();
         int var5 = var3.getWidth();
         Dimension2D var6 = getBestCursorSize(var5, var4);
         float var7 = (float)var6.getWidth();
         float var8 = (float)var6.getHeight();
         if (!(var7 <= 0.0F) && !(var8 <= 0.0F)) {
            switch (var3.getPixelFormat()) {
               case INT_ARGB_PRE:
                  return createPlatformImageCursor((int)var1, (int)var2, var5, var4, var3.getPixelBuffer());
               case BYTE_RGB:
               case BYTE_BGRA_PRE:
               case BYTE_GRAY:
                  ByteBuffer var9 = (ByteBuffer)var3.getPixelBuffer();
                  float var10 = var7 / (float)var5;
                  float var11 = var8 / (float)var4;
                  int var12 = (int)(var1 * var10);
                  int var13 = (int)(var2 * var11);
                  PushbroomScaler var14 = ScalerFactory.createScaler(var5, var4, var3.getBytesPerPixelUnit(), (int)var7, (int)var8, true);
                  byte[] var15 = new byte[var9.limit()];
                  int var16 = var3.getScanlineStride();

                  for(int var17 = 0; var17 < var4; ++var17) {
                     var9.position(var17 * var16);
                     var9.get(var15, 0, var16);
                     if (var14 != null) {
                        var14.putSourceScanline(var15, 0);
                     }
                  }

                  var9.rewind();
                  Image var18 = var3.iconify(var14.getDestination(), (int)var7, (int)var8);
                  return createPlatformImageCursor(var12, var13, var18.getWidth(), var18.getHeight(), var18.getPixelBuffer());
               default:
                  throw new IllegalArgumentException("QuantumToolkit.createImageCursor: bad image format");
            }
         } else {
            return Application.GetApplication().createCursor(1);
         }
      }
   }

   private static Cursor createPlatformImageCursor(int var0, int var1, int var2, int var3, Object var4) {
      Application var5 = Application.GetApplication();
      return var5.createCursor(var0, var1, var5.createPixels(var2, var3, (IntBuffer)var4));
   }
}
