/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelPromiseAggregator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class PendingWriteQueue {
    private final ChannelHandlerContext ctx;
    private PendingWrite tail;
    private final MessageSizeEstimator.Handle estimatorHandle;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
    private final ChannelOutboundBuffer buffer;
    private int size;
    private PendingWrite head;

    public void removeAndFail(Throwable throwable) {
        assert (this.ctx.executor().inEventLoop());
        if (throwable == null) {
            throw new NullPointerException("cause");
        }
        PendingWrite pendingWrite = this.head;
        if (pendingWrite == null) {
            return;
        }
        ReferenceCountUtil.safeRelease(pendingWrite.msg);
        ChannelPromise channelPromise = pendingWrite.promise;
        PendingWriteQueue.safeFail(channelPromise, throwable);
        this.recycle(pendingWrite);
    }

    public boolean isEmpty() {
        assert (this.ctx.executor().inEventLoop());
        return this.head == null;
    }

    public ChannelFuture removeAndWriteAll() {
        assert (this.ctx.executor().inEventLoop());
        PendingWrite pendingWrite = this.head;
        if (pendingWrite == null) {
            return null;
        }
        if (this.size == 1) {
            return this.removeAndWrite();
        }
        ChannelPromise channelPromise = this.ctx.newPromise();
        ChannelPromiseAggregator channelPromiseAggregator = new ChannelPromiseAggregator(channelPromise);
        while (pendingWrite != null) {
            PendingWrite pendingWrite2 = pendingWrite.next;
            Object object = pendingWrite.msg;
            ChannelPromise channelPromise2 = pendingWrite.promise;
            this.recycle(pendingWrite);
            this.ctx.write(object, channelPromise2);
            channelPromiseAggregator.add(channelPromise2);
            pendingWrite = pendingWrite2;
        }
        this.assertEmpty();
        return channelPromise;
    }

    public ChannelFuture removeAndWrite() {
        assert (this.ctx.executor().inEventLoop());
        PendingWrite pendingWrite = this.head;
        if (pendingWrite == null) {
            return null;
        }
        Object object = pendingWrite.msg;
        ChannelPromise channelPromise = pendingWrite.promise;
        this.recycle(pendingWrite);
        return this.ctx.write(object, channelPromise);
    }

    public ChannelPromise remove() {
        assert (this.ctx.executor().inEventLoop());
        PendingWrite pendingWrite = this.head;
        if (pendingWrite == null) {
            return null;
        }
        ChannelPromise channelPromise = pendingWrite.promise;
        ReferenceCountUtil.safeRelease(pendingWrite.msg);
        this.recycle(pendingWrite);
        return channelPromise;
    }

    public void add(Object object, ChannelPromise channelPromise) {
        assert (this.ctx.executor().inEventLoop());
        if (object == null) {
            throw new NullPointerException("msg");
        }
        if (channelPromise == null) {
            throw new NullPointerException("promise");
        }
        int n = this.estimatorHandle.size(object);
        if (n < 0) {
            n = 0;
        }
        PendingWrite pendingWrite = PendingWrite.newInstance(object, n, channelPromise);
        PendingWrite pendingWrite2 = this.tail;
        if (pendingWrite2 == null) {
            this.tail = this.head = pendingWrite;
        } else {
            pendingWrite2.next = pendingWrite;
            this.tail = pendingWrite;
        }
        ++this.size;
        this.buffer.incrementPendingOutboundBytes(pendingWrite.size);
    }

    private void recycle(PendingWrite pendingWrite) {
        PendingWrite pendingWrite2 = pendingWrite.next;
        this.buffer.decrementPendingOutboundBytes(pendingWrite.size);
        pendingWrite.recycle();
        --this.size;
        if (pendingWrite2 == null) {
            this.tail = null;
            this.head = null;
            assert (this.size == 0);
        } else {
            this.head = pendingWrite2;
            assert (this.size > 0);
        }
    }

    private void assertEmpty() {
        assert (this.tail == null && this.head == null && this.size == 0);
    }

    public void removeAndFailAll(Throwable throwable) {
        assert (this.ctx.executor().inEventLoop());
        if (throwable == null) {
            throw new NullPointerException("cause");
        }
        PendingWrite pendingWrite = this.head;
        while (pendingWrite != null) {
            PendingWrite pendingWrite2 = pendingWrite.next;
            ReferenceCountUtil.safeRelease(pendingWrite.msg);
            ChannelPromise channelPromise = pendingWrite.promise;
            this.recycle(pendingWrite);
            PendingWriteQueue.safeFail(channelPromise, throwable);
            pendingWrite = pendingWrite2;
        }
        this.assertEmpty();
    }

    public Object current() {
        assert (this.ctx.executor().inEventLoop());
        PendingWrite pendingWrite = this.head;
        if (pendingWrite == null) {
            return null;
        }
        return pendingWrite.msg;
    }

    public PendingWriteQueue(ChannelHandlerContext channelHandlerContext) {
        if (channelHandlerContext == null) {
            throw new NullPointerException("ctx");
        }
        this.ctx = channelHandlerContext;
        this.buffer = channelHandlerContext.channel().unsafe().outboundBuffer();
        this.estimatorHandle = channelHandlerContext.channel().config().getMessageSizeEstimator().newHandle();
    }

    public int size() {
        assert (this.ctx.executor().inEventLoop());
        return this.size;
    }

    private static void safeFail(ChannelPromise channelPromise, Throwable throwable) {
        if (!(channelPromise instanceof VoidChannelPromise) && !channelPromise.tryFailure(throwable)) {
            logger.warn("Failed to mark a promise as failure because it's done already: {}", (Object)channelPromise, (Object)throwable);
        }
    }

    static final class PendingWrite {
        private static final Recycler<PendingWrite> RECYCLER = new Recycler<PendingWrite>(){

            @Override
            protected PendingWrite newObject(Recycler.Handle handle) {
                return new PendingWrite(handle);
            }
        };
        private Object msg;
        private ChannelPromise promise;
        private PendingWrite next;
        private long size;
        private final Recycler.Handle handle;

        static PendingWrite newInstance(Object object, int n, ChannelPromise channelPromise) {
            PendingWrite pendingWrite = RECYCLER.get();
            pendingWrite.size = n;
            pendingWrite.msg = object;
            pendingWrite.promise = channelPromise;
            return pendingWrite;
        }

        private PendingWrite(Recycler.Handle handle) {
            this.handle = handle;
        }

        private void recycle() {
            this.size = 0L;
            this.next = null;
            this.msg = null;
            this.promise = null;
            RECYCLER.recycle(this, this.handle);
        }
    }
}

