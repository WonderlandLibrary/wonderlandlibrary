/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl;

import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ReadOnlyBufferException;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerWrapper;

public class PointerBuffer
implements Comparable {
    private static final boolean is64Bit;
    protected final ByteBuffer pointers;
    protected final Buffer view;
    protected final IntBuffer view32;
    protected final LongBuffer view64;

    public PointerBuffer(int capacity) {
        this(BufferUtils.createByteBuffer(capacity * PointerBuffer.getPointerSize()));
    }

    public PointerBuffer(ByteBuffer source) {
        if (LWJGLUtil.CHECKS) {
            PointerBuffer.checkSource(source);
        }
        this.pointers = source.slice().order(source.order());
        if (is64Bit) {
            this.view32 = null;
            this.view64 = this.pointers.asLongBuffer();
            this.view = this.view64;
        } else {
            this.view32 = this.pointers.asIntBuffer();
            this.view = this.view32;
            this.view64 = null;
        }
    }

    private static void checkSource(ByteBuffer source) {
        int alignment;
        if (!source.isDirect()) {
            throw new IllegalArgumentException("The source buffer is not direct.");
        }
        int n = alignment = is64Bit ? 8 : 4;
        if ((MemoryUtil.getAddress0(source) + (long)source.position()) % (long)alignment != 0L || source.remaining() % alignment != 0) {
            throw new IllegalArgumentException("The source buffer is not aligned to " + alignment + " bytes.");
        }
    }

    public ByteBuffer getBuffer() {
        return this.pointers;
    }

    public static boolean is64Bit() {
        return is64Bit;
    }

    public static int getPointerSize() {
        return is64Bit ? 8 : 4;
    }

    public final int capacity() {
        return this.view.capacity();
    }

    public final int position() {
        return this.view.position();
    }

    public final int positionByte() {
        return this.position() * PointerBuffer.getPointerSize();
    }

    public final PointerBuffer position(int newPosition) {
        this.view.position(newPosition);
        return this;
    }

    public final int limit() {
        return this.view.limit();
    }

    public final PointerBuffer limit(int newLimit) {
        this.view.limit(newLimit);
        return this;
    }

    public final PointerBuffer mark() {
        this.view.mark();
        return this;
    }

    public final PointerBuffer reset() {
        this.view.reset();
        return this;
    }

    public final PointerBuffer clear() {
        this.view.clear();
        return this;
    }

    public final PointerBuffer flip() {
        this.view.flip();
        return this;
    }

    public final PointerBuffer rewind() {
        this.view.rewind();
        return this;
    }

    public final int remaining() {
        return this.view.remaining();
    }

    public final int remainingByte() {
        return this.remaining() * PointerBuffer.getPointerSize();
    }

    public final boolean hasRemaining() {
        return this.view.hasRemaining();
    }

    public static PointerBuffer allocateDirect(int capacity) {
        return new PointerBuffer(capacity);
    }

    protected PointerBuffer newInstance(ByteBuffer source) {
        return new PointerBuffer(source);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public PointerBuffer slice() {
        int pointerSize = PointerBuffer.getPointerSize();
        this.pointers.position(this.view.position() * pointerSize);
        this.pointers.limit(this.view.limit() * pointerSize);
        try {
            PointerBuffer pointerBuffer = this.newInstance(this.pointers);
            return pointerBuffer;
        }
        finally {
            this.pointers.clear();
        }
    }

    public PointerBuffer duplicate() {
        PointerBuffer buffer = this.newInstance(this.pointers);
        buffer.position(this.view.position());
        buffer.limit(this.view.limit());
        return buffer;
    }

    public PointerBuffer asReadOnlyBuffer() {
        PointerBufferR buffer = new PointerBufferR(this.pointers);
        buffer.position(this.view.position());
        buffer.limit(this.view.limit());
        return buffer;
    }

    public boolean isReadOnly() {
        return false;
    }

    public long get() {
        if (is64Bit) {
            return this.view64.get();
        }
        return (long)this.view32.get() & 0xFFFFFFFFL;
    }

    public PointerBuffer put(long l) {
        if (is64Bit) {
            this.view64.put(l);
        } else {
            this.view32.put((int)l);
        }
        return this;
    }

    public PointerBuffer put(PointerWrapper pointer) {
        return this.put(pointer.getPointer());
    }

    public static void put(ByteBuffer target, long l) {
        if (is64Bit) {
            target.putLong(l);
        } else {
            target.putInt((int)l);
        }
    }

    public long get(int index) {
        if (is64Bit) {
            return this.view64.get(index);
        }
        return (long)this.view32.get(index) & 0xFFFFFFFFL;
    }

    public PointerBuffer put(int index, long l) {
        if (is64Bit) {
            this.view64.put(index, l);
        } else {
            this.view32.put(index, (int)l);
        }
        return this;
    }

    public PointerBuffer put(int index, PointerWrapper pointer) {
        return this.put(index, pointer.getPointer());
    }

    public static void put(ByteBuffer target, int index, long l) {
        if (is64Bit) {
            target.putLong(index, l);
        } else {
            target.putInt(index, (int)l);
        }
    }

    public PointerBuffer get(long[] dst, int offset, int length) {
        if (is64Bit) {
            this.view64.get(dst, offset, length);
        } else {
            PointerBuffer.checkBounds(offset, length, dst.length);
            if (length > this.view32.remaining()) {
                throw new BufferUnderflowException();
            }
            int end = offset + length;
            for (int i = offset; i < end; ++i) {
                dst[i] = (long)this.view32.get() & 0xFFFFFFFFL;
            }
        }
        return this;
    }

    public PointerBuffer get(long[] dst) {
        return this.get(dst, 0, dst.length);
    }

    public PointerBuffer put(PointerBuffer src) {
        if (is64Bit) {
            this.view64.put(src.view64);
        } else {
            this.view32.put(src.view32);
        }
        return this;
    }

    public PointerBuffer put(long[] src, int offset, int length) {
        if (is64Bit) {
            this.view64.put(src, offset, length);
        } else {
            PointerBuffer.checkBounds(offset, length, src.length);
            if (length > this.view32.remaining()) {
                throw new BufferOverflowException();
            }
            int end = offset + length;
            for (int i = offset; i < end; ++i) {
                this.view32.put((int)src[i]);
            }
        }
        return this;
    }

    public final PointerBuffer put(long[] src) {
        return this.put(src, 0, src.length);
    }

    public PointerBuffer compact() {
        if (is64Bit) {
            this.view64.compact();
        } else {
            this.view32.compact();
        }
        return this;
    }

    public ByteOrder order() {
        if (is64Bit) {
            return this.view64.order();
        }
        return this.view32.order();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(48);
        sb.append(this.getClass().getName());
        sb.append("[pos=");
        sb.append(this.position());
        sb.append(" lim=");
        sb.append(this.limit());
        sb.append(" cap=");
        sb.append(this.capacity());
        sb.append("]");
        return sb.toString();
    }

    public int hashCode() {
        int h = 1;
        int p = this.position();
        for (int i = this.limit() - 1; i >= p; --i) {
            h = 31 * h + (int)this.get(i);
        }
        return h;
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof PointerBuffer)) {
            return false;
        }
        PointerBuffer that = (PointerBuffer)ob;
        if (this.remaining() != that.remaining()) {
            return false;
        }
        int p = this.position();
        int i = this.limit() - 1;
        int j = that.limit() - 1;
        while (i >= p) {
            long v2;
            long v1 = this.get(i);
            if (v1 != (v2 = that.get(j))) {
                return false;
            }
            --i;
            --j;
        }
        return true;
    }

    public int compareTo(Object o) {
        PointerBuffer that = (PointerBuffer)o;
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        int i = this.position();
        int j = that.position();
        while (i < n) {
            long v2;
            long v1 = this.get(i);
            if (v1 != (v2 = that.get(j))) {
                if (v1 < v2) {
                    return -1;
                }
                return 1;
            }
            ++i;
            ++j;
        }
        return this.remaining() - that.remaining();
    }

    private static void checkBounds(int off, int len, int size) {
        if ((off | len | off + len | size - (off + len)) < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static {
        boolean is64 = false;
        try {
            Method m = Class.forName("org.lwjgl.Sys").getDeclaredMethod("is64Bit", null);
            is64 = (Boolean)m.invoke(null, null);
        }
        catch (Throwable throwable) {
        }
        finally {
            is64Bit = is64;
        }
    }

    private static final class PointerBufferR
    extends PointerBuffer {
        PointerBufferR(ByteBuffer source) {
            super(source);
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        protected PointerBuffer newInstance(ByteBuffer source) {
            return new PointerBufferR(source);
        }

        @Override
        public PointerBuffer asReadOnlyBuffer() {
            return this.duplicate();
        }

        @Override
        public PointerBuffer put(long l) {
            throw new ReadOnlyBufferException();
        }

        @Override
        public PointerBuffer put(int index, long l) {
            throw new ReadOnlyBufferException();
        }

        @Override
        public PointerBuffer put(PointerBuffer src) {
            throw new ReadOnlyBufferException();
        }

        @Override
        public PointerBuffer put(long[] src, int offset, int length) {
            throw new ReadOnlyBufferException();
        }

        @Override
        public PointerBuffer compact() {
            throw new ReadOnlyBufferException();
        }
    }
}

