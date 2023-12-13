/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.PointerBuffer;

public final class BufferUtils {
    public static ByteBuffer createByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }

    public static ShortBuffer createShortBuffer(int size) {
        return BufferUtils.createByteBuffer(size << 1).asShortBuffer();
    }

    public static CharBuffer createCharBuffer(int size) {
        return BufferUtils.createByteBuffer(size << 1).asCharBuffer();
    }

    public static IntBuffer createIntBuffer(int size) {
        return BufferUtils.createByteBuffer(size << 2).asIntBuffer();
    }

    public static LongBuffer createLongBuffer(int size) {
        return BufferUtils.createByteBuffer(size << 3).asLongBuffer();
    }

    public static FloatBuffer createFloatBuffer(int size) {
        return BufferUtils.createByteBuffer(size << 2).asFloatBuffer();
    }

    public static DoubleBuffer createDoubleBuffer(int size) {
        return BufferUtils.createByteBuffer(size << 3).asDoubleBuffer();
    }

    public static PointerBuffer createPointerBuffer(int size) {
        return PointerBuffer.allocateDirect(size);
    }

    public static int getElementSizeExponent(Buffer buf) {
        if (buf instanceof ByteBuffer) {
            return 0;
        }
        if (buf instanceof ShortBuffer || buf instanceof CharBuffer) {
            return 1;
        }
        if (buf instanceof FloatBuffer || buf instanceof IntBuffer) {
            return 2;
        }
        if (buf instanceof LongBuffer || buf instanceof DoubleBuffer) {
            return 3;
        }
        throw new IllegalStateException("Unsupported buffer type: " + buf);
    }

    public static int getOffset(Buffer buffer) {
        return buffer.position() << BufferUtils.getElementSizeExponent(buffer);
    }

    public static void zeroBuffer(ByteBuffer b2) {
        BufferUtils.zeroBuffer0(b2, b2.position(), b2.remaining());
    }

    public static void zeroBuffer(ShortBuffer b2) {
        BufferUtils.zeroBuffer0(b2, (long)b2.position() * 2L, (long)b2.remaining() * 2L);
    }

    public static void zeroBuffer(CharBuffer b2) {
        BufferUtils.zeroBuffer0(b2, (long)b2.position() * 2L, (long)b2.remaining() * 2L);
    }

    public static void zeroBuffer(IntBuffer b2) {
        BufferUtils.zeroBuffer0(b2, (long)b2.position() * 4L, (long)b2.remaining() * 4L);
    }

    public static void zeroBuffer(FloatBuffer b2) {
        BufferUtils.zeroBuffer0(b2, (long)b2.position() * 4L, (long)b2.remaining() * 4L);
    }

    public static void zeroBuffer(LongBuffer b2) {
        BufferUtils.zeroBuffer0(b2, (long)b2.position() * 8L, (long)b2.remaining() * 8L);
    }

    public static void zeroBuffer(DoubleBuffer b2) {
        BufferUtils.zeroBuffer0(b2, (long)b2.position() * 8L, (long)b2.remaining() * 8L);
    }

    private static native void zeroBuffer0(Buffer var0, long var1, long var3);

    static native long getBufferAddress(Buffer var0);
}

