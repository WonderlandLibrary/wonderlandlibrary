package com.sun.javafx.collections;

import java.util.Arrays;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableIntegerArray;

public class ObservableIntegerArrayImpl extends ObservableArrayBase implements ObservableIntegerArray {
   private static final int[] INITIAL = new int[0];
   private int[] array;
   private int size;
   private static final int MAX_ARRAY_SIZE = 2147483639;

   public ObservableIntegerArrayImpl() {
      this.array = INITIAL;
      this.size = 0;
   }

   public ObservableIntegerArrayImpl(int... var1) {
      this.array = INITIAL;
      this.size = 0;
      this.setAll(var1);
   }

   public ObservableIntegerArrayImpl(ObservableIntegerArray var1) {
      this.array = INITIAL;
      this.size = 0;
      this.setAll(var1);
   }

   public void clear() {
      this.resize(0);
   }

   public int size() {
      return this.size;
   }

   private void addAllInternal(ObservableIntegerArray var1, int var2, int var3) {
      this.growCapacity(var3);
      var1.copyTo(var2, this.array, this.size, var3);
      this.size += var3;
      this.fireChange(var3 != 0, this.size - var3, this.size);
   }

   private void addAllInternal(int[] var1, int var2, int var3) {
      this.growCapacity(var3);
      System.arraycopy(var1, var2, this.array, this.size, var3);
      this.size += var3;
      this.fireChange(var3 != 0, this.size - var3, this.size);
   }

   public void addAll(ObservableIntegerArray var1) {
      this.addAllInternal((ObservableIntegerArray)var1, 0, var1.size());
   }

   public void addAll(int... var1) {
      this.addAllInternal((int[])var1, 0, var1.length);
   }

   public void addAll(ObservableIntegerArray var1, int var2, int var3) {
      this.rangeCheck(var1, var2, var3);
      this.addAllInternal(var1, var2, var3);
   }

   public void addAll(int[] var1, int var2, int var3) {
      this.rangeCheck(var1, var2, var3);
      this.addAllInternal(var1, var2, var3);
   }

   private void setAllInternal(ObservableIntegerArray var1, int var2, int var3) {
      boolean var4 = this.size() != var3;
      if (var1 == this) {
         if (var2 == 0) {
            this.resize(var3);
         } else {
            System.arraycopy(this.array, var2, this.array, 0, var3);
            this.size = var3;
            this.fireChange(var4, 0, this.size);
         }
      } else {
         this.size = 0;
         this.ensureCapacity(var3);
         var1.copyTo(var2, (int[])this.array, 0, var3);
         this.size = var3;
         this.fireChange(var4, 0, this.size);
      }

   }

   private void setAllInternal(int[] var1, int var2, int var3) {
      boolean var4 = this.size() != var3;
      this.size = 0;
      this.ensureCapacity(var3);
      System.arraycopy(var1, var2, this.array, 0, var3);
      this.size = var3;
      this.fireChange(var4, 0, this.size);
   }

   public void setAll(ObservableIntegerArray var1) {
      this.setAllInternal((ObservableIntegerArray)var1, 0, var1.size());
   }

   public void setAll(ObservableIntegerArray var1, int var2, int var3) {
      this.rangeCheck(var1, var2, var3);
      this.setAllInternal(var1, var2, var3);
   }

   public void setAll(int[] var1, int var2, int var3) {
      this.rangeCheck(var1, var2, var3);
      this.setAllInternal(var1, var2, var3);
   }

   public void setAll(int[] var1) {
      this.setAllInternal((int[])var1, 0, var1.length);
   }

   public void set(int var1, int[] var2, int var3, int var4) {
      this.rangeCheck(var1 + var4);
      System.arraycopy(var2, var3, this.array, var1, var4);
      this.fireChange(false, var1, var1 + var4);
   }

