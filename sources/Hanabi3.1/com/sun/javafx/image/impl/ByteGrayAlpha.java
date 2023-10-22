package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;

public class ByteGrayAlpha {
   public static final BytePixelGetter getter;
   public static final BytePixelSetter setter;
   public static final BytePixelAccessor accessor;

   public static ByteToBytePixelConverter ToByteGrayAlphaPreConverter() {
      return ByteGrayAlpha.ToByteGrayAlphaPreConv.instance;
   }

   public static ByteToBytePixelConverter ToByteBgraConverter() {
      return ByteGrayAlpha.ToByteBgraSameConv.nonpremul;
   }

   static {
      getter = ByteGrayAlpha.Accessor.nonpremul;
      setter = ByteGrayAlpha.Accessor.nonpremul;
      accessor = ByteGrayAlpha.Accessor.nonpremul;
   }

   static class ToByteBgraSameConv extends BaseByteToByteConverter {
      static final ByteToBytePixelConverter nonpremul = new ToByteBgraSameConv(false);
      static final ByteToBytePixelConverter premul = new ToByteBgraSameConv(true);

      private ToByteBgraSameConv(boolean var1) {
         super(var1 ? ByteGrayAlphaPre.getter : ByteGrayAlpha.getter, var1 ? ByteBgraPre.setter : ByteBgra.setter);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 2;
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = var1[var2++];
               byte var11 = var1[var2++];
               var4[var5++] = var10;
               var4[var5++] = var10;
               var4[var5++] = var10;
               var4[var5++] = var11;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 2;
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = var1.get(var2++);
               byte var11 = var1.get(var2++);
               var4.put(var5++, var10);
               var4.put(var5++, var10);
               var4.put(var5++, var10);
               var4.put(var5++, var11);
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class ToByteGrayAlphaPreConv extends BaseByteToByteConverter {
      static final ByteToBytePixelConverter instance = new ToByteGrayAlphaPreConv();

      private ToByteGrayAlphaPreConv() {
         super(ByteGrayAlpha.getter, ByteGrayAlphaPre.setter);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 2;
         var6 -= var7 * 2;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1[var2++] & 255;
               byte var11 = var1[var2++];
               if (var11 != -1) {
                  if (var11 == 0) {
                     var10 = 0;
                  } else {
                     var10 = (var10 * (var11 & 255) + 127) / 255;
                  }
               }

               var4[var5++] = (byte)var10;
               var4[var5++] = (byte)var11;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 2;
         var6 -= var7 * 2;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1.get(var2++) & 255;
               byte var11 = var1.get(var2++);
               if (var11 != -1) {
                  if (var11 == 0) {
                     var10 = 0;
                  } else {
                     var10 = (var10 * (var11 & 255) + 127) / 255;
                  }
               }

               var4.put(var5++, (byte)var10);
               var4.put(var5++, (byte)var11);
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class Accessor implements BytePixelAccessor {
      static final BytePixelAccessor nonpremul = new Accessor(false);
      static final BytePixelAccessor premul = new Accessor(true);
      private boolean isPremult;

      private Accessor(boolean var1) {
         this.isPremult = var1;
      }

      public AlphaType getAlphaType() {
         return this.isPremult ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
      }

      public int getNumElements() {
         return 2;
      }

      public int getArgb(byte[] var1, int var2) {
         int var3 = var1[var2] & 255;
         int var4 = var1[var2 + 1] & 255;
         if (this.isPremult) {
            var3 = PixelUtils.PreToNonPre(var3, var4);
         }

         return var4 << 24 | var3 << 16 | var3 << 8 | var3;
      }

      public int getArgbPre(byte[] var1, int var2) {
         int var3 = var1[var2] & 255;
         int var4 = var1[var2 + 1] & 255;
         if (!this.isPremult) {
            var3 = PixelUtils.NonPretoPre(var3, var4);
         }

         return var4 << 24 | var3 << 16 | var3 << 8 | var3;
      }

      public int getArgb(ByteBuffer var1, int var2) {
         int var3 = var1.get(var2) & 255;
         int var4 = var1.get(var2 + 1) & 255;
         if (this.isPremult) {
            var3 = PixelUtils.PreToNonPre(var3, var4);
         }

         return var4 << 24 | var3 << 16 | var3 << 8 | var3;
      }

      public int getArgbPre(ByteBuffer var1, int var2) {
         int var3 = var1.get(var2) & 255;
         int var4 = var1.get(var2 + 1) & 255;
         if (!this.isPremult) {
            var3 = PixelUtils.NonPretoPre(var3, var4);
         }

         return var4 << 24 | var3 << 16 | var3 << 8 | var3;
      }

      public void setArgb(byte[] var1, int var2, int var3) {
         int var4 = PixelUtils.RgbToGray(var3);
         int var5 = var3 >>> 24;
         if (this.isPremult) {
            var4 = PixelUtils.NonPretoPre(var4, var5);
         }

         var1[var2] = (byte)var4;
         var1[var2 + 1] = (byte)var5;
      }

      public void setArgbPre(byte[] var1, int var2, int var3) {
         int var4 = PixelUtils.RgbToGray(var3);
         int var5 = var3 >>> 24;
         if (!this.isPremult) {
            var4 = PixelUtils.PreToNonPre(var4, var5);
         }

         var1[var2] = (byte)var4;
         var1[var2 + 1] = (byte)var5;
      }

      public void setArgb(ByteBuffer var1, int var2, int var3) {
         int var4 = PixelUtils.RgbToGray(var3);
         int var5 = var3 >>> 24;
         if (this.isPremult) {
            var4 = PixelUtils.NonPretoPre(var4, var5);
         }

         var1.put(var2, (byte)var4);
         var1.put(var2 + 1, (byte)var5);
      }

      public void setArgbPre(ByteBuffer var1, int var2, int var3) {
         int var4 = PixelUtils.RgbToGray(var3);
         int var5 = var3 >>> 24;
         if (!this.isPremult) {
            var4 = PixelUtils.PreToNonPre(var4, var5);
         }

         var1.put(var2, (byte)var4);
         var1.put(var2 + 1, (byte)var5);
      }
   }
}
