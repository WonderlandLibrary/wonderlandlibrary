package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
class EmptyImmutableSortedSet extends ImmutableSortedSet {
   EmptyImmutableSortedSet(Comparator var1) {
      super(var1);
   }

   public int size() {
      return 0;
   }

   public boolean isEmpty() {
      return true;
   }

   public boolean contains(@Nullable Object var1) {
      return false;
   }

   public boolean containsAll(Collection var1) {
      return var1.isEmpty();
   }

   public UnmodifiableIterator iterator() {
      return Iterators.emptyIterator();
   }

   @GwtIncompatible("NavigableSet")
   public UnmodifiableIterator descendingIterator() {
      return Iterators.emptyIterator();
   }

   boolean isPartialView() {
      return false;
   }

   public ImmutableList asList() {
      return ImmutableList.of();
   }

   int copyIntoArray(Object[] var1, int var2) {
      return var2;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof Set) {
         Set var2 = (Set)var1;
         return var2.isEmpty();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return 0;
   }

   public String toString() {
      return "[]";
   }

   public Object first() {
      throw new NoSuchElementException();
   }

   public Object last() {
      throw new NoSuchElementException();
   }

   ImmutableSortedSet headSetImpl(Object var1, boolean var2) {
      return this;
   }

   ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4) {
      return this;
   }

   ImmutableSortedSet tailSetImpl(Object var1, boolean var2) {
      return this;
   }

   int indexOf(@Nullable Object var1) {
      return -1;
   }

   ImmutableSortedSet createDescendingSet() {
      return new EmptyImmutableSortedSet(Ordering.from(this.comparator).reverse());
   }

   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   public Iterator iterator() {
      return this.iterator();
   }
}
