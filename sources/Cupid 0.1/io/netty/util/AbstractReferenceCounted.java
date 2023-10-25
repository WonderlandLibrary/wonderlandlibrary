package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCounted implements ReferenceCounted {
  private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> refCntUpdater;
  
  static {
    AtomicIntegerFieldUpdater<AbstractReferenceCounted> updater = PlatformDependent.newAtomicIntegerFieldUpdater(AbstractReferenceCounted.class, "refCnt");
    if (updater == null)
      updater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCnt"); 
    refCntUpdater = updater;
  }
  
  private volatile int refCnt = 1;
  
  public final int refCnt() {
    return this.refCnt;
  }
  
  protected final void setRefCnt(int refCnt) {
    this.refCnt = refCnt;
  }
  
  public ReferenceCounted retain() {
    int refCnt;
    do {
      refCnt = this.refCnt;
      if (refCnt == 0)
        throw new IllegalReferenceCountException(0, 1); 
      if (refCnt == Integer.MAX_VALUE)
        throw new IllegalReferenceCountException(2147483647, 1); 
    } while (!refCntUpdater.compareAndSet(this, refCnt, refCnt + 1));
    return this;
  }
  
  public ReferenceCounted retain(int increment) {
    int refCnt;
    if (increment <= 0)
      throw new IllegalArgumentException("increment: " + increment + " (expected: > 0)"); 
    do {
      refCnt = this.refCnt;
      if (refCnt == 0)
        throw new IllegalReferenceCountException(0, 1); 
      if (refCnt > Integer.MAX_VALUE - increment)
        throw new IllegalReferenceCountException(refCnt, increment); 
    } while (!refCntUpdater.compareAndSet(this, refCnt, refCnt + increment));
    return this;
  }
  
  public final boolean release() {
    while (true) {
      int refCnt = this.refCnt;
      if (refCnt == 0)
        throw new IllegalReferenceCountException(0, -1); 
      if (refCntUpdater.compareAndSet(this, refCnt, refCnt - 1)) {
        if (refCnt == 1) {
          deallocate();
          return true;
        } 
        return false;
      } 
    } 
  }
  
  public final boolean release(int decrement) {
    if (decrement <= 0)
      throw new IllegalArgumentException("decrement: " + decrement + " (expected: > 0)"); 
    while (true) {
      int refCnt = this.refCnt;
      if (refCnt < decrement)
        throw new IllegalReferenceCountException(refCnt, -decrement); 
      if (refCntUpdater.compareAndSet(this, refCnt, refCnt - decrement)) {
        if (refCnt == decrement) {
          deallocate();
          return true;
        } 
        return false;
      } 
    } 
  }
  
  protected abstract void deallocate();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\AbstractReferenceCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */