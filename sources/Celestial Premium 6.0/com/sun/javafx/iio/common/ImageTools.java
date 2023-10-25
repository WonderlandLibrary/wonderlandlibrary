/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ImageTools {
    public static final int PROGRESS_INTERVAL = 5;

    public static int readFully(InputStream inputStream, byte[] arrby, int n, int n2) throws IOException {
        if (n2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n3 = n2;
        if (n < 0 || n2 < 0 || n + n2 > arrby.length || n + n2 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > b.length!");
        }
        while (n2 > 0) {
            int n4 = inputStream.read(arrby, n, n2);
            if (n4 == -1) {
                throw new EOFException();
            }
            n += n4;
            n2 -= n4;
        }
        return n3;
    }

    public static int readFully(InputStream inputStream, byte[] arrby) throws IOException {
        return ImageTools.readFully(inputStream, arrby, 0, arrby.length);
    }

    public static void skipFully(InputStream inputStream, long l) throws IOException {
        while (l > 0L) {
            long l2 = inputStream.skip(l);
            if (l2 <= 0L) {
                if (inputStream.read() == -1) {
                    throw new EOFException();
                }
                --l;
                continue;
            }
            l -= l2;
        }
    }

    public static ImageStorage.ImageType getConvertedType(ImageStorage.ImageType imageType) {
        ImageStorage.ImageType imageType2 = imageType;
        switch (imageType) {
            case GRAY: {
                imageType2 = ImageStorage.ImageType.GRAY;
                break;
            }
            case GRAY_ALPHA: 
            case GRAY_ALPHA_PRE: 
            case PALETTE_ALPHA: 
            case PALETTE_ALPHA_PRE: 
            case PALETTE_TRANS: 
            case RGBA: {
                imageType2 = ImageStorage.ImageType.RGBA_PRE;
                break;
            }
            case PALETTE: 
            case RGB: {
                imageType2 = ImageStorage.ImageType.RGB;
                break;
            }
            case RGBA_PRE: {
                imageType2 = ImageStorage.ImageType.RGBA_PRE;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported ImageType " + imageType);
            }
        }
        return imageType2;
    }

    public static byte[] createImageArray(ImageStorage.ImageType imageType, int n, int n2) {
        int n3 = 0;
        switch (imageType) {
            case GRAY: 
            case PALETTE_ALPHA: 
            case PALETTE_ALPHA_PRE: 
            case PALETTE: {
                n3 = 1;
                break;
            }
            case GRAY_ALPHA: 
            case GRAY_ALPHA_PRE: {
                n3 = 2;
                break;
            }
            case RGB: {
                n3 = 3;
                break;
            }
            case RGBA: 
            case RGBA_PRE: {
                n3 = 4;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported ImageType " + imageType);
            }
        }
        return new byte[n * n2 * n3];
    }

    public static ImageFrame convertImageFrame(ImageFrame imageFrame) {
        ImageFrame imageFrame2;
        ImageStorage.ImageType imageType = imageFrame.getImageType();
        ImageStorage.ImageType imageType2 = ImageTools.getConvertedType(imageType);
        if (imageType2 == imageType) {
            imageFrame2 = imageFrame;
        } else {
            byte[] arrby = null;
            Buffer buffer = imageFrame.getImageData();
            if (!(buffer instanceof ByteBuffer)) {
                throw new IllegalArgumentException("!(frame.getImageData() instanceof ByteBuffer)");
            }
            ByteBuffer byteBuffer = (ByteBuffer)buffer;
            if (byteBuffer.hasArray()) {
                arrby = byteBuffer.array();
            } else {
                arrby = new byte[byteBuffer.capacity()];
                byteBuffer.get(arrby);
            }
            int n = imageFrame.getWidth();
            int n2 = imageFrame.getHeight();
            int n3 = imageFrame.getStride();
            byte[] arrby2 = ImageTools.createImageArray(imageType2, n, n2);
            ByteBuffer byteBuffer2 = ByteBuffer.wrap(arrby2);
            int n4 = arrby2.length / n2;
            byte[][] arrby3 = imageFrame.getPalette();
            ImageMetadata imageMetadata = imageFrame.getMetadata();
            int n5 = imageMetadata.transparentIndex != null ? imageMetadata.transparentIndex : 0;
            ImageTools.convert(n, n2, imageType, arrby, 0, n3, arrby2, 0, n4, arrby3, n5, false);
            ImageMetadata imageMetadata2 = new ImageMetadata(imageMetadata.gamma, imageMetadata.blackIsZero, null, imageMetadata.backgroundColor, null, imageMetadata.delayTime, imageMetadata.loopCount, imageMetadata.imageWidth, imageMetadata.imageHeight, imageMetadata.imageLeftPosition, imageMetadata.imageTopPosition, imageMetadata.disposalMethod);
            imageFrame2 = new ImageFrame(imageType2, byteBuffer2, n, n2, n4, null, imageMetadata2);
        }
        return imageFrame2;
    }

    public static byte[] convert(int n, int n2, ImageStorage.ImageType imageType, byte[] arrby, int n3, int n4, byte[] arrby2, int n5, int n6, byte[][] arrby3, int n7, boolean bl) {
        if (imageType == ImageStorage.ImageType.GRAY || imageType == ImageStorage.ImageType.RGB || imageType == ImageStorage.ImageType.RGBA_PRE) {
            if (arrby != arrby2) {
                int n8 = n;
                if (imageType == ImageStorage.ImageType.RGB) {
                    n8 *= 3;
                } else if (imageType == ImageStorage.ImageType.RGBA_PRE) {
                    n8 *= 4;
                }
                if (n2 == 1) {
                    System.arraycopy(arrby, n3, arrby2, n5, n8);
                } else {
                    int n9 = n3;
                    int n10 = n5;
                    for (int i = 0; i < n2; ++i) {
                        System.arraycopy(arrby, n9, arrby2, n10, n8);
                        n9 += n4;
                        n10 += n6;
                    }
                }
            }
        } else if (imageType == ImageStorage.ImageType.GRAY_ALPHA || imageType == ImageStorage.ImageType.GRAY_ALPHA_PRE) {
            int n11 = n3;
            int n12 = n5;
            if (imageType == ImageStorage.ImageType.GRAY_ALPHA) {
                for (int i = 0; i < n2; ++i) {
                    int n13 = n11;
                    int n14 = n12;
                    for (int j = 0; j < n; ++j) {
                        byte by = arrby[n13++];
                        int n15 = arrby[n13++] & 0xFF;
                        float f = (float)n15 / 255.0f;
                        by = (byte)(f * (float)(by & 0xFF));
                        arrby2[n14++] = by;
                        arrby2[n14++] = by;
                        arrby2[n14++] = by;
                        arrby2[n14++] = (byte)n15;
                    }
                    n11 += n4;
                    n12 += n6;
                }
            } else {
                for (int i = 0; i < n2; ++i) {
                    int n16 = n11;
                    int n17 = n12;
                    for (int j = 0; j < n; ++j) {
                        byte by = arrby[n16++];
                        arrby2[n17++] = by;
                        arrby2[n17++] = by;
                        arrby2[n17++] = by;
                        arrby2[n17++] = arrby[n16++];
                    }
                    n11 += n4;
                    n12 += n6;
                }
            }
        } else if (imageType == ImageStorage.ImageType.PALETTE) {
            int n18 = n3;
            int n19 = n5;
            byte[] arrby4 = arrby3[0];
            byte[] arrby5 = arrby3[1];
            byte[] arrby6 = arrby3[2];
            int n20 = n18;
            int n21 = n19;
            for (int i = 0; i < n; ++i) {
                int n22 = arrby[n20++] & 0xFF;
                arrby2[n21++] = arrby4[n22];
                arrby2[n21++] = arrby5[n22];
                arrby2[n21++] = arrby6[n22];
                n19 += n6;
            }
        } else if (imageType == ImageStorage.ImageType.PALETTE_ALPHA) {
            int n23 = n3;
            int n24 = n5;
            byte[] arrby7 = arrby3[0];
            byte[] arrby8 = arrby3[1];
            byte[] arrby9 = arrby3[2];
            byte[] arrby10 = arrby3[3];
            int n25 = n23;
            int n26 = n24;
            for (int i = 0; i < n; ++i) {
                int n27 = arrby[n25++] & 0xFF;
                byte by = arrby7[n27];
                byte by2 = arrby8[n27];
                byte by3 = arrby9[n27];
                int n28 = arrby10[n27] & 0xFF;
                float f = (float)n28 / 255.0f;
                arrby2[n26++] = (byte)(f * (float)(by & 0xFF));
                arrby2[n26++] = (byte)(f * (float)(by2 & 0xFF));
                arrby2[n26++] = (byte)(f * (float)(by3 & 0xFF));
                arrby2[n26++] = (byte)n28;
            }
            n23 += n4;
            n24 += n6;
        } else if (imageType == ImageStorage.ImageType.PALETTE_ALPHA_PRE) {
            int n29 = n3;
            int n30 = n5;
            byte[] arrby11 = arrby3[0];
            byte[] arrby12 = arrby3[1];
            byte[] arrby13 = arrby3[2];
            byte[] arrby14 = arrby3[3];
            for (int i = 0; i < n2; ++i) {
                int n31 = n29;
                int n32 = n30;
                for (int j = 0; j < n; ++j) {
                    int n33 = arrby[n31++] & 0xFF;
                    arrby2[n32++] = arrby11[n33];
                    arrby2[n32++] = arrby12[n33];
                    arrby2[n32++] = arrby13[n33];
                    arrby2[n32++] = arrby14[n33];
                }
                n29 += n4;
                n30 += n6;
            }
        } else if (imageType == ImageStorage.ImageType.PALETTE_TRANS) {
            int n34 = n3;
            int n35 = n5;
            for (int i = 0; i < n2; ++i) {
                int n36 = n34;
                int n37 = n35;
                byte[] arrby15 = arrby3[0];
                byte[] arrby16 = arrby3[1];
                byte[] arrby17 = arrby3[2];
                for (int j = 0; j < n; ++j) {
                    int n38;
                    if ((n38 = arrby[n36++] & 0xFF) == n7) {
                        if (bl) {
                            n37 += 4;
                            continue;
                        }
                        arrby2[n37++] = 0;
                        arrby2[n37++] = 0;
                        arrby2[n37++] = 0;
                        arrby2[n37++] = 0;
                        continue;
                    }
                    arrby2[n37++] = arrby15[n38];
                    arrby2[n37++] = arrby16[n38];
                    arrby2[n37++] = arrby17[n38];
                    arrby2[n37++] = -1;
                }
                n34 += n4;
                n35 += n6;
            }
        } else if (imageType == ImageStorage.ImageType.RGBA) {
            int n39 = n3;
            int n40 = n5;
            for (int i = 0; i < n2; ++i) {
                int n41 = n39;
                int n42 = n40;
                for (int j = 0; j < n; ++j) {
                    byte by = arrby[n41++];
                    byte by4 = arrby[n41++];
                    byte by5 = arrby[n41++];
                    int n43 = arrby[n41++] & 0xFF;
                    float f = (float)n43 / 255.0f;
                    arrby2[n42++] = (byte)(f * (float)(by & 0xFF));
                    arrby2[n42++] = (byte)(f * (float)(by4 & 0xFF));
                    arrby2[n42++] = (byte)(f * (float)(by5 & 0xFF));
                    arrby2[n42++] = (byte)n43;
                }
                n39 += n4;
                n40 += n6;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported ImageType " + imageType);
        }
        return arrby2;
    }

    public static String getScaledImageName(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = string.lastIndexOf(47);
        String string2 = n < 0 ? string : string.substring(n + 1);
        int n2 = string2.lastIndexOf(".");
        if (n2 < 0) {
            n2 = string2.length();
        }
        if (n >= 0) {
            stringBuilder.append(string.substring(0, n + 1));
        }
        stringBuilder.append(string2.substring(0, n2));
        stringBuilder.append("@2x");
        stringBuilder.append(string2.substring(n2));
        return stringBuilder.toString();
    }

    public static InputStream createInputStream(String string) throws IOException {
        Serializable serializable;
        InputStream inputStream = null;
        try {
            serializable = new File(string);
            if (((File)serializable).exists()) {
                inputStream = new FileInputStream((File)serializable);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (inputStream == null) {
            serializable = new URL(string);
            inputStream = ((URL)serializable).openStream();
        }
        return inputStream;
    }

    private static void computeUpdatedPixels(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int[] arrn, int n10) {
        boolean bl = false;
        int n11 = -1;
        int n12 = -1;
        int n13 = -1;
        for (int i = 0; i < n8; ++i) {
            int n14 = n7 + i * n9;
            if (n14 < n || (n14 - n) % n6 != 0) continue;
            if (n14 >= n + n2) break;
            int n15 = n3 + (n14 - n) / n6;
            if (n15 < n4) continue;
            if (n15 > n5) break;
            if (!bl) {
                n11 = n15;
                bl = true;
            } else if (n12 == -1) {
                n12 = n15;
            }
            n13 = n15;
        }
        arrn[n10] = n11;
        arrn[n10 + 2] = !bl ? 0 : n13 - n11 + 1;
        arrn[n10 + 4] = Math.max(n12 - n11, 1);
    }

    public static int[] computeUpdatedPixels(Rectangle rectangle, Point2D point2D, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12) {
        int[] arrn = new int[6];
        ImageTools.computeUpdatedPixels(rectangle.x, rectangle.width, (int)(point2D.x + 0.5f), n, n3, n5, n7, n9, n11, arrn, 0);
        ImageTools.computeUpdatedPixels(rectangle.y, rectangle.height, (int)(point2D.y + 0.5f), n2, n4, n6, n8, n10, n12, arrn, 1);
        return arrn;
    }

    public static int[] computeDimensions(int n, int n2, int n3, int n4, boolean bl) {
        int n5;
        int n6 = n3 < 0 ? 0 : n3;
        int n7 = n5 = n4 < 0 ? 0 : n4;
        if (n6 == 0 && n5 == 0) {
            n6 = n;
            n5 = n2;
        } else if (n6 != n || n5 != n2) {
            if (bl) {
                if (n6 == 0) {
                    n6 = Math.round((float)n * (float)n5 / (float)n2);
                } else if (n5 == 0) {
                    n5 = Math.round((float)n2 * (float)n6 / (float)n);
                } else {
                    float f = Math.min((float)n6 / (float)n, (float)n5 / (float)n2);
                    n6 = Math.round((float)n * f);
                    n5 = Math.round((float)n2 * f);
                }
            } else {
                if (n5 == 0) {
                    n5 = n2;
                }
                if (n6 == 0) {
                    n6 = n;
                }
            }
            if (n6 == 0) {
                n6 = 1;
            }
            if (n5 == 0) {
                n5 = 1;
            }
        }
        return new int[]{n6, n5};
    }

    public static ImageFrame scaleImageFrame(ImageFrame imageFrame, int n, int n2, boolean bl) {
        int n3 = ImageStorage.getNumBands(imageFrame.getImageType());
        ByteBuffer byteBuffer = ImageTools.scaleImage((ByteBuffer)imageFrame.getImageData(), imageFrame.getWidth(), imageFrame.getHeight(), n3, n, n2, bl);
        return new ImageFrame(imageFrame.getImageType(), byteBuffer, n, n2, n * n3, null, imageFrame.getMetadata());
    }

    public static ByteBuffer scaleImage(ByteBuffer byteBuffer, int n, int n2, int n3, int n4, int n5, boolean bl) {
        PushbroomScaler pushbroomScaler = ScalerFactory.createScaler(n, n2, n3, n4, n5, bl);
        int n6 = n * n3;
        if (byteBuffer.hasArray()) {
            byte[] arrby = byteBuffer.array();
            for (int i = 0; i != n2; ++i) {
                pushbroomScaler.putSourceScanline(arrby, i * n6);
            }
        } else {
            byte[] arrby = new byte[n6];
            for (int i = 0; i != n2; ++i) {
                byteBuffer.get(arrby);
                pushbroomScaler.putSourceScanline(arrby, 0);
            }
        }
        return pushbroomScaler.getDestination();
    }
}

