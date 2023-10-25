package com.google.common.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AtomicDouble extends Number implements Serializable {
  private static final long serialVersionUID = 0L;
  
  private volatile transient long value;
  
  private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
  
  public AtomicDouble(double initialValue) {
    this.value = Double.doubleToRawLongBits(initialValue);
  }
  
  public AtomicDouble() {}
  
  public final double get() {
    return Double.longBitsToDouble(this.value);
  }
  
  public final void set(double newValue) {
    long next = Double.doubleToRawLongBits(newValue);
    this.value = next;
  }
  
  public final void lazySet(double newValue) {
    set(newValue);
  }
  
  public final double getAndSet(double newValue) {
    long next = Double.doubleToRawLongBits(newValue);
    return Double.longBitsToDouble(updater.getAndSet(this, next));
  }
  
  public final boolean compareAndSet(double expect, double update) {
    return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
  }
  
  public final boolean weakCompareAndSet(double expect, double update) {
    return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
  }
  
  public final double getAndAdd(double delta) {
    while (true) {
      long current = this.value;
      double currentVal = Double.longBitsToDouble(current);
      double nextVal = currentVal + delta;
      long next = Double.doubleToRawLongBits(nextVal);
      if (updater.compareAndSet(this, current, next))
        return currentVal; 
    } 
  }
  
  public final double addAndGet(double delta) {
    while (true) {
      long current = this.value;
      double currentVal = Double.longBitsToDouble(current);
      double nextVal = currentVal + delta;
      long next = Double.doubleToRawLongBits(nextVal);
      if (updater.compareAndSet(this, current, next))
        return nextVal; 
    } 
  }
  
  public String toString() {
    return Double.toString(get());
  }
  
  public int intValue() {
    return (int)get();
  }
  
  public long longValue() {
    return (long)get();
  }
  
  public float floatValue() {
    return (float)get();
  }
  
  public double doubleValue() {
    return get();
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    s.writeDouble(get());
  }
  
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    set(s.readDouble());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AtomicDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */