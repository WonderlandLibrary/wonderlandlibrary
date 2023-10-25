package io.netty.util.internal.chmv8;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

public abstract class CountedCompleter<T> extends ForkJoinTask<T> {
  private static final long serialVersionUID = 5232453752276485070L;
  
  final CountedCompleter<?> completer;
  
  volatile int pending;
  
  private static final Unsafe U;
  
  private static final long PENDING;
  
  protected CountedCompleter(CountedCompleter<?> completer, int initialPendingCount) {
    this.completer = completer;
    this.pending = initialPendingCount;
  }
  
  protected CountedCompleter(CountedCompleter<?> completer) {
    this.completer = completer;
  }
  
  protected CountedCompleter() {
    this.completer = null;
  }
  
  public void onCompletion(CountedCompleter<?> caller) {}
  
  public boolean onExceptionalCompletion(Throwable ex, CountedCompleter<?> caller) {
    return true;
  }
  
  public final CountedCompleter<?> getCompleter() {
    return this.completer;
  }
  
  public final int getPendingCount() {
    return this.pending;
  }
  
  public final void setPendingCount(int count) {
    this.pending = count;
  }
  
  public final void addToPendingCount(int delta) {
    int c;
    do {
    
    } while (!U.compareAndSwapInt(this, PENDING, c = this.pending, c + delta));
  }
  
  public final boolean compareAndSetPendingCount(int expected, int count) {
    return U.compareAndSwapInt(this, PENDING, expected, count);
  }
  
  public final int decrementPendingCountUnlessZero() {
    int c;
    do {
    
    } while ((c = this.pending) != 0 && !U.compareAndSwapInt(this, PENDING, c, c - 1));
    return c;
  }
  
  public final CountedCompleter<?> getRoot() {
    CountedCompleter<?> a = this;
    CountedCompleter<?> p;
    while ((p = a.completer) != null)
      a = p; 
    return a;
  }
  
  public final void tryComplete() {
    int c;
    CountedCompleter<?> a = this, s = a;
    do {
      while ((c = a.pending) == 0) {
        a.onCompletion(s);
        if ((a = (s = a).completer) == null) {
          s.quietlyComplete();
          return;
        } 
      } 
    } while (!U.compareAndSwapInt(a, PENDING, c, c - 1));
  }
  
  public final void propagateCompletion() {
    int c;
    CountedCompleter<?> a = this, s = a;
    do {
      while ((c = a.pending) == 0) {
        if ((a = (s = a).completer) == null) {
          s.quietlyComplete();
          return;
        } 
      } 
    } while (!U.compareAndSwapInt(a, PENDING, c, c - 1));
  }
  
  public void complete(T rawResult) {
    setRawResult(rawResult);
    onCompletion(this);
    quietlyComplete();
    CountedCompleter<?> p;
    if ((p = this.completer) != null)
      p.tryComplete(); 
  }
  
  public final CountedCompleter<?> firstComplete() {
    while (true) {
      int c;
      if ((c = this.pending) == 0)
        return this; 
      if (U.compareAndSwapInt(this, PENDING, c, c - 1))
        return null; 
    } 
  }
  
  public final CountedCompleter<?> nextComplete() {
    CountedCompleter<?> p;
    if ((p = this.completer) != null)
      return p.firstComplete(); 
    quietlyComplete();
    return null;
  }
  
  public final void quietlyCompleteRoot() {
    CountedCompleter<?> a = this;
    while (true) {
      CountedCompleter<?> p;
      if ((p = a.completer) == null) {
        a.quietlyComplete();
        return;
      } 
      a = p;
    } 
  }
  
  void internalPropagateException(Throwable ex) {
    CountedCompleter<?> a = this, s = a;
    while (a.onExceptionalCompletion(ex, s) && (a = (s = a).completer) != null && a.status >= 0 && a.recordExceptionalCompletion(ex) == Integer.MIN_VALUE);
  }
  
  protected final boolean exec() {
    compute();
    return false;
  }
  
  public T getRawResult() {
    return null;
  }
  
  protected void setRawResult(T t) {}
  
  static {
    try {
      U = getUnsafe();
      PENDING = U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
    } catch (Exception e) {
      throw new Error(e);
    } 
  }
  
  private static Unsafe getUnsafe() {
    try {
      return Unsafe.getUnsafe();
    } catch (SecurityException tryReflectionInstead) {
      try {
        return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
              public Unsafe run() throws Exception {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                  f.setAccessible(true);
                  Object x = f.get(null);
                  if (k.isInstance(x))
                    return k.cast(x); 
                } 
                throw new NoSuchFieldError("the Unsafe");
              }
            });
      } catch (PrivilegedActionException e) {
        throw new RuntimeException("Could not initialize intrinsics", e.getCause());
      } 
    } 
  }
  
  public abstract void compute();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\chmv8\CountedCompleter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */