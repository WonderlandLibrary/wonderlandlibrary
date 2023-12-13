package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("NavigableMap")
public class ImmutableRangeMap implements RangeMap {
   private static final ImmutableRangeMap EMPTY = new ImmutableRangeMap(ImmutableList.of(), ImmutableList.of());
   private final ImmutableList ranges;
   private final ImmutableList values;

   public static ImmutableRangeMap of() {
      return EMPTY;
   }

   public static ImmutableRangeMap of(Range var0, Object var1) {
      return new ImmutableRangeMap(ImmutableList.of(var0), ImmutableList.of(var1));
   }

   public static ImmutableRangeMap copyOf(RangeMap var0) {
      if (var0 instanceof ImmutableRangeMap) {
         return (ImmutableRangeMap)var0;
      } else {
         Map var1 = var0.asMapOfRanges();
         ImmutableList.Builder var2 = new ImmutableList.Builder(var1.size());
         ImmutableList.Builder var3 = new ImmutableList.Builder(var1.size());
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var2.add(var5.getKey());
            var3.add(var5.getValue());
         }

         return new ImmutableRangeMap(var2.build(), var3.build());
      }
   }

   public static ImmutableRangeMap.Builder builder() {
      return new ImmutableRangeMap.Builder();
   }

   ImmutableRangeMap(ImmutableList var1, ImmutableList var2) {
      this.ranges = var1;
      this.values = var2;
   }

   @Nullable
   public Object get(Comparable var1) {
      int var2 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)Cut.belowValue(var1), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if (var2 == -1) {
         return null;
      } else {
         Range var3 = (Range)this.ranges.get(var2);
         return var3.contains(var1) ? this.values.get(var2) : null;
      }
   }

   @Nullable
   public Entry getEntry(Comparable var1) {
      int var2 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)Cut.belowValue(var1), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if (var2 == -1) {
         return null;
      } else {
         Range var3 = (Range)this.ranges.get(var2);
         return var3.contains(var1) ? Maps.immutableEntry(var3, this.values.get(var2)) : null;
      }
   }

   public Range span() {
      if (this.ranges.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         Range var1 = (Range)this.ranges.get(0);
         Range var2 = (Range)this.ranges.get(this.ranges.size() - 1);
         return Range.create(var1.lowerBound, var2.upperBound);
      }
   }

   public void put(Range var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public void putAll(RangeMap var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void remove(Range var1) {
      throw new UnsupportedOperationException();
   }

   public ImmutableMap asMapOfRanges() {
      if (this.ranges.isEmpty()) {
         return ImmutableMap.of();
      } else {
         RegularImmutableSortedSet var1 = new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING);
         return new RegularImmutableSortedMap(var1, this.values);
      }
   }

   public ImmutableRangeMap subRangeMap(Range var1) {
      if (((Range)Preconditions.checkNotNull(var1)).isEmpty()) {
         return of();
      } else if (!this.ranges.isEmpty() && !var1.encloses(this.span())) {
         int var2 = SortedLists.binarySearch(this.ranges, (Function)Range.upperBoundFn(), (Comparable)var1.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
         int var3 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)var1.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
         if (var2 >= var3) {
            return of();
         } else {
            int var5 = var3 - var2;
            ImmutableList var6 = new ImmutableList(this, var5, var2, var1) {
               final int val$len;
               final int val$off;
               final Range val$range;
               final ImmutableRangeMap this$0;

               {
                  this.this$0 = var1;
                  this.val$len = var2;
                  this.val$off = var3;
                  this.val$range = var4;
               }

               public int size() {
                  return this.val$len;
               }

               public Range get(int var1) {
                  Preconditions.checkElementIndex(var1, this.val$len);
                  return var1 != 0 && var1 != this.val$len - 1 ? (Range)ImmutableRangeMap.access$000(this.this$0).get(var1 + this.val$off) : ((Range)ImmutableRangeMap.access$000(this.this$0).get(var1 + this.val$off)).intersection(this.val$range);
               }

               boolean isPartialView() {
                  return true;
               }

               public Object get(int var1) {
                  return this.get(var1);
               }
            };
            return new ImmutableRangeMap(this, var6, this.values.subList(var2, var3), var1, this) {
               final Range val$range;
               final ImmutableRangeMap val$outer;
               final ImmutableRangeMap this$0;

               {
                  this.this$0 = var1;
                  this.val$range = var4;
                  this.val$outer = var5;
               }

               public ImmutableRangeMap subRangeMap(Range var1) {
                  return this.val$range.isConnected(var1) ? this.val$outer.subRangeMap(var1.intersection(this.val$range)) : ImmutableRangeMap.of();
               }

               public RangeMap subRangeMap(Range var1) {
                  return this.subRangeMap(var1);
               }

               public Map asMapOfRanges() {
                  return super.asMapOfRanges();
               }
            };
         }
      } else {
         return this;
      }
   }

   public int hashCode() {
      return this.asMapOfRanges().hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof RangeMap) {
         RangeMap var2 = (RangeMap)var1;
         return this.asMapOfRanges().equals(var2.asMapOfRanges());
      } else {
         return false;
      }
   }

   public String toString() {
      return this.asMapOfRanges().toString();
   }

   public RangeMap subRangeMap(Range var1) {
      return this.subRangeMap(var1);
   }

   public Map asMapOfRanges() {
      return this.asMapOfRanges();
   }

   static ImmutableList access$000(ImmutableRangeMap var0) {
      return var0.ranges;
   }

   public static final class Builder {
      private final RangeSet keyRanges = TreeRangeSet.create();
      private final RangeMap rangeMap = TreeRangeMap.create();

      public ImmutableRangeMap.Builder put(Range var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         Preconditions.checkArgument(!var1.isEmpty(), "Range must not be empty, but was %s", var1);
         if (!this.keyRanges.complement().encloses(var1)) {
            Iterator var3 = this.rangeMap.asMapOfRanges().entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               Range var5 = (Range)var4.getKey();
               if (var5.isConnected(var1) && !var5.intersection(var1).isEmpty()) {
                  throw new IllegalArgumentException("Overlapping ranges: range " + var1 + " overlaps with entry " + var4);
               }
            }
         }

         this.keyRanges.add(var1);
         this.rangeMap.put(var1, var2);
         return this;
      }

      public ImmutableRangeMap.Builder putAll(RangeMap var1) {
         Iterator var2 = var1.asMapOfRanges().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.put((Range)var3.getKey(), var3.getValue());
         }

         return this;
      }

      public ImmutableRangeMap build() {
         Map var1 = this.rangeMap.asMapOfRanges();
         ImmutableList.Builder var2 = new ImmutableList.Builder(var1.size());
         ImmutableList.Builder var3 = new ImmutableList.Builder(var1.size());
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var2.add(var5.getKey());
            var3.add(var5.getValue());
         }

         return new ImmutableRangeMap(var2.build(), var3.build());
      }
   }
}
