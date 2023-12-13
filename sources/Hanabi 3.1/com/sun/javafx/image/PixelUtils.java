package com.sun.javafx.image;

import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteIndexed;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.General;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;

public class PixelUtils {
   private PixelUtils() {
   }

   public static int RgbToGray(int var0, int var1, int var2) {
      return (int)((double)var0 * 0.3 + (double)var1 * 0.59 + (double)var2 * 0.11);
   }

   public static int RgbToGray(int var0) {
      return RgbToGray(var0 >> 16 & 255, var0 >> 8 & 255, var0 & 255);
   }

   public static int NonPretoPre(int var0, int var1) {
      if (var1 == 255) {
         return var0;
      } else {
         return var1 == 0 ? 0 : (var0 * var1 + 127) / 255;
      }
   }

   public static int PreToNonPre(int var0, int var1) {
      if (var1 != 255 && var1 != 0) {
         return var0 >= var1 ? 255 : (var0 * 255 + (var1 >> 1)) / var1;
      } else {
         return var0;
      }
   }

   public static int NonPretoPre(int var0) {
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

   public static int PretoNonPre(int var0) {
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

   public static BytePixelGetter getByteGetter(PixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA:
            return ByteBgra.getter;
         case BYTE_BGRA_PRE:
            return ByteBgraPre.getter;
         case BYTE_RGB:
            return ByteRgb.getter;
         case BYTE_INDEXED:
            return ByteIndexed.createGetter(var0);
         case INT_ARGB:
         case INT_ARGB_PRE:
         default:
            return null;
      }
   }

   public static IntPixelGetter getIntGetter(PixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA:
         case BYTE_BGRA_PRE:
         case BYTE_RGB:
         case BYTE_INDEXED:
         default:
            return null;
         case INT_ARGB:
            return IntArgb.getter;
         case INT_ARGB_PRE:
            return IntArgbPre.getter;
      }
   }

