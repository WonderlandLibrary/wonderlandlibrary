// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.Sequence;
import java.util.concurrent.TimeUnit;
import com.lmax.disruptor.WaitStrategy;

class TimeoutBlockingWaitStrategy implements WaitStrategy
{
    private final Object mutex;
    private final long timeoutInNanos;
    private static final int ONE_MILLISECOND_IN_NANOSECONDS = 1000000;
    
    public TimeoutBlockingWaitStrategy(final long timeout, final TimeUnit units) {
        this.mutex = new Object();
        this.timeoutInNanos = units.toNanos(timeout);
    }
    
    public long waitFor(final long sequence, final Sequence cursorSequence, final Sequence dependentSequence, final SequenceBarrier barrier) throws AlertException, InterruptedException, TimeoutException {
        long timeoutNanos = this.timeoutInNanos;
        if (cursorSequence.get() < sequence) {
            synchronized (this.mutex) {
                while (cursorSequence.get() < sequence) {
                    barrier.checkAlert();
                    timeoutNanos = awaitNanos(this.mutex, timeoutNanos);
                    if (timeoutNanos <= 0L) {
                        throw TimeoutException.INSTANCE;
                    }
                }
            }
        }
        long availableSequence;
        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();
        }
        return availableSequence;
    }
    
    public void signalAllWhenBlocking() {
        synchronized (this.mutex) {
            this.mutex.notifyAll();
        }
    }
    
    @Override
    public String toString() {
        return "TimeoutBlockingWaitStrategy{mutex=" + this.mutex + ", timeoutInNanos=" + this.timeoutInNanos + '}';
    }
    
    private static long awaitNanos(final Object mutex, final long timeoutNanos) throws InterruptedException {
        final long millis = timeoutNanos / 1000000L;
        final long nanos = timeoutNanos % 1000000L;
        final long t0 = System.nanoTime();
        mutex.wait(millis, (int)nanos);
        final long t2 = System.nanoTime();
        return timeoutNanos - (t2 - t0);
    }
}
