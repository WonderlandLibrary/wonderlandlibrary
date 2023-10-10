package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import java.util.NavigableSet;
import javax.annotation.Nullable;

class DescendingImmutableSortedSet extends ImmutableSortedSet {
   private final ImmutableSortedSet forward;

   DescendingImmutableSortedSet(ImmutableSortedSet var1) {
      super(Ordering.from(var1.comparator()).reverse());
      this.forward = var1;
   }

   public int size() {
      return this.forward.size();
   }

   public UnmodifiableIterator iterator() {
      return this.forward.descendingIterator();
   }

   ImmutableSortedSet headSetImpl(Object var1, boolean var2) {
      return this.forward.tailSet(var1, var2).descendingSet();
   }

   ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4) {
      return this.forward.subSet(var3, var4, var1, var2).descendingSet();
   }

   ImmutableSortedSet tailSetImpl(Object var1, boolean var2) {
      return this.forward.headSet(var1, var2).descendingSet();
   }

   @GwtIncompatible("NavigableSet")
   public ImmutableSortedSet descendingSet() {
      return this.forward;
   }

   @GwtIncompatible("NavigableSet")
   public UnmodifiableIterator descendingIterator() {
      return this.forward.iterator();
   }

   @GwtIncompatible("NavigableSet")
   ImmutableSortedSet createDescendingSet() {
      throw new AssertionError("should never be called");
   }

   public Object lower(Object var1) {
      return this.forward.higher(var1);
   }

   public Object floor(Object var1) {
      return this.forward.ceiling(var1);
   }

   public Object ceiling(Object var1) {
      return this.forward.floor(var1);
   }

   public Object higher(Object var1) {
      return this.forward.lower(var1);
   }

   int indexOf(@Nullable Object var1) {
      int var2 = this.forward.indexOf(var1);
      return var2 == -1 ? var2 : this.size() - 1 - var2;
   }

   boolean isPartialView() {
      return this.forward.isPartialView();
   }

   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   public NavigableSet descendingSet() {
      return this.descendingSet();
   }

   public Iterator iterator() {
      return this.iterator();
   }
}