   public void set(int var1, ObservableIntegerArray var2, int var3, int var4) {
      this.rangeCheck(var1 + var4);
      var2.copyTo(var3, this.array, var1, var4);
      this.fireChange(false, var1, var1 + var4);
   }

   public int[] toArray(int[] var1) {
      if (var1 == null || this.size() > var1.length) {
         var1 = new int[this.size()];
      }

      System.arraycopy(this.array, 0, var1, 0, this.size());
      return var1;
   }

   public int get(int var1) {
      this.rangeCheck(var1 + 1);
      return this.array[var1];
   }

   public void set(int var1, int var2) {
      this.rangeCheck(var1 + 1);
      this.array[var1] = var2;
      this.fireChange(false, var1, var1 + 1);
   }

   public int[] toArray(int var1, int[] var2, int var3) {
      this.rangeCheck(var1 + var3);
      if (var2 == null || var3 > var2.length) {
         var2 = new int[var3];
      }

      System.arraycopy(this.array, var1, var2, 0, var3);
      return var2;
   }

   public void copyTo(int var1, int[] var2, int var3, int var4) {
      this.rangeCheck(var1 + var4);
      System.arraycopy(this.array, var1, var2, var3, var4);
   }

   public void copyTo(int var1, ObservableIntegerArray var2, int var3, int var4) {
      this.rangeCheck(var1 + var4);
      var2.set(var3, this.array, var1, var4);
   }

   public void resize(int var1) {
      if (var1 < 0) {
         throw new NegativeArraySizeException("Can't resize to negative value: " + var1);
      } else {
         this.ensureCapacity(var1);
         int var2 = Math.min(this.size, var1);
         boolean var3 = this.size != var1;
         this.size = var1;
         Arrays.fill(this.array, var2, this.size, 0);
         this.fireChange(var3, var2, var1);
      }
   }

   private void growCapacity(int var1) {
      int var2 = this.size + var1;
      int var3 = this.array.length;
      if (var2 > this.array.length) {
         int var4 = var3 + (var3 >> 1);
         if (var4 < var2) {
            var4 = var2;
         }

         if (var4 > 2147483639) {
            var4 = hugeCapacity(var2);
         }

         this.ensureCapacity(var4);
      } else if (var1 > 0 && var2 < 0) {
         throw new OutOfMemoryError();
      }

   }

   public void ensureCapacity(int var1) {
      if (this.array.length < var1) {
         this.array = Arrays.copyOf(this.array, var1);
      }

   }

   private static int hugeCapacity(int var0) {
      if (var0 < 0) {
         throw new OutOfMemoryError();
      } else {
         return var0 > 2147483639 ? Integer.MAX_VALUE : 2147483639;
      }
   }

   public void trimToSize() {
      if (this.array.length != this.size) {
         int[] var1 = new int[this.size];
         System.arraycopy(this.array, 0, var1, 0, this.size);
         this.array = var1;
      }

   }

   private void rangeCheck(int var1) {
      if (var1 > this.size) {
         throw new ArrayIndexOutOfBoundsException(this.size);
      }
   }

   private void rangeCheck(ObservableIntegerArray var1, int var2, int var3) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 >= 0 && var2 + var3 <= var1.size()) {
         if (var3 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
         }
      } else {
         throw new ArrayIndexOutOfBoundsException(var1.size());
      }
   }

   private void rangeCheck(int[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 >= 0 && var2 + var3 <= var1.length) {
         if (var3 < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
         }
      } else {
         throw new ArrayIndexOutOfBoundsException(var1.length);
      }
   }

   public String toString() {
      if (this.array == null) {
         return "null";
      } else {
         int var1 = this.size() - 1;
         if (var1 == -1) {
            return "[]";
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append('[');
            int var3 = 0;

            while(true) {
               var2.append(this.array[var3]);
               if (var3 == var1) {
                  return var2.append(']').toString();
               }

               var2.append(", ");
               ++var3;
            }
         }
      }
   }
}
