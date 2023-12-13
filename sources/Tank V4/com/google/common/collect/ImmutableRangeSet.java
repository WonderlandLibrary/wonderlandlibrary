package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@Beta
public final class ImmutableRangeSet extends AbstractRangeSet implements Serializable {
   private static final ImmutableRangeSet EMPTY = new ImmutableRangeSet(ImmutableList.of());
   private static final ImmutableRangeSet ALL = new ImmutableRangeSet(ImmutableList.of(Range.all()));
   private final transient ImmutableList ranges;
   private transient ImmutableRangeSet complement;

   public static ImmutableRangeSet of() {
      return EMPTY;
   }

   static ImmutableRangeSet all() {
      return ALL;
   }

   public static ImmutableRangeSet of(Range var0) {
      Preconditions.checkNotNull(var0);
      if (var0.isEmpty()) {
         return of();
      } else {
         return var0.equals(Range.all()) ? all() : new ImmutableRangeSet(ImmutableList.of(var0));
      }
   }

   public static ImmutableRangeSet copyOf(RangeSet var0) {
      Preconditions.checkNotNull(var0);
      if (var0.isEmpty()) {
         return of();
      } else if (var0.encloses(Range.all())) {
         return all();
      } else {
         if (var0 instanceof ImmutableRangeSet) {
            ImmutableRangeSet var1 = (ImmutableRangeSet)var0;
            if (!var1.isPartialView()) {
               return var1;
            }
         }

         return new ImmutableRangeSet(ImmutableList.copyOf((Collection)var0.asRanges()));
      }
   }

   ImmutableRangeSet(ImmutableList var1) {
      this.ranges = var1;
   }

   private ImmutableRangeSet(ImmutableList var1, ImmutableRangeSet var2) {
      this.ranges = var1;
      this.complement = var2;
   }

