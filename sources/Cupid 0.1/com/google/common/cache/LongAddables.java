package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Supplier;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible(emulated = true)
final class LongAddables {
  private static final Supplier<LongAddable> SUPPLIER;
  
  static {
    Supplier<LongAddable> supplier;
    try {
      new LongAdder();
      supplier = new Supplier<LongAddable>() {
          public LongAddable get() {
            return new LongAdder();
          }
        };
    } catch (Throwable t) {
      supplier = new Supplier<LongAddable>() {
          public LongAddable get() {
            return new LongAddables.PureJavaLongAddable();
          }
        };
    } 
    SUPPLIER = supplier;
  }
  
  public static LongAddable create() {
    return (LongAddable)SUPPLIER.get();
  }
  
  private static final class PureJavaLongAddable extends AtomicLong implements LongAddable {
    private PureJavaLongAddable() {}
    
    public void increment() {
      getAndIncrement();
    }
    
    public void add(long x) {
      getAndAdd(x);
    }
    
    public long sum() {
      return get();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\cache\LongAddables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */