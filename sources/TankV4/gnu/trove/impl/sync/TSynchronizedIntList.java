package gnu.trove.impl.sync;

import gnu.trove.function.TIntFunction;
import gnu.trove.list.TIntList;
import gnu.trove.procedure.TIntProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TSynchronizedIntList extends TSynchronizedIntCollection implements TIntList {
   static final long serialVersionUID = -7754090372962971524L;
   final TIntList list;

   public TSynchronizedIntList(TIntList var1) {
      super(var1);
      this.list = var1;
   }

   public TSynchronizedIntList(TIntList var1, Object var2) {
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

   public int get(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.get(var1);
   }

   public int set(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.set(var1, var2);
   }

   public void set(int var1, int[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.set(var1, var2);
   }

   public void set(int var1, int[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.set(var1, var2, var3, var4);
   }

   public int replace(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.replace(var1, var2);
   }

   public void remove(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.remove(var1, var2);
   }

   public int removeAt(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.removeAt(var1);
   }

   public void add(int[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.add(var1);
   }

   public void add(int[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      this.list.add(var1, var2, var3);
   }

   public void insert(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, int[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, int[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.insert(var1, var2, var3, var4);
   }

   public int indexOf(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.lastIndexOf(var1);
   }

   public TIntList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedIntList(this.list.subList(var1, var2), this.mutex);
   }

   public int[] toArray(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.toArray(var1, var2);
   }

   public int[] toArray(int[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.toArray(var1, var2, var3);
   }

   public int[] toArray(int[] var1, int var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.list.toArray(var1, var2, var3, var4);
   }

   public int indexOf(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.lastIndexOf(var1, var2);
   }

   public void fill(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.fill(var1);
   }

   public void fill(int var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
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

   public int binarySearch(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.binarySearch(var1);
   }

   public int binarySearch(int var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.binarySearch(var1, var2, var3);
   }

   public TIntList grep(TIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.grep(var1);
   }

   public TIntList inverseGrep(TIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.inverseGrep(var1);
   }

   public int max() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.max();
   }

   public int min() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.min();
   }

   public int sum() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.sum();
   }

   public boolean forEachDescending(TIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.forEachDescending(var1);
   }

   public void transformValues(TIntFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.transformValues(var1);
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessIntList(this.list) : this;
   }
}
