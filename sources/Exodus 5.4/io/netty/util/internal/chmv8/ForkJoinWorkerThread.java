/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.chmv8;

import io.netty.util.internal.chmv8.ForkJoinPool;

public class ForkJoinWorkerThread
extends Thread {
    final ForkJoinPool pool;
    final ForkJoinPool.WorkQueue workQueue;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        Throwable throwable = null;
        try {
            this.onStart();
            this.pool.runWorker(this.workQueue);
        }
        catch (Throwable throwable3) {
            throwable = throwable3;
            try {
                this.onTermination(throwable);
                this.pool.deregisterWorker(this, throwable);
                return;
            }
            catch (Throwable throwable4) {
                if (throwable == null) {
                    throwable = throwable4;
                }
                this.pool.deregisterWorker(this, throwable);
            }
            return;
        }
        try {
            this.onTermination(throwable);
            this.pool.deregisterWorker(this, throwable);
            return;
        }
        catch (Throwable throwable2) {
            if (throwable == null) {
                throwable = throwable2;
            }
            this.pool.deregisterWorker(this, throwable);
        }
    }

    protected void onStart() {
    }

    protected ForkJoinWorkerThread(ForkJoinPool forkJoinPool) {
        super("aForkJoinWorkerThread");
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    public ForkJoinPool getPool() {
        return this.pool;
    }

    public int getPoolIndex() {
        return this.workQueue.poolIndex >>> 1;
    }

    protected void onTermination(Throwable throwable) {
    }
}

