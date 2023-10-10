package com.google.common.collect;

import java.util.Iterator;
import javax.annotation.Nullable;

abstract class AbstractRangeSet implements RangeSet {
   public boolean contains(Comparable var1) {
      return this.rangeContaining(var1) != null;
   }

   public abstract Range rangeContaining(Comparable var1);

   public boolean isEmpty() {
      return this.asRanges().isEmpty();
   }

   public void add(Range var1) {
      throw new UnsupportedOperationException();
   }

   public void remove(Range var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      this.remove(Range.all());
   }

   public boolean enclosesAll(RangeSet var1) {
      Iterator var2 = var1.asRanges().iterator();

      Range var3;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         var3 = (Range)var2.next();
      } while(this.encloses(var3));

      return false;
   }

   public void addAll(RangeSet var1) {
      Iterator var2 = var1.asRanges().iterator();

      while(var2.hasNext()) {
         Range var3 = (Range)var2.next();
         this.add(var3);
      }

   }

   public void removeAll(RangeSet var1) {
      Iterator var2 = var1.asRanges().iterator();

      while(var2.hasNext()) {
         Range var3 = (Range)var2.next();
         this.remove(var3);
      }

   }

   public abstract boolean encloses(Range var1);

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof RangeSet) {
         RangeSet var2 = (RangeSet)var1;
         return this.asRanges().equals(var2.asRanges());
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return this.asRanges().hashCode();
   }

   public final String toString() {
      return this.asRanges().toString();
   }
}
