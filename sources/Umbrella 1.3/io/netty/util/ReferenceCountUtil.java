/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util;

import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class ReferenceCountUtil {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);
    private static final Map<Thread, List<Entry>> pendingReleases = new IdentityHashMap<Thread, List<Entry>>();

    public static <T> T retain(T msg) {
        if (msg instanceof ReferenceCounted) {
            return (T)((ReferenceCounted)msg).retain();
        }
        return msg;
    }

    public static <T> T retain(T msg, int increment) {
        if (msg instanceof ReferenceCounted) {
            return (T)((ReferenceCounted)msg).retain(increment);
        }
        return msg;
    }

    public static boolean release(Object msg) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted)msg).release();
        }
        return false;
    }

    public static boolean release(Object msg, int decrement) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted)msg).release(decrement);
        }
        return false;
    }

    public static <T> T releaseLater(T msg) {
        return ReferenceCountUtil.releaseLater(msg, 1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <T> T releaseLater(T msg, int decrement) {
        if (msg instanceof ReferenceCounted) {
            Map<Thread, List<Entry>> map = pendingReleases;
            synchronized (map) {
                Thread thread = Thread.currentThread();
                List<Entry> entries = pendingReleases.get(thread);
                if (entries == null) {
                    if (pendingReleases.isEmpty()) {
                        ReleasingTask task = new ReleasingTask();
                        task.future = GlobalEventExecutor.INSTANCE.scheduleWithFixedDelay(task, 1L, 1L, TimeUnit.SECONDS);
                    }
                    entries = new ArrayList<Entry>();
                    pendingReleases.put(thread, entries);
                }
                entries.add(new Entry((ReferenceCounted)msg, decrement));
            }
        }
        return msg;
    }

    private ReferenceCountUtil() {
    }

    private static final class ReleasingTask
    implements Runnable {
        volatile ScheduledFuture<?> future;

        private ReleasingTask() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            Map map = pendingReleases;
            synchronized (map) {
                Iterator i = pendingReleases.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry e = i.next();
                    if (((Thread)e.getKey()).isAlive()) continue;
                    ReleasingTask.releaseAll((Iterable)e.getValue());
                    i.remove();
                }
                if (pendingReleases.isEmpty()) {
                    this.future.cancel(false);
                }
            }
        }

        private static void releaseAll(Iterable<Entry> entries) {
            for (Entry e : entries) {
                try {
                    if (!e.obj.release(e.decrement)) {
                        logger.warn("Non-zero refCnt: {}", (Object)e);
                        continue;
                    }
                    logger.warn("Released: {}", (Object)e);
                }
                catch (Exception ex) {
                    logger.warn("Failed to release an object: {}", (Object)e.obj, (Object)ex);
                }
            }
        }
    }

    private static final class Entry {
        final ReferenceCounted obj;
        final int decrement;

        Entry(ReferenceCounted obj, int decrement) {
            this.obj = obj;
            this.decrement = decrement;
        }

        public String toString() {
            return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
        }
    }
}

