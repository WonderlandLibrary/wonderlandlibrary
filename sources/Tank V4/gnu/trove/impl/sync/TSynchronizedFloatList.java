package gnu.trove.impl.sync;

import gnu.trove.function.TFloatFunction;
import gnu.trove.list.TFloatList;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TSynchronizedFloatList extends TSynchronizedFloatCollection implements TFloatList {
   static final long serialVersionUID = -7754090372962971524L;
   final TFloatList list;

   public TSynchronizedFloatList(TFloatList var1) {
      super(var1);
      this.list = var1;
   }

   public TSynchronizedFloatList(TFloatList var1, Object var2) {
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

   public float get(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.get(var1);
   }

   public float set(int var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.set(var1, var2);
   }

   public void set(int var1, float[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.set(var1, var2);
   }

   public void set(int var1, float[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.set(var1, var2, var3, var4);
   }

   public float replace(int var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.replace(var1, var2);
   }

   public void remove(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.remove(var1, var2);
   }

   public float removeAt(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.removeAt(var1);
   }

   public void add(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.add(var1);
   }

   public void add(float[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      this.list.add(var1, var2, var3);
   }

   public void insert(int var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, float[] var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      this.list.insert(var1, var2);
   }

   public void insert(int var1, float[] var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      this.list.insert(var1, var2, var3, var4);
   }

   public int indexOf(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.lastIndexOf(var1);
   }

   public TFloatList subList(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return new TSynchronizedFloatList(this.list.subList(var1, var2), this.mutex);
   }

   public float[] toArray(int var1, int var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.toArray(var1, var2);
   }

   public float[] toArray(float[] var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.toArray(var1, var2, var3);
   }

   public float[] toArray(float[] var1, int var2, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.list.toArray(var1, var2, var3, var4);
   }

   public int indexOf(int var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.list.lastIndexOf(var1, var2);
   }

   public void fill(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.fill(var1);
   }

   public void fill(int var1, int var2, float var3) {
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

   public int binarySearch(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.binarySearch(var1);
   }

   public int binarySearch(float var1, int var2, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.list.binarySearch(var1, var2, var3);
   }

   public TFloatList grep(TFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.grep(var1);
   }

   public TFloatList inverseGrep(TFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.inverseGrep(var1);
   }

   public float max() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.max();
   }

   public float min() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.min();
   }

   public float sum() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.list.sum();
   }

   public boolean forEachDescending(TFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.list.forEachDescending(var1);
   }

   public void transformValues(TFloatFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.list.transformValues(var1);
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessFloatList(this.list) : this;
   }
}
