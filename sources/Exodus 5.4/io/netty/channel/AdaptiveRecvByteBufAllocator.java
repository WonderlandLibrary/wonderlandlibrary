/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import java.util.ArrayList;

public class AdaptiveRecvByteBufAllocator
implements RecvByteBufAllocator {
    private static final int INDEX_INCREMENT = 4;
    private static final int INDEX_DECREMENT = 1;
    private final int maxIndex;
    static final int DEFAULT_MAXIMUM = 65536;
    private final int initial;
    private static final int[] SIZE_TABLE;
    private final int minIndex;
    static final int DEFAULT_MINIMUM = 64;
    public static final AdaptiveRecvByteBufAllocator DEFAULT;
    static final int DEFAULT_INITIAL = 1024;

    public AdaptiveRecvByteBufAllocator(int n, int n2, int n3) {
        if (n <= 0) {
            throw new IllegalArgumentException("minimum: " + n);
        }
        if (n2 < n) {
            throw new IllegalArgumentException("initial: " + n2);
        }
        if (n3 < n2) {
            throw new IllegalArgumentException("maximum: " + n3);
        }
        int n4 = AdaptiveRecvByteBufAllocator.getSizeTableIndex(n);
        this.minIndex = SIZE_TABLE[n4] < n ? n4 + 1 : n4;
        int n5 = AdaptiveRecvByteBufAllocator.getSizeTableIndex(n3);
        this.maxIndex = SIZE_TABLE[n5] > n3 ? n5 - 1 : n5;
        this.initial = n2;
    }

    private AdaptiveRecvByteBufAllocator() {
        this(64, 1024, 65536);
    }

    private static int getSizeTableIndex(int n) {
        int n2;
        int n3;
        int n4 = 0;
        int n5 = SIZE_TABLE.length - 1;
        while (true) {
            if (n5 < n4) {
                return n4;
            }
            if (n5 == n4) {
                return n5;
            }
            n3 = n4 + n5 >>> 1;
            n2 = SIZE_TABLE[n3];
            int n6 = SIZE_TABLE[n3 + 1];
            if (n > n6) {
                n4 = n3 + 1;
                continue;
            }
            if (n >= n2) break;
            n5 = n3 - 1;
        }
        if (n == n2) {
            return n3;
        }
        return n3 + 1;
    }

    static {
        int n;
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (n = 16; n < 512; n += 16) {
            arrayList.add(n);
        }
        for (n = 512; n > 0; n <<= 1) {
            arrayList.add(n);
        }
        SIZE_TABLE = new int[arrayList.size()];
        for (n = 0; n < SIZE_TABLE.length; ++n) {
            AdaptiveRecvByteBufAllocator.SIZE_TABLE[n] = (Integer)arrayList.get(n);
        }
        DEFAULT = new AdaptiveRecvByteBufAllocator();
    }

    @Override
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl(this.minIndex, this.maxIndex, this.initial);
    }

    private static final class HandleImpl
    implements RecvByteBufAllocator.Handle {
        private int index;
        private final int minIndex;
        private final int maxIndex;
        private int nextReceiveBufferSize;
        private boolean decreaseNow;

        @Override
        public ByteBuf allocate(ByteBufAllocator byteBufAllocator) {
            return byteBufAllocator.ioBuffer(this.nextReceiveBufferSize);
        }

        HandleImpl(int n, int n2, int n3) {
            this.minIndex = n;
            this.maxIndex = n2;
            this.index = AdaptiveRecvByteBufAllocator.getSizeTableIndex(n3);
            this.nextReceiveBufferSize = SIZE_TABLE[this.index];
        }

        @Override
        public int guess() {
            return this.nextReceiveBufferSize;
        }

        @Override
        public void record(int n) {
            if (n <= SIZE_TABLE[Math.max(0, this.index - 1 - 1)]) {
                if (this.decreaseNow) {
                    this.index = Math.max(this.index - 1, this.minIndex);
                    this.nextReceiveBufferSize = SIZE_TABLE[this.index];
                    this.decreaseNow = false;
                } else {
                    this.decreaseNow = true;
                }
            } else if (n >= this.nextReceiveBufferSize) {
                this.index = Math.min(this.index + 4, this.maxIndex);
                this.nextReceiveBufferSize = SIZE_TABLE[this.index];
                this.decreaseNow = false;
            }
        }
    }
}

