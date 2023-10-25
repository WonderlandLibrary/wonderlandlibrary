package org.apache.logging.log4j.core.async;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jctools.queues.MpscArrayQueue;

@Plugin(name = "JCToolsBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
public class JCToolsBlockingQueueFactory<E> implements BlockingQueueFactory<E> {
  private final WaitStrategy waitStrategy;
  
  private JCToolsBlockingQueueFactory(WaitStrategy waitStrategy) {
    this.waitStrategy = waitStrategy;
  }
  
  public BlockingQueue<E> create(int capacity) {
    return new MpscBlockingQueue<>(capacity, this.waitStrategy);
  }
  
  @PluginFactory
  public static <E> JCToolsBlockingQueueFactory<E> createFactory(@PluginAttribute(value = "WaitStrategy", defaultString = "PARK") WaitStrategy waitStrategy) {
    return new JCToolsBlockingQueueFactory<>(waitStrategy);
  }
  
  private static final class MpscBlockingQueue<E> extends MpscArrayQueue<E> implements BlockingQueue<E> {
    private final JCToolsBlockingQueueFactory.WaitStrategy waitStrategy;
    
    MpscBlockingQueue(int capacity, JCToolsBlockingQueueFactory.WaitStrategy waitStrategy) {
      super(capacity);
      this.waitStrategy = waitStrategy;
    }
    
    public int drainTo(Collection<? super E> c) {
      return drainTo(c, capacity());
    }
    
    public int drainTo(Collection<? super E> c, int maxElements) {
      return drain(e -> c.add(e), maxElements);
    }
    
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
      int idleCounter = 0;
      long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
      while (true) {
        if (offer(e))
          return true; 
        if (System.nanoTime() - timeoutNanos > 0L)
          return false; 
        idleCounter = this.waitStrategy.idle(idleCounter);
        if (Thread.interrupted())
          throw new InterruptedException(); 
      } 
    }
    
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
      int idleCounter = 0;
      long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
      while (true) {
        E result = poll();
        if (result != null)
          return result; 
        if (System.nanoTime() - timeoutNanos > 0L)
          return null; 
        idleCounter = this.waitStrategy.idle(idleCounter);
        if (Thread.interrupted())
          throw new InterruptedException(); 
      } 
    }
    
    public void put(E e) throws InterruptedException {
      int idleCounter = 0;
      while (true) {
        if (offer(e))
          return; 
        idleCounter = this.waitStrategy.idle(idleCounter);
        if (Thread.interrupted())
          throw new InterruptedException(); 
      } 
    }
    
    public boolean offer(E e) {
      return offerIfBelowThreshold(e, capacity() - 32);
    }
    
    public int remainingCapacity() {
      return capacity() - size();
    }
    
    public E take() throws InterruptedException {
      int idleCounter = 100;
      while (true) {
        E result = (E)relaxedPoll();
        if (result != null)
          return result; 
        idleCounter = this.waitStrategy.idle(idleCounter);
        if (Thread.interrupted())
          throw new InterruptedException(); 
      } 
    }
  }
  
  public enum WaitStrategy {
    SPIN, YIELD, PARK, PROGRESSIVE;
    
    private final JCToolsBlockingQueueFactory.Idle idle;
    
    static {
      SPIN = new WaitStrategy("SPIN", 0, idleCounter -> idleCounter + 1);
      YIELD = new WaitStrategy("YIELD", 1, idleCounter -> {
            Thread.yield();
            return idleCounter + 1;
          });
      PARK = new WaitStrategy("PARK", 2, idleCounter -> {
            LockSupport.parkNanos(1L);
            return idleCounter + 1;
          });
      PROGRESSIVE = new WaitStrategy("PROGRESSIVE", 3, idleCounter -> {
            if (idleCounter > 200) {
              LockSupport.parkNanos(1L);
            } else if (idleCounter > 100) {
              Thread.yield();
            } 
            return idleCounter + 1;
          });
    }
    
    private int idle(int idleCounter) {
      return this.idle.idle(idleCounter);
    }
    
    WaitStrategy(JCToolsBlockingQueueFactory.Idle idle) {
      this.idle = idle;
    }
  }
  
  private static interface Idle {
    int idle(int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\JCToolsBlockingQueueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */