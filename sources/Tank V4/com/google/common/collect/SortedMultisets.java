package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class SortedMultisets {
   private SortedMultisets() {
   }

   private static Object getElementOrThrow(Multiset.Entry var0) {
      if (var0 == null) {
         throw new NoSuchElementException();
      } else {
         return var0.getElement();
      }
   }

   private static Object getElementOrNull(@Nullable Multiset.Entry var0) {
      return var0 == null ? null : var0.getElement();
   }

   static Object access$000(Multiset.Entry var0) {
      return getElementOrThrow(var0);
   }

   static Object access$100(Multiset.Entry var0) {
      return getElementOrNull(var0);
   }

   @GwtIncompatible("Navigable")
   static class NavigableElementSet extends SortedMultisets.ElementSet implements NavigableSet {
      NavigableElementSet(SortedMultiset var1) {
         super(var1);
      }

      public Object lower(Object var1) {
         return SortedMultisets.access$100(this.multiset().headMultiset(var1, BoundType.OPEN).lastEntry());
      }

      public Object floor(Object var1) {
         return SortedMultisets.access$100(this.multiset().headMultiset(var1, BoundType.CLOSED).lastEntry());
      }

      public Object ceiling(Object var1) {
         return SortedMultisets.access$100(this.multiset().tailMultiset(var1, BoundType.CLOSED).firstEntry());
      }

      public Object higher(Object var1) {
         return SortedMultisets.access$100(this.multiset().tailMultiset(var1, BoundType.OPEN).firstEntry());
      }

      public NavigableSet descendingSet() {
         return new SortedMultisets.NavigableElementSet(this.multiset().descendingMultiset());
      }

      public Iterator descendingIterator() {
         return this.descendingSet().iterator();
      }

      public Object pollFirst() {
         return SortedMultisets.access$100(this.multiset().pollFirstEntry());
      }

      public Object pollLast() {
         return SortedMultisets.access$100(this.multiset().pollLastEntry());
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         return new SortedMultisets.NavigableElementSet(this.multiset().subMultiset(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         return new SortedMultisets.NavigableElementSet(this.multiset().headMultiset(var1, BoundType.forBoolean(var2)));
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         return new SortedMultisets.NavigableElementSet(this.multiset().tailMultiset(var1, BoundType.forBoolean(var2)));
      }
   }

   static class ElementSet extends Multisets.ElementSet implements SortedSet {
      private final SortedMultiset multiset;

      ElementSet(SortedMultiset var1) {
         this.multiset = var1;
      }

      final SortedMultiset multiset() {
         return this.multiset;
      }

      public Comparator comparator() {
         return this.multiset().comparator();
      }

      public SortedSet subSet(Object var1, Object var2) {
         return this.multiset().subMultiset(var1, BoundType.CLOSED, var2, BoundType.OPEN).elementSet();
      }

      public SortedSet headSet(Object var1) {
         return this.multiset().headMultiset(var1, BoundType.OPEN).elementSet();
      }

      public SortedSet tailSet(Object var1) {
         return this.multiset().tailMultiset(var1, BoundType.CLOSED).elementSet();
      }

      public Object first() {
         return SortedMultisets.access$000(this.multiset().firstEntry());
      }

      public Object last() {
         return SortedMultisets.access$000(this.multiset().lastEntry());
      }

      Multiset multiset() {
         return this.multiset();
      }
   }
}
