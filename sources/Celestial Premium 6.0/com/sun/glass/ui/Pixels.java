/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public abstract class Pixels {
    protected final int width;
    protected final int height;
    protected final int bytesPerComponent;
    protected final ByteBuffer bytes;
    protected final IntBuffer ints;
    private final float scalex;
    private final float scaley;

    public static int getNativeFormat() {
        Application.checkEventThread();
        return Application.GetApplication().staticPixels_getNativeFormat();
    }

    protected Pixels(int n, int n2, ByteBuffer byteBuffer) {
        this.width = n;
        this.height = n2;
        this.bytesPerComponent = 1;
        this.bytes = byteBuffer.slice();
        if (this.width <= 0 || this.height <= 0 || this.width * this.height * 4 > this.bytes.capacity()) {
            throw new IllegalArgumentException("Too small byte buffer size " + this.width + "x" + this.height + " [" + this.width * this.height * 4 + "] > " + this.bytes.capacity());
        }
        this.ints = null;
        this.scalex = 1.0f;
        this.scaley = 1.0f;
    }

    protected Pixels(int n, int n2, IntBuffer intBuffer) {
        this.width = n;
        this.height = n2;
        this.bytesPerComponent = 4;
        this.ints = intBuffer.slice();
        if (this.width <= 0 || this.height <= 0 || this.width * this.height > this.ints.capacity()) {
            throw new IllegalArgumentException("Too small int buffer size " + this.width + "x" + this.height + " [" + this.width * this.height + "] > " + this.ints.capacity());
        }
        this.bytes = null;
        this.scalex = 1.0f;
        this.scaley = 1.0f;
    }

    protected Pixels(int n, int n2, IntBuffer intBuffer, float f, float f2) {
        this.width = n;
        this.height = n2;
        this.bytesPerComponent = 4;
        this.ints = intBuffer.slice();
        if (this.width <= 0 || this.height <= 0 || this.width * this.height > this.ints.capacity()) {
            throw new IllegalArgumentException("Too small int buffer size " + this.width + "x" + this.height + " [" + this.width * this.height + "] > " + this.ints.capacity());
        }
        this.bytes = null;
        this.scalex = f;
        this.scaley = f2;
    }

    public final float getScaleX() {
        Application.checkEventThread();
        return this.scalex;
    }

    public final float getScaleY() {
        Application.checkEventThread();
        return this.scaley;
    }

    public final float getScaleXUnsafe() {
        return this.scalex;
    }

    public final float getScaleYUnsafe() {
        return this.scaley;
    }

    public final int getWidth() {
        Application.checkEventThread();
        return this.width;
    }

    public final int getWidthUnsafe() {
        return this.width;
    }

    public final int getHeight() {
        Application.checkEventThread();
        return this.height;
    }

    public final int getHeightUnsafe() {
        return this.height;
    }

    public final int getBytesPerComponent() {
        Application.checkEventThread();
        return this.bytesPerComponent;
    }

    public final Buffer getPixels() {
        if (this.bytes != null) {
            this.bytes.rewind();
            return this.bytes;
        }
        if (this.ints != null) {
            this.ints.rewind();
            return this.ints;
        }
        throw new RuntimeException("Unexpected Pixels state.");
    }

    public final Buffer getBuffer() {
        if (this.bytes != null) {
            return this.bytes;
        }
        if (this.ints != null) {
            return this.ints;
        }
        throw new RuntimeException("Unexpected Pixels state.");
    }

    public final ByteBuffer asByteBuffer() {
        Application.checkEventThread();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(this.getWidth() * this.getHeight() * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.rewind();
        this.asByteBuffer(byteBuffer);
        return byteBuffer;
    }

    public final void asByteBuffer(ByteBuffer byteBuffer) {
        Application.checkEventThread();
        if (!byteBuffer.isDirect()) {
            throw new RuntimeException("Expected direct buffer.");
        }
        if (byteBuffer.remaining() < this.getWidth() * this.getHeight() * 4) {
            throw new RuntimeException("Too small buffer.");
        }
        this._fillDirectByteBuffer(byteBuffer);
    }

    private void attachData(long l) {
        int[] arrn;
        if (this.ints != null) {
            arrn = !this.ints.isDirect() ? this.ints.array() : null;
            this._attachInt(l, this.width, this.height, this.ints, arrn, arrn != null ? this.ints.arrayOffset() : 0);
        }
        if (this.bytes != null) {
            arrn = !this.bytes.isDirect() ? this.bytes.array() : null;
            this._attachByte(l, this.width, this.height, this.bytes, (byte[])arrn, arrn != null ? this.bytes.arrayOffset() : 0);
        }
    }

    protected abstract void _fillDirectByteBuffer(ByteBuffer var1);

    protected abstract void _attachInt(long var1, int var3, int var4, IntBuffer var5, int[] var6, int var7);

    protected abstract void _attachByte(long var1, int var3, int var4, ByteBuffer var5, byte[] var6, int var7);

    public final boolean equals(Object object) {
        boolean bl;
        Application.checkEventThread();
        boolean bl2 = bl = object != null && this.getClass().equals(object.getClass());
        if (bl) {
            Pixels pixels = (Pixels)object;
            boolean bl3 = bl = this.getWidth() == pixels.getWidth() && this.getHeight() == pixels.getHeight();
            if (bl) {
                ByteBuffer byteBuffer;
                ByteBuffer byteBuffer2 = this.asByteBuffer();
                bl = byteBuffer2.compareTo(byteBuffer = pixels.asByteBuffer()) == 0;
            }
        }
        return bl;
    }

    public final int hashCode() {
        Application.checkEventThread();
        int n = this.getWidth();
        n = 31 * n + this.getHeight();
        n = 17 * n + this.asByteBuffer().hashCode();
        return n;
    }

    public static class Format {
        public static final int BYTE_BGRA_PRE = 1;
        public static final int BYTE_ARGB = 2;
    }
}

