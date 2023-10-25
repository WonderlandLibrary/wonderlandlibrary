/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.Native;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class EpollEventLoop
extends SingleThreadEventLoop {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(EpollEventLoop.class);
    private boolean overflown;
    private volatile int wakenUp;
    private final IntObjectMap<AbstractEpollChannel> ids;
    private final long[] events;
    private int id;
    private static final AtomicIntegerFieldUpdater<EpollEventLoop> WAKEN_UP_UPDATER;
    private final int epollFd;
    private volatile int ioRatio;
    private final int eventFd;

    @Override
    protected Queue<Runnable> newTaskQueue() {
        return PlatformDependent.newMpscQueue();
    }

    void modify(AbstractEpollChannel abstractEpollChannel) {
        assert (this.inEventLoop());
        Native.epollCtlMod(this.epollFd, abstractEpollChannel.fd, abstractEpollChannel.flags, abstractEpollChannel.id);
    }

    @Override
    protected void run() {
        while (true) {
            boolean bl = WAKEN_UP_UPDATER.getAndSet(this, 0) == 1;
            try {
                int n;
                if (this.hasTasks()) {
                    n = Native.epollWait(this.epollFd, this.events, 0);
                } else {
                    n = this.epollWait(bl);
                    if (this.wakenUp == 1) {
                        Native.eventFdWrite(this.eventFd, 1L);
                    }
                }
                int n2 = this.ioRatio;
                if (n2 == 100) {
                    if (n > 0) {
                        this.processReady(this.events, n);
                    }
                    this.runAllTasks();
                } else {
                    long l = System.nanoTime();
                    if (n > 0) {
                        this.processReady(this.events, n);
                    }
                    long l2 = System.nanoTime() - l;
                    this.runAllTasks(l2 * (long)(100 - n2) / (long)n2);
                }
                if (!this.isShuttingDown()) continue;
                this.closeAll();
                if (!this.confirmShutdown()) continue;
            }
            catch (Throwable throwable) {
                logger.warn("Unexpected exception in the selector loop.", throwable);
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException interruptedException) {}
                continue;
            }
            break;
        }
    }

    private void closeAll() {
        Native.epollWait(this.epollFd, this.events, 0);
        ArrayList<AbstractEpollChannel> arrayList = new ArrayList<AbstractEpollChannel>(this.ids.size());
        for (IntObjectMap.Entry<AbstractEpollChannel> object : this.ids.entries()) {
            arrayList.add(object.value());
        }
        for (AbstractEpollChannel abstractEpollChannel : arrayList) {
            abstractEpollChannel.unsafe().close(abstractEpollChannel.unsafe().voidPromise());
        }
    }

    private int epollWait(boolean bl) {
        int n = 0;
        long l = System.nanoTime();
        long l2 = l + this.delayNanos(l);
        while (true) {
            int n2;
            long l3;
            if ((l3 = (l2 - l + 500000L) / 1000000L) <= 0L) {
                if (n != 0 || (n2 = Native.epollWait(this.epollFd, this.events, 0)) <= 0) break;
                return n2;
            }
            n2 = Native.epollWait(this.epollFd, this.events, (int)l3);
            ++n;
            if (n2 != 0 || bl || this.wakenUp == 1 || this.hasTasks() || this.hasScheduledTasks()) {
                return n2;
            }
            l = System.nanoTime();
        }
        return 0;
    }

    void remove(AbstractEpollChannel abstractEpollChannel) {
        assert (this.inEventLoop());
        if (this.ids.remove(abstractEpollChannel.id) != null && abstractEpollChannel.isOpen()) {
            Native.epollCtlDel(this.epollFd, abstractEpollChannel.fd);
        }
    }

    private void processReady(long[] lArray, int n) {
        for (int i = 0; i < n; ++i) {
            long l = lArray[i];
            int n2 = (int)(l >> 32);
            if (n2 == 0) {
                Native.eventFdRead(this.eventFd);
                continue;
            }
            boolean bl = (l & 1L) != 0L;
            boolean bl2 = (l & 2L) != 0L;
            boolean bl3 = (l & 8L) != 0L;
            AbstractEpollChannel abstractEpollChannel = this.ids.get(n2);
            if (abstractEpollChannel == null) continue;
            AbstractEpollChannel.AbstractEpollUnsafe abstractEpollUnsafe = (AbstractEpollChannel.AbstractEpollUnsafe)abstractEpollChannel.unsafe();
            if (bl2 && abstractEpollChannel.isOpen()) {
                abstractEpollUnsafe.epollOutReady();
            }
            if (bl && abstractEpollChannel.isOpen()) {
                abstractEpollUnsafe.epollInReady();
            }
            if (!bl3 || !abstractEpollChannel.isOpen()) continue;
            abstractEpollUnsafe.epollRdHupReady();
        }
    }

    static {
        AtomicIntegerFieldUpdater<Object> atomicIntegerFieldUpdater = PlatformDependent.newAtomicIntegerFieldUpdater(EpollEventLoop.class, "wakenUp");
        if (atomicIntegerFieldUpdater == null) {
            atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(EpollEventLoop.class, "wakenUp");
        }
        WAKEN_UP_UPDATER = atomicIntegerFieldUpdater;
    }

    @Override
    protected void wakeup(boolean bl) {
        if (!bl && WAKEN_UP_UPDATER.compareAndSet(this, 0, 1)) {
            Native.eventFdWrite(this.eventFd, 1L);
        }
    }

    public void setIoRatio(int n) {
        if (n <= 0 || n > 100) {
            throw new IllegalArgumentException("ioRatio: " + n + " (expected: 0 < ioRatio <= 100)");
        }
        this.ioRatio = n;
    }

    public int getIoRatio() {
        return this.ioRatio;
    }

    private int nextId() {
        int n = this.id;
        if (n == Integer.MAX_VALUE) {
            this.overflown = true;
            n = 0;
        }
        if (this.overflown) {
            while (this.ids.containsKey(++n)) {
            }
            this.id = n;
        } else {
            this.id = ++n;
        }
        return n;
    }

    @Override
    protected void cleanup() {
        try {
            Native.close(this.epollFd);
        }
        catch (IOException iOException) {
            logger.warn("Failed to close the epoll fd.", iOException);
        }
        try {
            Native.close(this.eventFd);
        }
        catch (IOException iOException) {
            logger.warn("Failed to close the event fd.", iOException);
        }
    }

    void add(AbstractEpollChannel abstractEpollChannel) {
        assert (this.inEventLoop());
        int n = this.nextId();
        Native.epollCtlAdd(this.epollFd, abstractEpollChannel.fd, abstractEpollChannel.flags, n);
        abstractEpollChannel.id = n;
        this.ids.put(n, abstractEpollChannel);
    }

    EpollEventLoop(EventLoopGroup eventLoopGroup, ThreadFactory threadFactory, int n) {
        block8: {
            super(eventLoopGroup, threadFactory, false);
            this.ids = new IntObjectHashMap<AbstractEpollChannel>();
            this.ioRatio = 50;
            this.events = new long[n];
            boolean bl = false;
            int n2 = -1;
            int n3 = -1;
            this.epollFd = n2 = Native.epollCreate();
            this.eventFd = n3 = Native.eventFd();
            Native.epollCtlAdd(n2, n3, 1, 0);
            bl = true;
            if (bl) break block8;
            if (n2 != -1) {
                try {
                    Native.close(n2);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (n3 != -1) {
                try {
                    Native.close(n3);
                }
                catch (Exception exception) {}
            }
        }
    }
}

