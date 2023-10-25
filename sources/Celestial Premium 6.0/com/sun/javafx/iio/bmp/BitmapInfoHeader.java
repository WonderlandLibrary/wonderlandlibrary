/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.bmp.LEInputStream;
import java.io.IOException;

final class BitmapInfoHeader {
    static final int BIH_SIZE = 40;
    static final int BIH4_SIZE = 108;
    static final int BIH5_SIZE = 124;
    static final int BI_RGB = 0;
    static final int BI_RLE8 = 1;
    static final int BI_RLE4 = 2;
    static final int BI_BITFIELDS = 3;
    static final int BI_JPEG = 4;
    static final int BI_PNG = 5;
    final int biSize;
    final int biWidth;
    final int biHeight;
    final short biPlanes;
    final short biBitCount;
    final int biCompression;
    final int biSizeImage;
    final int biXPelsPerMeter;
    final int biYPelsPerMeter;
    final int biClrUsed;
    final int biClrImportant;

    BitmapInfoHeader(LEInputStream lEInputStream) throws IOException {
        this.biSize = lEInputStream.readInt();
        this.biWidth = lEInputStream.readInt();
        this.biHeight = lEInputStream.readInt();
        this.biPlanes = lEInputStream.readShort();
        this.biBitCount = lEInputStream.readShort();
        this.biCompression = lEInputStream.readInt();
        this.biSizeImage = lEInputStream.readInt();
        this.biXPelsPerMeter = lEInputStream.readInt();
        this.biYPelsPerMeter = lEInputStream.readInt();
        this.biClrUsed = lEInputStream.readInt();
        this.biClrImportant = lEInputStream.readInt();
        if (this.biSize > 40) {
            if (this.biSize == 108 || this.biSize == 124) {
                lEInputStream.skipBytes(this.biSize - 40);
            } else {
                throw new IOException("BitmapInfoHeader is corrupt");
            }
        }
        this.validate();
    }

    void validate() throws IOException {
        if (this.biBitCount < 1 || this.biCompression == 4 || this.biCompression == 5) {
            throw new IOException("Unsupported BMP image: Embedded JPEG or PNG images are not supported");
        }
        switch (this.biCompression) {
            case 2: {
                if (this.biBitCount == 4) break;
                throw new IOException("Invalid BMP image: Only 4 bpp images can be RLE4 compressed");
            }
            case 1: {
                if (this.biBitCount == 8) break;
                throw new IOException("Invalid BMP image: Only 8 bpp images can be RLE8 compressed");
            }
            case 3: {
                if (this.biBitCount == 16 || this.biBitCount == 32) break;
                throw new IOException("Invalid BMP image: Only 16 or 32 bpp images can use BITFIELDS compression");
            }
            case 0: {
                break;
            }
            default: {
                throw new IOException("Unknown BMP compression type");
            }
        }
    }
}

