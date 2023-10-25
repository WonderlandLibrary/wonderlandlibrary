/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.local;

import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import java.util.concurrent.ThreadFactory;

final class LocalEventLoop
extends SingleThreadEventLoop {
    @Override
    protected void run() {
        do {
            Runnable runnable;
            if ((runnable = this.takeTask()) == null) continue;
            runnable.run();
            this.updateLastExecutionTime();
        } while (!this.confirmShutdown());
    }

    LocalEventLoop(LocalEventLoopGroup localEventLoopGroup, ThreadFactory threadFactory) {
        super(localEventLoopGroup, threadFactory, true);
    }
}

