package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableSortedSet extends ImmutableSortedSetFauxverideShim implements NavigableSet, SortedIterable {
   private static final Comparator NATURAL_ORDER = Ordering.natural();
   private static final ImmutableSortedSet NATURAL_EMPTY_SET;
   final transient Comparator comparator;
   @GwtIncompatible("NavigableSet")
   transient ImmutableSortedSet descendingSet;

   private static ImmutableSortedSet emptySet() {
      return NATURAL_EMPTY_SET;
   }

   static ImmutableSortedSet emptySet(Comparator var0) {
      return (ImmutableSortedSet)(NATURAL_ORDER.equals(var0) ? emptySet() : new EmptyImmutableSortedSet(var0));
   }

   public static ImmutableSortedSet of() {
      return emptySet();
   }

   public static ImmutableSortedSet of(Comparable var0) {
      return new RegularImmutableSortedSet(ImmutableList.of(var0), Ordering.natural());
   }

   public static ImmutableSortedSet of(Comparable var0, Comparable var1) {
      return construct(Ordering.natural(), 2, var0, var1);
   }

   public static ImmutableSortedSet of(Comparable var0, Comparable var1, Comparable var2) {
      return construct(Ordering.natural(), 3, var0, var1, var2);
   }

   public static ImmutableSortedSet of(Comparable var0, Comparable var1, Comparable var2, Comparable var3) {
      return construct(Ordering.natural(), 4, var0, var1, var2, var3);
   }

   public static ImmutableSortedSet of(Comparable var0, Comparable var1, Comparable var2, Comparable var3, Comparable var4) {
      return construct(Ordering.natural(), 5, var0, var1, var2, var3, var4);
   }

   public static ImmutableSortedSet of(Comparable var0, Comparable var1, Comparable var2, Comparable var3, Comparable var4, Comparable var5, Comparable... var6) {
      Comparable[] var7 = new Comparable[6 + var6.length];
      var7[0] = var0;
      var7[1] = var1;
      var7[2] = var2;
      var7[3] = var3;
      var7[4] = var4;
      var7[5] = var5;
      System.arraycopy(var6, 0, var7, 6, var6.length);
      return construct(Ordering.natural(), var7.length, (Comparable[])var7);
   }

   public static ImmutableSortedSet copyOf(Comparable[] var0) {
      return construct(Ordering.natural(), var0.length, (Object[])var0.clone());
   }

   public static ImmutableSortedSet copyOf(Iterable var0) {
      Ordering var1 = Ordering.natural();
      return copyOf(var1, (Iterable)var0);
   }

   public static ImmutableSortedSet copyOf(Collection var0) {
      Ordering var1 = Ordering.natural();
      return copyOf(var1, (Collection)var0);
   }

   public static ImmutableSortedSet copyOf(Iterator var0) {
      Ordering var1 = Ordering.natural();
      return copyOf(var1, (Iterator)var0);
   }

   public static ImmutableSortedSet copyOf(Comparator var0, Iterator var1) {
      return (new ImmutableSortedSet.Builder(var0)).addAll(var1).build();
   }

   public static ImmutableSortedSet copyOf(Comparator var0, Iterable var1) {
      Preconditions.checkNotNull(var0);
      boolean var2 = SortedIterables.hasSameComparator(var0, var1);
      if (var2 && var1 instanceof ImmutableSortedSet) {
         ImmutableSortedSet var3 = (ImmutableSortedSet)var1;
         if (!var3.isPartialView()) {
            return var3;
         }
      }

      Object[] var4 = (Object[])Iterables.toArray(var1);
      return construct(var0, var4.length, var4);
   }

   public static ImmutableSortedSet copyOf(Comparator var0, Collection var1) {
      return copyOf(var0, (Iterable)var1);
   }

   public static ImmutableSortedSet copyOfSorted(SortedSet var0) {
      Comparator var1 = SortedIterables.comparator(var0);
      ImmutableList var2 = ImmutableList.copyOf((Collection)var0);
      return (ImmutableSortedSet)(var2.isEmpty() ? emptySet(var1) : new RegularImmutableSortedSet(var2, var1));
   }

   static ImmutableSortedSet construct(Comparator var0, int var1, Object... var2) {
      if (var1 == 0) {
         return emptySet(var0);
      } else {
         ObjectArrays.checkElementsNotNull(var2, var1);
         Arrays.sort(var2, 0, var1, var0);
         int var3 = 1;

         for(int var4 = 1; var4 < var1; ++var4) {
            Object var5 = var2[var4];
            Object var6 = var2[var3 - 1];
            if (var0.compare(var5, var6) != 0) {
               var2[var3++] = var5;
            }
         }

         Arrays.fill(var2, var3, var1, (Object)null);
         return new RegularImmutableSortedSet(ImmutableList.asImmutableList(var2, var3), var0);
      }
   }

   public static ImmutableSortedSet.Builder orderedBy(Comparator var0) {
      return new ImmutableSortedSet.Builder(var0);
   }

   public static ImmutableSortedSet.Builder reverseOrder() {
      return new ImmutableSortedSet.Builder(Ordering.natural().reverse());
   }

   public static ImmutableSortedSet.Builder naturalOrder() {
      return new ImmutableSortedSet.Builder(Ordering.natural());
   }

   int unsafeCompare(Object var1, Object var2) {
      return unsafeCompare(this.comparator, var1, var2);
   }

   static int unsafeCompare(Comparator var0, Object var1, Object var2) {
      return var0.compare(var1, var2);
   }

   ImmutableSortedSet(Comparator var1) {
      this.comparator = var1;
   }

   public Comparator comparator() {
      return this.comparator;
   }

   public abstract UnmodifiableIterator iterator();

   public ImmutableSortedSet headSet(Object var1) {
      return this.headSet(var1, false);
   }

   @GwtIncompatible("NavigableSet")
   public ImmutableSortedSet headSet(Object var1, boolean var2) {
      return this.headSetImpl(Preconditions.checkNotNull(var1), var2);
   }

   public ImmutableSortedSet subSet(Object var1, Object var2) {
      return this.subSet(var1, true, var2, false);
   }

   @GwtIncompatible("NavigableSet")
   public ImmutableSortedSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(this.comparator.compare(var1, var3) <= 0);
      return this.subSetImpl(var1, var2, var3, var4);
   }

   public ImmutableSortedSet tailSet(Object var1) {
      return this.tailSet(var1, true);
   }

   @GwtIncompatible("NavigableSet")
   public ImmutableSortedSet tailSet(Object var1, boolean var2) {
      return this.tailSetImpl(Preconditions.checkNotNull(var1), var2);
   }

   abstract ImmutableSortedSet headSetImpl(Object var1, boolean var2);

   abstract ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4);

   abstract ImmutableSortedSet tailSetImpl(Object var1, boolean var2);

   @GwtIncompatible("NavigableSet")
   public Object lower(Object var1) {
      return Iterators.getNext(this.headSet(var1, false).descendingIterator(), (Object)null);
   }

   @GwtIncompatible("NavigableSet")
   public Object floor(Object var1) {
      return Iterators.getNext(this.headSet(var1, true).descendingIterator(), (Object)null);
   }

   @GwtIncompatible("NavigableSet")
   public Object ceiling(Object var1) {
      return Iterables.getFirst(this.tailSet(var1, true), (Object)null);
   }

   @GwtIncompatible("NavigableSet")
   public Object higher(Object var1) {
      return Iterables.getFirst(this.tailSet(var1, false), (Object)null);
   }

   public Object first() {
      return this.iterator().next();
   }

   public Object last() {
      return this.descendingIterator().next();
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible("NavigableSet")
   public final Object pollFirst() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible("NavigableSet")
   public final Object pollLast() {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible("NavigableSet")
   public ImmutableSortedSet descendingSet() {
      ImmutableSortedSet var1 = this.descendingSet;
      if (var1 == null) {
         var1 = this.descendingSet = this.createDescendingSet();
         var1.descendingSet = this;
      }

      return var1;
   }

   @GwtIncompatible("NavigableSet")
   ImmutableSortedSet createDescendingSet() {
      return new DescendingImmutableSortedSet(this);
   }

   @GwtIncompatible("NavigableSet")
   public abstract UnmodifiableIterator descendingIterator();

   abstract int indexOf(@Nullable Object var1);

   private void readObject(ObjectInputStream var1) throws InvalidObjectException {
      throw new InvalidObjectException("Use SerializedForm");
   }

   Object writeReplace() {
      return new ImmutableSortedSet.SerializedForm(this.comparator, this.toArray());
   }

   public Iterator iterator() {
      return this.iterator();
   }

   public SortedSet tailSet(Object var1) {
      return this.tailSet(var1);
   }

   public SortedSet headSet(Object var1) {
      return this.headSet(var1);
   }

   public SortedSet subSet(Object var1, Object var2) {
      return this.subSet(var1, var2);
   }

   public NavigableSet tailSet(Object var1, boolean var2) {
      return this.tailSet(var1, var2);
   }

   public NavigableSet headSet(Object var1, boolean var2) {
      return this.headSet(var1, var2);
   }

   public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
      return this.subSet(var1, var2, var3, var4);
   }

   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   public NavigableSet descendingSet() {
      return this.descendingSet();
   }

   static {
      NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(NATURAL_ORDER);
   }

   private static class SerializedForm implements Serializable {
      final Comparator comparator;
      final Object[] elements;
      private static final long serialVersionUID = 0L;

      public SerializedForm(Comparator var1, Object[] var2) {
         this.comparator = var1;
         this.elements = var2;
      }

      Object readResolve() {
         return (new ImmutableSortedSet.Builder(this.comparator)).add((Object[])this.elements).build();
      }
   }

   public static final class Builder extends ImmutableSet.Builder {
      private final Comparator comparator;

      public Builder(Comparator var1) {
         this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      }

      public ImmutableSortedSet.Builder add(Object var1) {
         super.add(var1);
         return this;
      }

      public ImmutableSortedSet.Builder add(Object... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableSortedSet.Builder addAll(Iterable var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableSortedSet.Builder addAll(Iterator var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableSortedSet build() {
         Object[] var1 = (Object[])this.contents;
         ImmutableSortedSet var2 = ImmutableSortedSet.construct(this.comparator, this.size, var1);
         this.size = var2.size();
         return var2;
      }

      public ImmutableSet build() {
         return this.build();
      }

      public ImmutableSet.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      public ImmutableSet.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      public ImmutableSet.Builder add(Object[] var1) {
         return this.add(var1);
      }

      public ImmutableSet.Builder add(Object var1) {
         return this.add(var1);
      }

      public ImmutableCollection.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      public ImmutableCollection.Builder add(Object[] var1) {
         return this.add(var1);
      }

      public ImmutableCollection.ArrayBasedBuilder add(Object var1) {
         return this.add(var1);
      }

      public ImmutableCollection build() {
         return this.build();
      }

      public ImmutableCollection.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }
}
