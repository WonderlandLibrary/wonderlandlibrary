package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteGray {
   public static final BytePixelGetter getter;
   public static final BytePixelSetter setter;
   public static final BytePixelAccessor accessor;
   private static ByteToBytePixelConverter ToByteGrayObj;

   public static ByteToBytePixelConverter ToByteGrayConverter() {
      if (ToByteGrayObj == null) {
         ToByteGrayObj = BaseByteToByteConverter.create(accessor);
      }

      return ToByteGrayObj;
   }

   public static ByteToBytePixelConverter ToByteBgraConverter() {
      return ByteGray.ToByteBgrfConv.nonpremult;
   }

   public static ByteToBytePixelConverter ToByteBgraPreConverter() {
      return ByteGray.ToByteBgrfConv.premult;
   }

   public static ByteToIntPixelConverter ToIntArgbConverter() {
      return ByteGray.ToIntFrgbConv.nonpremult;
   }

   public static ByteToIntPixelConverter ToIntArgbPreConverter() {
      return ByteGray.ToIntFrgbConv.premult;
   }

   public static ByteToBytePixelConverter ToByteBgrConverter() {
      return ByteGray.ToByteRgbAnyConv.bgr;
   }

   static {
      getter = ByteGray.Accessor.instance;
      setter = ByteGray.Accessor.instance;
      accessor = ByteGray.Accessor.instance;
   }

   static class ToByteRgbAnyConv extends BaseByteToByteConverter {
      static ToByteRgbAnyConv bgr;

      private ToByteRgbAnyConv(BytePixelSetter var1) {
         super(ByteGray.getter, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var6 -= var7 * 3;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1[var2 + var9] & 255;
               var4[var5++] = (byte)var10;
               var4[var5++] = (byte)var10;
               var4[var5++] = (byte)var10;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var6 -= var7 * 3;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1.get(var2 + var9) & 255;
               var4.put(var5++, (byte)var10);
               var4.put(var5++, (byte)var10);
               var4.put(var5++, (byte)var10);
            }

            var2 += var3;
            var5 += var6;
         }
      }

      static {
         bgr = new ToByteRgbAnyConv(ByteBgr.setter);
      }
   }

   static class ToIntFrgbConv extends BaseByteToIntConverter {
      public static final ByteToIntPixelConverter nonpremult;
      public static final ByteToIntPixelConverter premult;

      private ToIntFrgbConv(IntPixelSetter var1) {
         super(ByteGray.getter, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1[var2 + var9] & 255;
               var4[var5 + var9] = -16777216 | var10 << 16 | var10 << 8 | var10;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1.get(var2 + var9) & 255;
               var4.put(var5 + var9, -16777216 | var10 << 16 | var10 << 8 | var10);
            }

            var2 += var3;
            var5 += var6;
         }
      }

      static {
         nonpremult = new ToIntFrgbConv(IntArgb.setter);
         premult = new ToIntFrgbConv(IntArgbPre.setter);
      }
   }

   static class ToByteBgrfConv extends BaseByteToByteConverter {
      public static final ByteToBytePixelConverter nonpremult;
      public static final ByteToBytePixelConverter premult;

      ToByteBgrfConv(BytePixelSetter var1) {
         super(ByteGray.getter, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = var1[var2 + var9];
               var4[var5++] = var10;
               var4[var5++] = var10;
               var4[var5++] = var10;
               var4[var5++] = -1;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = var1.get(var2 + var9);
               var4.put(var5, var10);
               var4.put(var5 + 1, var10);
               var4.put(var5 + 2, var10);
               var4.put(var5 + 3, (byte)-1);
               var5 += 4;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      static {
         nonpremult = new ToByteBgrfConv(ByteBgra.setter);
         premult = new ToByteBgrfConv(ByteBgraPre.setter);
      }
   }

   static class Accessor implements BytePixelAccessor {
      static final BytePixelAccessor instance = new Accessor();

      private Accessor() {
      }

      public AlphaType getAlphaType() {
         return AlphaType.OPAQUE;
      }

      public int getNumElements() {
         return 1;
      }

      public int getArgb(byte[] var1, int var2) {
         int var3 = var1[var2] & 255;
         return -16777216 | var3 << 16 | var3 << 8 | var3;
      }

      public int getArgbPre(byte[] var1, int var2) {
         int var3 = var1[var2] & 255;
         return -16777216 | var3 << 16 | var3 << 8 | var3;
      }

      public int getArgb(ByteBuffer var1, int var2) {
         int var3 = var1.get(var2) & 255;
         return -16777216 | var3 << 16 | var3 << 8 | var3;
      }

      public int getArgbPre(ByteBuffer var1, int var2) {
         int var3 = var1.get(var2) & 255;
         return -16777216 | var3 << 16 | var3 << 8 | var3;
      }

      public void setArgb(byte[] var1, int var2, int var3) {
         var1[var2] = (byte)PixelUtils.RgbToGray(var3);
      }

      public void setArgbPre(byte[] var1, int var2, int var3) {
         this.setArgb(var1, var2, PixelUtils.PretoNonPre(var3));
      }

      public void setArgb(ByteBuffer var1, int var2, int var3) {
         var1.put(var2, (byte)PixelUtils.RgbToGray(var3));
      }

      public void setArgbPre(ByteBuffer var1, int var2, int var3) {
         this.setArgb(var1, var2, PixelUtils.PretoNonPre(var3));
      }
   }
}
