package com.google.common.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AtomicDouble extends Number implements Serializable {
   private static final long serialVersionUID = 0L;
   private transient volatile long value;
   private static final AtomicLongFieldUpdater updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");

   public AtomicDouble(double var1) {
      this.value = Double.doubleToRawLongBits(var1);
   }

   public AtomicDouble() {
   }

   public final double get() {
      return Double.longBitsToDouble(this.value);
   }

   public final void set(double var1) {
      long var3 = Double.doubleToRawLongBits(var1);
      this.value = var3;
   }

   public final void lazySet(double var1) {
      this.set(var1);
   }

   public final double getAndSet(double var1) {
      long var3 = Double.doubleToRawLongBits(var1);
      return Double.longBitsToDouble(updater.getAndSet(this, var3));
   }

   public final boolean compareAndSet(double var1, double var3) {
      return updater.compareAndSet(this, Double.doubleToRawLongBits(var1), Double.doubleToRawLongBits(var3));
   }

   public final boolean weakCompareAndSet(double var1, double var3) {
      return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(var1), Double.doubleToRawLongBits(var3));
   }

   public final double getAndAdd(double var1) {
      long var3;
      double var5;
      long var9;
      do {
         var3 = this.value;
         var5 = Double.longBitsToDouble(var3);
         double var7 = var5 + var1;
         var9 = Double.doubleToRawLongBits(var7);
      } while(!updater.compareAndSet(this, var3, var9));

      return var5;
   }

   public final double addAndGet(double var1) {
      long var3;
      double var7;
      long var9;
      do {
         var3 = this.value;
         double var5 = Double.longBitsToDouble(var3);
         var7 = var5 + var1;
         var9 = Double.doubleToRawLongBits(var7);
      } while(!updater.compareAndSet(this, var3, var9));

      return var7;
   }

   public String toString() {
      return Double.toString(this.get());
   }

   public int intValue() {
      return (int)this.get();
   }

   public long longValue() {
      return (long)this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return this.get();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeDouble(this.get());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.set(var1.readDouble());
   }
}
