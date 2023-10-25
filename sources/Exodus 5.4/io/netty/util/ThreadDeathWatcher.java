/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ThreadDeathWatcher {
    private static final ThreadFactory threadFactory;
    private static final Queue<Entry> pendingEntries;
    private static final Watcher watcher;
    private static volatile Thread watcherThread;
    private static final AtomicBoolean started;
    private static final InternalLogger logger;

    public static boolean awaitInactivity(long l, TimeUnit timeUnit) throws InterruptedException {
        if (timeUnit == null) {
            throw new NullPointerException("unit");
        }
        Thread thread = watcherThread;
        if (thread != null) {
            thread.join(timeUnit.toMillis(l));
            return !thread.isAlive();
        }
        return true;
    }

    private ThreadDeathWatcher() {
    }

    static {
        logger = InternalLoggerFactory.getInstance(ThreadDeathWatcher.class);
        threadFactory = new DefaultThreadFactory(ThreadDeathWatcher.class, true, 1);
        pendingEntries = PlatformDependent.newMpscQueue();
        watcher = new Watcher();
        started = new AtomicBoolean();
    }

    public static void unwatch(Thread thread, Runnable runnable) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        ThreadDeathWatcher.schedule(thread, runnable, false);
    }

    public static void watch(Thread thread, Runnable runnable) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (runnable == null) {
            throw new NullPointerException("task");
        }
        if (!thread.isAlive()) {
            throw new IllegalArgumentException("thread must be alive.");
        }
        ThreadDeathWatcher.schedule(thread, runnable, true);
    }

    private static void schedule(Thread thread, Runnable runnable, boolean bl) {
        pendingEntries.add(new Entry(thread, runnable, bl));
        if (started.compareAndSet(false, true)) {
            Thread thread2 = threadFactory.newThread(watcher);
            thread2.start();
            watcherThread = thread2;
        }
    }

    private static final class Entry
    extends MpscLinkedQueueNode<Entry> {
        final Runnable task;
        final boolean isWatch;
        final Thread thread;

        @Override
        public Entry value() {
            return this;
        }

        public int hashCode() {
            return this.thread.hashCode() ^ this.task.hashCode();
        }

        Entry(Thread thread, Runnable runnable, boolean bl) {
            this.thread = thread;
            this.task = runnable;
            this.isWatch = bl;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry)object;
            return this.thread == entry.thread && this.task == entry.task;
        }
    }

    private static final class Watcher
    implements Runnable {
        private final List<Entry> watchees = new ArrayList<Entry>();

        private void fetchWatchees() {
            Entry entry;
            while ((entry = (Entry)pendingEntries.poll()) != null) {
                if (entry.isWatch) {
                    this.watchees.add(entry);
                    continue;
                }
                this.watchees.remove(entry);
            }
        }

        private void notifyWatchees() {
            List<Entry> list = this.watchees;
            int n = 0;
            while (n < list.size()) {
                Entry entry = list.get(n);
                if (!entry.thread.isAlive()) {
                    list.remove(n);
                    try {
                        entry.task.run();
                    }
                    catch (Throwable throwable) {
                        logger.warn("Thread death watcher task raised an exception:", throwable);
                    }
                    continue;
                }
                ++n;
            }
        }

        private Watcher() {
        }

        @Override
        public void run() {
            while (true) {
                this.fetchWatchees();
                this.notifyWatchees();
                this.fetchWatchees();
                this.notifyWatchees();
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                if (!this.watchees.isEmpty() || !pendingEntries.isEmpty()) continue;
                boolean bl = started.compareAndSet(true, false);
                assert (bl);
                if (pendingEntries.isEmpty() || !started.compareAndSet(false, true)) break;
            }
        }
    }
}

