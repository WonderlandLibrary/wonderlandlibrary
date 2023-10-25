/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.AdvancedLeakAwareByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.SimpleLeakAwareByteBuf;
import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

public abstract class AbstractByteBufAllocator
implements ByteBufAllocator {
    private static final int DEFAULT_MAX_COMPONENTS = 16;
    private final ByteBuf emptyBuf;
    private final boolean directByDefault;
    private static final int DEFAULT_INITIAL_CAPACITY = 256;

    protected AbstractByteBufAllocator() {
        this(false);
    }

    @Override
    public CompositeByteBuf compositeBuffer(int n) {
        if (this.directByDefault) {
            return this.compositeDirectBuffer(n);
        }
        return this.compositeHeapBuffer(n);
    }

    @Override
    public ByteBuf directBuffer(int n, int n2) {
        if (n == 0 && n2 == 0) {
            return this.emptyBuf;
        }
        AbstractByteBufAllocator.validate(n, n2);
        return this.newDirectBuffer(n, n2);
    }

    @Override
    public ByteBuf heapBuffer() {
        return this.heapBuffer(256, Integer.MAX_VALUE);
    }

    @Override
    public ByteBuf ioBuffer(int n) {
        if (PlatformDependent.hasUnsafe()) {
            return this.directBuffer(n);
        }
        return this.heapBuffer(n);
    }

    protected abstract ByteBuf newHeapBuffer(int var1, int var2);

    @Override
    public CompositeByteBuf compositeBuffer() {
        if (this.directByDefault) {
            return this.compositeDirectBuffer();
        }
        return this.compositeHeapBuffer();
    }

    protected static ByteBuf toLeakAwareBuffer(ByteBuf byteBuf) {
        switch (ResourceLeakDetector.getLevel()) {
            case SIMPLE: {
                ResourceLeak resourceLeak = AbstractByteBuf.leakDetector.open(byteBuf);
                if (resourceLeak == null) break;
                byteBuf = new SimpleLeakAwareByteBuf(byteBuf, resourceLeak);
                break;
            }
            case ADVANCED: 
            case PARANOID: {
                ResourceLeak resourceLeak = AbstractByteBuf.leakDetector.open(byteBuf);
                if (resourceLeak == null) break;
                byteBuf = new AdvancedLeakAwareByteBuf(byteBuf, resourceLeak);
            }
        }
        return byteBuf;
    }

    @Override
    public CompositeByteBuf compositeDirectBuffer(int n) {
        return new CompositeByteBuf(this, true, n);
    }

    @Override
    public ByteBuf buffer(int n, int n2) {
        if (this.directByDefault) {
            return this.directBuffer(n, n2);
        }
        return this.heapBuffer(n, n2);
    }

    protected AbstractByteBufAllocator(boolean bl) {
        this.directByDefault = bl && PlatformDependent.hasUnsafe();
        this.emptyBuf = new EmptyByteBuf(this);
    }

    @Override
    public CompositeByteBuf compositeDirectBuffer() {
        return this.compositeDirectBuffer(16);
    }

    @Override
    public ByteBuf buffer(int n) {
        if (this.directByDefault) {
            return this.directBuffer(n);
        }
        return this.heapBuffer(n);
    }

    @Override
    public ByteBuf heapBuffer(int n, int n2) {
        if (n == 0 && n2 == 0) {
            return this.emptyBuf;
        }
        AbstractByteBufAllocator.validate(n, n2);
        return this.newHeapBuffer(n, n2);
    }

    @Override
    public ByteBuf ioBuffer(int n, int n2) {
        if (PlatformDependent.hasUnsafe()) {
            return this.directBuffer(n, n2);
        }
        return this.heapBuffer(n, n2);
    }

    @Override
    public ByteBuf directBuffer(int n) {
        return this.directBuffer(n, Integer.MAX_VALUE);
    }

    @Override
    public CompositeByteBuf compositeHeapBuffer() {
        return this.compositeHeapBuffer(16);
    }

    @Override
    public ByteBuf ioBuffer() {
        if (PlatformDependent.hasUnsafe()) {
            return this.directBuffer(256);
        }
        return this.heapBuffer(256);
    }

    @Override
    public ByteBuf heapBuffer(int n) {
        return this.heapBuffer(n, Integer.MAX_VALUE);
    }

    private static void validate(int n, int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("initialCapacity: " + n + " (expectd: 0+)");
        }
        if (n > n2) {
            throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", n, n2));
        }
    }

    @Override
    public CompositeByteBuf compositeHeapBuffer(int n) {
        return new CompositeByteBuf(this, false, n);
    }

    protected abstract ByteBuf newDirectBuffer(int var1, int var2);

    @Override
    public ByteBuf directBuffer() {
        return this.directBuffer(256, Integer.MAX_VALUE);
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "(directByDefault: " + this.directByDefault + ')';
    }

    @Override
    public ByteBuf buffer() {
        if (this.directByDefault) {
            return this.directBuffer();
        }
        return this.heapBuffer();
    }
}

