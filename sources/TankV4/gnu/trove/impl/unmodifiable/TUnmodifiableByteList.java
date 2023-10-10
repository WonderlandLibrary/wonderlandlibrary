package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TByteFunction;
import gnu.trove.list.TByteList;
import gnu.trove.procedure.TByteProcedure;
import java.util.Random;
import java.util.RandomAccess;

public class TUnmodifiableByteList extends TUnmodifiableByteCollection implements TByteList {
   static final long serialVersionUID = -283967356065247728L;
   final TByteList list;

   public TUnmodifiableByteList(TByteList var1) {
      super(var1);
      this.list = var1;
   }

   public boolean equals(Object var1) {
      return var1 == this || this.list.equals(var1);
   }

   public int hashCode() {
      return this.list.hashCode();
   }

   public byte get(int var1) {
      return this.list.get(var1);
   }

   public int indexOf(byte var1) {
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(byte var1) {
      return this.list.lastIndexOf(var1);
   }

   public byte[] toArray(int var1, int var2) {
      return this.list.toArray(var1, var2);
   }

   public byte[] toArray(byte[] var1, int var2, int var3) {
      return this.list.toArray(var1, var2, var3);
   }

   public byte[] toArray(byte[] var1, int var2, int var3, int var4) {
      return this.list.toArray(var1, var2, var3, var4);
   }

   public boolean forEachDescending(TByteProcedure var1) {
      return this.list.forEachDescending(var1);
   }

   public int binarySearch(byte var1) {
      return this.list.binarySearch(var1);
   }

   public int binarySearch(byte var1, int var2, int var3) {
      return this.list.binarySearch(var1, var2, var3);
   }

   public int indexOf(int var1, byte var2) {
      return this.list.indexOf(var1, var2);
   }

   public int lastIndexOf(int var1, byte var2) {
      return this.list.lastIndexOf(var1, var2);
   }

   public TByteList grep(TByteProcedure var1) {
      return this.list.grep(var1);
   }

   public TByteList inverseGrep(TByteProcedure var1) {
      return this.list.inverseGrep(var1);
   }

   public byte max() {
      return this.list.max();
   }

   public byte min() {
      return this.list.min();
   }

   public byte sum() {
      return this.list.sum();
   }

   public TByteList subList(int var1, int var2) {
      return new TUnmodifiableByteList(this.list.subList(var1, var2));
   }

   private Object readResolve() {
      return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessByteList(this.list) : this;
   }

   public void add(byte[] var1) {
      throw new UnsupportedOperationException();
   }

   public void add(byte[] var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   public byte removeAt(int var1) {
      throw new UnsupportedOperationException();
   }

   public void remove(int var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, byte[] var2) {
      throw new UnsupportedOperationException();
   }

   public void insert(int var1, byte[] var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public byte set(int var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void set(int var1, byte[] var2) {
      throw new UnsupportedOperationException();
   }

   public void set(int var1, byte[] var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public byte replace(int var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TByteFunction var1) {
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

   public void fill(byte var1) {
      throw new UnsupportedOperationException();
   }

   public void fill(int var1, int var2, byte var3) {
      throw new UnsupportedOperationException();
   }
}
