/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.local;

import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.local.LocalEventLoop;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.ThreadFactory;

public class LocalEventLoopGroup
extends MultithreadEventLoopGroup {
    public LocalEventLoopGroup(int n) {
        this(n, null);
    }

    public LocalEventLoopGroup(int n, ThreadFactory threadFactory) {
        super(n, threadFactory, new Object[0]);
    }

    @Override
    protected EventExecutor newChild(ThreadFactory threadFactory, Object ... objectArray) throws Exception {
        return new LocalEventLoop(this, threadFactory);
    }

    public LocalEventLoopGroup() {
        this(0);
    }
}