   public static PixelGetter getGetter(PixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA:
         case BYTE_BGRA_PRE:
         case BYTE_RGB:
         case BYTE_INDEXED:
            return getByteGetter(var0);
         case INT_ARGB:
         case INT_ARGB_PRE:
            return getIntGetter(var0);
         default:
            return null;
      }
   }

   public static BytePixelSetter getByteSetter(WritablePixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA:
            return ByteBgra.setter;
         case BYTE_BGRA_PRE:
            return ByteBgraPre.setter;
         case BYTE_RGB:
         case BYTE_INDEXED:
         case INT_ARGB:
         case INT_ARGB_PRE:
         default:
            return null;
      }
   }

   public static IntPixelSetter getIntSetter(WritablePixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA:
         case BYTE_BGRA_PRE:
         case BYTE_RGB:
         case BYTE_INDEXED:
         default:
            return null;
         case INT_ARGB:
            return IntArgb.setter;
         case INT_ARGB_PRE:
            return IntArgbPre.setter;
      }
   }

   public static PixelSetter getSetter(WritablePixelFormat var0) {
      switch (var0.getType()) {
         case BYTE_BGRA:
         case BYTE_BGRA_PRE:
            return getByteSetter(var0);
         case BYTE_RGB:
         case BYTE_INDEXED:
         default:
            return null;
         case INT_ARGB:
         case INT_ARGB_PRE:
            return getIntSetter(var0);
      }
   }

   public static PixelConverter getConverter(PixelGetter var0, PixelSetter var1) {
      if (var0 instanceof BytePixelGetter) {
         return (PixelConverter)(var1 instanceof BytePixelSetter ? getB2BConverter((BytePixelGetter)var0, (BytePixelSetter)var1) : getB2IConverter((BytePixelGetter)var0, (IntPixelSetter)var1));
      } else {
         return (PixelConverter)(var1 instanceof BytePixelSetter ? getI2BConverter((IntPixelGetter)var0, (BytePixelSetter)var1) : getI2IConverter((IntPixelGetter)var0, (IntPixelSetter)var1));
      }
   }

   public static ByteToBytePixelConverter getB2BConverter(PixelGetter var0, PixelSetter var1) {
      if (var0 == ByteBgra.getter) {
         if (var1 == ByteBgra.setter) {
            return ByteBgra.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return ByteBgra.ToByteBgraPreConverter();
         }
      } else if (var0 == ByteBgraPre.getter) {
         if (var1 == ByteBgra.setter) {
            return ByteBgraPre.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return ByteBgraPre.ToByteBgraPreConverter();
         }
      } else if (var0 == ByteRgb.getter) {
         if (var1 == ByteBgra.setter) {
            return ByteRgb.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return ByteRgb.ToByteBgraPreConverter();
         }

         if (var1 == ByteBgr.setter) {
            return ByteRgb.ToByteBgrConverter();
         }
      } else if (var0 == ByteBgr.getter) {
         if (var1 == ByteBgr.setter) {
            return ByteBgr.ToByteBgrConverter();
         }

         if (var1 == ByteBgra.setter) {
            return ByteBgr.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return ByteBgr.ToByteBgraPreConverter();
         }
      } else if (var0 == ByteGray.getter) {
         if (var1 == ByteGray.setter) {
            return ByteGray.ToByteGrayConverter();
         }

         if (var1 == ByteBgr.setter) {
            return ByteGray.ToByteBgrConverter();
         }

         if (var1 == ByteBgra.setter) {
            return ByteGray.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return ByteGray.ToByteBgraPreConverter();
         }
      } else if (var0 instanceof ByteIndexed.Getter && (var1 == ByteBgra.setter || var1 == ByteBgraPre.setter)) {
         return ByteIndexed.createToByteBgraAny((BytePixelGetter)var0, (BytePixelSetter)var1);
      }

      if (var1 == ByteGray.setter) {
         return null;
      } else {
         return var0.getAlphaType() != AlphaType.OPAQUE && var1.getAlphaType() == AlphaType.OPAQUE ? null : General.create((BytePixelGetter)var0, (BytePixelSetter)var1);
      }
   }

   public static ByteToIntPixelConverter getB2IConverter(PixelGetter var0, PixelSetter var1) {
      if (var0 == ByteBgra.getter) {
         if (var1 == IntArgb.setter) {
            return ByteBgra.ToIntArgbConverter();
         }

         if (var1 == IntArgbPre.setter) {
            return ByteBgra.ToIntArgbPreConverter();
         }
      } else if (var0 == ByteBgraPre.getter) {
         if (var1 == IntArgb.setter) {
            return ByteBgraPre.ToIntArgbConverter();
         }

         if (var1 == IntArgbPre.setter) {
            return ByteBgraPre.ToIntArgbPreConverter();
         }
      } else if (var0 == ByteRgb.getter) {
         if (var1 == IntArgb.setter) {
            return ByteRgb.ToIntArgbConverter();
         }

         if (var1 == IntArgbPre.setter) {
            return ByteRgb.ToIntArgbPreConverter();
         }
      } else if (var0 == ByteBgr.getter) {
         if (var1 == IntArgb.setter) {
            return ByteBgr.ToIntArgbConverter();
         }

         if (var1 == IntArgbPre.setter) {
            return ByteBgr.ToIntArgbPreConverter();
         }
      } else if (var0 == ByteGray.getter) {
         if (var1 == IntArgbPre.setter) {
            return ByteGray.ToIntArgbPreConverter();
         }

         if (var1 == IntArgb.setter) {
            return ByteGray.ToIntArgbConverter();
         }
      } else if (var0 instanceof ByteIndexed.Getter && (var1 == IntArgb.setter || var1 == IntArgbPre.setter)) {
         return ByteIndexed.createToIntArgbAny((BytePixelGetter)var0, (IntPixelSetter)var1);
      }

      return var0.getAlphaType() != AlphaType.OPAQUE && var1.getAlphaType() == AlphaType.OPAQUE ? null : General.create((BytePixelGetter)var0, (IntPixelSetter)var1);
   }

   public static IntToBytePixelConverter getI2BConverter(PixelGetter var0, PixelSetter var1) {
      if (var0 == IntArgb.getter) {
         if (var1 == ByteBgra.setter) {
            return IntArgb.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return IntArgb.ToByteBgraPreConverter();
         }
      } else if (var0 == IntArgbPre.getter) {
         if (var1 == ByteBgra.setter) {
            return IntArgbPre.ToByteBgraConverter();
         }

         if (var1 == ByteBgraPre.setter) {
            return IntArgbPre.ToByteBgraPreConverter();
         }
      }

      if (var1 == ByteGray.setter) {
         return null;
      } else {
         return var0.getAlphaType() != AlphaType.OPAQUE && var1.getAlphaType() == AlphaType.OPAQUE ? null : General.create((IntPixelGetter)var0, (BytePixelSetter)var1);
      }
   }

   public static IntToIntPixelConverter getI2IConverter(PixelGetter var0, PixelSetter var1) {
      if (var0 == IntArgb.getter) {
         if (var1 == IntArgb.setter) {
            return IntArgb.ToIntArgbConverter();
         }

         if (var1 == IntArgbPre.setter) {
            return IntArgb.ToIntArgbPreConverter();
         }
      } else if (var0 == IntArgbPre.getter) {
         if (var1 == IntArgb.setter) {
            return IntArgbPre.ToIntArgbConverter();
         }

         if (var1 == IntArgbPre.setter) {
            return IntArgbPre.ToIntArgbPreConverter();
         }
      }

      return var0.getAlphaType() != AlphaType.OPAQUE && var1.getAlphaType() == AlphaType.OPAQUE ? null : General.create((IntPixelGetter)var0, (IntPixelSetter)var1);
   }
}
