package io.netty.util.concurrent;

import io.netty.util.Signal;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V> {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
  
  private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
  
  private static final int MAX_LISTENER_STACK_DEPTH = 8;
  
  private static final Signal SUCCESS = Signal.valueOf(DefaultPromise.class.getName() + ".SUCCESS");
  
  private static final Signal UNCANCELLABLE = Signal.valueOf(DefaultPromise.class.getName() + ".UNCANCELLABLE");
  
  private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(new CancellationException());
  
  private final EventExecutor executor;
  
  private volatile Object result;
  
  private Object listeners;
  
  private LateListeners lateListeners;
  
  private short waiters;
  
  static {
    CANCELLATION_CAUSE_HOLDER.cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
  }
  
  public DefaultPromise(EventExecutor executor) {
    if (executor == null)
      throw new NullPointerException("executor"); 
    this.executor = executor;
  }
  
  protected DefaultPromise() {
    this.executor = null;
  }
  
  protected EventExecutor executor() {
    return this.executor;
  }
  
  public boolean isCancelled() {
    return isCancelled0(this.result);
  }
  
  private static boolean isCancelled0(Object result) {
    return (result instanceof CauseHolder && ((CauseHolder)result).cause instanceof CancellationException);
  }
  
  public boolean isCancellable() {
    return (this.result == null);
  }
  
  public boolean isDone() {
    return isDone0(this.result);
  }
  
  private static boolean isDone0(Object result) {
    return (result != null && result != UNCANCELLABLE);
  }
  
  public boolean isSuccess() {
    Object result = this.result;
    if (result == null || result == UNCANCELLABLE)
      return false; 
    return !(result instanceof CauseHolder);
  }
  
  public Throwable cause() {
    Object result = this.result;
    if (result instanceof CauseHolder)
      return ((CauseHolder)result).cause; 
    return null;
  }
  
  public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
    if (listener == null)
      throw new NullPointerException("listener"); 
    if (isDone()) {
      notifyLateListener(listener);
      return this;
    } 
    synchronized (this) {
      if (!isDone()) {
        if (this.listeners == null) {
          this.listeners = listener;
        } else if (this.listeners instanceof DefaultFutureListeners) {
          ((DefaultFutureListeners)this.listeners).add(listener);
        } else {
          GenericFutureListener<? extends Future<V>> firstListener = (GenericFutureListener<? extends Future<V>>)this.listeners;
          this.listeners = new DefaultFutureListeners(firstListener, listener);
        } 
        return this;
      } 
    } 
    notifyLateListener(listener);
    return this;
  }
  
  public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
    if (listeners == null)
      throw new NullPointerException("listeners"); 
    for (GenericFutureListener<? extends Future<? super V>> l : listeners) {
      if (l == null)
        break; 
      addListener(l);
    } 
    return this;
  }
  
  public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
    if (listener == null)
      throw new NullPointerException("listener"); 
    if (isDone())
      return this; 
    synchronized (this) {
      if (!isDone())
        if (this.listeners instanceof DefaultFutureListeners) {
          ((DefaultFutureListeners)this.listeners).remove(listener);
        } else if (this.listeners == listener) {
          this.listeners = null;
        }  
    } 
    return this;
  }
  
  public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
    if (listeners == null)
      throw new NullPointerException("listeners"); 
    for (GenericFutureListener<? extends Future<? super V>> l : listeners) {
      if (l == null)
        break; 
      removeListener(l);
    } 
    return this;
  }
  
  public Promise<V> sync() throws InterruptedException {
    await();
    rethrowIfFailed();
    return this;
  }
  
  public Promise<V> syncUninterruptibly() {
    awaitUninterruptibly();
    rethrowIfFailed();
    return this;
  }
  
  private void rethrowIfFailed() {
    Throwable cause = cause();
    if (cause == null)
      return; 
    PlatformDependent.throwException(cause);
  }
  
  public Promise<V> await() throws InterruptedException {
    if (isDone())
      return this; 
    if (Thread.interrupted())
      throw new InterruptedException(toString()); 
    synchronized (this) {
      while (!isDone()) {
        checkDeadLock();
        incWaiters();
        try {
          wait();
        } finally {
          decWaiters();
        } 
      } 
    } 
    return this;
  }
  
  public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
    return await0(unit.toNanos(timeout), true);
  }
  
  public boolean await(long timeoutMillis) throws InterruptedException {
    return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
  }
  
  public Promise<V> awaitUninterruptibly() {
    if (isDone())
      return this; 
    boolean interrupted = false;
    synchronized (this) {
      while (!isDone()) {
        checkDeadLock();
        incWaiters();
        try {
          wait();
        } catch (InterruptedException e) {
          interrupted = true;
        } finally {
          decWaiters();
        } 
      } 
    } 
    if (interrupted)
      Thread.currentThread().interrupt(); 
    return this;
  }
  
  public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
    try {
      return await0(unit.toNanos(timeout), false);
    } catch (InterruptedException e) {
      throw new InternalError();
    } 
  }
  
  public boolean awaitUninterruptibly(long timeoutMillis) {
    try {
      return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
    } catch (InterruptedException e) {
      throw new InternalError();
    } 
  }
  
  private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
    if (isDone())
      return true; 
    if (timeoutNanos <= 0L)
      return isDone(); 
    if (interruptable && Thread.interrupted())
      throw new InterruptedException(toString()); 
    long startTime = System.nanoTime();
    long waitTime = timeoutNanos;
    boolean interrupted = false;
    try {
    
    } finally {
      if (interrupted)
        Thread.currentThread().interrupt(); 
    } 
  }
  
  protected void checkDeadLock() {
    EventExecutor e = executor();
    if (e != null && e.inEventLoop())
      throw new BlockingOperationException(toString()); 
  }
  
  public Promise<V> setSuccess(V result) {
    if (setSuccess0(result)) {
      notifyListeners();
      return this;
    } 
    throw new IllegalStateException("complete already: " + this);
  }
  
  public boolean trySuccess(V result) {
    if (setSuccess0(result)) {
      notifyListeners();
      return true;
    } 
    return false;
  }
  
  public Promise<V> setFailure(Throwable cause) {
    if (setFailure0(cause)) {
      notifyListeners();
      return this;
    } 
    throw new IllegalStateException("complete already: " + this, cause);
  }
  
  public boolean tryFailure(Throwable cause) {
    if (setFailure0(cause)) {
      notifyListeners();
      return true;
    } 
    return false;
  }
  
  public boolean cancel(boolean mayInterruptIfRunning) {
    Object result = this.result;
    if (isDone0(result) || result == UNCANCELLABLE)
      return false; 
    synchronized (this) {
      result = this.result;
      if (isDone0(result) || result == UNCANCELLABLE)
        return false; 
      this.result = CANCELLATION_CAUSE_HOLDER;
      if (hasWaiters())
        notifyAll(); 
    } 
    notifyListeners();
    return true;
  }
  
  public boolean setUncancellable() {
    Object result = this.result;
    if (isDone0(result))
      return !isCancelled0(result); 
    synchronized (this) {
      result = this.result;
      if (isDone0(result))
        return !isCancelled0(result); 
      this.result = UNCANCELLABLE;
    } 
    return true;
  }
  
  private boolean setFailure0(Throwable cause) {
    if (cause == null)
      throw new NullPointerException("cause"); 
    if (isDone())
      return false; 
    synchronized (this) {
      if (isDone())
        return false; 
      this.result = new CauseHolder(cause);
      if (hasWaiters())
        notifyAll(); 
    } 
    return true;
  }
  
  private boolean setSuccess0(V result) {
    if (isDone())
      return false; 
    synchronized (this) {
      if (isDone())
        return false; 
      if (result == null) {
        this.result = SUCCESS;
      } else {
        this.result = result;
      } 
      if (hasWaiters())
        notifyAll(); 
    } 
    return true;
  }
  
  public V getNow() {
    Object result = this.result;
    if (result instanceof CauseHolder || result == SUCCESS)
      return null; 
    return (V)result;
  }
  
  private boolean hasWaiters() {
    return (this.waiters > 0);
  }
  
  private void incWaiters() {
    if (this.waiters == Short.MAX_VALUE)
      throw new IllegalStateException("too many waiters: " + this); 
    this.waiters = (short)(this.waiters + 1);
  }
  
  private void decWaiters() {
    this.waiters = (short)(this.waiters - 1);
  }
  
  private void notifyListeners() {
    Object listeners = this.listeners;
    if (listeners == null)
      return; 
    EventExecutor executor = executor();
    if (executor.inEventLoop()) {
      InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
      int stackDepth = threadLocals.futureListenerStackDepth();
      if (stackDepth < 8) {
        threadLocals.setFutureListenerStackDepth(stackDepth + 1);
        try {
          if (listeners instanceof DefaultFutureListeners) {
            notifyListeners0(this, (DefaultFutureListeners)listeners);
          } else {
            final GenericFutureListener<? extends Future<V>> l = (GenericFutureListener<? extends Future<V>>)listeners;
            notifyListener0(this, l);
          } 
        } finally {
          this.listeners = null;
          threadLocals.setFutureListenerStackDepth(stackDepth);
        } 
        return;
      } 
    } 
    if (listeners instanceof DefaultFutureListeners) {
      final DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
      execute(executor, new Runnable() {
            public void run() {
              DefaultPromise.notifyListeners0(DefaultPromise.this, dfl);
              DefaultPromise.this.listeners = null;
            }
          });
    } else {
      final GenericFutureListener<? extends Future<V>> l = (GenericFutureListener<? extends Future<V>>)listeners;
      execute(executor, new Runnable() {
            public void run() {
              DefaultPromise.notifyListener0(DefaultPromise.this, l);
              DefaultPromise.this.listeners = null;
            }
          });
    } 
  }
  
  private static void notifyListeners0(Future<?> future, DefaultFutureListeners listeners) {
    GenericFutureListener[] arrayOfGenericFutureListener = (GenericFutureListener[])listeners.listeners();
    int size = listeners.size();
    for (int i = 0; i < size; i++)
      notifyListener0(future, arrayOfGenericFutureListener[i]); 
  }
  
  private void notifyLateListener(GenericFutureListener<?> l) {
    EventExecutor executor = executor();
    if (executor.inEventLoop())
      if (this.listeners == null && this.lateListeners == null) {
        InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
        int stackDepth = threadLocals.futureListenerStackDepth();
        if (stackDepth < 8) {
          threadLocals.setFutureListenerStackDepth(stackDepth + 1);
          try {
            notifyListener0(this, l);
          } finally {
            threadLocals.setFutureListenerStackDepth(stackDepth);
          } 
          return;
        } 
      } else {
        LateListeners lateListeners = this.lateListeners;
        if (lateListeners == null)
          this.lateListeners = lateListeners = new LateListeners(); 
        lateListeners.add(l);
        execute(executor, lateListeners);
        return;
      }  
    execute(executor, new LateListenerNotifier(l));
  }
  
  protected static void notifyListener(EventExecutor eventExecutor, final Future<?> future, final GenericFutureListener<?> l) {
    if (eventExecutor.inEventLoop()) {
      InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
      int stackDepth = threadLocals.futureListenerStackDepth();
      if (stackDepth < 8) {
        threadLocals.setFutureListenerStackDepth(stackDepth + 1);
        try {
          notifyListener0(future, l);
        } finally {
          threadLocals.setFutureListenerStackDepth(stackDepth);
        } 
        return;
      } 
    } 
    execute(eventExecutor, new Runnable() {
          public void run() {
            DefaultPromise.notifyListener0(future, l);
          }
        });
  }
  
  private static void execute(EventExecutor executor, Runnable task) {
    try {
      executor.execute(task);
    } catch (Throwable t) {
      rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
    } 
  }
  
  static void notifyListener0(Future future, GenericFutureListener<Future> l) {
    try {
      l.operationComplete(future);
    } catch (Throwable t) {
      if (logger.isWarnEnabled())
        logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t); 
    } 
  }
  
  private synchronized Object progressiveListeners() {
    Object listeners = this.listeners;
    if (listeners == null)
      return null; 
    if (listeners instanceof DefaultFutureListeners) {
      DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
      int progressiveSize = dfl.progressiveSize();
      switch (progressiveSize) {
        case 0:
          return null;
        case 1:
          for (GenericFutureListener<?> l : dfl.listeners()) {
            if (l instanceof GenericProgressiveFutureListener)
              return l; 
          } 
          return null;
      } 
      GenericFutureListener[] arrayOfGenericFutureListener = (GenericFutureListener[])dfl.listeners();
      GenericProgressiveFutureListener[] arrayOfGenericProgressiveFutureListener = new GenericProgressiveFutureListener[progressiveSize];
      for (int i = 0, j = 0; j < progressiveSize; i++) {
        GenericFutureListener<?> l = arrayOfGenericFutureListener[i];
        if (l instanceof GenericProgressiveFutureListener)
          arrayOfGenericProgressiveFutureListener[j++] = (GenericProgressiveFutureListener)l; 
      } 
      return arrayOfGenericProgressiveFutureListener;
    } 
    if (listeners instanceof GenericProgressiveFutureListener)
      return listeners; 
    return null;
  }
  
  void notifyProgressiveListeners(final long progress, final long total) {
    Object listeners = progressiveListeners();
    if (listeners == null)
      return; 
    final ProgressiveFuture<V> self = (ProgressiveFuture<V>)this;
    EventExecutor executor = executor();
    if (executor.inEventLoop()) {
      if (listeners instanceof GenericProgressiveFutureListener[]) {
        notifyProgressiveListeners0(self, (GenericProgressiveFutureListener<?>[])listeners, progress, total);
      } else {
        notifyProgressiveListener0(self, (GenericProgressiveFutureListener)listeners, progress, total);
      } 
    } else if (listeners instanceof GenericProgressiveFutureListener[]) {
      final GenericProgressiveFutureListener[] array = (GenericProgressiveFutureListener[])listeners;
      execute(executor, new Runnable() {
            public void run() {
              DefaultPromise.notifyProgressiveListeners0(self, (GenericProgressiveFutureListener<?>[])array, progress, total);
            }
          });
    } else {
      final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener<ProgressiveFuture<V>>)listeners;
      execute(executor, new Runnable() {
            public void run() {
              DefaultPromise.notifyProgressiveListener0(self, l, progress, total);
            }
          });
    } 
  }
  
  private static void notifyProgressiveListeners0(ProgressiveFuture<?> future, GenericProgressiveFutureListener<?>[] listeners, long progress, long total) {
    for (GenericProgressiveFutureListener<?> l : listeners) {
      if (l == null)
        break; 
      notifyProgressiveListener0(future, l, progress, total);
    } 
  }
  
  private static void notifyProgressiveListener0(ProgressiveFuture future, GenericProgressiveFutureListener<ProgressiveFuture> l, long progress, long total) {
    try {
      l.operationProgressed(future, progress, total);
    } catch (Throwable t) {
      if (logger.isWarnEnabled())
        logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t); 
    } 
  }
  
  private static final class CauseHolder {
    final Throwable cause;
    
    CauseHolder(Throwable cause) {
      this.cause = cause;
    }
  }
  
  public String toString() {
    return toStringBuilder().toString();
  }
  
  protected StringBuilder toStringBuilder() {
    StringBuilder buf = new StringBuilder(64);
    buf.append(StringUtil.simpleClassName(this));
    buf.append('@');
    buf.append(Integer.toHexString(hashCode()));
    Object result = this.result;
    if (result == SUCCESS) {
      buf.append("(success)");
    } else if (result == UNCANCELLABLE) {
      buf.append("(uncancellable)");
    } else if (result instanceof CauseHolder) {
      buf.append("(failure(");
      buf.append(((CauseHolder)result).cause);
      buf.append(')');
    } else {
      buf.append("(incomplete)");
    } 
    return buf;
  }
  
  private final class LateListeners extends ArrayDeque<GenericFutureListener<?>> implements Runnable {
    private static final long serialVersionUID = -687137418080392244L;
    
    LateListeners() {
      super(2);
    }
    
    public void run() {
      if (DefaultPromise.this.listeners == null) {
        while (true) {
          GenericFutureListener<?> l = poll();
          if (l == null)
            break; 
          DefaultPromise.notifyListener0(DefaultPromise.this, l);
        } 
      } else {
        DefaultPromise.execute(DefaultPromise.this.executor(), this);
      } 
    }
  }
  
  private final class LateListenerNotifier implements Runnable {
    private GenericFutureListener<?> l;
    
    LateListenerNotifier(GenericFutureListener<?> l) {
      this.l = l;
    }
    
    public void run() {
      DefaultPromise<V>.LateListeners lateListeners = DefaultPromise.this.lateListeners;
      if (this.l != null) {
        if (lateListeners == null)
          DefaultPromise.this.lateListeners = lateListeners = new DefaultPromise.LateListeners(); 
        lateListeners.add(this.l);
        this.l = null;
      } 
      lateListeners.run();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\DefaultPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */