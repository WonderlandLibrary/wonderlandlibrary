/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.ReferenceCounted;
import io.netty.util.ThreadDeathWatcher;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class ReferenceCountUtil {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);

    public static <T> T releaseLater(T t) {
        return ReferenceCountUtil.releaseLater(t, 1);
    }

    public static boolean release(Object object) {
        if (object instanceof ReferenceCounted) {
            return ((ReferenceCounted)object).release();
        }
        return false;
    }

    public static <T> T releaseLater(T t, int n) {
        if (t instanceof ReferenceCounted) {
            ThreadDeathWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted)t, n));
        }
        return t;
    }

    public static <T> T retain(T t) {
        if (t instanceof ReferenceCounted) {
            return (T)((ReferenceCounted)t).retain();
        }
        return t;
    }

    public static <T> T retain(T t, int n) {
        if (t instanceof ReferenceCounted) {
            return (T)((ReferenceCounted)t).retain(n);
        }
        return t;
    }

    public static void safeRelease(Object object, int n) {
        block2: {
            try {
                ReferenceCountUtil.release(object, n);
            }
            catch (Throwable throwable) {
                if (!logger.isWarnEnabled()) break block2;
                logger.warn("Failed to release a message: {} (decrement: {})", object, n, throwable);
            }
        }
    }

    public static boolean release(Object object, int n) {
        if (object instanceof ReferenceCounted) {
            return ((ReferenceCounted)object).release(n);
        }
        return false;
    }

    public static void safeRelease(Object object) {
        try {
            ReferenceCountUtil.release(object);
        }
        catch (Throwable throwable) {
            logger.warn("Failed to release a message: {}", object, (Object)throwable);
        }
    }

    private ReferenceCountUtil() {
    }

    private static final class ReleasingTask
    implements Runnable {
        private final int decrement;
        private final ReferenceCounted obj;

        public String toString() {
            return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
        }

        ReleasingTask(ReferenceCounted referenceCounted, int n) {
            this.obj = referenceCounted;
            this.decrement = n;
        }

        @Override
        public void run() {
            try {
                if (!this.obj.release(this.decrement)) {
                    logger.warn("Non-zero refCnt: {}", (Object)this);
                } else {
                    logger.debug("Released: {}", (Object)this);
                }
            }
            catch (Exception exception) {
                logger.warn("Failed to release an object: {}", (Object)this.obj, (Object)exception);
            }
        }
    }
}

