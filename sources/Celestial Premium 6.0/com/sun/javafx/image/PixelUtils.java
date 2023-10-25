/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.PixelFormat
 *  javafx.scene.image.WritablePixelFormat
 */
package com.sun.javafx.image;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteIndexed;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.General;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;

public class PixelUtils {
    private PixelUtils() {
    }

    public static int RgbToGray(int n, int n2, int n3) {
        return (int)((double)n * 0.3 + (double)n2 * 0.59 + (double)n3 * 0.11);
    }

    public static int RgbToGray(int n) {
        return PixelUtils.RgbToGray(n >> 16 & 0xFF, n >> 8 & 0xFF, n & 0xFF);
    }

    public static int NonPretoPre(int n, int n2) {
        if (n2 == 255) {
            return n;
        }
        if (n2 == 0) {
            return 0;
        }
        return (n * n2 + 127) / 255;
    }

    public static int PreToNonPre(int n, int n2) {
        if (n2 == 255 || n2 == 0) {
            return n;
        }
        return n >= n2 ? 255 : (n * 255 + (n2 >> 1)) / n2;
    }

    public static int NonPretoPre(int n) {
        int n2 = n >>> 24;
        if (n2 == 255) {
            return n;
        }
        if (n2 == 0) {
            return 0;
        }
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        n3 = (n3 * n2 + 127) / 255;
        n4 = (n4 * n2 + 127) / 255;
        n5 = (n5 * n2 + 127) / 255;
        return n2 << 24 | n3 << 16 | n4 << 8 | n5;
    }

    public static int PretoNonPre(int n) {
        int n2 = n >>> 24;
        if (n2 == 255 || n2 == 0) {
            return n;
        }
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        int n6 = n2 >> 1;
        n3 = n3 >= n2 ? 255 : (n3 * 255 + n6) / n2;
        n4 = n4 >= n2 ? 255 : (n4 * 255 + n6) / n2;
        n5 = n5 >= n2 ? 255 : (n5 * 255 + n6) / n2;
        return n2 << 24 | n3 << 16 | n4 << 8 | n5;
    }

    public static BytePixelGetter getByteGetter(PixelFormat<ByteBuffer> pixelFormat) {
        switch (pixelFormat.getType()) {
            case BYTE_BGRA: {
                return ByteBgra.getter;
            }
            case BYTE_BGRA_PRE: {
                return ByteBgraPre.getter;
            }
            case BYTE_RGB: {
                return ByteRgb.getter;
            }
            case BYTE_INDEXED: {
                return ByteIndexed.createGetter(pixelFormat);
            }
        }
        return null;
    }

    public static IntPixelGetter getIntGetter(PixelFormat<IntBuffer> pixelFormat) {
        switch (pixelFormat.getType()) {
            case INT_ARGB: {
                return IntArgb.getter;
            }
            case INT_ARGB_PRE: {
                return IntArgbPre.getter;
            }
        }
        return null;
    }

    public static <T extends Buffer> PixelGetter<T> getGetter(PixelFormat<T> pixelFormat) {
        switch (pixelFormat.getType()) {
            case BYTE_BGRA: 
            case BYTE_BGRA_PRE: 
            case BYTE_RGB: 
            case BYTE_INDEXED: {
                return PixelUtils.getByteGetter(pixelFormat);
            }
            case INT_ARGB: 
            case INT_ARGB_PRE: {
                return PixelUtils.getIntGetter(pixelFormat);
            }
        }
        return null;
    }

    public static BytePixelSetter getByteSetter(WritablePixelFormat<ByteBuffer> writablePixelFormat) {
        switch (writablePixelFormat.getType()) {
            case BYTE_BGRA: {
                return ByteBgra.setter;
            }
            case BYTE_BGRA_PRE: {
                return ByteBgraPre.setter;
            }
        }
        return null;
    }

