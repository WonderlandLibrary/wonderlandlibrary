package io.netty.util.concurrent;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup {
  private final EventExecutor[] children;
  
  private final AtomicInteger childIndex = new AtomicInteger();
  
  private final AtomicInteger terminatedChildren = new AtomicInteger();
  
  private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
  
  private final EventExecutorChooser chooser;
  
  protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
    if (nThreads <= 0)
      throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", new Object[] { Integer.valueOf(nThreads) })); 
    if (threadFactory == null)
      threadFactory = newDefaultThreadFactory(); 
    this.children = (EventExecutor[])new SingleThreadEventExecutor[nThreads];
    if (isPowerOfTwo(this.children.length)) {
      this.chooser = new PowerOfTwoEventExecutorChooser();
    } else {
      this.chooser = new GenericEventExecutorChooser();
    } 
    for (int i = 0; i < nThreads; i++)
      boolean success = false; 
    FutureListener<Object> terminationListener = new FutureListener() {
        public void operationComplete(Future<Object> future) throws Exception {
          if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length)
            MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null); 
        }
      };
    for (EventExecutor e : this.children)
      e.terminationFuture().addListener(terminationListener); 
  }
  
  protected ThreadFactory newDefaultThreadFactory() {
    return new DefaultThreadFactory(getClass());
  }
  
  public EventExecutor next() {
    return this.chooser.next();
  }
  
  public Iterator<EventExecutor> iterator() {
    return children().iterator();
  }
  
  public final int executorCount() {
    return this.children.length;
  }
  
  protected Set<EventExecutor> children() {
    Set<EventExecutor> children = Collections.newSetFromMap(new LinkedHashMap<EventExecutor, Boolean>());
    Collections.addAll(children, this.children);
    return children;
  }
  
  public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
    for (EventExecutor l : this.children)
      l.shutdownGracefully(quietPeriod, timeout, unit); 
    return terminationFuture();
  }
  
  public Future<?> terminationFuture() {
    return this.terminationFuture;
  }
  
  @Deprecated
  public void shutdown() {
    for (EventExecutor l : this.children)
      l.shutdown(); 
  }
  
  public boolean isShuttingDown() {
    for (EventExecutor l : this.children) {
      if (!l.isShuttingDown())
        return false; 
    } 
    return true;
  }
  
  public boolean isShutdown() {
    for (EventExecutor l : this.children) {
      if (!l.isShutdown())
        return false; 
    } 
    return true;
  }
  
  public boolean isTerminated() {
    for (EventExecutor l : this.children) {
      if (!l.isTerminated())
        return false; 
    } 
    return true;
  }
  
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    long deadline = System.nanoTime() + unit.toNanos(timeout);
    EventExecutor[] arr$;
    int len$, i$;
    for (arr$ = this.children, len$ = arr$.length, i$ = 0; i$ < len$; ) {
      EventExecutor l = arr$[i$];
      while (true) {
        long timeLeft = deadline - System.nanoTime();
        if (timeLeft <= 0L)
          break; 
        if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS))
          i$++; 
      } 
    } 
    return isTerminated();
  }
  
  private static boolean isPowerOfTwo(int val) {
    return ((val & -val) == val);
  }
  
  protected abstract EventExecutor newChild(ThreadFactory paramThreadFactory, Object... paramVarArgs) throws Exception;
  
  private static interface EventExecutorChooser {
    EventExecutor next();
  }
  
  private final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser {
    private PowerOfTwoEventExecutorChooser() {}
    
    public EventExecutor next() {
      return MultithreadEventExecutorGroup.this.children[MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() & MultithreadEventExecutorGroup.this.children.length - 1];
    }
  }
  
  private final class GenericEventExecutorChooser implements EventExecutorChooser {
    private GenericEventExecutorChooser() {}
    
    public EventExecutor next() {
      return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\MultithreadEventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */