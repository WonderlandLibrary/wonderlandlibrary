package javafx.scene.image;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class PixelFormat {
   private Type type;

   PixelFormat(Type var1) {
      this.type = var1;
   }

   public static WritablePixelFormat getIntArgbInstance() {
      return WritablePixelFormat.IntArgb.INSTANCE;
   }

   public static WritablePixelFormat getIntArgbPreInstance() {
      return WritablePixelFormat.IntArgbPre.INSTANCE;
   }

   public static WritablePixelFormat getByteBgraInstance() {
      return WritablePixelFormat.ByteBgra.INSTANCE;
   }

   public static WritablePixelFormat getByteBgraPreInstance() {
      return WritablePixelFormat.ByteBgraPre.INSTANCE;
   }

   public static PixelFormat getByteRgbInstance() {
      return PixelFormat.ByteRgb.instance;
   }

   public static PixelFormat createByteIndexedPremultipliedInstance(int[] var0) {
      return PixelFormat.IndexedPixelFormat.createByte(var0, true);
   }

   public static PixelFormat createByteIndexedInstance(int[] var0) {
      return PixelFormat.IndexedPixelFormat.createByte(var0, false);
   }

   public Type getType() {
      return this.type;
   }

   public abstract boolean isWritable();

   public abstract boolean isPremultiplied();

   static int NonPretoPre(int var0) {
      int var1 = var0 >>> 24;
      if (var1 == 255) {
         return var0;
      } else if (var1 == 0) {
         return 0;
      } else {
         int var2 = var0 >> 16 & 255;
         int var3 = var0 >> 8 & 255;
         int var4 = var0 & 255;
         var2 = (var2 * var1 + 127) / 255;
         var3 = (var3 * var1 + 127) / 255;
         var4 = (var4 * var1 + 127) / 255;
         return var1 << 24 | var2 << 16 | var3 << 8 | var4;
      }
   }

   static int PretoNonPre(int var0) {
      int var1 = var0 >>> 24;
      if (var1 != 255 && var1 != 0) {
         int var2 = var0 >> 16 & 255;
         int var3 = var0 >> 8 & 255;
         int var4 = var0 & 255;
         int var5 = var1 >> 1;
         var2 = var2 >= var1 ? 255 : (var2 * 255 + var5) / var1;
         var3 = var3 >= var1 ? 255 : (var3 * 255 + var5) / var1;
         var4 = var4 >= var1 ? 255 : (var4 * 255 + var5) / var1;
         return var1 << 24 | var2 << 16 | var3 << 8 | var4;
      } else {
         return var0;
      }
   }

   public abstract int getArgb(Buffer var1, int var2, int var3, int var4);

   static class IndexedPixelFormat extends PixelFormat {
      int[] precolors;
      int[] nonprecolors;
      boolean premult;

      static PixelFormat createByte(int[] var0, boolean var1) {
         return new IndexedPixelFormat(PixelFormat.Type.BYTE_INDEXED, var1, Arrays.copyOf(var0, 256));
      }

      private IndexedPixelFormat(Type var1, boolean var2, int[] var3) {
         super(var1);
         if (var2) {
            this.precolors = var3;
         } else {
            this.nonprecolors = var3;
         }

         this.premult = var2;
      }

      public boolean isWritable() {
         return false;
      }

      public boolean isPremultiplied() {
         return this.premult;
      }

      int[] getPreColors() {
         if (this.precolors == null) {
            int[] var1 = new int[this.nonprecolors.length];

            for(int var2 = 0; var2 < var1.length; ++var2) {
               var1[var2] = NonPretoPre(this.nonprecolors[var2]);
            }

            this.precolors = var1;
         }

         return this.precolors;
      }

      int[] getNonPreColors() {
         if (this.nonprecolors == null) {
            int[] var1 = new int[this.precolors.length];

            for(int var2 = 0; var2 < var1.length; ++var2) {
               var1[var2] = PretoNonPre(this.precolors[var2]);
            }

            this.nonprecolors = var1;
         }

         return this.nonprecolors;
      }

      public int getArgb(ByteBuffer var1, int var2, int var3, int var4) {
         return this.getNonPreColors()[var1.get(var3 * var4 + var2) & 255];
      }
   }

   static class ByteRgb extends PixelFormat {
      static final ByteRgb instance = new ByteRgb();

      private ByteRgb() {
         super(PixelFormat.Type.BYTE_RGB);
      }

      public boolean isWritable() {
         return true;
      }

      public boolean isPremultiplied() {
         return false;
      }

      public int getArgb(ByteBuffer var1, int var2, int var3, int var4) {
         int var5 = var3 * var4 + var2 * 3;
         int var6 = var1.get(var5) & 255;
         int var7 = var1.get(var5 + 1) & 255;
         int var8 = var1.get(var5 + 2) & 255;
         return -16777216 | var6 << 16 | var7 << 8 | var8;
      }
   }

   public static enum Type {
      INT_ARGB_PRE,
      INT_ARGB,
      BYTE_BGRA_PRE,
      BYTE_BGRA,
      BYTE_RGB,
      BYTE_INDEXED;
   }
}
