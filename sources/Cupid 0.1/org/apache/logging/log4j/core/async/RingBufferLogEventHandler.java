package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;

public class RingBufferLogEventHandler implements SequenceReportingEventHandler<RingBufferLogEvent>, LifecycleAware {
  private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
  
  private Sequence sequenceCallback;
  
  private int counter;
  
  private long threadId = -1L;
  
  public void setSequenceCallback(Sequence sequenceCallback) {
    this.sequenceCallback = sequenceCallback;
  }
  
  public void onEvent(RingBufferLogEvent event, long sequence, boolean endOfBatch) throws Exception {
    try {
      if (event.isPopulated())
        event.execute(endOfBatch); 
    } finally {
      event.clear();
      notifyCallback(sequence);
    } 
  }
  
  private void notifyCallback(long sequence) {
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
  
  public void onShutdown() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */