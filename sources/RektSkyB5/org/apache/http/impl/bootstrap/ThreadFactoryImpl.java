/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.bootstrap;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

class ThreadFactoryImpl
implements ThreadFactory {
    private final String namePrefix;
    private final ThreadGroup group;
    private final AtomicLong count;

    ThreadFactoryImpl(String namePrefix, ThreadGroup group) {
        this.namePrefix = namePrefix;
        this.group = group;
        this.count = new AtomicLong();
    }

    ThreadFactoryImpl(String namePrefix) {
        this(namePrefix, null);
    }

    @Override
    public Thread newThread(Runnable target) {
        return new Thread(this.group, target, this.namePrefix + "-" + this.count.incrementAndGet());
    }
}