    public static IntPixelSetter getIntSetter(WritablePixelFormat<IntBuffer> writablePixelFormat) {
        switch (writablePixelFormat.getType()) {
            case INT_ARGB: {
                return IntArgb.setter;
            }
            case INT_ARGB_PRE: {
                return IntArgbPre.setter;
            }
        }
        return null;
    }

    public static <T extends Buffer> PixelSetter<T> getSetter(WritablePixelFormat<T> writablePixelFormat) {
        switch (writablePixelFormat.getType()) {
            case BYTE_BGRA: 
            case BYTE_BGRA_PRE: {
                return PixelUtils.getByteSetter(writablePixelFormat);
            }
            case INT_ARGB: 
            case INT_ARGB_PRE: {
                return PixelUtils.getIntSetter(writablePixelFormat);
            }
        }
        return null;
    }

    public static <T extends Buffer, U extends Buffer> PixelConverter<T, U> getConverter(PixelGetter<T> pixelGetter, PixelSetter<U> pixelSetter) {
        if (pixelGetter instanceof BytePixelGetter) {
            if (pixelSetter instanceof BytePixelSetter) {
                return PixelUtils.getB2BConverter((BytePixelGetter)pixelGetter, (BytePixelSetter)pixelSetter);
            }
            return PixelUtils.getB2IConverter((BytePixelGetter)pixelGetter, (IntPixelSetter)pixelSetter);
        }
        if (pixelSetter instanceof BytePixelSetter) {
            return PixelUtils.getI2BConverter((IntPixelGetter)pixelGetter, (BytePixelSetter)pixelSetter);
        }
        return PixelUtils.getI2IConverter((IntPixelGetter)pixelGetter, (IntPixelSetter)pixelSetter);
    }

