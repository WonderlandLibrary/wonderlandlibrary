/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.bootstrap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.impl.bootstrap.Worker;

class WorkerPoolExecutor
extends ThreadPoolExecutor {
    private final Map<Worker, Boolean> workerSet = new ConcurrentHashMap<Worker, Boolean>();

    public WorkerPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @Override
    protected void beforeExecute(Thread t2, Runnable r2) {
        if (r2 instanceof Worker) {
            this.workerSet.put((Worker)r2, Boolean.TRUE);
        }
    }

    @Override
    protected void afterExecute(Runnable r2, Throwable t2) {
        if (r2 instanceof Worker) {
            this.workerSet.remove(r2);
        }
    }

    public Set<Worker> getWorkers() {
        return new HashSet<Worker>(this.workerSet.keySet());
    }
}

