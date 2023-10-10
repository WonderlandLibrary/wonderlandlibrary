package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class SingletonImmutableSet extends ImmutableSet {
   final transient Object element;
   private transient int cachedHashCode;

   SingletonImmutableSet(Object var1) {
      this.element = Preconditions.checkNotNull(var1);
   }

   SingletonImmutableSet(Object var1, int var2) {
      this.element = var1;
      this.cachedHashCode = var2;
   }

   public int size() {
      return 1;
   }

   public boolean isEmpty() {
      return false;
   }

   public boolean contains(Object var1) {
      return this.element.equals(var1);
   }

   public UnmodifiableIterator iterator() {
      return Iterators.singletonIterator(this.element);
   }

   boolean isPartialView() {
      return false;
   }

   int copyIntoArray(Object[] var1, int var2) {
      var1[var2] = this.element;
      return var2 + 1;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Set)) {
         return false;
      } else {
         Set var2 = (Set)var1;
         return var2.size() == 1 && this.element.equals(var2.iterator().next());
      }
   }

   public final int hashCode() {
      int var1 = this.cachedHashCode;
      if (var1 == 0) {
         this.cachedHashCode = var1 = this.element.hashCode();
      }

      return var1;
   }

   boolean isHashCodeFast() {
      return this.cachedHashCode != 0;
   }

   public String toString() {
      String var1 = this.element.toString();
      return (new StringBuilder(var1.length() + 2)).append('[').append(var1).append(']').toString();
   }

   public Iterator iterator() {
      return this.iterator();
   }
}
