// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.SequenceReportingEventHandler;

public class RingBufferLogEventHandler implements SequenceReportingEventHandler<RingBufferLogEvent>, LifecycleAware
{
    private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
    private Sequence sequenceCallback;
    private int counter;
    private long threadId;
    
    public RingBufferLogEventHandler() {
        this.threadId = -1L;
    }
    
    public void setSequenceCallback(final Sequence sequenceCallback) {
        this.sequenceCallback = sequenceCallback;
    }
    
    public void onEvent(final RingBufferLogEvent event, final long sequence, final boolean endOfBatch) throws Exception {
        try {
            if (event.isPopulated()) {
                event.execute(endOfBatch);
            }
        }
        finally {
            event.clear();
            this.notifyCallback(sequence);
        }
    }
    
    private void notifyCallback(final long sequence) {
        if (++this.counter > 50) {
            this.sequenceCallback.set(sequence);
            this.counter = 0;
        }
    }
    
    public long getThreadId() {
        return this.threadId;
    }
    
    public void onStart() {
        this.threadId = Thread.currentThread().getId();
    }
    
    public void onShutdown() {
    }
}
