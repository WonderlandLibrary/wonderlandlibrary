package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class RegularImmutableSortedSet extends ImmutableSortedSet {
   private final transient ImmutableList elements;

   RegularImmutableSortedSet(ImmutableList var1, Comparator var2) {
      super(var2);
      this.elements = var1;
      Preconditions.checkArgument(!var1.isEmpty());
   }

   public UnmodifiableIterator iterator() {
      return this.elements.iterator();
   }

   @GwtIncompatible("NavigableSet")
   public UnmodifiableIterator descendingIterator() {
      return this.elements.reverse().iterator();
   }

   public boolean isEmpty() {
      return false;
   }

   public int size() {
      return this.elements.size();
   }

   public boolean contains(Object var1) {
      try {
         return var1 != null && this.unsafeBinarySearch(var1) >= 0;
      } catch (ClassCastException var3) {
         return false;
      }
   }

   public boolean containsAll(Collection var1) {
      if (var1 instanceof Multiset) {
         var1 = ((Multiset)var1).elementSet();
      }

      if (SortedIterables.hasSameComparator(this.comparator(), (Iterable)var1) && ((Collection)var1).size() > 1) {
         PeekingIterator var2 = Iterators.peekingIterator((Iterator)this.iterator());
         Iterator var3 = ((Collection)var1).iterator();
         Object var4 = var3.next();

         try {
            while(var2.hasNext()) {
               int var5 = this.unsafeCompare(var2.peek(), var4);
               if (var5 < 0) {
                  var2.next();
               } else if (var5 == 0) {
                  if (!var3.hasNext()) {
                     return true;
                  }

                  var4 = var3.next();
               } else if (var5 > 0) {
                  return false;
               }
            }

            return false;
         } catch (NullPointerException var6) {
            return false;
         } catch (ClassCastException var7) {
            return false;
         }
      } else {
         return super.containsAll((Collection)var1);
      }
   }

   private int unsafeBinarySearch(Object var1) throws ClassCastException {
      return Collections.binarySearch(this.elements, var1, this.unsafeComparator());
   }

   boolean isPartialView() {
      return this.elements.isPartialView();
   }

   int copyIntoArray(Object[] var1, int var2) {
      return this.elements.copyIntoArray(var1, var2);
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Set)) {
         return false;
      } else {
         Set var2 = (Set)var1;
         if (this.size() != var2.size()) {
            return false;
         } else if (SortedIterables.hasSameComparator(this.comparator, var2)) {
            Iterator var3 = var2.iterator();

            try {
               UnmodifiableIterator var4 = this.iterator();

               Object var5;
               Object var6;
               do {
                  if (!var4.hasNext()) {
                     return true;
                  }

                  var5 = var4.next();
                  var6 = var3.next();
               } while(var6 != null && this.unsafeCompare(var5, var6) == 0);

               return false;
            } catch (ClassCastException var7) {
               return false;
            } catch (NoSuchElementException var8) {
               return false;
            }
         } else {
            return this.containsAll(var2);
         }
      }
   }

   public Object first() {
      return this.elements.get(0);
   }

   public Object last() {
      return this.elements.get(this.size() - 1);
   }

   public Object lower(Object var1) {
      int var2 = this.headIndex(var1, false) - 1;
      return var2 == -1 ? null : this.elements.get(var2);
   }

   public Object floor(Object var1) {
      int var2 = this.headIndex(var1, true) - 1;
      return var2 == -1 ? null : this.elements.get(var2);
   }

   public Object ceiling(Object var1) {
      int var2 = this.tailIndex(var1, true);
      return var2 == this.size() ? null : this.elements.get(var2);
   }

   public Object higher(Object var1) {
      int var2 = this.tailIndex(var1, false);
      return var2 == this.size() ? null : this.elements.get(var2);
   }

   ImmutableSortedSet headSetImpl(Object var1, boolean var2) {
      return this.getSubSet(0, this.headIndex(var1, var2));
   }

   int headIndex(Object var1, boolean var2) {
      return SortedLists.binarySearch(this.elements, (Object)Preconditions.checkNotNull(var1), (Comparator)this.comparator(), var2 ? SortedLists.KeyPresentBehavior.FIRST_AFTER : SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
   }

   ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4) {
      return this.tailSetImpl(var1, var2).headSetImpl(var3, var4);
   }

   ImmutableSortedSet tailSetImpl(Object var1, boolean var2) {
      return this.getSubSet(this.tailIndex(var1, var2), this.size());
   }

   int tailIndex(Object var1, boolean var2) {
      return SortedLists.binarySearch(this.elements, (Object)Preconditions.checkNotNull(var1), (Comparator)this.comparator(), var2 ? SortedLists.KeyPresentBehavior.FIRST_PRESENT : SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
   }

   Comparator unsafeComparator() {
      return this.comparator;
   }

   ImmutableSortedSet getSubSet(int var1, int var2) {
      if (var1 == 0 && var2 == this.size()) {
         return this;
      } else {
         return (ImmutableSortedSet)(var1 < var2 ? new RegularImmutableSortedSet(this.elements.subList(var1, var2), this.comparator) : emptySet(this.comparator));
      }
   }

   int indexOf(@Nullable Object var1) {
      if (var1 == null) {
         return -1;
      } else {
         int var2;
         try {
            var2 = SortedLists.binarySearch(this.elements, (Object)var1, (Comparator)this.unsafeComparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
         } catch (ClassCastException var4) {
            return -1;
         }

         return var2 >= 0 ? var2 : -1;
      }
   }

   ImmutableList createAsList() {
      return new ImmutableSortedAsList(this, this.elements);
   }

   ImmutableSortedSet createDescendingSet() {
      return new RegularImmutableSortedSet(this.elements.reverse(), Ordering.from(this.comparator).reverse());
   }

   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   public Iterator iterator() {
      return this.iterator();
   }
}
