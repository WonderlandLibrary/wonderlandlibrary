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

public class ByteBgr {
   public static final BytePixelGetter getter;
   public static final BytePixelSetter setter;
   public static final BytePixelAccessor accessor;
   private static ByteToBytePixelConverter ToByteBgrObj;

   public static ByteToBytePixelConverter ToByteBgrConverter() {
      if (ToByteBgrObj == null) {
         ToByteBgrObj = BaseByteToByteConverter.create(accessor);
      }

      return ToByteBgrObj;
   }

   public static ByteToBytePixelConverter ToByteBgraConverter() {
      return ByteBgr.ToByteBgrfConv.nonpremult;
   }

   public static ByteToBytePixelConverter ToByteBgraPreConverter() {
      return ByteBgr.ToByteBgrfConv.premult;
   }

   public static ByteToIntPixelConverter ToIntArgbConverter() {
      return ByteBgr.ToIntFrgbConv.nonpremult;
   }

   public static ByteToIntPixelConverter ToIntArgbPreConverter() {
      return ByteBgr.ToIntFrgbConv.premult;
   }

   public static ByteToBytePixelConverter ToByteArgbConverter() {
      return ByteBgr.ToByteFrgbConv.nonpremult;
   }

   static {
      getter = ByteBgr.Accessor.instance;
      setter = ByteBgr.Accessor.instance;
      accessor = ByteBgr.Accessor.instance;
   }

   static class ToByteFrgbConv extends BaseByteToByteConverter {
      static final ByteToBytePixelConverter nonpremult;

      private ToByteFrgbConv(BytePixelSetter var1) {
         super(ByteBgr.getter, var1);
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
               var4.put(var5++, (byte)-1);
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
         nonpremult = new ToByteFrgbConv(ByteArgb.setter);
      }
   }

   static class ToIntFrgbConv extends BaseByteToIntConverter {
      public static final ByteToIntPixelConverter nonpremult;
      public static final ByteToIntPixelConverter premult;

      private ToIntFrgbConv(IntPixelSetter var1) {
         super(ByteBgr.getter, var1);
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
               var4[var5 + var9] = -16777216 | var12 << 16 | var11 << 8 | var10;
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
               int var10 = var1.get(var2++) & 255;
               int var11 = var1.get(var2++) & 255;
               int var12 = var1.get(var2++) & 255;
               var4.put(var5 + var9, -16777216 | var12 << 16 | var11 << 8 | var10);
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
         super(ByteBgr.getter, var1);
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
               var4[var5++] = var1[var2++];
               var4[var5++] = var1[var2++];
               var4[var5++] = var1[var2++];
               var4[var5++] = -1;
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
               var4.put(var5++, var1.get(var2++));
               var4.put(var5++, var1.get(var2++));
               var4.put(var5++, var1.get(var2++));
               var4.put(var5++, (byte)-1);
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
         return 3;
      }

      public int getArgb(byte[] var1, int var2) {
         return var1[var2] & 255 | (var1[var2 + 1] & 255) << 8 | (var1[var2 + 2] & 255) << 16 | -16777216;
      }

      public int getArgbPre(byte[] var1, int var2) {
         return var1[var2] & 255 | (var1[var2 + 1] & 255) << 8 | (var1[var2 + 2] & 255) << 16 | -16777216;
      }

      public int getArgb(ByteBuffer var1, int var2) {
         return var1.get(var2) & 255 | (var1.get(var2 + 1) & 255) << 8 | (var1.get(var2 + 2) & 255) << 16 | -16777216;
      }

      public int getArgbPre(ByteBuffer var1, int var2) {
         return var1.get(var2) & 255 | (var1.get(var2 + 1) & 255) << 8 | (var1.get(var2 + 2) & 255) << 16 | -16777216;
      }

      public void setArgb(byte[] var1, int var2, int var3) {
         var1[var2] = (byte)var3;
         var1[var2 + 1] = (byte)(var3 >> 8);
         var1[var2 + 2] = (byte)(var3 >> 16);
      }

      public void setArgbPre(byte[] var1, int var2, int var3) {
         this.setArgb(var1, var2, PixelUtils.PretoNonPre(var3));
      }

      public void setArgb(ByteBuffer var1, int var2, int var3) {
         var1.put(var2, (byte)var3);
         var1.put(var2 + 1, (byte)(var3 >> 8));
         var1.put(var2 + 2, (byte)(var3 >> 16));
      }

      public void setArgbPre(ByteBuffer var1, int var2, int var3) {
         this.setArgb(var1, var2, PixelUtils.PretoNonPre(var3));
      }
   }
}
