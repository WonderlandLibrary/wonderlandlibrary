package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.ListIterator;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
class RegularImmutableList extends ImmutableList {
   private final transient int offset;
   private final transient int size;
   private final transient Object[] array;

   RegularImmutableList(Object[] var1, int var2, int var3) {
      this.offset = var2;
      this.size = var3;
      this.array = var1;
   }

   RegularImmutableList(Object[] var1) {
      this(var1, 0, var1.length);
   }

   public int size() {
      return this.size;
   }

   boolean isPartialView() {
      return this.size != this.array.length;
   }

   int copyIntoArray(Object[] var1, int var2) {
      System.arraycopy(this.array, this.offset, var1, var2, this.size);
      return var2 + this.size;
   }

   public Object get(int var1) {
      Preconditions.checkElementIndex(var1, this.size);
      return this.array[var1 + this.offset];
   }

   public int indexOf(@Nullable Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < this.size; ++var2) {
            if (this.array[this.offset + var2].equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   public int lastIndexOf(@Nullable Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = this.size - 1; var2 >= 0; --var2) {
            if (this.array[this.offset + var2].equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   ImmutableList subListUnchecked(int var1, int var2) {
      return new RegularImmutableList(this.array, this.offset + var1, var2 - var1);
   }

   public UnmodifiableListIterator listIterator(int var1) {
      return Iterators.forArray(this.array, this.offset, this.size, var1);
   }

   public ListIterator listIterator(int var1) {
      return this.listIterator(var1);
   }
}
