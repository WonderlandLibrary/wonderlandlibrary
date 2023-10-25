/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.FileRegion;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public final class ChannelOutboundBuffer {
    private static final FastThreadLocal<ByteBuffer[]> NIO_BUFFERS;
    private static final InternalLogger logger;
    private volatile int writable = 1;
    private boolean inFail;
    private Entry unflushedEntry;
    private int flushed;
    private static final AtomicIntegerFieldUpdater<ChannelOutboundBuffer> WRITABLE_UPDATER;
    private long nioBufferSize;
    private Entry tailEntry;
    private static final AtomicLongFieldUpdater<ChannelOutboundBuffer> TOTAL_PENDING_SIZE_UPDATER;
    private final Channel channel;
    private Entry flushedEntry;
    private int nioBufferCount;
    private volatile long totalPendingSize;

    void close(final ClosedChannelException closedChannelException) {
        if (this.inFail) {
            this.channel.eventLoop().execute(new Runnable(){

                @Override
                public void run() {
                    ChannelOutboundBuffer.this.close(closedChannelException);
                }
            });
            return;
        }
        this.inFail = true;
        if (this.channel.isOpen()) {
            throw new IllegalStateException("close() must be invoked after the channel is closed.");
        }
        if (!this.isEmpty()) {
            throw new IllegalStateException("close() must be invoked after all flushed writes are handled.");
        }
        for (Entry entry = this.unflushedEntry; entry != null; entry = entry.recycleAndGetNext()) {
            int n = entry.pendingSize;
            TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -n);
            if (entry.cancelled) continue;
            ReferenceCountUtil.safeRelease(entry.msg);
            ChannelOutboundBuffer.safeFail(entry.promise, closedChannelException);
        }
        this.inFail = false;
    }

    private static void safeFail(ChannelPromise channelPromise, Throwable throwable) {
        if (!(channelPromise instanceof VoidChannelPromise) && !channelPromise.tryFailure(throwable)) {
            logger.warn("Failed to mark a promise as failure because it's done already: {}", (Object)channelPromise, (Object)throwable);
        }
    }

    private boolean isFlushedEntry(Entry entry) {
        return entry != null && entry != this.unflushedEntry;
    }

    ChannelOutboundBuffer(AbstractChannel abstractChannel) {
        this.channel = abstractChannel;
    }

    private void removeEntry(Entry entry) {
        if (--this.flushed == 0) {
            this.flushedEntry = null;
            if (entry == this.tailEntry) {
                this.tailEntry = null;
                this.unflushedEntry = null;
            }
        } else {
            this.flushedEntry = entry.next;
        }
    }

    public void addFlush() {
        Entry entry = this.unflushedEntry;
        if (entry != null) {
            if (this.flushedEntry == null) {
                this.flushedEntry = entry;
            }
            do {
                ++this.flushed;
                if (entry.promise.setUncancellable()) continue;
                int n = entry.cancel();
                this.decrementPendingOutboundBytes(n);
            } while ((entry = entry.next) != null);
            this.unflushedEntry = null;
        }
    }

    private static int fillBufferArray(ByteBuffer[] byteBufferArray, ByteBuffer[] byteBufferArray2, int n) {
        for (ByteBuffer byteBuffer : byteBufferArray) {
            if (byteBuffer == null) break;
            byteBufferArray2[n++] = byteBuffer;
        }
        return n;
    }

    public int nioBufferCount() {
        return this.nioBufferCount;
    }

    void incrementPendingOutboundBytes(long l) {
        if (l == 0L) {
            return;
        }
        long l2 = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, l);
        if (l2 > (long)this.channel.config().getWriteBufferHighWaterMark() && WRITABLE_UPDATER.compareAndSet(this, 1, 0)) {
            this.channel.pipeline().fireChannelWritabilityChanged();
        }
    }

    public long nioBufferSize() {
        return this.nioBufferSize;
    }

    boolean isWritable() {
        return this.writable != 0;
    }

    @Deprecated
    public void recycle() {
    }

    private static void safeSuccess(ChannelPromise channelPromise) {
        if (!(channelPromise instanceof VoidChannelPromise) && !channelPromise.trySuccess()) {
            logger.warn("Failed to mark a promise as success because it is done already: {}", (Object)channelPromise);
        }
    }

    public Object current() {
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return null;
        }
        return entry.msg;
    }

    public void progress(long l) {
        Entry entry = this.flushedEntry;
        assert (entry != null);
        ChannelPromise channelPromise = entry.promise;
        if (channelPromise instanceof ChannelProgressivePromise) {
            long l2;
            entry.progress = l2 = entry.progress + l;
            ((ChannelProgressivePromise)channelPromise).tryProgress(l2, entry.total);
        }
    }

    void decrementPendingOutboundBytes(long l) {
        if (l == 0L) {
            return;
        }
        long l2 = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -l);
        if ((l2 == 0L || l2 < (long)this.channel.config().getWriteBufferLowWaterMark()) && WRITABLE_UPDATER.compareAndSet(this, 0, 1)) {
            this.channel.pipeline().fireChannelWritabilityChanged();
        }
    }

    public void forEachFlushedMessage(MessageProcessor messageProcessor) throws Exception {
        if (messageProcessor == null) {
            throw new NullPointerException("processor");
        }
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return;
        }
        do {
            if (entry.cancelled || messageProcessor.processMessage(entry.msg)) continue;
            return;
        } while (this.isFlushedEntry(entry = entry.next));
    }

    public boolean remove() {
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return false;
        }
        Object object = entry.msg;
        ChannelPromise channelPromise = entry.promise;
        int n = entry.pendingSize;
        this.removeEntry(entry);
        if (!entry.cancelled) {
            ReferenceCountUtil.safeRelease(object);
            ChannelOutboundBuffer.safeSuccess(channelPromise);
            this.decrementPendingOutboundBytes(n);
        }
        entry.recycle();
        return true;
    }

    public ByteBuffer[] nioBuffers() {
        long l = 0L;
        int n = 0;
        InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.get();
        ByteBuffer[] byteBufferArray = NIO_BUFFERS.get(internalThreadLocalMap);
        Entry entry = this.flushedEntry;
        while (this.isFlushedEntry(entry) && entry.msg instanceof ByteBuf) {
            if (!entry.cancelled) {
                ByteBuf byteBuf = (ByteBuf)entry.msg;
                int n2 = byteBuf.readerIndex();
                int n3 = byteBuf.writerIndex() - n2;
                if (n3 > 0) {
                    Object object;
                    int n4;
                    l += (long)n3;
                    int n5 = entry.count;
                    if (n5 == -1) {
                        entry.count = n5 = byteBuf.nioBufferCount();
                    }
                    if ((n4 = n + n5) > byteBufferArray.length) {
                        byteBufferArray = ChannelOutboundBuffer.expandNioBufferArray(byteBufferArray, n4, n);
                        NIO_BUFFERS.set(internalThreadLocalMap, byteBufferArray);
                    }
                    if (n5 == 1) {
                        object = entry.buf;
                        if (object == null) {
                            object = byteBuf.internalNioBuffer(n2, n3);
                            entry.buf = object;
                        }
                        byteBufferArray[n++] = object;
                    } else {
                        object = entry.bufs;
                        if (object == null) {
                            object = byteBuf.nioBuffers();
                            entry.bufs = object;
                        }
                        n = ChannelOutboundBuffer.fillBufferArray(object, byteBufferArray, n);
                    }
                }
            }
            entry = entry.next;
        }
        this.nioBufferCount = n;
        this.nioBufferSize = l;
        return byteBufferArray;
    }

    void failFlushed(Throwable throwable) {
        if (this.inFail) {
            return;
        }
        this.inFail = true;
        while (this.remove(throwable)) {
        }
        this.inFail = false;
    }

    public long totalPendingWriteBytes() {
        return this.totalPendingSize;
    }

    private static long total(Object object) {
        if (object instanceof ByteBuf) {
            return ((ByteBuf)object).readableBytes();
        }
        if (object instanceof FileRegion) {
            return ((FileRegion)object).count();
        }
        if (object instanceof ByteBufHolder) {
            return ((ByteBufHolder)object).content().readableBytes();
        }
        return -1L;
    }

    public boolean remove(Throwable throwable) {
        Entry entry = this.flushedEntry;
        if (entry == null) {
            return false;
        }
        Object object = entry.msg;
        ChannelPromise channelPromise = entry.promise;
        int n = entry.pendingSize;
        this.removeEntry(entry);
        if (!entry.cancelled) {
            ReferenceCountUtil.safeRelease(object);
            ChannelOutboundBuffer.safeFail(channelPromise, throwable);
            this.decrementPendingOutboundBytes(n);
        }
        entry.recycle();
        return true;
    }

    private static ByteBuffer[] expandNioBufferArray(ByteBuffer[] byteBufferArray, int n, int n2) {
        int n3 = byteBufferArray.length;
        do {
            if ((n3 <<= 1) >= 0) continue;
            throw new IllegalStateException();
        } while (n > n3);
        ByteBuffer[] byteBufferArray2 = new ByteBuffer[n3];
        System.arraycopy(byteBufferArray, 0, byteBufferArray2, 0, n2);
        return byteBufferArray2;
    }

    public void addMessage(Object object, int n, ChannelPromise channelPromise) {
        Entry entry = Entry.newInstance(object, n, ChannelOutboundBuffer.total(object), channelPromise);
        if (this.tailEntry == null) {
            this.flushedEntry = null;
            this.tailEntry = entry;
        } else {
            Entry entry2 = this.tailEntry;
            entry2.next = entry;
            this.tailEntry = entry;
        }
        if (this.unflushedEntry == null) {
            this.unflushedEntry = entry;
        }
        this.incrementPendingOutboundBytes(n);
    }

    public int size() {
        return this.flushed;
    }

    static {
        logger = InternalLoggerFactory.getInstance(ChannelOutboundBuffer.class);
        NIO_BUFFERS = new FastThreadLocal<ByteBuffer[]>(){

            @Override
            protected ByteBuffer[] initialValue() throws Exception {
                return new ByteBuffer[1024];
            }
        };
        AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(ChannelOutboundBuffer.class, "writable");
        if (atomicIntegerFieldUpdater == null) {
            atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "writable");
        }
        WRITABLE_UPDATER = atomicIntegerFieldUpdater;
        AtomicLongFieldUpdater<Object> atomicLongFieldUpdater = PlatformDependent.newAtomicLongFieldUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
        if (atomicLongFieldUpdater == null) {
            atomicLongFieldUpdater = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
        }
        TOTAL_PENDING_SIZE_UPDATER = atomicLongFieldUpdater;
    }

    public void removeBytes(long l) {
        block4: {
            int n;
            ByteBuf byteBuf;
            while (true) {
                Object object;
                if (!((object = this.current()) instanceof ByteBuf)) {
                    assert (l == 0L);
                    break block4;
                }
                byteBuf = (ByteBuf)object;
                n = byteBuf.readerIndex();
                int n2 = byteBuf.writerIndex() - n;
                if ((long)n2 > l) break;
                if (l != 0L) {
                    this.progress(n2);
                    l -= (long)n2;
                }
                this.remove();
            }
            if (l == 0L) break block4;
            byteBuf.readerIndex(n + (int)l);
            this.progress(l);
        }
    }

    public boolean isEmpty() {
        return this.flushed == 0;
    }

    static final class Entry {
        int pendingSize;
        Object msg;
        int count = -1;
        long total;
        ChannelPromise promise;
        long progress;
        ByteBuffer buf;
        private final Recycler.Handle handle;
        Entry next;
        ByteBuffer[] bufs;
        boolean cancelled;
        private static final Recycler<Entry> RECYCLER = new Recycler<Entry>(){

            @Override
            protected Entry newObject(Recycler.Handle handle) {
                return new Entry(handle);
            }
        };

        int cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                int n = this.pendingSize;
                ReferenceCountUtil.safeRelease(this.msg);
                this.msg = Unpooled.EMPTY_BUFFER;
                this.pendingSize = 0;
                this.total = 0L;
                this.progress = 0L;
                this.bufs = null;
                this.buf = null;
                return n;
            }
            return 0;
        }

        static Entry newInstance(Object object, int n, long l, ChannelPromise channelPromise) {
            Entry entry = RECYCLER.get();
            entry.msg = object;
            entry.pendingSize = n;
            entry.total = l;
            entry.promise = channelPromise;
            return entry;
        }

        private Entry(Recycler.Handle handle) {
            this.handle = handle;
        }

        Entry recycleAndGetNext() {
            Entry entry = this.next;
            this.recycle();
            return entry;
        }

        void recycle() {
            this.next = null;
            this.bufs = null;
            this.buf = null;
            this.msg = null;
            this.promise = null;
            this.progress = 0L;
            this.total = 0L;
            this.pendingSize = 0;
            this.count = -1;
            this.cancelled = false;
            RECYCLER.recycle(this, this.handle);
        }
    }

    public static interface MessageProcessor {
        public boolean processMessage(Object var1) throws Exception;
    }
}

