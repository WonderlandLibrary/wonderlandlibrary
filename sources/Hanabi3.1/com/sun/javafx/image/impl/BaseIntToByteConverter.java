package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class BaseIntToByteConverter implements IntToBytePixelConverter {
   protected final IntPixelGetter getter;
   protected final BytePixelSetter setter;
   protected final int nDstElems;

   BaseIntToByteConverter(IntPixelGetter var1, BytePixelSetter var2) {
      this.getter = var1;
      this.setter = var2;
      this.nDstElems = var2.getNumElements();
   }

   public final IntPixelGetter getGetter() {
      return this.getter;
   }

   public final BytePixelSetter getSetter() {
      return this.setter;
   }

   abstract void doConvert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

   abstract void doConvert(IntBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);

   public final void convert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         this.doConvert(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public final void convert(IntBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7 * this.nDstElems) {
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

   public final void convert(IntBuffer var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         if (var1.hasArray()) {
            int[] var9 = var1.array();
            var2 += var1.arrayOffset();
            this.doConvert(var9, var2, var3, var4, var5, var6, var7, var8);
         } else {
            ByteBuffer var10 = ByteBuffer.wrap(var4);
            this.doConvert(var1, var2, var3, var10, var5, var6, var7, var8);
         }

      }
   }

   public final void convert(int[] var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
      if (var7 > 0 && var8 > 0) {
         if (var3 == var7 && var6 == var7 * this.nDstElems) {
            var7 *= var8;
            var8 = 1;
         }

         if (var4.hasArray()) {
            byte[] var9 = var4.array();
            var5 += var4.arrayOffset();
            this.doConvert(var1, var2, var3, var9, var5, var6, var7, var8);
         } else {
            IntBuffer var10 = IntBuffer.wrap(var1);
            this.doConvert(var10, var2, var3, var4, var5, var6, var7, var8);
         }

      }
   }
}
