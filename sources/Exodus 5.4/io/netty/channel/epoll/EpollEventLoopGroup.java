/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.epoll.EpollEventLoop;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ThreadFactory;

public final class EpollEventLoopGroup
extends MultithreadEventLoopGroup {
    @Override
    protected EventExecutor newChild(ThreadFactory threadFactory, Object ... objectArray) throws Exception {
        return new EpollEventLoop((EventLoopGroup)this, threadFactory, (Integer)objectArray[0]);
    }

    public EpollEventLoopGroup(int n) {
        this(n, null);
    }

    public EpollEventLoopGroup() {
        this(0);
    }

    public EpollEventLoopGroup(int n, ThreadFactory threadFactory, int n2) {
        super(n, threadFactory, n2);
    }

    public EpollEventLoopGroup(int n, ThreadFactory threadFactory) {
        this(n, threadFactory, 128);
    }

    public void setIoRatio(int n) {
        for (EventExecutor eventExecutor : this.children()) {
            ((EpollEventLoop)eventExecutor).setIoRatio(n);
        }
    }
}

