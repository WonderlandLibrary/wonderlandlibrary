package gnu.trove.impl.sync;

import gnu.trove.function.TDoubleFunction;
import gnu.trove.list.TDoubleList;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TSynchronizedDoubleList extends TSynchronizedDoubleCollection implements TDoubleList {
   static final long serialVersionUID = -7754090372962971524L;
   final TDoubleList list;

   public TSynchronizedDoubleList(TDoubleList var1) {
      super(var1);
      this.list = var1;
   }

   public TSynchronizedDoubleList(TDoubleList var1, Object var2) {
      super(var1, var2);
      this.list = var1;
   }

   public boolean equals(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.equals(var1);
   }

   public int hashCode() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.hashCode();
   }

   public double get(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.get(var1);
   }

   public double set(int var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.set(var1, var2);
   }

   public void set(int var1, double[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.set(var1, var2);
   }

   public void set(int var1, double[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.set(var1, var2, var3, var4);
   }

   public double replace(int var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.replace(var1, var2);
   }

   public void remove(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.remove(var1, var2);
   }

   public double removeAt(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.removeAt(var1);
   }

   public void add(double[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.add(var1);
   }

   public void add(double[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      this.list.add(var1, var2, var3);
   }

   public void insert(int var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, double[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, double[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.insert(var1, var2, var3, var4);
   }

   public int indexOf(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.lastIndexOf(var1);
   }

   public TDoubleList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedDoubleList(this.list.subList(var1, var2), this.mutex);
   }

   public double[] toArray(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.toArray(var1, var2);
   }

   public double[] toArray(double[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.toArray(var1, var2, var3);
   }

   public double[] toArray(double[] var1, int var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.list.toArray(var1, var2, var3, var4);
   }

   public int indexOf(int var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.lastIndexOf(var1, var2);
   }

   public void fill(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.fill(var1);
   }

   public void fill(int var1, int var2, double var3) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.fill(var1, var2, var3);
   }

   public void reverse() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.list.reverse();
   }

   public void reverse(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.reverse(var1, var2);
   }

   public void shuffle(Random var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.shuffle(var1);
   }

   public void sort() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.list.sort();
   }

   public void sort(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.sort(var1, var2);
   }

   public int binarySearch(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.binarySearch(var1);
   }

   public int binarySearch(double var1, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.list.binarySearch(var1, var3, var4);
   }

   public TDoubleList grep(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.grep(var1);
   }

   public TDoubleList inverseGrep(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.inverseGrep(var1);
   }

   public double max() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.max();
   }

   public double min() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.min();
   }

   public double sum() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.sum();
   }

   public boolean forEachDescending(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.forEachDescending(var1);
   }

   public void transformValues(TDoubleFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.transformValues(var1);
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessDoubleList(this.list) : this;
   }
}
