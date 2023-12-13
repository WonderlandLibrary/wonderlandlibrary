package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class BaseByteToIntConverter implements ByteToIntPixelConverter {
   protected final BytePixelGetter getter;
   protected final IntPixelSetter setter;
   protected final int nSrcElems;

   BaseByteToIntConverter(BytePixelGetter var1, IntPixelSetter var2) {
      this.getter = var1;
      this.setter = var2;
      this.nSrcElems = var1.getNumElements();
   }

   public final BytePixelGetter getGetter() {
      return this.getter;
   }

   public final IntPixelSetter getSetter() {
      return this.setter;
   }

   abstract void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

   abstract void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);

   public final void convert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7) {
            var7 *= var8;
            var8 = 1;
         }

         this.doConvert(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public final void convert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7) {
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

   public final void convert(ByteBuffer var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7) {
            var7 *= var8;
            var8 = 1;
         }

         if (var1.hasArray()) {
            byte[] var9 = var1.array();
            var2 += var1.arrayOffset();
            this.doConvert(var9, var2, var3, var4, var5, var6, var7, var8);
         } else {
            IntBuffer var10 = IntBuffer.wrap(var4);
            this.doConvert(var1, var2, var3, var10, var5, var6, var7, var8);
         }

      }
   }

   public final void convert(byte[] var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 * this.nSrcElems && var6 == var7) {
            var7 *= var8;
            var8 = 1;
         }

         if (var4.hasArray()) {
            int[] var9 = var4.array();
            var5 += var4.arrayOffset();
            this.doConvert(var1, var2, var3, var9, var5, var6, var7, var8);
         } else {
            ByteBuffer var10 = ByteBuffer.wrap(var1);
            this.doConvert(var10, var2, var3, var4, var5, var6, var7, var8);
         }

      }
   }
}
