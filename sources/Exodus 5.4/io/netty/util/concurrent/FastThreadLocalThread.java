/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;

public class FastThreadLocalThread
extends Thread {
    private InternalThreadLocalMap threadLocalMap;

    public FastThreadLocalThread(ThreadGroup threadGroup, Runnable runnable, String string) {
        super(threadGroup, runnable, string);
    }

    public FastThreadLocalThread() {
    }

    public FastThreadLocalThread(ThreadGroup threadGroup, String string) {
        super(threadGroup, string);
    }

    public FastThreadLocalThread(ThreadGroup threadGroup, Runnable runnable) {
        super(threadGroup, runnable);
    }

    public final void setThreadLocalMap(InternalThreadLocalMap internalThreadLocalMap) {
        this.threadLocalMap = internalThreadLocalMap;
    }

    public FastThreadLocalThread(Runnable runnable) {
        super(runnable);
    }

    public FastThreadLocalThread(String string) {
        super(string);
    }

    public final InternalThreadLocalMap threadLocalMap() {
        return this.threadLocalMap;
    }

    public FastThreadLocalThread(ThreadGroup threadGroup, Runnable runnable, String string, long l) {
        super(threadGroup, runnable, string, l);
    }

    public FastThreadLocalThread(Runnable runnable, String string) {
        super(runnable, string);
    }
}

