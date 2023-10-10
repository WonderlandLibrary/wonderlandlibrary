package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TDoubleFunction;
import gnu.trove.list.TDoubleList;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TUnmodifiableDoubleList extends TUnmodifiableDoubleCollection implements TDoubleList {
   static final long serialVersionUID = -283967356065247728L;
   final TDoubleList list;

   public TUnmodifiableDoubleList(TDoubleList var1) {
      super(var1);
      this.list = var1;
   }

   public boolean equals(Object var1) {
      return var1 == this || this.list.equals(var1);
   }

   public int hashCode() {
      return this.list.hashCode();
   }

   public double get(int var1) {
      return this.list.get(var1);
   }

   public int indexOf(double var1) {
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(double var1) {
      return this.list.lastIndexOf(var1);
   }

   public double[] toArray(int var1, int var2) {
      return this.list.toArray(var1, var2);
   }

   public double[] toArray(double[] var1, int var2, int var3) {
      return this.list.toArray(var1, var2, var3);
   }

   public double[] toArray(double[] var1, int var2, int var3, int var4) {
      return this.list.toArray(var1, var2, var3, var4);
   }

   public boolean forEachDescending(TDoubleProcedure var1) {
      return this.list.forEachDescending(var1);
   }

   public int binarySearch(double var1) {
      return this.list.binarySearch(var1);
   }

   public int binarySearch(double var1, int var3, int var4) {
      return this.list.binarySearch(var1, var3, var4);
   }

   public int indexOf(int var1, double var2) {
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, double var2) {
      return this.list.lastIndexOf(var1, var2);
   }

   public TDoubleList grep(TDoubleProcedure var1) {
      return this.list.grep(var1);
   }

   public TDoubleList inverseGrep(TDoubleProcedure var1) {
      return this.list.inverseGrep(var1);
   }

   public double max() {
      return this.list.max();
   }

   public double min() {
      return this.list.min();
   }

   public double sum() {
      return this.list.sum();
   }

   public TDoubleList subList(int var1, int var2) {
      return new TUnmodifiableDoubleList(this.list.subList(var1, var2));
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessDoubleList(this.list) : this;
   }

   public void add(double[] var1) {
      throw new UnsupportedOperationException();
   }

   public void add(double[] var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   public double removeAt(int var1) {
      throw new UnsupportedOperationException();
   }

   public void remove(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, double[] var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, double[] var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public double set(int var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void set(int var1, double[] var2) {
      throw new UnsupportedOperationException();
   }

   public void set(int var1, double[] var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public double replace(int var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public void reverse() {
      throw new UnsupportedOperationException();
   }

   public void reverse(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void shuffle(Random var1) {
      throw new UnsupportedOperationException();
   }

   public void sort() {
      throw new UnsupportedOperationException();
   }

   public void sort(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void fill(double var1) {
      throw new UnsupportedOperationException();
   }

   public void fill(int var1, int var2, double var3) {
      throw new UnsupportedOperationException();
   }
}
