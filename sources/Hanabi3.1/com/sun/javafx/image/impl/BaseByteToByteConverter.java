package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import java.nio.ByteBuffer;

abstract class BaseByteToByteConverter implements ByteToBytePixelConverter {
   protected final BytePixelGetter getter;
   protected final BytePixelSetter setter;
   protected final int nSrcElems;
   protected final int nDstElems;

   BaseByteToByteConverter(BytePixelGetter var1, BytePixelSetter var2) {
      this.getter = var1;
      this.setter = var2;
      this.nSrcElems = var1.getNumElements();
      this.nDstElems = var2.getNumElements();
   }

   public final BytePixelGetter getGetter() {
      return this.getter;
   }

   public final BytePixelSetter getSetter() {
      return this.setter;
   }

   abstract void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

   abstract void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);

   public final void convert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         this.doConvert(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public final void convert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         if (var1.hasArray() && var4.hasArray()) {
            var2 += var1.arrayOffset();
            var5 += var4.arrayOffset();
            this.doConvert(var1.array(), var2, var3, var4.array(), var5, var6, var7, var8);
         } else {
            this.doConvert(var1, var2, var3, var4, var5, var6, var7, var8);
         }

      }
   }

   public final void convert(ByteBuffer var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         if (var1.hasArray()) {
            byte[] var9 = var1.array();
            var2 += var1.arrayOffset();
            this.doConvert(var9, var2, var3, var4, var5, var6, var7, var8);
         } else {
            ByteBuffer var10 = ByteBuffer.wrap(var4);
            this.doConvert(var1, var2, var3, var10, var5, var6, var7, var8);
         }

      }
   }

   public final void convert(byte[] var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         if (var4.hasArray()) {
            byte[] var9 = var4.array();
            var5 += var4.arrayOffset();
            this.doConvert(var1, var2, var3, var9, var5, var6, var7, var8);
         } else {
            ByteBuffer var10 = ByteBuffer.wrap(var1);
            this.doConvert(var10, var2, var3, var4, var5, var6, var7, var8);
         }

      }
   }

   static ByteToBytePixelConverter create(BytePixelAccessor var0) {
      return new ByteAnyToSameConverter(var0);
   }

   public static ByteToBytePixelConverter createReorderer(BytePixelGetter var0, BytePixelSetter var1, int var2, int var3, int var4, int var5) {
      return new FourByteReorderer(var0, var1, var2, var3, var4, var5);
   }

   static class FourByteReorderer extends BaseByteToByteConverter {
      private final int c0;
      private final int c1;
      private final int c2;
      private final int c3;

      FourByteReorderer(BytePixelGetter var1, BytePixelSetter var2, int var3, int var4, int var5, int var6) {
         super(var1, var2);
         this.c0 = var3;
         this.c1 = var4;
         this.c2 = var5;
         this.c3 = var6;
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
               byte var10 = var1[var2 + this.c0];
               byte var11 = var1[var2 + this.c1];
               byte var12 = var1[var2 + this.c2];
               byte var13 = var1[var2 + this.c3];
               var4[var5++] = var10;
               var4[var5++] = var11;
               var4[var5++] = var12;
               var4[var5++] = var13;
               var2 += 4;
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
               byte var10 = var1.get(var2 + this.c0);
               byte var11 = var1.get(var2 + this.c1);
               byte var12 = var1.get(var2 + this.c2);
               byte var13 = var1.get(var2 + this.c3);
               var4.put(var5, var10);
               var4.put(var5 + 1, var11);
               var4.put(var5 + 2, var12);
               var4.put(var5 + 3, var13);
               var2 += 4;
               var5 += 4;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   static class ByteAnyToSameConverter extends BaseByteToByteConverter {
      ByteAnyToSameConverter(BytePixelAccessor var1) {
         super(var1, var1);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            System.arraycopy(var1, var2, var4, var5, var7 * this.nSrcElems);
            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         int var9 = var1.limit();
         int var10 = var1.position();
         int var11 = var4.position();

         try {
            while(true) {
               --var8;
               if (var8 < 0) {
                  return;
               }

               int var12 = var2 + var7 * this.nSrcElems;
               if (var12 > var9) {
                  throw new IndexOutOfBoundsException("" + var9);
               }

               var1.limit(var12);
               var1.position(var2);
               var4.position(var5);
               var4.put(var1);
               var2 += var3;
               var5 += var6;
            }
         } finally {
            var1.limit(var9);
            var1.position(var10);
            var4.position(var11);
         }
      }
   }
}