   public boolean encloses(Range var1) {
      int var2 = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), var1.lowerBound, Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      return var2 != -1 && ((Range)this.ranges.get(var2)).encloses(var1);
   }

   public Range rangeContaining(Comparable var1) {
      int var2 = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(var1), Ordering.natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if (var2 != -1) {
         Range var3 = (Range)this.ranges.get(var2);
         return var3.contains(var1) ? var3 : null;
      } else {
         return null;
      }
   }

   public Range span() {
      if (this.ranges.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         return Range.create(((Range)this.ranges.get(0)).lowerBound, ((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
      }
   }

   public boolean isEmpty() {
      return this.ranges.isEmpty();
   }

   public void add(Range var1) {
      throw new UnsupportedOperationException();
   }

   public void addAll(RangeSet var1) {
      throw new UnsupportedOperationException();
   }

   public void remove(Range var1) {
      throw new UnsupportedOperationException();
   }

   public void removeAll(RangeSet var1) {
      throw new UnsupportedOperationException();
   }

   public ImmutableSet asRanges() {
      return (ImmutableSet)(this.ranges.isEmpty() ? ImmutableSet.of() : new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING));
   }

   public ImmutableRangeSet complement() {
      ImmutableRangeSet var1 = this.complement;
      if (var1 != null) {
         return var1;
      } else if (this.ranges.isEmpty()) {
         return this.complement = all();
      } else if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.all())) {
         return this.complement = of();
      } else {
         ImmutableRangeSet.ComplementRanges var2 = new ImmutableRangeSet.ComplementRanges(this);
         var1 = this.complement = new ImmutableRangeSet(var2, this);
         return var1;
      }
   }

   private ImmutableList intersectRanges(Range var1) {
      if (!this.ranges.isEmpty() && !var1.isEmpty()) {
         if (var1.encloses(this.span())) {
            return this.ranges;
         } else {
            int var2;
            if (var1.hasLowerBound()) {
               var2 = SortedLists.binarySearch(this.ranges, (Function)Range.upperBoundFn(), (Comparable)var1.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
            } else {
               var2 = 0;
            }

            int var3;
            if (var1.hasUpperBound()) {
               var3 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)var1.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
            } else {
               var3 = this.ranges.size();
            }

            int var4 = var3 - var2;
            return var4 == 0 ? ImmutableList.of() : new ImmutableList(this, var4, var2, var1) {
               final int val$length;
               final int val$fromIndex;
               final Range val$range;
               final ImmutableRangeSet this$0;

               {
                  this.this$0 = var1;
                  this.val$length = var2;
                  this.val$fromIndex = var3;
                  this.val$range = var4;
               }

               public int size() {
                  return this.val$length;
               }

               public Range get(int var1) {
                  Preconditions.checkElementIndex(var1, this.val$length);
                  return var1 != 0 && var1 != this.val$length - 1 ? (Range)ImmutableRangeSet.access$000(this.this$0).get(var1 + this.val$fromIndex) : ((Range)ImmutableRangeSet.access$000(this.this$0).get(var1 + this.val$fromIndex)).intersection(this.val$range);
               }

               boolean isPartialView() {
                  return true;
               }

               public Object get(int var1) {
                  return this.get(var1);
               }
            };
         }
      } else {
         return ImmutableList.of();
      }
   }

   public ImmutableRangeSet subRangeSet(Range var1) {
      if (!this.isEmpty()) {
         Range var2 = this.span();
         if (var1.encloses(var2)) {
            return this;
         }

         if (var1.isConnected(var2)) {
            return new ImmutableRangeSet(this.intersectRanges(var1));
         }
      }

      return of();
   }

   public ImmutableSortedSet asSet(DiscreteDomain var1) {
      Preconditions.checkNotNull(var1);
      if (this.isEmpty()) {
         return ImmutableSortedSet.of();
      } else {
         Range var2 = this.span().canonical(var1);
         if (!var2.hasLowerBound()) {
            throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
         } else {
            if (!var2.hasUpperBound()) {
               try {
                  var1.maxValue();
               } catch (NoSuchElementException var4) {
                  throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
               }
            }

            return new ImmutableRangeSet.AsSet(this, var1);
         }
      }
   }

   boolean isPartialView() {
      return this.ranges.isPartialView();
   }

   public static ImmutableRangeSet.Builder builder() {
      return new ImmutableRangeSet.Builder();
   }

   Object writeReplace() {
      return new ImmutableRangeSet.SerializedForm(this.ranges);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public boolean enclosesAll(RangeSet var1) {
      return super.enclosesAll(var1);
   }

   public void clear() {
      super.clear();
   }

   public boolean contains(Comparable var1) {
      return super.contains(var1);
   }

   public RangeSet subRangeSet(Range var1) {
      return this.subRangeSet(var1);
   }

   public RangeSet complement() {
      return this.complement();
   }

   public Set asRanges() {
      return this.asRanges();
   }

   static ImmutableList access$000(ImmutableRangeSet var0) {
      return var0.ranges;
   }

   private static final class SerializedForm implements Serializable {
      private final ImmutableList ranges;

      SerializedForm(ImmutableList var1) {
         this.ranges = var1;
      }

      Object readResolve() {
         if (this.ranges.isEmpty()) {
            return ImmutableRangeSet.of();
         } else {
            return this.ranges.equals(ImmutableList.of(Range.all())) ? ImmutableRangeSet.all() : new ImmutableRangeSet(this.ranges);
         }
      }
   }

   public static class Builder {
      private final RangeSet rangeSet = TreeRangeSet.create();

      public ImmutableRangeSet.Builder add(Range var1) {
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("range must not be empty, but was " + var1);
         } else if (this.rangeSet.complement().encloses(var1)) {
            this.rangeSet.add(var1);
            return this;
         } else {
            Iterator var2 = this.rangeSet.asRanges().iterator();

            while(var2.hasNext()) {
               Range var3 = (Range)var2.next();
               Preconditions.checkArgument(!var3.isConnected(var1) || var3.intersection(var1).isEmpty(), "Ranges may not overlap, but received %s and %s", var3, var1);
            }

            throw new AssertionError("should have thrown an IAE above");
         }
      }

      public ImmutableRangeSet.Builder addAll(RangeSet var1) {
         Iterator var2 = var1.asRanges().iterator();

         while(var2.hasNext()) {
            Range var3 = (Range)var2.next();
            this.add(var3);
         }

         return this;
      }

      public ImmutableRangeSet build() {
         return ImmutableRangeSet.copyOf(this.rangeSet);
      }
   }

   private static class AsSetSerializedForm implements Serializable {
      private final ImmutableList ranges;
      private final DiscreteDomain domain;

      AsSetSerializedForm(ImmutableList var1, DiscreteDomain var2) {
         this.ranges = var1;
         this.domain = var2;
      }

      Object readResolve() {
         return (new ImmutableRangeSet(this.ranges)).asSet(this.domain);
      }
   }

   private final class AsSet extends ImmutableSortedSet {
      private final DiscreteDomain domain;
      private transient Integer size;
      final ImmutableRangeSet this$0;

      AsSet(ImmutableRangeSet var1, DiscreteDomain var2) {
         super(Ordering.natural());
         this.this$0 = var1;
         this.domain = var2;
      }

      public int size() {
         Integer var1 = this.size;
         if (var1 == null) {
            long var2 = 0L;
            Iterator var4 = ImmutableRangeSet.access$000(this.this$0).iterator();

            while(var4.hasNext()) {
               Range var5 = (Range)var4.next();
               var2 += (long)ContiguousSet.create(var5, this.domain).size();
               if (var2 >= 2147483647L) {
                  break;
               }
            }

            var1 = this.size = Ints.saturatedCast(var2);
         }

         return var1;
      }

      public UnmodifiableIterator iterator() {
         return new AbstractIterator(this) {
            final Iterator rangeItr;
            Iterator elemItr;
            final ImmutableRangeSet.AsSet this$1;

            {
               this.this$1 = var1;
               this.rangeItr = ImmutableRangeSet.access$000(this.this$1.this$0).iterator();
               this.elemItr = Iterators.emptyIterator();
            }

            protected Comparable computeNext() {
               while(true) {
                  if (!this.elemItr.hasNext()) {
                     if (this.rangeItr.hasNext()) {
                        this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), ImmutableRangeSet.AsSet.access$100(this.this$1)).iterator();
                        continue;
                     }

                     return (Comparable)this.endOfData();
                  }

                  return (Comparable)this.elemItr.next();
               }
            }

            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      @GwtIncompatible("NavigableSet")
      public UnmodifiableIterator descendingIterator() {
         return new AbstractIterator(this) {
            final Iterator rangeItr;
            Iterator elemItr;
            final ImmutableRangeSet.AsSet this$1;

            {
               this.this$1 = var1;
               this.rangeItr = ImmutableRangeSet.access$000(this.this$1.this$0).reverse().iterator();
               this.elemItr = Iterators.emptyIterator();
            }

            protected Comparable computeNext() {
               while(true) {
                  if (!this.elemItr.hasNext()) {
                     if (this.rangeItr.hasNext()) {
                        this.elemItr = ContiguousSet.create((Range)this.rangeItr.next(), ImmutableRangeSet.AsSet.access$100(this.this$1)).descendingIterator();
                        continue;
                     }

                     return (Comparable)this.endOfData();
                  }

                  return (Comparable)this.elemItr.next();
               }
            }

            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      ImmutableSortedSet subSet(Range var1) {
         return this.this$0.subRangeSet(var1).asSet(this.domain);
      }

      ImmutableSortedSet headSetImpl(Comparable var1, boolean var2) {
         return this.subSet(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      ImmutableSortedSet subSetImpl(Comparable var1, boolean var2, Comparable var3, boolean var4) {
         return !var2 && !var4 && Range.compareOrThrow(var1, var3) == 0 ? ImmutableSortedSet.of() : this.subSet(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      ImmutableSortedSet tailSetImpl(Comparable var1, boolean var2) {
         return this.subSet(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      int indexOf(Object var1) {
         if (var1 == null) {
            Comparable var2 = (Comparable)var1;
            long var3 = 0L;

            Range var6;
            for(Iterator var5 = ImmutableRangeSet.access$000(this.this$0).iterator(); var5.hasNext(); var3 += (long)ContiguousSet.create(var6, this.domain).size()) {
               var6 = (Range)var5.next();
               if (var6.contains(var2)) {
                  return Ints.saturatedCast(var3 + (long)ContiguousSet.create(var6, this.domain).indexOf(var2));
               }
            }

            throw new AssertionError("impossible");
         } else {
            return -1;
         }
      }

      boolean isPartialView() {
         return ImmutableRangeSet.access$000(this.this$0).isPartialView();
      }

      public String toString() {
         return ImmutableRangeSet.access$000(this.this$0).toString();
      }

      Object writeReplace() {
         return new ImmutableRangeSet.AsSetSerializedForm(ImmutableRangeSet.access$000(this.this$0), this.domain);
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

      static DiscreteDomain access$100(ImmutableRangeSet.AsSet var0) {
         return var0.domain;
      }
   }

   private final class ComplementRanges extends ImmutableList {
      private final boolean positiveBoundedBelow;
      private final boolean positiveBoundedAbove;
      private final int size;
      final ImmutableRangeSet this$0;

      ComplementRanges(ImmutableRangeSet var1) {
         this.this$0 = var1;
         this.positiveBoundedBelow = ((Range)ImmutableRangeSet.access$000(var1).get(0)).hasLowerBound();
         this.positiveBoundedAbove = ((Range)Iterables.getLast(ImmutableRangeSet.access$000(var1))).hasUpperBound();
         int var2 = ImmutableRangeSet.access$000(var1).size() - 1;
         if (this.positiveBoundedBelow) {
            ++var2;
         }

         if (this.positiveBoundedAbove) {
            ++var2;
         }

         this.size = var2;
      }

      public int size() {
         return this.size;
      }

      public Range get(int var1) {
         Preconditions.checkElementIndex(var1, this.size);
         Cut var2;
         if (this.positiveBoundedBelow) {
            var2 = var1 == 0 ? Cut.belowAll() : ((Range)ImmutableRangeSet.access$000(this.this$0).get(var1 - 1)).upperBound;
         } else {
            var2 = ((Range)ImmutableRangeSet.access$000(this.this$0).get(var1)).upperBound;
         }

         Cut var3;
         if (this.positiveBoundedAbove && var1 == this.size - 1) {
            var3 = Cut.aboveAll();
         } else {
            var3 = ((Range)ImmutableRangeSet.access$000(this.this$0).get(var1 + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
         }

         return Range.create(var2, var3);
      }

      boolean isPartialView() {
         return true;
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }
}
