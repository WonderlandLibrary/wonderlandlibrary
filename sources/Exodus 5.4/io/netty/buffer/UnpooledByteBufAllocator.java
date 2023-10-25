/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.util.internal.PlatformDependent;

public final class UnpooledByteBufAllocator
extends AbstractByteBufAllocator {
    public static final UnpooledByteBufAllocator DEFAULT = new UnpooledByteBufAllocator(PlatformDependent.directBufferPreferred());

    public UnpooledByteBufAllocator(boolean bl) {
        super(bl);
    }

    @Override
    protected ByteBuf newDirectBuffer(int n, int n2) {
        AbstractReferenceCountedByteBuf abstractReferenceCountedByteBuf = PlatformDependent.hasUnsafe() ? new UnpooledUnsafeDirectByteBuf((ByteBufAllocator)this, n, n2) : new UnpooledDirectByteBuf((ByteBufAllocator)this, n, n2);
        return UnpooledByteBufAllocator.toLeakAwareBuffer(abstractReferenceCountedByteBuf);
    }

    @Override
    public boolean isDirectBufferPooled() {
        return false;
    }

    @Override
    protected ByteBuf newHeapBuffer(int n, int n2) {
        return new UnpooledHeapByteBuf((ByteBufAllocator)this, n, n2);
    }
}

