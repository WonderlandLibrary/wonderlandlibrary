package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class RegularImmutableSortedMultiset extends ImmutableSortedMultiset {
   private final transient RegularImmutableSortedSet elementSet;
   private final transient int[] counts;
   private final transient long[] cumulativeCounts;
   private final transient int offset;
   private final transient int length;

   RegularImmutableSortedMultiset(RegularImmutableSortedSet var1, int[] var2, long[] var3, int var4, int var5) {
      this.elementSet = var1;
      this.counts = var2;
      this.cumulativeCounts = var3;
      this.offset = var4;
      this.length = var5;
   }

   Multiset.Entry getEntry(int var1) {
      return Multisets.immutableEntry(this.elementSet.asList().get(var1), this.counts[this.offset + var1]);
   }

   public Multiset.Entry firstEntry() {
      return this.getEntry(0);
   }

   public Multiset.Entry lastEntry() {
      return this.getEntry(this.length - 1);
   }

   public int count(@Nullable Object var1) {
      int var2 = this.elementSet.indexOf(var1);
      return var2 == -1 ? 0 : this.counts[var2 + this.offset];
   }

   public int size() {
      long var1 = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
      return Ints.saturatedCast(var1);
   }

   public ImmutableSortedSet elementSet() {
      return this.elementSet;
   }

   public ImmutableSortedMultiset headMultiset(Object var1, BoundType var2) {
      return this.getSubMultiset(0, this.elementSet.headIndex(var1, Preconditions.checkNotNull(var2) == BoundType.CLOSED));
   }

   public ImmutableSortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.getSubMultiset(this.elementSet.tailIndex(var1, Preconditions.checkNotNull(var2) == BoundType.CLOSED), this.length);
   }

   ImmutableSortedMultiset getSubMultiset(int var1, int var2) {
      Preconditions.checkPositionIndexes(var1, var2, this.length);
      if (var1 == var2) {
         return emptyMultiset(this.comparator());
      } else if (var1 == 0 && var2 == this.length) {
         return this;
      } else {
         RegularImmutableSortedSet var3 = (RegularImmutableSortedSet)this.elementSet.getSubSet(var1, var2);
         return new RegularImmutableSortedMultiset(var3, this.counts, this.cumulativeCounts, this.offset + var1, var2 - var1);
      }
   }

   boolean isPartialView() {
      return this.offset > 0 || this.length < this.counts.length;
   }

   public SortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.tailMultiset(var1, var2);
   }

   public SortedMultiset headMultiset(Object var1, BoundType var2) {
      return this.headMultiset(var1, var2);
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
