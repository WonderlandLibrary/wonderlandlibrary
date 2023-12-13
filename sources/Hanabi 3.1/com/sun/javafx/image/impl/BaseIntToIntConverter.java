package com.sun.javafx.image.impl;

import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToIntPixelConverter;
import java.nio.IntBuffer;

public abstract class BaseIntToIntConverter implements IntToIntPixelConverter {
   protected final IntPixelGetter getter;
   protected final IntPixelSetter setter;

   public BaseIntToIntConverter(IntPixelGetter var1, IntPixelSetter var2) {
      this.getter = var1;
      this.setter = var2;
   }

   public final IntPixelGetter getGetter() {
      return this.getter;
   }

   public final IntPixelSetter getSetter() {
      return this.setter;
   }

   abstract void doConvert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

   abstract void doConvert(IntBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);

   public final void convert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7) {
            var7 *= var8;
            var8 = 1;
         }

         this.doConvert(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public final void convert(IntBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7) {
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

   public final void convert(IntBuffer var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7) {
            var7 *= var8;
            var8 = 1;
         }

         if (var1.hasArray()) {
            int[] var9 = var1.array();
            var2 += var1.arrayOffset();
            this.doConvert(var9, var2, var3, var4, var5, var6, var7, var8);
         } else {
            IntBuffer var10 = IntBuffer.wrap(var4);
            this.doConvert(var1, var2, var3, var10, var5, var6, var7, var8);
         }

      }
   }

   public final void convert(int[] var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7) {
            var7 *= var8;
            var8 = 1;
         }

         if (var4.hasArray()) {
            int[] var9 = var4.array();
            var5 += var4.arrayOffset();
            this.doConvert(var1, var2, var3, var9, var5, var6, var7, var8);
         } else {
            IntBuffer var10 = IntBuffer.wrap(var1);
            this.doConvert(var10, var2, var3, var4, var5, var6, var7, var8);
         }

      }
   }

   static IntToIntPixelConverter create(IntPixelAccessor var0) {
      return new IntAnyToSameConverter(var0);
   }

   static class IntAnyToSameConverter extends BaseIntToIntConverter {
      IntAnyToSameConverter(IntPixelAccessor var1) {
         super(var1, var1);
      }

      void doConvert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            System.arraycopy(var1, var2, var4, var5, var7);
            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(IntBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         int var9 = var1.limit();
         int var10 = var1.position();
         int var11 = var4.position();

         try {
            while(true) {
               --var8;
               if (var8 < 0) {
                  return;
               }

               int var12 = var2 + var7;
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
