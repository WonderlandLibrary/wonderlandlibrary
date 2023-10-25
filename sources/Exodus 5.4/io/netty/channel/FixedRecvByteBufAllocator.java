/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;

public class FixedRecvByteBufAllocator
implements RecvByteBufAllocator {
    private final RecvByteBufAllocator.Handle handle;

    public FixedRecvByteBufAllocator(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("bufferSize must greater than 0: " + n);
        }
        this.handle = new HandleImpl(n);
    }

    @Override
    public RecvByteBufAllocator.Handle newHandle() {
        return this.handle;
    }

    private static final class HandleImpl
    implements RecvByteBufAllocator.Handle {
        private final int bufferSize;

        @Override
        public ByteBuf allocate(ByteBufAllocator byteBufAllocator) {
            return byteBufAllocator.ioBuffer(this.bufferSize);
        }

        @Override
        public void record(int n) {
        }

        @Override
        public int guess() {
            return this.bufferSize;
        }

        HandleImpl(int n) {
            this.bufferSize = n;
        }
    }
}

