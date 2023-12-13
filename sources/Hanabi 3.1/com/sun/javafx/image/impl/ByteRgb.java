package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteRgb {
   public static final BytePixelGetter getter;

   public static ByteToBytePixelConverter ToByteBgraConverter() {
      return ByteRgb.ToByteBgrfConv.nonpremult;
   }

   public static ByteToBytePixelConverter ToByteBgraPreConverter() {
      return ByteRgb.ToByteBgrfConv.premult;
   }

   public static ByteToIntPixelConverter ToIntArgbConverter() {
      return ByteRgb.ToIntFrgbConv.nonpremult;
   }

   public static ByteToIntPixelConverter ToIntArgbPreConverter() {
      return ByteRgb.ToIntFrgbConv.premult;
   }

   public static ByteToBytePixelConverter ToByteArgbConverter() {
      return ByteRgb.ToByteFrgbConv.nonpremult;
   }

   public static final ByteToBytePixelConverter ToByteBgrConverter() {
      return ByteRgb.SwapThreeByteConverter.rgbToBgrInstance;
   }

   static {
      getter = ByteRgb.Getter.instance;
   }

   static class SwapThreeByteConverter extends BaseByteToByteConverter {
      static final ByteToBytePixelConverter rgbToBgrInstance;

      public SwapThreeByteConverter(BytePixelGetter var1, BytePixelSetter var2) {
         super(var1, var2);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;
         var3 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               var4[var5++] = var1[var2 + 2];
               var4[var5++] = var1[var2 + 1];
               var4[var5++] = var1[var2];
               var2 += 3;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;
         var3 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               var4.put(var5++, var1.get(var2 + 2));
               var4.put(var5++, var1.get(var2 + 1));
               var4.put(var5++, var1.get(var2));
               var2 += 3;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      static {
         rgbToBgrInstance = new SwapThreeByteConverter(ByteRgb.getter, ByteBgr.accessor);
      }
   }

   static class ToByteFrgbConv extends BaseByteToByteConverter {
      static final ByteToBytePixelConverter nonpremult;

      private ToByteFrgbConv(BytePixelSetter var1) {
         super(ByteRgb.getter, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;
         var3 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               var4[var5++] = -1;
               var4[var5++] = var1[var2++];
               var4[var5++] = var1[var2++];
               var4[var5++] = var1[var2++];
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;
         var3 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               var4.put(var5++, (byte)-1);
               var4.put(var5++, var1.get(var2++));
               var4.put(var5++, var1.get(var2++));
               var4.put(var5++, var1.get(var2++));
            }

            var2 += var3;
            var5 += var6;
         }
      }

      static {
         nonpremult = new ToByteFrgbConv(ByteArgb.setter);
      }
   }

   static class ToIntFrgbConv extends BaseByteToIntConverter {
      public static final ByteToIntPixelConverter nonpremult;
      public static final ByteToIntPixelConverter premult;

      private ToIntFrgbConv(IntPixelSetter var1) {
         super(ByteRgb.getter, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1[var2++] & 255;
               int var11 = var1[var2++] & 255;
               int var12 = var1[var2++] & 255;
               var4[var5 + var9] = -16777216 | var10 << 16 | var11 << 8 | var12;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1.get(var2) & 255;
               int var11 = var1.get(var2 + 1) & 255;
               int var12 = var1.get(var2 + 2) & 255;
               var2 += 3;
               var4.put(var5 + var9, -16777216 | var10 << 16 | var11 << 8 | var12);
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

      private ToByteBgrfConv(BytePixelSetter var1) {
         super(ByteRgb.getter, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               var4[var5++] = var1[var2 + 2];
               var4[var5++] = var1[var2 + 1];
               var4[var5++] = var1[var2];
               var4[var5++] = -1;
               var2 += 3;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 3;
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               var4.put(var5, var1.get(var2 + 2));
               var4.put(var5 + 1, var1.get(var2 + 1));
               var4.put(var5 + 2, var1.get(var2));
               var4.put(var5 + 3, (byte)-1);
               var2 += 3;
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

   static class Getter implements BytePixelGetter {
      static final BytePixelGetter instance = new Getter();

      private Getter() {
      }

      public AlphaType getAlphaType() {
         return AlphaType.OPAQUE;
      }

      public int getNumElements() {
         return 3;
      }

      public int getArgb(byte[] var1, int var2) {
         return var1[var2 + 2] & 255 | (var1[var2 + 1] & 255) << 8 | (var1[var2] & 255) << 16 | -16777216;
      }

      public int getArgbPre(byte[] var1, int var2) {
         return var1[var2 + 2] & 255 | (var1[var2 + 1] & 255) << 8 | (var1[var2] & 255) << 16 | -16777216;
      }

      public int getArgb(ByteBuffer var1, int var2) {
         return var1.get(var2 + 2) & 255 | (var1.get(var2 + 1) & 255) << 8 | (var1.get(var2) & 255) << 16 | -16777216;
      }

      public int getArgbPre(ByteBuffer var1, int var2) {
         return var1.get(var2 + 2) & 255 | (var1.get(var2 + 1) & 255) << 8 | (var1.get(var2) & 255) << 16 | -16777216;
      }
   }
}
