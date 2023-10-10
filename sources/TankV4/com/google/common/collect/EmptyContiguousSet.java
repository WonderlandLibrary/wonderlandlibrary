package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class EmptyContiguousSet extends ContiguousSet {
   EmptyContiguousSet(DiscreteDomain var1) {
      super(var1);
   }

   public Comparable first() {
      throw new NoSuchElementException();
   }

   public Comparable last() {
      throw new NoSuchElementException();
   }

   public int size() {
      return 0;
   }

   public ContiguousSet intersection(ContiguousSet var1) {
      return this;
   }

   public Range range() {
      throw new NoSuchElementException();
   }

   public Range range(BoundType var1, BoundType var2) {
      throw new NoSuchElementException();
   }

   ContiguousSet headSetImpl(Comparable var1, boolean var2) {
      return this;
   }

   ContiguousSet subSetImpl(Comparable var1, boolean var2, Comparable var3, boolean var4) {
      return this;
   }

   ContiguousSet tailSetImpl(Comparable var1, boolean var2) {
      return this;
   }

   @GwtIncompatible("not used by GWT emulation")
   int indexOf(Object var1) {
      return -1;
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

   public boolean isEmpty() {
      return true;
   }

   public ImmutableList asList() {
      return ImmutableList.of();
   }

   public String toString() {
      return "[]";
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

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new EmptyContiguousSet.SerializedForm(this.domain);
   }

   @GwtIncompatible("NavigableSet")
   ImmutableSortedSet createDescendingSet() {
      return new EmptyImmutableSortedSet(Ordering.natural().reverse());
   }

   public Object last() {
      return this.last();
   }

   public Object first() {
      return this.first();
   }

   ImmutableSortedSet tailSetImpl(Object var1, boolean var2) {
      return this.tailSetImpl((Comparable)var1, var2);
   }

   ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4) {
      return this.subSetImpl((Comparable)var1, var2, (Comparable)var3, var4);
   }

   ImmutableSortedSet headSetImpl(Object var1, boolean var2) {
      return this.headSetImpl((Comparable)var1, var2);
   }

   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   public Iterator iterator() {
      return this.iterator();
   }

   @GwtIncompatible("serialization")
   private static final class SerializedForm implements Serializable {
      private final DiscreteDomain domain;
      private static final long serialVersionUID = 0L;

      private SerializedForm(DiscreteDomain var1) {
         this.domain = var1;
      }

      private Object readResolve() {
         return new EmptyContiguousSet(this.domain);
      }

      SerializedForm(DiscreteDomain var1, Object var2) {
         this(var1);
      }
   }
}
