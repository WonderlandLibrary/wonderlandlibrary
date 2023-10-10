package com.google.common.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicDoubleArray implements Serializable {
   private static final long serialVersionUID = 0L;
   private transient AtomicLongArray longs;

   public AtomicDoubleArray(int var1) {
      this.longs = new AtomicLongArray(var1);
   }

   public AtomicDoubleArray(double[] var1) {
      int var2 = var1.length;
      long[] var3 = new long[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = Double.doubleToRawLongBits(var1[var4]);
      }

      this.longs = new AtomicLongArray(var3);
   }

   public final int length() {
      return this.longs.length();
   }

   public final double get(int var1) {
      return Double.longBitsToDouble(this.longs.get(var1));
   }

   public final void set(int var1, double var2) {
      long var4 = Double.doubleToRawLongBits(var2);
      this.longs.set(var1, var4);
   }

   public final void lazySet(int var1, double var2) {
      this.set(var1, var2);
   }

   public final double getAndSet(int var1, double var2) {
      long var4 = Double.doubleToRawLongBits(var2);
      return Double.longBitsToDouble(this.longs.getAndSet(var1, var4));
   }

   public final boolean compareAndSet(int var1, double var2, double var4) {
      return this.longs.compareAndSet(var1, Double.doubleToRawLongBits(var2), Double.doubleToRawLongBits(var4));
   }

   public final boolean weakCompareAndSet(int var1, double var2, double var4) {
      return this.longs.weakCompareAndSet(var1, Double.doubleToRawLongBits(var2), Double.doubleToRawLongBits(var4));
   }

   public final double getAndAdd(int var1, double var2) {
      long var4;
      double var6;
      long var10;
      do {
         var4 = this.longs.get(var1);
         var6 = Double.longBitsToDouble(var4);
         double var8 = var6 + var2;
         var10 = Double.doubleToRawLongBits(var8);
      } while(!this.longs.compareAndSet(var1, var4, var10));

      return var6;
   }

   public double addAndGet(int var1, double var2) {
      long var4;
      double var8;
      long var10;
      do {
         var4 = this.longs.get(var1);
         double var6 = Double.longBitsToDouble(var4);
         var8 = var6 + var2;
         var10 = Double.doubleToRawLongBits(var8);
      } while(!this.longs.compareAndSet(var1, var4, var10));

      return var8;
   }

   public String toString() {
      int var1 = this.length() - 1;
      if (var1 == -1) {
         return "[]";
      } else {
         StringBuilder var2 = new StringBuilder(19 * (var1 + 1));
         var2.append('[');
         int var3 = 0;

         while(true) {
            var2.append(Double.longBitsToDouble(this.longs.get(var3)));
            if (var3 == var1) {
               return var2.append(']').toString();
            }

            var2.append(',').append(' ');
            ++var3;
         }
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      int var2 = this.length();
      var1.writeInt(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.writeDouble(this.get(var3));
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      this.longs = new AtomicLongArray(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         this.set(var3, var1.readDouble());
      }

   }
}
