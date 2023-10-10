package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class GeneralRange implements Serializable {
   private final Comparator comparator;
   private final boolean hasLowerBound;
   @Nullable
   private final Object lowerEndpoint;
   private final BoundType lowerBoundType;
   private final boolean hasUpperBound;
   @Nullable
   private final Object upperEndpoint;
   private final BoundType upperBoundType;
   private transient GeneralRange reverse;

   static GeneralRange from(Range var0) {
      Comparable var1 = var0.hasLowerBound() ? var0.lowerEndpoint() : null;
      BoundType var2 = var0.hasLowerBound() ? var0.lowerBoundType() : BoundType.OPEN;
      Comparable var3 = var0.hasUpperBound() ? var0.upperEndpoint() : null;
      BoundType var4 = var0.hasUpperBound() ? var0.upperBoundType() : BoundType.OPEN;
      return new GeneralRange(Ordering.natural(), var0.hasLowerBound(), var1, var2, var0.hasUpperBound(), var3, var4);
   }

   static GeneralRange all(Comparator var0) {
      return new GeneralRange(var0, false, (Object)null, BoundType.OPEN, false, (Object)null, BoundType.OPEN);
   }

   static GeneralRange downTo(Comparator var0, @Nullable Object var1, BoundType var2) {
      return new GeneralRange(var0, true, var1, var2, false, (Object)null, BoundType.OPEN);
   }

   static GeneralRange upTo(Comparator var0, @Nullable Object var1, BoundType var2) {
      return new GeneralRange(var0, false, (Object)null, BoundType.OPEN, true, var1, var2);
   }

   static GeneralRange range(Comparator var0, @Nullable Object var1, BoundType var2, @Nullable Object var3, BoundType var4) {
      return new GeneralRange(var0, true, var1, var2, true, var3, var4);
   }

   private GeneralRange(Comparator var1, boolean var2, @Nullable Object var3, BoundType var4, boolean var5, @Nullable Object var6, BoundType var7) {
      this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      this.hasLowerBound = var2;
      this.hasUpperBound = var5;
      this.lowerEndpoint = var3;
      this.lowerBoundType = (BoundType)Preconditions.checkNotNull(var4);
      this.upperEndpoint = var6;
      this.upperBoundType = (BoundType)Preconditions.checkNotNull(var7);
      if (var2) {
         var1.compare(var3, var3);
      }

      if (var5) {
         var1.compare(var6, var6);
      }

      if (var2 && var5) {
         int var8 = var1.compare(var3, var6);
         Preconditions.checkArgument(var8 <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", var3, var6);
         if (var8 == 0) {
            Preconditions.checkArgument(var4 != BoundType.OPEN | var7 != BoundType.OPEN);
         }
      }

   }

   Comparator comparator() {
      return this.comparator;
   }

   boolean hasLowerBound() {
      return this.hasLowerBound;
   }

   boolean hasUpperBound() {
      return this.hasUpperBound;
   }

   boolean isEmpty() {
      Object var10001;
      label26: {
         if (this.hasUpperBound()) {
            var10001 = this.getUpperEndpoint();
            if (var10001 != false) {
               break label26;
            }
         }

         if (this.hasLowerBound()) {
            var10001 = this.getLowerEndpoint();
            if (var10001 == false) {
               break label26;
            }
         }

         boolean var10000 = false;
         return (boolean)var10001;
      }

      var10001 = true;
      return (boolean)var10001;
   }

   boolean contains(@Nullable Object var1) {
      GeneralRange var10001;
      if (var1 == false) {
         var10001 = this;
         if (var1 == false) {
            boolean var10002 = true;
            return (boolean)var10001;
         }
      }

      var10001 = false;
      return (boolean)var10001;
   }

   GeneralRange intersect(GeneralRange var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(this.comparator.equals(var1.comparator));
      boolean var2 = this.hasLowerBound;
      Object var3 = this.getLowerEndpoint();
      BoundType var4 = this.getLowerBoundType();
      if (!this.hasLowerBound()) {
         var2 = var1.hasLowerBound;
         var3 = var1.getLowerEndpoint();
         var4 = var1.getLowerBoundType();
      } else if (var1.hasLowerBound()) {
         int var5 = this.comparator.compare(this.getLowerEndpoint(), var1.getLowerEndpoint());
         if (var5 < 0 || var5 == 0 && var1.getLowerBoundType() == BoundType.OPEN) {
            var3 = var1.getLowerEndpoint();
            var4 = var1.getLowerBoundType();
         }
      }

      boolean var9 = this.hasUpperBound;
      Object var6 = this.getUpperEndpoint();
      BoundType var7 = this.getUpperBoundType();
      int var8;
      if (!this.hasUpperBound()) {
         var9 = var1.hasUpperBound;
         var6 = var1.getUpperEndpoint();
         var7 = var1.getUpperBoundType();
      } else if (var1.hasUpperBound()) {
         var8 = this.comparator.compare(this.getUpperEndpoint(), var1.getUpperEndpoint());
         if (var8 > 0 || var8 == 0 && var1.getUpperBoundType() == BoundType.OPEN) {
            var6 = var1.getUpperEndpoint();
            var7 = var1.getUpperBoundType();
         }
      }

      if (var2 && var9) {
         var8 = this.comparator.compare(var3, var6);
         if (var8 > 0 || var8 == 0 && var4 == BoundType.OPEN && var7 == BoundType.OPEN) {
            var3 = var6;
            var4 = BoundType.OPEN;
            var7 = BoundType.CLOSED;
         }
      }

      return new GeneralRange(this.comparator, var2, var3, var4, var9, var6, var7);
   }

   public boolean equals(@Nullable Object var1) {
      if (!(var1 instanceof GeneralRange)) {
         return false;
      } else {
         GeneralRange var2 = (GeneralRange)var1;
         return this.comparator.equals(var2.comparator) && this.hasLowerBound == var2.hasLowerBound && this.hasUpperBound == var2.hasUpperBound && this.getLowerBoundType().equals(var2.getLowerBoundType()) && this.getUpperBoundType().equals(var2.getUpperBoundType()) && Objects.equal(this.getLowerEndpoint(), var2.getLowerEndpoint()) && Objects.equal(this.getUpperEndpoint(), var2.getUpperEndpoint());
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.comparator, this.getLowerEndpoint(), this.getLowerBoundType(), this.getUpperEndpoint(), this.getUpperBoundType());
   }

   GeneralRange reverse() {
      GeneralRange var1 = this.reverse;
      if (var1 == null) {
         var1 = new GeneralRange(Ordering.from(this.comparator).reverse(), this.hasUpperBound, this.getUpperEndpoint(), this.getUpperBoundType(), this.hasLowerBound, this.getLowerEndpoint(), this.getLowerBoundType());
         var1.reverse = this;
         return this.reverse = var1;
      } else {
         return var1;
      }
   }

   public String toString() {
      return this.comparator + ":" + (this.lowerBoundType == BoundType.CLOSED ? '[' : '(') + (this.hasLowerBound ? this.lowerEndpoint : "-∞") + ',' + (this.hasUpperBound ? this.upperEndpoint : "∞") + (this.upperBoundType == BoundType.CLOSED ? ']' : ')');
   }

   Object getLowerEndpoint() {
      return this.lowerEndpoint;
   }

   BoundType getLowerBoundType() {
      return this.lowerBoundType;
   }

   Object getUpperEndpoint() {
      return this.upperEndpoint;
   }

   BoundType getUpperBoundType() {
      return this.upperBoundType;
   }
}
