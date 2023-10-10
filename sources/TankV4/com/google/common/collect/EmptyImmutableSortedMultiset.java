package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class EmptyImmutableSortedMultiset extends ImmutableSortedMultiset {
   private final ImmutableSortedSet elementSet;

   EmptyImmutableSortedMultiset(Comparator var1) {
      this.elementSet = ImmutableSortedSet.emptySet(var1);
   }

   public Multiset.Entry firstEntry() {
      return null;
   }

   public Multiset.Entry lastEntry() {
      return null;
   }

   public int count(@Nullable Object var1) {
      return 0;
   }

   public boolean containsAll(Collection var1) {
      return var1.isEmpty();
   }

   public int size() {
      return 0;
   }

   public ImmutableSortedSet elementSet() {
      return this.elementSet;
   }

   Multiset.Entry getEntry(int var1) {
      throw new AssertionError("should never be called");
   }

   public ImmutableSortedMultiset headMultiset(Object var1, BoundType var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      return this;
   }

   public ImmutableSortedMultiset tailMultiset(Object var1, BoundType var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      return this;
   }

   public UnmodifiableIterator iterator() {
      return Iterators.emptyIterator();
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof Multiset) {
         Multiset var2 = (Multiset)var1;
         return var2.isEmpty();
      } else {
         return false;
      }
   }

   boolean isPartialView() {
      return false;
   }

   int copyIntoArray(Object[] var1, int var2) {
      return var2;
   }

   public ImmutableList asList() {
      return ImmutableList.of();
   }

   public SortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.tailMultiset(var1, var2);
   }

   public SortedMultiset headMultiset(Object var1, BoundType var2) {
      return this.headMultiset(var1, var2);
   }

   public Iterator iterator() {
      return this.iterator();
   }

   public NavigableSet elementSet() {
      return this.elementSet();
   }

   public SortedSet elementSet() {
      return this.elementSet();
   }

   public Set elementSet() {
      return this.elementSet();
   }
}
