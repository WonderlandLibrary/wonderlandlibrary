/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.util.concurrent.ThreadFactory;

final class DefaultEventExecutor
extends SingleThreadEventExecutor {
    DefaultEventExecutor(DefaultEventExecutorGroup defaultEventExecutorGroup, ThreadFactory threadFactory) {
        super(defaultEventExecutorGroup, threadFactory, true);
    }

    @Override
    protected void run() {
        do {
            Runnable runnable;
            if ((runnable = this.takeTask()) == null) continue;
            runnable.run();
            this.updateLastExecutionTime();
        } while (!this.confirmShutdown());
    }
}

