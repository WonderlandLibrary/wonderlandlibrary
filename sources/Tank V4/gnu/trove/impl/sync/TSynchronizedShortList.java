package gnu.trove.impl.sync;

import gnu.trove.function.TShortFunction;
import gnu.trove.list.TShortList;
import gnu.trove.procedure.TShortProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TSynchronizedShortList extends TSynchronizedShortCollection implements TShortList {
   static final long serialVersionUID = -7754090372962971524L;
   final TShortList list;

   public TSynchronizedShortList(TShortList var1) {
      super(var1);
      this.list = var1;
   }

   public TSynchronizedShortList(TShortList var1, Object var2) {
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

   public short get(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.get(var1);
   }

   public short set(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.set(var1, var2);
   }

   public void set(int var1, short[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.set(var1, var2);
   }

   public void set(int var1, short[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.set(var1, var2, var3, var4);
   }

   public short replace(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.replace(var1, var2);
   }

   public void remove(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.remove(var1, var2);
   }

   public short removeAt(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.removeAt(var1);
   }

   public void add(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.add(var1);
   }

   public void add(short[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      this.list.add(var1, var2, var3);
   }

   public void insert(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, short[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, short[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.insert(var1, var2, var3, var4);
   }

   public int indexOf(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.lastIndexOf(var1);
   }

   public TShortList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedShortList(this.list.subList(var1, var2), this.mutex);
   }

   public short[] toArray(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.toArray(var1, var2);
   }

   public short[] toArray(short[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.toArray(var1, var2, var3);
   }

   public short[] toArray(short[] var1, int var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.list.toArray(var1, var2, var3, var4);
   }

   public int indexOf(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.lastIndexOf(var1, var2);
   }

   public void fill(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.fill(var1);
   }

   public void fill(int var1, int var2, short var3) {
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

   public int binarySearch(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.binarySearch(var1);
   }

   public int binarySearch(short var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.binarySearch(var1, var2, var3);
   }

   public TShortList grep(TShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.grep(var1);
   }

   public TShortList inverseGrep(TShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.inverseGrep(var1);
   }

   public short max() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.max();
   }

   public short min() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.min();
   }

   public short sum() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.sum();
   }

   public boolean forEachDescending(TShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.forEachDescending(var1);
   }

   public void transformValues(TShortFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.transformValues(var1);
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessShortList(this.list) : this;
   }
}
