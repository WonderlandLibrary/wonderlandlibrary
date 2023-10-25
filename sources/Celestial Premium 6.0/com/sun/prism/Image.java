/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.PixelFormat
 *  javafx.scene.image.PixelReader
 *  javafx.scene.image.WritablePixelFormat
 *  javafx.util.Pair
 */
package com.sun.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageStorage;
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
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteGrayAlpha;
import com.sun.javafx.image.impl.ByteGrayAlphaPre;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.ByteRgba;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BufferUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.util.Pair;

public class Image
implements PlatformImage {
    static final WritablePixelFormat<ByteBuffer> FX_ByteBgraPre_FORMAT = javafx.scene.image.PixelFormat.getByteBgraPreInstance();
    static final WritablePixelFormat<IntBuffer> FX_IntArgbPre_FORMAT = javafx.scene.image.PixelFormat.getIntArgbPreInstance();
    static final javafx.scene.image.PixelFormat<ByteBuffer> FX_ByteRgb_FORMAT = javafx.scene.image.PixelFormat.getByteRgbInstance();
    private final Buffer pixelBuffer;
    private final int minX;
    private final int minY;
    private final int width;
    private final int height;
    private final int scanlineStride;
    private final PixelFormat pixelFormat;
    private final float pixelScale;
    private Serial serial = new Serial();
    private Accessor<?> pixelaccessor;
    static javafx.scene.image.PixelFormat<ByteBuffer> FX_ByteGray_FORMAT;

    public static Image fromIntArgbPreData(int[] arrn, int n, int n2) {
        return new Image(PixelFormat.INT_ARGB_PRE, arrn, n, n2);
    }

    public static Image fromIntArgbPreData(IntBuffer intBuffer, int n, int n2) {
        return new Image(PixelFormat.INT_ARGB_PRE, intBuffer, n, n2);
    }

    public static Image fromIntArgbPreData(IntBuffer intBuffer, int n, int n2, int n3) {
        return new Image(PixelFormat.INT_ARGB_PRE, intBuffer, n, n2, 0, 0, n3);
    }

    public static Image fromIntArgbPreData(IntBuffer intBuffer, int n, int n2, int n3, float f) {
        return new Image(PixelFormat.INT_ARGB_PRE, intBuffer, n, n2, 0, 0, n3, f);
    }

    public static Image fromByteBgraPreData(byte[] arrby, int n, int n2) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, arrby, n, n2);
    }

    public static Image fromByteBgraPreData(byte[] arrby, int n, int n2, float f) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, ByteBuffer.wrap(arrby), n, n2, 0, 0, 0, f);
    }

    public static Image fromByteBgraPreData(ByteBuffer byteBuffer, int n, int n2) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer, n, n2);
    }

    public static Image fromPixelBufferPreData(PixelFormat pixelFormat, Buffer buffer, int n, int n2) {
        return new Image(pixelFormat, buffer, n, n2);
    }

    public static Image fromByteBgraPreData(ByteBuffer byteBuffer, int n, int n2, int n3) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer, n, n2, 0, 0, n3);
    }

    public static Image fromByteBgraPreData(ByteBuffer byteBuffer, int n, int n2, int n3, float f) {
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer, n, n2, 0, 0, n3, f);
    }

    public static Image fromByteRgbData(byte[] arrby, int n, int n2) {
        return new Image(PixelFormat.BYTE_RGB, arrby, n, n2);
    }

    public static Image fromByteRgbData(ByteBuffer byteBuffer, int n, int n2) {
        return new Image(PixelFormat.BYTE_RGB, byteBuffer, n, n2);
    }

    public static Image fromByteRgbData(ByteBuffer byteBuffer, int n, int n2, int n3) {
        return new Image(PixelFormat.BYTE_RGB, byteBuffer, n, n2, 0, 0, n3);
    }

    public static Image fromByteRgbData(ByteBuffer byteBuffer, int n, int n2, int n3, float f) {
        return new Image(PixelFormat.BYTE_RGB, byteBuffer, n, n2, 0, 0, n3, f);
    }

    public static Image fromByteGrayData(byte[] arrby, int n, int n2) {
        return new Image(PixelFormat.BYTE_GRAY, arrby, n, n2);
    }

    public static Image fromByteGrayData(ByteBuffer byteBuffer, int n, int n2) {
        return new Image(PixelFormat.BYTE_GRAY, byteBuffer, n, n2);
    }

    public static Image fromByteGrayData(ByteBuffer byteBuffer, int n, int n2, int n3) {
        return new Image(PixelFormat.BYTE_GRAY, byteBuffer, n, n2, 0, 0, n3);
    }

    public static Image fromByteGrayData(ByteBuffer byteBuffer, int n, int n2, int n3, float f) {
        return new Image(PixelFormat.BYTE_GRAY, byteBuffer, n, n2, 0, 0, n3, f);
    }

    public static Image fromByteAlphaData(byte[] arrby, int n, int n2) {
        return new Image(PixelFormat.BYTE_ALPHA, arrby, n, n2);
    }

    public static Image fromByteAlphaData(ByteBuffer byteBuffer, int n, int n2) {
        return new Image(PixelFormat.BYTE_ALPHA, byteBuffer, n, n2);
    }

    public static Image fromByteAlphaData(ByteBuffer byteBuffer, int n, int n2, int n3) {
        return new Image(PixelFormat.BYTE_ALPHA, byteBuffer, n, n2, 0, 0, n3);
    }

    public static Image fromByteApple422Data(byte[] arrby, int n, int n2) {
        return new Image(PixelFormat.BYTE_APPLE_422, arrby, n, n2);
    }

    public static Image fromByteApple422Data(ByteBuffer byteBuffer, int n, int n2) {
        return new Image(PixelFormat.BYTE_APPLE_422, byteBuffer, n, n2);
    }

    public static Image fromByteApple422Data(ByteBuffer byteBuffer, int n, int n2, int n3) {
        return new Image(PixelFormat.BYTE_APPLE_422, byteBuffer, n, n2, 0, 0, n3);
    }

    public static Image fromFloatMapData(FloatBuffer floatBuffer, int n, int n2) {
        return new Image(PixelFormat.FLOAT_XYZW, floatBuffer, n, n2);
    }

    public static Image convertImageFrame(ImageFrame imageFrame) {
        ByteBuffer byteBuffer = (ByteBuffer)imageFrame.getImageData();
        ImageStorage.ImageType imageType = imageFrame.getImageType();
        int n = imageFrame.getWidth();
        int n2 = imageFrame.getHeight();
        int n3 = imageFrame.getStride();
        float f = imageFrame.getPixelScale();
        switch (imageType) {
            case GRAY: {
                return Image.fromByteGrayData(byteBuffer, n, n2, n3, f);
            }
            case RGB: {
                return Image.fromByteRgbData(byteBuffer, n, n2, n3, f);
            }
            case RGBA: {
                ByteBgra.ToByteBgraPreConverter().convert(byteBuffer, 0, n3, byteBuffer, 0, n3, n, n2);
            }
            case RGBA_PRE: {
                ByteRgba.ToByteBgraConverter().convert(byteBuffer, 0, n3, byteBuffer, 0, n3, n, n2);
                return Image.fromByteBgraPreData(byteBuffer, n, n2, n3, f);
            }
            case GRAY_ALPHA: {
                ByteGrayAlpha.ToByteGrayAlphaPreConverter().convert(byteBuffer, 0, n3, byteBuffer, 0, n3, n, n2);
            }
            case GRAY_ALPHA_PRE: {
                if (n3 != n * 2) {
                    throw new AssertionError((Object)"Bad stride for GRAY_ALPHA");
                }
                byte[] arrby = new byte[n * n2 * 4];
                ByteGrayAlphaPre.ToByteBgraPreConverter().convert(byteBuffer, 0, n3, arrby, 0, n * 4, n, n2);
                return Image.fromByteBgraPreData(arrby, n, n2, f);
            }
        }
        throw new RuntimeException("Unknown image type: " + imageType);
    }

    private Image(PixelFormat pixelFormat, int[] arrn, int n, int n2) {
        this(pixelFormat, IntBuffer.wrap(arrn), n, n2, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, byte[] arrby, int n, int n2) {
        this(pixelFormat, ByteBuffer.wrap(arrby), n, n2, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer buffer, int n, int n2) {
        this(pixelFormat, buffer, n, n2, 0, 0, 0, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer buffer, int n, int n2, int n3, int n4, int n5) {
        this(pixelFormat, buffer, n, n2, n3, n4, n5, 1.0f);
    }

    private Image(PixelFormat pixelFormat, Buffer buffer, int n, int n2, int n3, int n4, int n5, float f) {
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format not supported " + pixelFormat.name());
        }
        if (n5 == 0) {
            n5 = n * pixelFormat.getBytesPerPixelUnit();
        }
        if (buffer == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (n <= 0 || n2 <= 0) {
            throw new IllegalArgumentException("Image dimensions must be > 0");
        }
        if (n3 < 0 || n4 < 0) {
            throw new IllegalArgumentException("Image minX and minY must be >= 0");
        }
        if ((n3 + n) * pixelFormat.getBytesPerPixelUnit() > n5) {
            throw new IllegalArgumentException("Image scanlineStride is too small");
        }
        if (n5 % pixelFormat.getBytesPerPixelUnit() != 0) {
            throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
        }
        this.pixelFormat = pixelFormat;
        this.pixelBuffer = buffer;
        this.width = n;
        this.height = n2;
        this.minX = n3;
        this.minY = n4;
        this.scanlineStride = n5;
        this.pixelScale = f;
    }

    public PixelFormat getPixelFormat() {
        return this.pixelFormat;
    }

    public PixelFormat.DataType getDataType() {
        return this.pixelFormat.getDataType();
    }

    public int getBytesPerPixelUnit() {
        return this.pixelFormat.getBytesPerPixelUnit();
    }

    public Buffer getPixelBuffer() {
        return this.pixelBuffer;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override
    public float getPixelScale() {
        return this.pixelScale;
    }

    public int getRowLength() {
        return this.scanlineStride / this.pixelFormat.getBytesPerPixelUnit();
    }

    public boolean isTightlyPacked() {
        return this.minX == 0 && this.minY == 0 && this.width == this.getRowLength();
    }

    public Image createSubImage(int n, int n2, int n3, int n4) {
        if (n3 <= 0 || n4 <= 0) {
            throw new IllegalArgumentException("Subimage dimensions must be > 0");
        }
        if (n < 0 || n2 < 0) {
            throw new IllegalArgumentException("Subimage minX and minY must be >= 0");
        }
        if (n + n3 > this.width) {
            throw new IllegalArgumentException("Subimage minX+width must be <= width of parent image");
        }
        if (n2 + n4 > this.height) {
            throw new IllegalArgumentException("Subimage minY+height must be <= height of parent image");
        }
        Image image = new Image(this.pixelFormat, this.pixelBuffer, n3, n4, this.minX + n, this.minY + n2, this.scanlineStride);
        image.serial = this.serial;
        return image;
    }

    public Image createPackedCopy() {
        int n = this.width * this.pixelFormat.getBytesPerPixelUnit();
        Buffer buffer = Image.createPackedBuffer(this.pixelBuffer, this.pixelFormat, this.minX, this.minY, this.width, this.height, this.scanlineStride);
        return new Image(this.pixelFormat, buffer, this.width, this.height, 0, 0, n);
    }

    public Image createPackedCopyIfNeeded() {
        int n = this.width * this.pixelFormat.getBytesPerPixelUnit();
        if (n == this.scanlineStride && this.minX == 0 && this.minY == 0) {
            return this;
        }
        return this.createPackedCopy();
    }

    public static Buffer createPackedBuffer(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5) {
        Buffer buffer2;
        if (n5 % pixelFormat.getBytesPerPixelUnit() != 0) {
            throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
        }
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format unsupported " + pixelFormat);
        }
        int n6 = pixelFormat.getElemsPerPixelUnit();
        int n7 = n5 / pixelFormat.getBytesPerPixelUnit();
        int n8 = n7 * n6;
        int n9 = n3 * n6;
        int n10 = n9 * n4;
        int n11 = n * n6 + n2 * n8;
        int n12 = 0;
        switch (pixelFormat.getDataType()) {
            case BYTE: {
                ByteBuffer byteBuffer = (ByteBuffer)buffer;
                ByteBuffer byteBuffer2 = BufferUtil.newByteBuffer(n10);
                for (int i = 0; i < n4; ++i) {
                    byteBuffer.limit(n11 + n9);
                    byteBuffer.position(n11);
                    byteBuffer2.limit(n12 + n9);
                    byteBuffer2.position(n12);
                    byteBuffer2.put(byteBuffer);
                    n11 += n8;
                    n12 += n9;
                }
                buffer2 = byteBuffer2;
                break;
            }
            case INT: {
                IntBuffer intBuffer = (IntBuffer)buffer;
                IntBuffer intBuffer2 = BufferUtil.newIntBuffer(n10);
                for (int i = 0; i < n4; ++i) {
                    intBuffer.limit(n11 + n9);
                    intBuffer.position(n11);
                    intBuffer2.limit(n12 + n9);
                    intBuffer2.position(n12);
                    intBuffer2.put(intBuffer);
                    n11 += n8;
                    n12 += n9;
                }
                buffer2 = intBuffer2;
                break;
            }
            case FLOAT: {
                FloatBuffer floatBuffer = (FloatBuffer)buffer;
                FloatBuffer floatBuffer2 = BufferUtil.newFloatBuffer(n10);
                for (int i = 0; i < n4; ++i) {
                    floatBuffer.limit(n11 + n9);
                    floatBuffer.position(n11);
                    floatBuffer2.limit(n12 + n9);
                    floatBuffer2.position(n12);
                    floatBuffer2.put(floatBuffer);
                    n11 += n8;
                    n12 += n9;
                }
                buffer2 = floatBuffer2;
                break;
            }
            default: {
                throw new InternalError("Unknown data type");
            }
        }
        buffer.limit(buffer.capacity());
        buffer.rewind();
        buffer2.limit(buffer2.capacity());
        buffer2.rewind();
        return buffer2;
    }

    public Image iconify(ByteBuffer byteBuffer, int n, int n2) {
        if (this.pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Format not supported " + this.pixelFormat);
        }
        int n3 = this.getBytesPerPixelUnit();
        int n4 = n * n3;
        ByteToIntPixelConverter byteToIntPixelConverter = n3 == 1 ? ByteGray.ToIntArgbPreConverter() : (this.pixelFormat == PixelFormat.BYTE_BGRA_PRE ? ByteBgraPre.ToIntArgbPreConverter() : ByteRgb.ToIntArgbPreConverter());
        int[] arrn = new int[n * n2];
        byteToIntPixelConverter.convert(byteBuffer, 0, n4, arrn, 0, n, n, n2);
        return new Image(PixelFormat.INT_ARGB_PRE, arrn, n, n2);
    }

    public String toString() {
        return super.toString() + " [format=" + this.pixelFormat + " width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " minX=" + this.minX + " minY=" + this.minY + " pixelBuffer=" + this.pixelBuffer + " bpp=" + this.getBytesPerPixelUnit() + "]";
    }

    public Serial getSerial() {
        return this.serial;
    }

    private void updateSerial() {
        this.updateSerial(null);
    }

    private void updateSerial(Rectangle rectangle) {
        this.serial.update(rectangle);
    }

    public Image promoteByteRgbToByteBgra() {
        ByteBuffer byteBuffer = (ByteBuffer)this.pixelBuffer;
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(this.width * this.height * 4);
        int n = this.minY * this.scanlineStride + this.minX * 3;
        ByteRgb.ToByteBgraPreConverter().convert(byteBuffer, n, this.scanlineStride, byteBuffer2, 0, this.width * 4, this.width, this.height);
        return new Image(PixelFormat.BYTE_BGRA_PRE, byteBuffer2, this.width, this.height, 0, 0, this.width * 4, this.getPixelScale());
    }

    private Accessor<?> getPixelAccessor() {
        if (this.pixelaccessor == null) {
            switch (this.getPixelFormat()) {
                default: {
                    this.pixelaccessor = new UnsupportedAccess();
                    break;
                }
                case BYTE_GRAY: {
                    this.pixelaccessor = new ByteAccess(Image.getGrayFXPixelFormat(), ByteGray.getter, null, (ByteBuffer)this.pixelBuffer, 1);
                    break;
                }
                case BYTE_RGB: {
                    this.pixelaccessor = new ByteRgbAccess((ByteBuffer)this.pixelBuffer);
                    break;
                }
                case BYTE_BGRA_PRE: {
                    this.pixelaccessor = new ByteAccess((javafx.scene.image.PixelFormat<ByteBuffer>)FX_ByteBgraPre_FORMAT, (ByteBuffer)this.pixelBuffer, 4);
                    break;
                }
                case INT_ARGB_PRE: {
                    this.pixelaccessor = new IntAccess((javafx.scene.image.PixelFormat<IntBuffer>)FX_IntArgbPre_FORMAT, (IntBuffer)this.pixelBuffer);
                }
            }
            if (this.pixelScale != 1.0f) {
                this.pixelaccessor = new ScaledAccessor(this.pixelaccessor, this.pixelScale);
            }
        }
        return this.pixelaccessor;
    }

    @Override
    public void bufferDirty(Rectangle rectangle) {
        this.updateSerial(rectangle);
    }

    public javafx.scene.image.PixelFormat<?> getPlatformPixelFormat() {
        return this.getPixelAccessor().getPlatformPixelFormat();
    }

    @Override
    public boolean isWritable() {
        return this.getPixelAccessor().isWritable();
    }

    @Override
    public PlatformImage promoteToWritableImage() {
        return this.getPixelAccessor().promoteToWritableImage();
    }

    @Override
    public int getArgb(int n, int n2) {
        return this.getPixelAccessor().getArgb(n, n2);
    }

    @Override
    public void setArgb(int n, int n2, int n3) {
        this.getPixelAccessor().setArgb(n, n2, n3);
        this.updateSerial();
    }

    @Override
    public <T extends Buffer> void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<T> writablePixelFormat, T t, int n5) {
        this.getPixelAccessor().getPixels(n, n2, n3, n4, writablePixelFormat, t, n5);
    }

    @Override
    public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n5, int n6) {
        this.getPixelAccessor().getPixels(n, n2, n3, n4, writablePixelFormat, arrby, n5, n6);
    }

    @Override
    public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n5, int n6) {
        this.getPixelAccessor().getPixels(n, n2, n3, n4, writablePixelFormat, arrn, n5, n6);
    }

    @Override
    public <T extends Buffer> void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<T> pixelFormat, T t, int n5) {
        this.getPixelAccessor().setPixels(n, n2, n3, n4, pixelFormat, t, n5);
        this.updateSerial();
    }

    @Override
    public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n5, int n6) {
        this.getPixelAccessor().setPixels(n, n2, n3, n4, pixelFormat, arrby, n5, n6);
        this.updateSerial();
    }

    @Override
    public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n5, int n6) {
        this.getPixelAccessor().setPixels(n, n2, n3, n4, pixelFormat, arrn, n5, n6);
        this.updateSerial();
    }

    @Override
    public void setPixels(int n, int n2, int n3, int n4, PixelReader pixelReader, int n5, int n6) {
        this.getPixelAccessor().setPixels(n, n2, n3, n4, pixelReader, n5, n6);
        this.updateSerial();
    }

    public boolean isOpaque() {
        return this.pixelFormat.isOpaque();
    }

    static <I extends Buffer> PixelSetter<I> getSetterIfWritable(javafx.scene.image.PixelFormat<I> pixelFormat) {
        if (pixelFormat instanceof WritablePixelFormat) {
            return PixelUtils.getSetter((WritablePixelFormat)pixelFormat);
        }
        return null;
    }

    static javafx.scene.image.PixelFormat<ByteBuffer> getGrayFXPixelFormat() {
        if (FX_ByteGray_FORMAT == null) {
            int[] arrn = new int[256];
            int n = -16777216;
            for (int i = 0; i < 256; ++i) {
                arrn[i] = n;
                n += 65793;
            }
            FX_ByteGray_FORMAT = javafx.scene.image.PixelFormat.createByteIndexedPremultipliedInstance((int[])arrn);
        }
        return FX_ByteGray_FORMAT;
    }

    public static class Serial {
        private int id = 0;
        private Rectangle dirtyRegion = null;

        Serial() {
        }

        public synchronized Pair<Integer, Rectangle> getIdRect() {
            return new Pair((Object)this.id, (Object)(this.dirtyRegion == null ? null : new Rectangle(this.dirtyRegion)));
        }

        public synchronized void update(Rectangle rectangle) {
            ++this.id;
            this.dirtyRegion = rectangle;
        }
    }

    abstract class Accessor<I extends Buffer> {
        Accessor() {
        }

        public abstract int getArgb(int var1, int var2);

        public abstract void setArgb(int var1, int var2, int var3);

        public abstract javafx.scene.image.PixelFormat<I> getPlatformPixelFormat();

        public abstract boolean isWritable();

        public abstract PlatformImage promoteToWritableImage();

        public abstract <T extends Buffer> void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<T> var5, T var6, int var7);

        public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<ByteBuffer> var5, byte[] var6, int var7, int var8);

        public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat<IntBuffer> var5, int[] var6, int var7, int var8);

        public abstract <T extends Buffer> void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat<T> var5, T var6, int var7);

        public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat<ByteBuffer> var5, byte[] var6, int var7, int var8);

        public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat<IntBuffer> var5, int[] var6, int var7, int var8);

        public abstract void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7);
    }

    class UnsupportedAccess
    extends ByteAccess {
        private UnsupportedAccess() {
            super(null, null, null, null, 0);
        }
    }

    class ByteAccess
    extends BaseAccessor<ByteBuffer> {
        ByteAccess(javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, PixelGetter<ByteBuffer> pixelGetter, PixelSetter<ByteBuffer> pixelSetter, ByteBuffer byteBuffer, int n) {
            super(Image.this, pixelFormat, pixelGetter, pixelSetter, (Buffer)byteBuffer, n);
        }

        ByteAccess(javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, ByteBuffer byteBuffer, int n) {
            super(Image.this, pixelFormat, (Buffer)byteBuffer, n);
        }

        @Override
        public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n5, int n6) {
            BytePixelSetter bytePixelSetter = PixelUtils.getByteSetter(writablePixelFormat);
            ByteToBytePixelConverter byteToBytePixelConverter = PixelUtils.getB2BConverter(this.getGetter(), bytePixelSetter);
            byteToBytePixelConverter.convert((ByteBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, arrby, n5, n6, n3, n4);
        }

        @Override
        public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n5, int n6) {
            IntPixelSetter intPixelSetter = PixelUtils.getIntSetter(writablePixelFormat);
            ByteToIntPixelConverter byteToIntPixelConverter = PixelUtils.getB2IConverter(this.getGetter(), intPixelSetter);
            byteToIntPixelConverter.convert((ByteBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, arrn, n5, n6, n3, n4);
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n5, int n6) {
            BytePixelGetter bytePixelGetter = PixelUtils.getByteGetter(pixelFormat);
            ByteToBytePixelConverter byteToBytePixelConverter = PixelUtils.getB2BConverter(bytePixelGetter, this.getSetter());
            byteToBytePixelConverter.convert(arrby, n5, n6, (ByteBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, n3, n4);
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n5, int n6) {
            IntPixelGetter intPixelGetter = PixelUtils.getIntGetter(pixelFormat);
            IntToBytePixelConverter intToBytePixelConverter = PixelUtils.getI2BConverter(intPixelGetter, this.getSetter());
            intToBytePixelConverter.convert(arrn, n5, n6, (ByteBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, n3, n4);
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, PixelReader pixelReader, int n5, int n6) {
            ByteBuffer byteBuffer = ((ByteBuffer)this.theBuffer).duplicate();
            byteBuffer.position(byteBuffer.position() + this.getIndex(n, n2));
            pixelReader.getPixels(n5, n6, n3, n4, (WritablePixelFormat)this.theFormat, (Buffer)byteBuffer, this.scanlineElems);
        }
    }

    class ByteRgbAccess
    extends ByteAccess {
        public ByteRgbAccess(ByteBuffer byteBuffer) {
            super(FX_ByteRgb_FORMAT, byteBuffer, 3);
        }

        @Override
        public PlatformImage promoteToWritableImage() {
            return Image.this.promoteByteRgbToByteBgra();
        }
    }

    class IntAccess
    extends BaseAccessor<IntBuffer> {
        IntAccess(javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, IntBuffer intBuffer) {
            super(Image.this, pixelFormat, (Buffer)intBuffer, 1);
        }

        @Override
        public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n5, int n6) {
            BytePixelSetter bytePixelSetter = PixelUtils.getByteSetter(writablePixelFormat);
            IntToBytePixelConverter intToBytePixelConverter = PixelUtils.getI2BConverter(this.getGetter(), bytePixelSetter);
            intToBytePixelConverter.convert((IntBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, arrby, n5, n6, n3, n4);
        }

        @Override
        public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n5, int n6) {
            IntPixelSetter intPixelSetter = PixelUtils.getIntSetter(writablePixelFormat);
            IntToIntPixelConverter intToIntPixelConverter = PixelUtils.getI2IConverter(this.getGetter(), intPixelSetter);
            intToIntPixelConverter.convert((IntBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, arrn, n5, n6, n3, n4);
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n5, int n6) {
            BytePixelGetter bytePixelGetter = PixelUtils.getByteGetter(pixelFormat);
            ByteToIntPixelConverter byteToIntPixelConverter = PixelUtils.getB2IConverter(bytePixelGetter, this.getSetter());
            byteToIntPixelConverter.convert(arrby, n5, n6, (IntBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, n3, n4);
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n5, int n6) {
            IntPixelGetter intPixelGetter = PixelUtils.getIntGetter(pixelFormat);
            IntToIntPixelConverter intToIntPixelConverter = PixelUtils.getI2IConverter(intPixelGetter, this.getSetter());
            intToIntPixelConverter.convert(arrn, n5, n6, (IntBuffer)this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, n3, n4);
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, PixelReader pixelReader, int n5, int n6) {
            IntBuffer intBuffer = ((IntBuffer)this.theBuffer).duplicate();
            intBuffer.position(intBuffer.position() + this.getIndex(n, n2));
            pixelReader.getPixels(n5, n6, n3, n4, (WritablePixelFormat)this.theFormat, (Buffer)intBuffer, this.scanlineElems);
        }
    }

    class ScaledAccessor<I extends Buffer>
    extends Accessor<I> {
        Accessor<I> theDelegate;
        float pixelScale;

        ScaledAccessor(Accessor<I> accessor, float f) {
            this.theDelegate = accessor;
            this.pixelScale = f;
        }

        private int scale(int n) {
            return (int)(((float)n + 0.5f) * this.pixelScale);
        }

        @Override
        public int getArgb(int n, int n2) {
            return this.theDelegate.getArgb(this.scale(n), this.scale(n2));
        }

        @Override
        public void setArgb(int n, int n2, int n3) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public javafx.scene.image.PixelFormat<I> getPlatformPixelFormat() {
            return this.theDelegate.getPlatformPixelFormat();
        }

        @Override
        public boolean isWritable() {
            return this.theDelegate.isWritable();
        }

        @Override
        public PlatformImage promoteToWritableImage() {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public <T extends Buffer> void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<T> writablePixelFormat, T t, int n5) {
            PixelSetter<T> pixelSetter = PixelUtils.getSetter(writablePixelFormat);
            int n6 = t.position();
            int n7 = pixelSetter.getNumElements();
            for (int i = 0; i < n4; ++i) {
                int n8 = this.scale(n2 + i);
                int n9 = n6;
                for (int j = 0; j < n3; ++j) {
                    int n10 = this.scale(n + j);
                    pixelSetter.setArgb(t, n9, this.theDelegate.getArgb(n10, n8));
                    n9 += n7;
                }
                n6 += n5;
            }
        }

        @Override
        public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<ByteBuffer> writablePixelFormat, byte[] arrby, int n5, int n6) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(arrby);
            byteBuffer.position(n5);
            this.getPixels(n, n2, n3, n4, writablePixelFormat, byteBuffer, n6);
        }

        @Override
        public void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<IntBuffer> writablePixelFormat, int[] arrn, int n5, int n6) {
            IntBuffer intBuffer = IntBuffer.wrap(arrn);
            intBuffer.position(n5);
            this.getPixels(n, n2, n3, n4, writablePixelFormat, intBuffer, n6);
        }

        @Override
        public <T extends Buffer> void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<T> pixelFormat, T t, int n5) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<ByteBuffer> pixelFormat, byte[] arrby, int n5, int n6) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<IntBuffer> pixelFormat, int[] arrn, int n5, int n6) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }

        @Override
        public void setPixels(int n, int n2, int n3, int n4, PixelReader pixelReader, int n5, int n6) {
            throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
        }
    }

    abstract class BaseAccessor<I extends Buffer>
    extends Accessor<I> {
        javafx.scene.image.PixelFormat<I> theFormat;
        PixelGetter<I> theGetter;
        PixelSetter<I> theSetter;
        I theBuffer;
        int pixelElems;
        int scanlineElems;
        int offsetElems;
        final /* synthetic */ Image this$0;

        /*
         * WARNING - Possible parameter corruption
         */
        BaseAccessor(javafx.scene.image.PixelFormat<I> pixelFormat, I i, int n2) {
            this((Image)n, (javafx.scene.image.PixelFormat)pixelFormat, PixelUtils.getGetter(pixelFormat), Image.getSetterIfWritable(pixelFormat), (Buffer)i, n2);
        }

        /*
         * WARNING - Possible parameter corruption
         */
        BaseAccessor(javafx.scene.image.PixelFormat<I> pixelFormat, PixelGetter<I> pixelGetter, PixelSetter<I> pixelSetter, I i, int n2) {
            this.this$0 = (Image)n;
            this.theFormat = pixelFormat;
            this.theGetter = pixelGetter;
            this.theSetter = pixelSetter;
            this.theBuffer = i;
            this.pixelElems = n2;
            this.scanlineElems = n.scanlineStride / n.pixelFormat.getDataType().getSizeInBytes();
            this.offsetElems = n.minY * this.scanlineElems + n.minX * n2;
        }

        public int getIndex(int n, int n2) {
            if (n < 0 || n2 < 0 || n >= this.this$0.width || n2 >= this.this$0.height) {
                throw new IndexOutOfBoundsException(n + ", " + n2);
            }
            return this.offsetElems + n2 * this.scanlineElems + n * this.pixelElems;
        }

        public I getBuffer() {
            return this.theBuffer;
        }

        public PixelGetter<I> getGetter() {
            if (this.theGetter == null) {
                throw new UnsupportedOperationException("Unsupported Image type");
            }
            return this.theGetter;
        }

        public PixelSetter<I> getSetter() {
            if (this.theSetter == null) {
                throw new UnsupportedOperationException("Unsupported Image type");
            }
            return this.theSetter;
        }

        @Override
        public javafx.scene.image.PixelFormat<I> getPlatformPixelFormat() {
            return this.theFormat;
        }

        @Override
        public boolean isWritable() {
            return this.theSetter != null;
        }

        @Override
        public PlatformImage promoteToWritableImage() {
            return this.this$0;
        }

        @Override
        public int getArgb(int n, int n2) {
            return this.getGetter().getArgb(this.getBuffer(), this.getIndex(n, n2));
        }

        @Override
        public void setArgb(int n, int n2, int n3) {
            this.getSetter().setArgb(this.getBuffer(), this.getIndex(n, n2), n3);
        }

        @Override
        public <T extends Buffer> void getPixels(int n, int n2, int n3, int n4, WritablePixelFormat<T> writablePixelFormat, T t, int n5) {
            PixelSetter<T> pixelSetter = PixelUtils.getSetter(writablePixelFormat);
            PixelConverter<I, I> pixelConverter = PixelUtils.getConverter(this.getGetter(), pixelSetter);
            int n6 = t.position();
            pixelConverter.convert(this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, t, n6, n5, n3, n4);
        }

        @Override
        public <T extends Buffer> void setPixels(int n, int n2, int n3, int n4, javafx.scene.image.PixelFormat<T> pixelFormat, T t, int n5) {
            PixelGetter<T> pixelGetter = PixelUtils.getGetter(pixelFormat);
            PixelConverter<T, I> pixelConverter = PixelUtils.getConverter(pixelGetter, this.getSetter());
            int n6 = t.position();
            pixelConverter.convert(t, n6, n5, this.getBuffer(), this.getIndex(n, n2), this.scanlineElems, n3, n4);
        }
    }
}

