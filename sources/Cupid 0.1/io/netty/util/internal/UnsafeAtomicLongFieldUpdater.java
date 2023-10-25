package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import sun.misc.Unsafe;

final class UnsafeAtomicLongFieldUpdater<T> extends AtomicLongFieldUpdater<T> {
  private final long offset;
  
  private final Unsafe unsafe;
  
  UnsafeAtomicLongFieldUpdater(Unsafe unsafe, Class<?> tClass, String fieldName) throws NoSuchFieldException {
    Field field = tClass.getDeclaredField(fieldName);
    if (!Modifier.isVolatile(field.getModifiers()))
      throw new IllegalArgumentException("Must be volatile"); 
    this.unsafe = unsafe;
    this.offset = unsafe.objectFieldOffset(field);
  }
  
  public boolean compareAndSet(T obj, long expect, long update) {
    return this.unsafe.compareAndSwapLong(obj, this.offset, expect, update);
  }
  
  public boolean weakCompareAndSet(T obj, long expect, long update) {
    return this.unsafe.compareAndSwapLong(obj, this.offset, expect, update);
  }
  
  public void set(T obj, long newValue) {
    this.unsafe.putLongVolatile(obj, this.offset, newValue);
  }
  
  public void lazySet(T obj, long newValue) {
    this.unsafe.putOrderedLong(obj, this.offset, newValue);
  }
  
  public long get(T obj) {
    return this.unsafe.getLongVolatile(obj, this.offset);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\UnsafeAtomicLongFieldUpdater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */