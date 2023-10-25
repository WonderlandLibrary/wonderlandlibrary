/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

class PixelUtils {
    private static ImageFormatDescription[] supportedFormats = ImageStorage.getSupportedDescriptions();

    private PixelUtils() {
    }

    protected static boolean supportedFormatType(String string) {
        for (ImageFormatDescription imageFormatDescription : supportedFormats) {
            for (String string2 : imageFormatDescription.getExtensions()) {
                if (!string.endsWith(string2)) continue;
                return true;
            }
        }
        return false;
    }

    public static Pixels imageToPixels(Image image) {
        Application application;
        PixelFormat.DataType dataType;
        block7: {
            ByteBuffer byteBuffer;
            block9: {
                block8: {
                    dataType = image.getDataType();
                    application = Application.GetApplication();
                    int n = Pixels.getNativeFormat();
                    if (dataType != PixelFormat.DataType.BYTE) break block7;
                    byteBuffer = (ByteBuffer)image.getPixelBuffer();
                    int n2 = image.getWidth();
                    int n3 = image.getHeight();
                    int n4 = image.getScanlineStride();
                    if (image.getBytesPerPixelUnit() != 3) break block8;
                    switch (n) {
                        case 2: {
                            byte[] arrby = new byte[n2 * n3 * 4];
                            ByteRgb.ToByteArgbConverter().convert(byteBuffer, 0, n4, arrby, 0, n2 * 4, n2, n3);
                            byteBuffer = ByteBuffer.wrap(arrby);
                            break block9;
                        }
                        case 1: {
                            byte[] arrby = new byte[n2 * n3 * 4];
                            ByteRgb.ToByteBgraPreConverter().convert(byteBuffer, 0, n4, arrby, 0, n2 * 4, n2, n3);
                            byteBuffer = ByteBuffer.wrap(arrby);
                            break block9;
                        }
                        default: {
                            throw new IllegalArgumentException("unhandled native format: " + n);
                        }
                    }
                }
                if (image.getPixelFormat() != PixelFormat.BYTE_BGRA_PRE) {
                    throw new IllegalArgumentException("non-RGB image format");
                }
            }
            Pixels pixels = application.createPixels(image.getWidth(), image.getHeight(), byteBuffer);
            return pixels;
        }
        if (dataType == PixelFormat.DataType.INT) {
            if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
                throw new UnsupportedOperationException("INT_ARGB_PRE only supported for LITTLE_ENDIAN machines");
            }
            IntBuffer intBuffer = (IntBuffer)image.getPixelBuffer();
            Pixels pixels = application.createPixels(image.getWidth(), image.getHeight(), intBuffer);
            return pixels;
        }
        throw new IllegalArgumentException("unhandled image type: " + dataType);
    }

    public static Image pixelsToImage(Pixels pixels) {
        Buffer buffer = pixels.getPixels();
        if (pixels.getBytesPerComponent() == 1) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer.capacity());
            byteBuffer.put((ByteBuffer)buffer);
            byteBuffer.rewind();
            return Image.fromByteBgraPreData(byteBuffer, pixels.getWidth(), pixels.getHeight());
        }
        if (pixels.getBytesPerComponent() == 4) {
            IntBuffer intBuffer = IntBuffer.allocate(buffer.capacity());
            intBuffer.put((IntBuffer)buffer);
            intBuffer.rewind();
            return Image.fromIntArgbPreData((IntBuffer)buffer, pixels.getWidth(), pixels.getHeight());
        }
        throw new IllegalArgumentException("unhandled pixel buffer: " + buffer.getClass().getName());
    }
}

