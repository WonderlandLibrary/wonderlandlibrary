package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.tk.Toolkit;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;

public class ByteIndexed {
   public static BytePixelGetter createGetter(PixelFormat var0) {
      return new Getter(var0);
   }

   public static ByteToBytePixelConverter createToByteBgraAny(BytePixelGetter var0, BytePixelSetter var1) {
      return new ToByteBgraAnyConverter(var0, var1);
   }

   public static ByteToIntPixelConverter createToIntArgbAny(BytePixelGetter var0, IntPixelSetter var1) {
      return new ToIntArgbAnyConverter(var0, var1);
   }

   static int[] getColors(BytePixelGetter var0, PixelSetter var1) {
      Getter var2 = (Getter)var0;
      return var1.getAlphaType() == AlphaType.PREMULTIPLIED ? var2.getPreColors() : var2.getNonPreColors();
   }

   public static class ToIntArgbAnyConverter extends BaseByteToIntConverter {
      public ToIntArgbAnyConverter(BytePixelGetter var1, IntPixelSetter var2) {
         super(var1, var2);
      }

      void doConvert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8) {
         int[] var9 = ByteIndexed.getColors(this.getGetter(), this.getSetter());

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var10 = 0; var10 < var7; ++var10) {
               var4[var5 + var10] = var9[var1[var2 + var10] & 255];
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8) {
         int[] var9 = ByteIndexed.getColors(this.getGetter(), this.getSetter());

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var10 = 0; var10 < var7; ++var10) {
               var4.put(var5 + var10, var9[var1.get(var2 + var10) & 255]);
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   public static class ToByteBgraAnyConverter extends BaseByteToByteConverter {
      public ToByteBgraAnyConverter(BytePixelGetter var1, BytePixelSetter var2) {
         super(var1, var2);
      }

      void doConvert(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
         int[] var9 = ByteIndexed.getColors(this.getGetter(), this.getSetter());
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var10 = 0; var10 < var7; ++var10) {
               int var11 = var9[var1[var2 + var10] & 255];
               var4[var5++] = (byte)var11;
               var4[var5++] = (byte)(var11 >> 8);
               var4[var5++] = (byte)(var11 >> 16);
               var4[var5++] = (byte)(var11 >> 24);
            }

            var2 += var3;
            var5 += var6;
         }
      }

      void doConvert(ByteBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
         int[] var9 = ByteIndexed.getColors(this.getGetter(), this.getSetter());
         var6 -= var7 * 4;

         while(true) {
            --var8;
            if (var8 < 0) {
               return;
            }

            for(int var10 = 0; var10 < var7; ++var10) {
               int var11 = var9[var1.get(var2 + var10) & 255];
               var4.put(var5, (byte)var11);
               var4.put(var5 + 1, (byte)(var11 >> 8));
               var4.put(var5 + 2, (byte)(var11 >> 16));
               var4.put(var5 + 3, (byte)(var11 >> 24));
               var5 += 4;
            }

            var2 += var3;
            var5 += var6;
         }
      }
   }

   public static class Getter implements BytePixelGetter {
      PixelFormat theFormat;
      private int[] precolors;
      private int[] nonprecolors;

      Getter(PixelFormat var1) {
         this.theFormat = var1;
      }

      int[] getPreColors() {
         if (this.precolors == null) {
            this.precolors = Toolkit.getImageAccessor().getPreColors(this.theFormat);
         }

         return this.precolors;
      }

      int[] getNonPreColors() {
         if (this.nonprecolors == null) {
            this.nonprecolors = Toolkit.getImageAccessor().getNonPreColors(this.theFormat);
         }

         return this.nonprecolors;
      }

      public AlphaType getAlphaType() {
         return this.theFormat.isPremultiplied() ? AlphaType.PREMULTIPLIED : AlphaType.NONPREMULTIPLIED;
      }

      public int getNumElements() {
         return 1;
      }

      public int getArgb(byte[] var1, int var2) {
         return this.getNonPreColors()[var1[var2] & 255];
      }

      public int getArgbPre(byte[] var1, int var2) {
         return this.getPreColors()[var1[var2] & 255];
      }

      public int getArgb(ByteBuffer var1, int var2) {
         return this.getNonPreColors()[var1.get(var2) & 255];
      }

      public int getArgbPre(ByteBuffer var1, int var2) {
         return this.getPreColors()[var1.get(var2) & 255];
      }
   }
}
