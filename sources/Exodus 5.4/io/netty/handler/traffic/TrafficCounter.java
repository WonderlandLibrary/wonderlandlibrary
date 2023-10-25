/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.traffic;

import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TrafficCounter {
    private long lastNonNullWrittenBytes;
    private final AtomicLong cumulativeWrittenBytes;
    private long lastWrittenBytes;
    private final ScheduledExecutorService executor;
    private final AtomicLong currentReadBytes;
    private final AtomicLong lastTime;
    private long lastReadThroughput;
    private final AtomicLong cumulativeReadBytes;
    private volatile ScheduledFuture<?> scheduledFuture;
    private long lastWriteThroughput;
    private long lastNonNullReadTime;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(TrafficCounter.class);
    final AtomicLong checkInterval;
    private final AbstractTrafficShapingHandler trafficShapingHandler;
    final String name;
    private long lastCumulativeTime;
    private Runnable monitor;
    final AtomicBoolean monitorActive;
    private final AtomicLong currentWrittenBytes = new AtomicLong();
    private long lastReadBytes;
    private long lastNonNullWrittenTime;
    private long lastNonNullReadBytes;

    public void configure(long l) {
        long l2 = l / 10L * 10L;
        if (this.checkInterval.get() != l2) {
            this.checkInterval.set(l2);
            if (l2 <= 0L) {
                this.stop();
                this.lastTime.set(System.currentTimeMillis());
            } else {
                this.start();
            }
        }
    }

    public synchronized long writeTimeToWait(long l, long l2, long l3) {
        this.bytesWriteFlowControl(l);
        if (l2 == 0L) {
            return 0L;
        }
        long l4 = this.currentWrittenBytes.get();
        long l5 = System.currentTimeMillis();
        long l6 = l5 - this.lastTime.get();
        if (l6 > 10L && l4 > 0L) {
            long l7 = (l4 * 1000L / l2 - l6) / 10L * 10L;
            if (l7 > 10L) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + l7 + ":" + l4 + ":" + l6);
                }
                return l7 > l3 ? l3 : l7;
            }
            return 0L;
        }
        if (this.lastNonNullWrittenBytes > 0L && this.lastNonNullWrittenTime + 10L < l5) {
            long l8 = l4 + this.lastNonNullWrittenBytes;
            long l9 = l5 - this.lastNonNullWrittenTime;
            long l10 = (l8 * 1000L / l2 - l9) / 10L * 10L;
            if (l10 > 10L) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + l10 + ":" + l8 + ":" + l9);
                }
                return l10 > l3 ? l3 : l10;
            }
        } else {
            long l11 = 10L + Math.abs(l6);
            long l12 = ((l4 += this.lastWrittenBytes) * 1000L / l2 - l11) / 10L * 10L;
            if (l12 > 10L) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + l12 + ":" + l4 + ":" + l11);
                }
                return l12 > l3 ? l3 : l12;
            }
        }
        return 0L;
    }

    public TrafficCounter(AbstractTrafficShapingHandler abstractTrafficShapingHandler, ScheduledExecutorService scheduledExecutorService, String string, long l) {
        this.currentReadBytes = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.lastTime = new AtomicLong();
        this.checkInterval = new AtomicLong(1000L);
        this.monitorActive = new AtomicBoolean();
        this.trafficShapingHandler = abstractTrafficShapingHandler;
        this.executor = scheduledExecutorService;
        this.name = string;
        this.lastCumulativeTime = System.currentTimeMillis();
        this.configure(l);
    }

    public long lastCumulativeTime() {
        return this.lastCumulativeTime;
    }

    public synchronized void start() {
        if (this.monitorActive.get()) {
            return;
        }
        this.lastTime.set(System.currentTimeMillis());
        if (this.checkInterval.get() > 0L) {
            this.monitorActive.set(true);
            this.monitor = new TrafficMonitoringTask(this.trafficShapingHandler, this);
            this.scheduledFuture = this.executor.schedule(this.monitor, this.checkInterval.get(), TimeUnit.MILLISECONDS);
        }
    }

    void bytesWriteFlowControl(long l) {
        this.currentWrittenBytes.addAndGet(l);
        this.cumulativeWrittenBytes.addAndGet(l);
    }

    public long lastReadBytes() {
        return this.lastReadBytes;
    }

    public synchronized long readTimeToWait(long l, long l2, long l3) {
        long l4 = System.currentTimeMillis();
        this.bytesRecvFlowControl(l);
        if (l2 == 0L) {
            return 0L;
        }
        long l5 = this.currentReadBytes.get();
        long l6 = l4 - this.lastTime.get();
        if (l6 > 10L && l5 > 0L) {
            long l7 = (l5 * 1000L / l2 - l6) / 10L * 10L;
            if (l7 > 10L) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + l7 + ":" + l5 + ":" + l6);
                }
                return l7 > l3 ? l3 : l7;
            }
            return 0L;
        }
        if (this.lastNonNullReadBytes > 0L && this.lastNonNullReadTime + 10L < l4) {
            long l8 = l5 + this.lastNonNullReadBytes;
            long l9 = l4 - this.lastNonNullReadTime;
            long l10 = (l8 * 1000L / l2 - l9) / 10L * 10L;
            if (l10 > 10L) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + l10 + ":" + l8 + ":" + l9);
                }
                return l10 > l3 ? l3 : l10;
            }
        } else {
            long l11 = 10L;
            long l12 = ((l5 += this.lastReadBytes) * 1000L / l2 - l11) / 10L * 10L;
            if (l12 > 10L) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Time: " + l12 + ":" + l5 + ":" + l11);
                }
                return l12 > l3 ? l3 : l12;
            }
        }
        return 0L;
    }

    public long cumulativeWrittenBytes() {
        return this.cumulativeWrittenBytes.get();
    }

    public long lastWrittenBytes() {
        return this.lastWrittenBytes;
    }

    public synchronized void stop() {
        if (!this.monitorActive.get()) {
            return;
        }
        this.monitorActive.set(false);
        this.resetAccounting(System.currentTimeMillis());
        if (this.trafficShapingHandler != null) {
            this.trafficShapingHandler.doAccounting(this);
        }
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
    }

    public long lastWriteThroughput() {
        return this.lastWriteThroughput;
    }

    public String toString() {
        return "Monitor " + this.name + " Current Speed Read: " + (this.lastReadThroughput >> 10) + " KB/s, Write: " + (this.lastWriteThroughput >> 10) + " KB/s Current Read: " + (this.currentReadBytes.get() >> 10) + " KB Current Write: " + (this.currentWrittenBytes.get() >> 10) + " KB";
    }

    public long lastReadThroughput() {
        return this.lastReadThroughput;
    }

    public long currentWrittenBytes() {
        return this.currentWrittenBytes.get();
    }

    public long currentReadBytes() {
        return this.currentReadBytes.get();
    }

    public long checkInterval() {
        return this.checkInterval.get();
    }

    public String name() {
        return this.name;
    }

    void bytesRecvFlowControl(long l) {
        this.currentReadBytes.addAndGet(l);
        this.cumulativeReadBytes.addAndGet(l);
    }

    public long lastTime() {
        return this.lastTime.get();
    }

    synchronized void resetAccounting(long l) {
        long l2 = l - this.lastTime.getAndSet(l);
        if (l2 == 0L) {
            return;
        }
        if (logger.isDebugEnabled() && l2 > 2L * this.checkInterval()) {
            logger.debug("Acct schedule not ok: " + l2 + " > 2*" + this.checkInterval() + " from " + this.name);
        }
        this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
        this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
        this.lastReadThroughput = this.lastReadBytes / l2 * 1000L;
        this.lastWriteThroughput = this.lastWrittenBytes / l2 * 1000L;
        if (this.lastWrittenBytes > 0L) {
            this.lastNonNullWrittenBytes = this.lastWrittenBytes;
            this.lastNonNullWrittenTime = l;
        }
        if (this.lastReadBytes > 0L) {
            this.lastNonNullReadBytes = this.lastReadBytes;
            this.lastNonNullReadTime = l;
        }
    }

    public long cumulativeReadBytes() {
        return this.cumulativeReadBytes.get();
    }

    public void resetCumulativeTime() {
        this.lastCumulativeTime = System.currentTimeMillis();
        this.cumulativeReadBytes.set(0L);
        this.cumulativeWrittenBytes.set(0L);
    }

    private static class TrafficMonitoringTask
    implements Runnable {
        private final AbstractTrafficShapingHandler trafficShapingHandler1;
        private final TrafficCounter counter;

        protected TrafficMonitoringTask(AbstractTrafficShapingHandler abstractTrafficShapingHandler, TrafficCounter trafficCounter) {
            this.trafficShapingHandler1 = abstractTrafficShapingHandler;
            this.counter = trafficCounter;
        }

        @Override
        public void run() {
            if (!this.counter.monitorActive.get()) {
                return;
            }
            long l = System.currentTimeMillis();
            this.counter.resetAccounting(l);
            if (this.trafficShapingHandler1 != null) {
                this.trafficShapingHandler1.doAccounting(this.counter);
            }
            this.counter.scheduledFuture = this.counter.executor.schedule(this, this.counter.checkInterval.get(), TimeUnit.MILLISECONDS);
        }
    }
}

