package com.google.common.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicDoubleArray implements Serializable {
  private static final long serialVersionUID = 0L;
  
  private transient AtomicLongArray longs;
  
  public AtomicDoubleArray(int length) {
    this.longs = new AtomicLongArray(length);
  }
  
  public AtomicDoubleArray(double[] array) {
    int len = array.length;
    long[] longArray = new long[len];
    for (int i = 0; i < len; i++)
      longArray[i] = Double.doubleToRawLongBits(array[i]); 
    this.longs = new AtomicLongArray(longArray);
  }
  
  public final int length() {
    return this.longs.length();
  }
  
  public final double get(int i) {
    return Double.longBitsToDouble(this.longs.get(i));
  }
  
  public final void set(int i, double newValue) {
    long next = Double.doubleToRawLongBits(newValue);
    this.longs.set(i, next);
  }
  
  public final void lazySet(int i, double newValue) {
    set(i, newValue);
  }
  
  public final double getAndSet(int i, double newValue) {
    long next = Double.doubleToRawLongBits(newValue);
    return Double.longBitsToDouble(this.longs.getAndSet(i, next));
  }
  
  public final boolean compareAndSet(int i, double expect, double update) {
    return this.longs.compareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
  }
  
  public final boolean weakCompareAndSet(int i, double expect, double update) {
    return this.longs.weakCompareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
  }
  
  public final double getAndAdd(int i, double delta) {
    while (true) {
      long current = this.longs.get(i);
      double currentVal = Double.longBitsToDouble(current);
      double nextVal = currentVal + delta;
      long next = Double.doubleToRawLongBits(nextVal);
      if (this.longs.compareAndSet(i, current, next))
        return currentVal; 
    } 
  }
  
  public double addAndGet(int i, double delta) {
    while (true) {
      long current = this.longs.get(i);
      double currentVal = Double.longBitsToDouble(current);
      double nextVal = currentVal + delta;
      long next = Double.doubleToRawLongBits(nextVal);
      if (this.longs.compareAndSet(i, current, next))
        return nextVal; 
    } 
  }
  
  public String toString() {
    int iMax = length() - 1;
    if (iMax == -1)
      return "[]"; 
    StringBuilder b = new StringBuilder(19 * (iMax + 1));
    b.append('[');
    for (int i = 0;; i++) {
      b.append(Double.longBitsToDouble(this.longs.get(i)));
      if (i == iMax)
        return b.append(']').toString(); 
      b.append(',').append(' ');
    } 
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    int length = length();
    s.writeInt(length);
    for (int i = 0; i < length; i++)
      s.writeDouble(get(i)); 
  }
  
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    int length = s.readInt();
    this.longs = new AtomicLongArray(length);
    for (int i = 0; i < length; i++)
      set(i, s.readDouble()); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\commo\\util\concurrent\AtomicDoubleArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */