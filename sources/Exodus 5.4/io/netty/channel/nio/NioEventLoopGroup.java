/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.nio;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.EventExecutor;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

public class NioEventLoopGroup
extends MultithreadEventLoopGroup {
    public NioEventLoopGroup(int n) {
        this(n, null);
    }

    public NioEventLoopGroup(int n, ThreadFactory threadFactory) {
        this(n, threadFactory, SelectorProvider.provider());
    }

    public NioEventLoopGroup() {
        this(0);
    }

    public void setIoRatio(int n) {
        for (EventExecutor eventExecutor : this.children()) {
            ((NioEventLoop)eventExecutor).setIoRatio(n);
        }
    }

    public void rebuildSelectors() {
        for (EventExecutor eventExecutor : this.children()) {
            ((NioEventLoop)eventExecutor).rebuildSelector();
        }
    }

    public NioEventLoopGroup(int n, ThreadFactory threadFactory, SelectorProvider selectorProvider) {
        super(n, threadFactory, selectorProvider);
    }

    @Override
    protected EventExecutor newChild(ThreadFactory threadFactory, Object ... objectArray) throws Exception {
        return new NioEventLoop(this, threadFactory, (SelectorProvider)objectArray[0]);
    }
}

