package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.PixelUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ByteBgraPre {
   public static final BytePixelGetter getter;
   public static final BytePixelSetter setter;
   public static final BytePixelAccessor accessor;
   private static ByteToBytePixelConverter ToByteBgraPreObj;

   public static ByteToBytePixelConverter ToByteBgraConverter() {
      return ByteBgraPre.ToByteBgraConv.instance;
   }

   public static ByteToBytePixelConverter ToByteBgraPreConverter() {
      if (ToByteBgraPreObj == null) {
         ToByteBgraPreObj = BaseByteToByteConverter.create(accessor);
      }

      return ToByteBgraPreObj;
   }

   public static ByteToIntPixelConverter ToIntArgbConverter() {
      return ByteBgraPre.ToIntArgbConv.instance;
   }

   public static ByteToIntPixelConverter ToIntArgbPreConverter() {
      return ByteBgra.ToIntArgbSameConv.premul;
   }

   static {
      getter = ByteBgraPre.Accessor.instance;
      setter = ByteBgraPre.Accessor.instance;
      accessor = ByteBgraPre.Accessor.instance;
   }

   public static class ToIntArgbConv extends BaseByteToIntConverter {
      public static final ByteToIntPixelConverter instance = new ToIntArgbConv();

      private ToIntArgbConv() {
         super(ByteBgraPre.getter, IntArgb.setter);
      }

      void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 4;
         var6 -= var7;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1[var2++] & 255;
               int var11 = var1[var2++] & 255;
               int var12 = var1[var2++] & 255;
               int var13 = var1[var2++] & 255;
               if (var13 > 0 && var13 < 255) {
                  int var14 = var13 >> 1;
                  var12 = (var12 * 255 + var14) / var13;
                  var11 = (var11 * 255 + var14) / var13;
                  var10 = (var10 * 255 + var14) / var13;
               }

               var4[var5++] = var13 << 24 | var12 << 16 | var11 << 8 | var10;
            }

            var5 += var6;
            var2 += var3;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               int var10 = var1.get(var2) & 255;
               int var11 = var1.get(var2 + 1) & 255;
               int var12 = var1.get(var2 + 2) & 255;
               int var13 = var1.get(var2 + 3) & 255;
               var2 += 4;
               if (var13 > 0 && var13 < 255) {
                  int var14 = var13 >> 1;
                  var12 = (var12 * 255 + var14) / var13;
                  var11 = (var11 * 255 + var14) / var13;
                  var10 = (var10 * 255 + var14) / var13;
               }

               var4.put(var5 + var9, var13 << 24 | var12 << 16 | var11 << 8 | var10);
            }

            var5 += var6;
            var2 += var3;
         }
      }
   }

   public static class ToByteBgraConv extends BaseByteToByteConverter {
      public static final ByteToBytePixelConverter instance = new ToByteBgraConv();

      private ToByteBgraConv() {
         super(ByteBgraPre.getter, ByteBgra.setter);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 4;
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = var1[var2++];
               byte var11 = var1[var2++];
               byte var12 = var1[var2++];
               int var13 = var1[var2++] & 255;
               if (var13 > 0 && var13 < 255) {
                  int var14 = var13 >> 1;
                  var10 = (byte)(((var10 & 255) * 255 + var14) / var13);
                  var11 = (byte)(((var11 & 255) * 255 + var14) / var13);
                  var12 = (byte)(((var12 & 255) * 255 + var14) / var13);
               }

               var4[var5++] = var10;
               var4[var5++] = var11;
               var4[var5++] = var12;
               var4[var5++] = (byte)var13;
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         var3 -= var7 * 4;
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var9 = 0; var9 < var7; ++var9) {
               byte var10 = var1.get(var2);
               byte var11 = var1.get(var2 + 1);
               byte var12 = var1.get(var2 + 2);
               int var13 = var1.get(var2 + 3) & 255;
               var2 += 4;
               if (var13 > 0 && var13 < 255) {
                  int var14 = var13 >> 1;
                  var10 = (byte)(((var10 & 255) * 255 + var14) / var13);
                  var11 = (byte)(((var11 & 255) * 255 + var14) / var13);
                  var12 = (byte)(((var12 & 255) * 255 + var14) / var13);
               }

               var4.put(var5, var10);
               var4.put(var5 + 1, var11);
               var4.put(var5 + 2, var12);
               var4.put(var5 + 3, (byte)var13);
               var5 += 4;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class Accessor implements BytePixelAccessor {
      static final BytePixelAccessor instance = new Accessor();

      private Accessor() {
      }

      public AlphaType getAlphaType() {
         return AlphaType.PREMULTIPLIED;
      }

      public int getNumElements() {
         return 4;
      }

      public int getArgb(byte[] var1, int var2) {
         return PixelUtils.PretoNonPre(this.getArgbPre(var1, var2));
      }

      public int getArgbPre(byte[] var1, int var2) {
         return var1[var2] & 255 | (var1[var2 + 1] & 255) << 8 | (var1[var2 + 2] & 255) << 16 | var1[var2 + 3] << 24;
      }

      public int getArgb(ByteBuffer var1, int var2) {
         return PixelUtils.PretoNonPre(this.getArgbPre(var1, var2));
      }

      public int getArgbPre(ByteBuffer var1, int var2) {
         return var1.get(var2) & 255 | (var1.get(var2 + 1) & 255) << 8 | (var1.get(var2 + 2) & 255) << 16 | var1.get(var2 + 3) << 24;
      }

      public void setArgb(byte[] var1, int var2, int var3) {
         this.setArgbPre(var1, var2, PixelUtils.NonPretoPre(var3));
      }

      public void setArgbPre(byte[] var1, int var2, int var3) {
         var1[var2] = (byte)var3;
         var1[var2 + 1] = (byte)(var3 >> 8);
         var1[var2 + 2] = (byte)(var3 >> 16);
         var1[var2 + 3] = (byte)(var3 >> 24);
      }

      public void setArgb(ByteBuffer var1, int var2, int var3) {
         this.setArgbPre(var1, var2, PixelUtils.NonPretoPre(var3));
      }

      public void setArgbPre(ByteBuffer var1, int var2, int var3) {
         var1.put(var2, (byte)var3);
         var1.put(var2 + 1, (byte)(var3 >> 8));
         var1.put(var2 + 2, (byte)(var3 >> 16));
         var1.put(var2 + 3, (byte)(var3 >> 24));
      }
   }
}
