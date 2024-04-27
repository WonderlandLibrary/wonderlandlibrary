/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.util.enhancement;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public interface Enhancement {
    public static final AtomicInteger counter = new AtomicInteger(0);
    public static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), r -> new Thread(r, String.format("Patcher Concurrency Thread %s", counter.incrementAndGet())));

    public String getName();

    default public void tick() {
    }
}

