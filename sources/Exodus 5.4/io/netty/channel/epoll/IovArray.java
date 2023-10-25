/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.epoll.Native;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.PlatformDependent;

final class IovArray
implements ChannelOutboundBuffer.MessageProcessor {
    private final long memoryAddress = PlatformDependent.allocateMemory(CAPACITY);
    private static final int CAPACITY;
    private int count;
    private static final FastThreadLocal<IovArray> ARRAY;
    private static final int ADDRESS_SIZE;
    private static final int IOV_SIZE;
    private long size;

    static IovArray get(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        IovArray iovArray = ARRAY.get();
        iovArray.size = 0L;
        iovArray.count = 0;
        channelOutboundBuffer.forEachFlushedMessage(iovArray);
        return iovArray;
    }

    private IovArray() {
    }

    long memoryAddress(int n) {
        return this.memoryAddress + (long)(IOV_SIZE * n);
    }

    long size() {
        return this.size;
    }

    long processWritten(int n, long l) {
        long l2 = this.memoryAddress(n);
        long l3 = l2 + (long)ADDRESS_SIZE;
        if (ADDRESS_SIZE == 8) {
            long l4 = PlatformDependent.getLong(l3);
            if (l4 > l) {
                long l5 = PlatformDependent.getLong(l2);
                PlatformDependent.putLong(l2, l5 + l);
                PlatformDependent.putLong(l3, l4 - l);
                return -1L;
            }
            return l4;
        }
        assert (ADDRESS_SIZE == 4);
        long l6 = PlatformDependent.getInt(l3);
        if (l6 > l) {
            int n2 = PlatformDependent.getInt(l2);
            PlatformDependent.putInt(l2, (int)((long)n2 + l));
            PlatformDependent.putInt(l3, (int)(l6 - l));
            return -1L;
        }
        return l6;
    }

    @Override
    public boolean processMessage(Object object) throws Exception {
        return object instanceof ByteBuf && this.add((ByteBuf)object);
    }

    static {
        ADDRESS_SIZE = PlatformDependent.addressSize();
        IOV_SIZE = 2 * ADDRESS_SIZE;
        CAPACITY = Native.IOV_MAX * IOV_SIZE;
        ARRAY = new FastThreadLocal<IovArray>(){

            @Override
            protected void onRemoval(IovArray iovArray) throws Exception {
                PlatformDependent.freeMemory(iovArray.memoryAddress);
            }

            @Override
            protected IovArray initialValue() throws Exception {
                return new IovArray();
            }
        };
    }

    private boolean add(ByteBuf byteBuf) {
        if (this.count == Native.IOV_MAX) {
            return false;
        }
        int n = byteBuf.readableBytes();
        if (n == 0) {
            return true;
        }
        long l = byteBuf.memoryAddress();
        int n2 = byteBuf.readerIndex();
        long l2 = this.memoryAddress(this.count++);
        long l3 = l2 + (long)ADDRESS_SIZE;
        if (ADDRESS_SIZE == 8) {
            PlatformDependent.putLong(l2, l + (long)n2);
            PlatformDependent.putLong(l3, n);
        } else {
            assert (ADDRESS_SIZE == 4);
            PlatformDependent.putInt(l2, (int)l + n2);
            PlatformDependent.putInt(l3, n);
        }
        this.size += (long)n;
        return true;
    }

    int count() {
        return this.count;
    }
}