    public static ByteToBytePixelConverter getB2BConverter(PixelGetter<ByteBuffer> pixelGetter, PixelSetter<ByteBuffer> pixelSetter) {
        if (pixelGetter == ByteBgra.getter) {
            if (pixelSetter == ByteBgra.setter) {
                return ByteBgra.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return ByteBgra.ToByteBgraPreConverter();
            }
        } else if (pixelGetter == ByteBgraPre.getter) {
            if (pixelSetter == ByteBgra.setter) {
                return ByteBgraPre.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return ByteBgraPre.ToByteBgraPreConverter();
            }
        } else if (pixelGetter == ByteRgb.getter) {
            if (pixelSetter == ByteBgra.setter) {
                return ByteRgb.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return ByteRgb.ToByteBgraPreConverter();
            }
            if (pixelSetter == ByteBgr.setter) {
                return ByteRgb.ToByteBgrConverter();
            }
        } else if (pixelGetter == ByteBgr.getter) {
            if (pixelSetter == ByteBgr.setter) {
                return ByteBgr.ToByteBgrConverter();
            }
            if (pixelSetter == ByteBgra.setter) {
                return ByteBgr.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return ByteBgr.ToByteBgraPreConverter();
            }
        } else if (pixelGetter == ByteGray.getter) {
            if (pixelSetter == ByteGray.setter) {
                return ByteGray.ToByteGrayConverter();
            }
            if (pixelSetter == ByteBgr.setter) {
                return ByteGray.ToByteBgrConverter();
            }
            if (pixelSetter == ByteBgra.setter) {
                return ByteGray.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return ByteGray.ToByteBgraPreConverter();
            }
        } else if (pixelGetter instanceof ByteIndexed.Getter && (pixelSetter == ByteBgra.setter || pixelSetter == ByteBgraPre.setter)) {
            return ByteIndexed.createToByteBgraAny((BytePixelGetter)pixelGetter, (BytePixelSetter)pixelSetter);
        }
        if (pixelSetter == ByteGray.setter) {
            return null;
        }
        if (pixelGetter.getAlphaType() != AlphaType.OPAQUE && pixelSetter.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((BytePixelGetter)pixelGetter, (BytePixelSetter)pixelSetter);
    }

    public static ByteToIntPixelConverter getB2IConverter(PixelGetter<ByteBuffer> pixelGetter, PixelSetter<IntBuffer> pixelSetter) {
        if (pixelGetter == ByteBgra.getter) {
            if (pixelSetter == IntArgb.setter) {
                return ByteBgra.ToIntArgbConverter();
            }
            if (pixelSetter == IntArgbPre.setter) {
                return ByteBgra.ToIntArgbPreConverter();
            }
        } else if (pixelGetter == ByteBgraPre.getter) {
            if (pixelSetter == IntArgb.setter) {
                return ByteBgraPre.ToIntArgbConverter();
            }
            if (pixelSetter == IntArgbPre.setter) {
                return ByteBgraPre.ToIntArgbPreConverter();
            }
        } else if (pixelGetter == ByteRgb.getter) {
            if (pixelSetter == IntArgb.setter) {
                return ByteRgb.ToIntArgbConverter();
            }
            if (pixelSetter == IntArgbPre.setter) {
                return ByteRgb.ToIntArgbPreConverter();
            }
        } else if (pixelGetter == ByteBgr.getter) {
            if (pixelSetter == IntArgb.setter) {
                return ByteBgr.ToIntArgbConverter();
            }
            if (pixelSetter == IntArgbPre.setter) {
                return ByteBgr.ToIntArgbPreConverter();
            }
        } else if (pixelGetter == ByteGray.getter) {
            if (pixelSetter == IntArgbPre.setter) {
                return ByteGray.ToIntArgbPreConverter();
            }
            if (pixelSetter == IntArgb.setter) {
                return ByteGray.ToIntArgbConverter();
            }
        } else if (pixelGetter instanceof ByteIndexed.Getter && (pixelSetter == IntArgb.setter || pixelSetter == IntArgbPre.setter)) {
            return ByteIndexed.createToIntArgbAny((BytePixelGetter)pixelGetter, (IntPixelSetter)pixelSetter);
        }
        if (pixelGetter.getAlphaType() != AlphaType.OPAQUE && pixelSetter.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((BytePixelGetter)pixelGetter, (IntPixelSetter)pixelSetter);
    }

    public static IntToBytePixelConverter getI2BConverter(PixelGetter<IntBuffer> pixelGetter, PixelSetter<ByteBuffer> pixelSetter) {
        if (pixelGetter == IntArgb.getter) {
            if (pixelSetter == ByteBgra.setter) {
                return IntArgb.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return IntArgb.ToByteBgraPreConverter();
            }
        } else if (pixelGetter == IntArgbPre.getter) {
            if (pixelSetter == ByteBgra.setter) {
                return IntArgbPre.ToByteBgraConverter();
            }
            if (pixelSetter == ByteBgraPre.setter) {
                return IntArgbPre.ToByteBgraPreConverter();
            }
        }
        if (pixelSetter == ByteGray.setter) {
            return null;
        }
        if (pixelGetter.getAlphaType() != AlphaType.OPAQUE && pixelSetter.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((IntPixelGetter)pixelGetter, (BytePixelSetter)pixelSetter);
    }

    public static IntToIntPixelConverter getI2IConverter(PixelGetter<IntBuffer> pixelGetter, PixelSetter<IntBuffer> pixelSetter) {
        if (pixelGetter == IntArgb.getter) {
            if (pixelSetter == IntArgb.setter) {
                return IntArgb.ToIntArgbConverter();
            }
            if (pixelSetter == IntArgbPre.setter) {
                return IntArgb.ToIntArgbPreConverter();
            }
        } else if (pixelGetter == IntArgbPre.getter) {
            if (pixelSetter == IntArgb.setter) {
                return IntArgbPre.ToIntArgbConverter();
            }
            if (pixelSetter == IntArgbPre.setter) {
                return IntArgbPre.ToIntArgbPreConverter();
            }
        }
        if (pixelGetter.getAlphaType() != AlphaType.OPAQUE && pixelSetter.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((IntPixelGetter)pixelGetter, (IntPixelSetter)pixelSetter);
    }
}

