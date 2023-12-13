package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TCharFunction;
import gnu.trove.list.TCharList;
import gnu.trove.procedure.TCharProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TUnmodifiableCharList extends TUnmodifiableCharCollection implements TCharList {
   static final long serialVersionUID = -283967356065247728L;
   final TCharList list;

   public TUnmodifiableCharList(TCharList var1) {
      super(var1);
      this.list = var1;
   }

   public boolean equals(Object var1) {
      return var1 == this || this.list.equals(var1);
   }

   public int hashCode() {
      return this.list.hashCode();
   }

   public char get(int var1) {
      return this.list.get(var1);
   }

   public int indexOf(char var1) {
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(char var1) {
      return this.list.lastIndexOf(var1);
   }

   public char[] toArray(int var1, int var2) {
      return this.list.toArray(var1, var2);
   }

   public char[] toArray(char[] var1, int var2, int var3) {
      return this.list.toArray(var1, var2, var3);
   }

   public char[] toArray(char[] var1, int var2, int var3, int var4) {
      return this.list.toArray(var1, var2, var3, var4);
   }

   public boolean forEachDescending(TCharProcedure var1) {
      return this.list.forEachDescending(var1);
   }

   public int binarySearch(char var1) {
      return this.list.binarySearch(var1);
   }

   public int binarySearch(char var1, int var2, int var3) {
      return this.list.binarySearch(var1, var2, var3);
   }

   public int indexOf(int var1, char var2) {
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, char var2) {
      return this.list.lastIndexOf(var1, var2);
   }

   public TCharList grep(TCharProcedure var1) {
      return this.list.grep(var1);
   }

   public TCharList inverseGrep(TCharProcedure var1) {
      return this.list.inverseGrep(var1);
   }

   public char max() {
      return this.list.max();
   }

   public char min() {
      return this.list.min();
   }

   public char sum() {
      return this.list.sum();
   }

   public TCharList subList(int var1, int var2) {
      return new TUnmodifiableCharList(this.list.subList(var1, var2));
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessCharList(this.list) : this;
   }

   public void add(char[] var1) {
      throw new UnsupportedOperationException();
   }

   public void add(char[] var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   public char removeAt(int var1) {
      throw new UnsupportedOperationException();
   }

   public void remove(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, char[] var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, char[] var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public char set(int var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void set(int var1, char[] var2) {
      throw new UnsupportedOperationException();
   }

   public void set(int var1, char[] var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public char replace(int var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TCharFunction var1) {
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

   public void fill(char var1) {
      throw new UnsupportedOperationException();
   }

   public void fill(int var1, int var2, char var3) {
      throw new UnsupportedOperationException();
   }
}
