/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.bmp.BMPDescriptor;
import com.sun.javafx.iio.bmp.BitmapInfoHeader;
import com.sun.javafx.iio.bmp.LEInputStream;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class BMPImageLoader
extends ImageLoaderImpl {
    static final short BM = 19778;
    static final int BFH_SIZE = 14;
    final LEInputStream data;
    int bfSize;
    int bfOffBits;
    byte[] bgra_palette;
    BitmapInfoHeader bih;
    int[] bitMasks;
    int[] bitOffsets;

    BMPImageLoader(InputStream inputStream) throws IOException {
        super(BMPDescriptor.theInstance);
        this.data = new LEInputStream(inputStream);
        if (this.data.readShort() != 19778) {
            throw new IOException("Invalid BMP file signature");
        }
        this.readHeader();
    }

    private void readHeader() throws IOException {
        this.bfSize = this.data.readInt();
        this.data.skipBytes(4);
        this.bfOffBits = this.data.readInt();
        this.bih = new BitmapInfoHeader(this.data);
        if (this.bfOffBits < this.bih.biSize + 14) {
            throw new IOException("Invalid bitmap bits offset");
        }
        if (this.bih.biSize + 14 != this.bfOffBits) {
            int n = this.bfOffBits - this.bih.biSize - 14;
            int n2 = n / 4;
            this.bgra_palette = new byte[n2 * 4];
            int n3 = this.data.in.read(this.bgra_palette);
            if (n3 < n) {
                this.data.skipBytes(n - n3);
            }
        }
        if (this.bih.biCompression == 3) {
            this.parseBitfields();
        } else if (this.bih.biCompression == 0 && this.bih.biBitCount == 16) {
            this.bitMasks = new int[]{31744, 992, 31};
            this.bitOffsets = new int[]{10, 5, 0};
        }
    }

    private void parseBitfields() throws IOException {
        if (this.bgra_palette.length != 12) {
            throw new IOException("Invalid bit masks");
        }
        this.bitMasks = new int[3];
        this.bitOffsets = new int[3];
        for (int i = 0; i < 3; ++i) {
            int n;
            this.bitMasks[i] = n = BMPImageLoader.getDWord(this.bgra_palette, i * 4);
            int n2 = 0;
            if (n != 0) {
                while ((n & 1) == 0) {
                    ++n2;
                    n >>>= 1;
                }
                if (!BMPImageLoader.isPow2Minus1(n)) {
                    throw new IOException("Bit mask is not contiguous");
                }
            }
            this.bitOffsets[i] = n2;
        }
        if (!BMPImageLoader.checkDisjointMasks(this.bitMasks[0], this.bitMasks[1], this.bitMasks[2])) {
            throw new IOException("Bit masks overlap");
        }
    }

    static boolean checkDisjointMasks(int n, int n2, int n3) {
        return (n & n2 | n & n3 | n2 & n3) == 0;
    }

    static boolean isPow2Minus1(int n) {
        return (n & n + 1) == 0;
    }

    @Override
    public void dispose() {
    }

    private void readRLE(byte[] arrby, int n, int n2, boolean bl) throws IOException {
        int n3 = this.bih.biSizeImage;
        if (n3 == 0) {
            n3 = this.bfSize - this.bfOffBits;
        }
        byte[] arrby2 = new byte[n3];
        ImageTools.readFully(this.data.in, arrby2);
        boolean bl2 = this.bih.biHeight > 0;
        int n4 = bl2 ? n2 - 1 : 0;
        int n5 = 0;
        int n6 = n4 * n;
        block5: while (n5 < n3) {
            int n7;
            int n8;
            int n9;
            int n10 = BMPImageLoader.getByte(arrby2, n5++);
            int n11 = BMPImageLoader.getByte(arrby2, n5++);
            if (n10 == 0) {
                switch (n11) {
                    case 0: {
                        n6 = (n4 += bl2 ? -1 : 1) * n;
                        break;
                    }
                    case 1: {
                        return;
                    }
                    case 2: {
                        n9 = BMPImageLoader.getByte(arrby2, n5++);
                        n8 = BMPImageLoader.getByte(arrby2, n5++);
                        n4 += n8;
                        n6 += n8 * n;
                        n6 += n9 * 3;
                        break;
                    }
                    default: {
                        n7 = 0;
                        for (int i = 0; i < n11; ++i) {
                            int n12;
                            if (bl) {
                                if ((i & 1) == 0) {
                                    n7 = BMPImageLoader.getByte(arrby2, n5++);
                                    n12 = (n7 & 0xF0) >> 4;
                                } else {
                                    n12 = n7 & 0xF;
                                }
                            } else {
                                n12 = BMPImageLoader.getByte(arrby2, n5++);
                            }
                            n6 = this.setRGBFromPalette(arrby, n6, n12);
                        }
                        if (bl) {
                            if ((n11 & 3) != 1 && (n11 & 3) != 2) continue block5;
                            ++n5;
                            break;
                        }
                        if ((n11 & 1) != 1) continue block5;
                        ++n5;
                        break;
                    }
                }
                continue;
            }
            if (bl) {
                n9 = (n11 & 0xF0) >> 4;
                n8 = n11 & 0xF;
                for (n7 = 0; n7 < n10; ++n7) {
                    n6 = this.setRGBFromPalette(arrby, n6, (n7 & 1) == 0 ? n9 : n8);
                }
                continue;
            }
            for (n9 = 0; n9 < n10; ++n9) {
                n6 = this.setRGBFromPalette(arrby, n6, n11);
            }
        }
    }

    private int setRGBFromPalette(byte[] arrby, int n, int n2) {
        arrby[n++] = this.bgra_palette[(n2 *= 4) + 2];
        arrby[n++] = this.bgra_palette[n2 + 1];
        arrby[n++] = this.bgra_palette[n2];
        return n;
    }

    private void readPackedBits(byte[] arrby, int n, int n2) throws IOException {
        int n3 = 8 / this.bih.biBitCount;
        int n4 = (this.bih.biWidth + n3 - 1) / n3;
        int n5 = n4 + 3 & 0xFFFFFFFC;
        int n6 = (1 << this.bih.biBitCount) - 1;
        byte[] arrby2 = new byte[n5];
        for (int i = 0; i != n2; ++i) {
            ImageTools.readFully(this.data.in, arrby2);
            int n7 = this.bih.biHeight < 0 ? i : n2 - i - 1;
            int n8 = n7 * n;
            for (int j = 0; j != this.bih.biWidth; ++j) {
                int n9 = j * this.bih.biBitCount;
                byte by = arrby2[n9 / 8];
                int n10 = 8 - (n9 & 7) - this.bih.biBitCount;
                int n11 = by >> n10 & n6;
                n8 = this.setRGBFromPalette(arrby, n8, n11);
            }
        }
    }

    private static int getDWord(byte[] arrby, int n) {
        return arrby[n] & 0xFF | (arrby[n + 1] & 0xFF) << 8 | (arrby[n + 2] & 0xFF) << 16 | (arrby[n + 3] & 0xFF) << 24;
    }

    private static int getWord(byte[] arrby, int n) {
        return arrby[n] & 0xFF | (arrby[n + 1] & 0xFF) << 8;
    }

    private static int getByte(byte[] arrby, int n) {
        return arrby[n] & 0xFF;
    }

    private static byte convertFrom5To8Bit(int n, int n2, int n3) {
        int n4 = (n & n2) >>> n3;
        return (byte)(n4 << 3 | n4 >> 2);
    }

    private static byte convertFromXTo8Bit(int n, int n2, int n3) {
        int n4 = (n & n2) >>> n3;
        return (byte)((double)n4 * 255.0 / (double)(n2 >>> n3));
    }

    private void read16Bit(byte[] arrby, int n, int n2, BitConverter bitConverter) throws IOException {
        int n3 = this.bih.biWidth * 2;
        int n4 = n3 + 3 & 0xFFFFFFFC;
        byte[] arrby2 = new byte[n4];
        for (int i = 0; i != n2; ++i) {
            ImageTools.readFully(this.data.in, arrby2);
            int n5 = this.bih.biHeight < 0 ? i : n2 - i - 1;
            int n6 = n5 * n;
            for (int j = 0; j != this.bih.biWidth; ++j) {
                int n7 = BMPImageLoader.getWord(arrby2, j * 2);
                for (int k = 0; k < 3; ++k) {
                    arrby[n6++] = bitConverter.convert(n7, this.bitMasks[k], this.bitOffsets[k]);
                }
            }
        }
    }

    private void read32BitRGB(byte[] arrby, int n, int n2) throws IOException {
        int n3 = this.bih.biWidth * 4;
        byte[] arrby2 = new byte[n3];
        for (int i = 0; i != n2; ++i) {
            ImageTools.readFully(this.data.in, arrby2);
            int n4 = this.bih.biHeight < 0 ? i : n2 - i - 1;
            int n5 = n4 * n;
            for (int j = 0; j != this.bih.biWidth; ++j) {
                int n6 = j * 4;
                arrby[n5++] = arrby2[n6 + 2];
                arrby[n5++] = arrby2[n6 + 1];
                arrby[n5++] = arrby2[n6];
            }
        }
    }

    private void read32BitBF(byte[] arrby, int n, int n2) throws IOException {
        int n3 = this.bih.biWidth * 4;
        byte[] arrby2 = new byte[n3];
        for (int i = 0; i != n2; ++i) {
            ImageTools.readFully(this.data.in, arrby2);
            int n4 = this.bih.biHeight < 0 ? i : n2 - i - 1;
            int n5 = n4 * n;
            for (int j = 0; j != this.bih.biWidth; ++j) {
                int n6 = j * 4;
                int n7 = BMPImageLoader.getDWord(arrby2, n6);
                for (int k = 0; k < 3; ++k) {
                    arrby[n5++] = BMPImageLoader.convertFromXTo8Bit(n7, this.bitMasks[k], this.bitOffsets[k]);
                }
            }
        }
    }

    private void read24Bit(byte[] arrby, int n, int n2) throws IOException {
        int n3 = n + 3 & 0xFFFFFFFC;
        int n4 = n3 - n;
        for (int i = 0; i != n2; ++i) {
            int n5 = this.bih.biHeight < 0 ? i : n2 - i - 1;
            int n6 = n5 * n;
            ImageTools.readFully(this.data.in, arrby, n6, n);
            this.data.skipBytes(n4);
            BMPImageLoader.BGRtoRGB(arrby, n6, n);
        }
    }

    static void BGRtoRGB(byte[] arrby, int n, int n2) {
        for (int i = n2 / 3; i != 0; --i) {
            byte by = arrby[n];
            byte by2 = arrby[n + 2];
            arrby[n + 2] = by;
            arrby[n] = by2;
            n += 3;
        }
    }

    @Override
    public ImageFrame load(int n, int n2, int n3, boolean bl, boolean bl2) throws IOException {
        if (0 != n) {
            return null;
        }
        int n4 = Math.abs(this.bih.biHeight);
        int[] arrn = ImageTools.computeDimensions(this.bih.biWidth, n4, n2, n3, bl);
        n2 = arrn[0];
        n3 = arrn[1];
        ImageMetadata imageMetadata = new ImageMetadata(null, Boolean.TRUE, null, null, null, null, null, n2, n3, null, null, null);
        this.updateImageMetadata(imageMetadata);
        int n5 = 3;
        int n6 = this.bih.biWidth * n5;
        byte[] arrby = new byte[n6 * n4];
        switch (this.bih.biBitCount) {
            case 1: {
                this.readPackedBits(arrby, n6, n4);
                break;
            }
            case 4: {
                if (this.bih.biCompression == 2) {
                    this.readRLE(arrby, n6, n4, true);
                    break;
                }
                this.readPackedBits(arrby, n6, n4);
                break;
            }
            case 8: {
                if (this.bih.biCompression == 1) {
                    this.readRLE(arrby, n6, n4, false);
                    break;
                }
                this.readPackedBits(arrby, n6, n4);
                break;
            }
            case 16: {
                if (this.bih.biCompression == 3) {
                    this.read16Bit(arrby, n6, n4, BMPImageLoader::convertFromXTo8Bit);
                    break;
                }
                this.read16Bit(arrby, n6, n4, BMPImageLoader::convertFrom5To8Bit);
                break;
            }
            case 32: {
                if (this.bih.biCompression == 3) {
                    this.read32BitBF(arrby, n6, n4);
                    break;
                }
                this.read32BitRGB(arrby, n6, n4);
                break;
            }
            case 24: {
                this.read24Bit(arrby, n6, n4);
                break;
            }
            default: {
                throw new IOException("Unknown BMP bit depth");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
        if (this.bih.biWidth != n2 || n4 != n3) {
            byteBuffer = ImageTools.scaleImage(byteBuffer, this.bih.biWidth, n4, n5, n2, n3, bl2);
        }
        return new ImageFrame(ImageStorage.ImageType.RGB, byteBuffer, n2, n3, n2 * n5, null, imageMetadata);
    }

    @FunctionalInterface
    private static interface BitConverter {
        public byte convert(int var1, int var2, int var3);
    }
}

