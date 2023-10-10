package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.io.Serializable;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtCompatible
abstract class Cut implements Comparable, Serializable {
   final Comparable endpoint;
   private static final long serialVersionUID = 0L;

   Cut(@Nullable Comparable var1) {
      this.endpoint = var1;
   }

   abstract boolean isLessThan(Comparable var1);

   abstract BoundType typeAsLowerBound();

   abstract BoundType typeAsUpperBound();

   abstract Cut withLowerBoundType(BoundType var1, DiscreteDomain var2);

   abstract Cut withUpperBoundType(BoundType var1, DiscreteDomain var2);

   abstract void describeAsLowerBound(StringBuilder var1);

   abstract void describeAsUpperBound(StringBuilder var1);

   abstract Comparable leastValueAbove(DiscreteDomain var1);

   abstract Comparable greatestValueBelow(DiscreteDomain var1);

   Cut canonical(DiscreteDomain var1) {
      return this;
   }

   public int compareTo(Cut var1) {
      if (var1 == belowAll()) {
         return 1;
      } else if (var1 == aboveAll()) {
         return -1;
      } else {
         int var2 = Range.compareOrThrow(this.endpoint, var1.endpoint);
         return var2 != 0 ? var2 : Booleans.compare(this instanceof Cut.AboveValue, var1 instanceof Cut.AboveValue);
      }
   }

   Comparable endpoint() {
      return this.endpoint;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Cut) {
         Cut var2 = (Cut)var1;

         try {
            int var3 = this.compareTo(var2);
            return var3 == 0;
         } catch (ClassCastException var4) {
         }
      }

      return false;
   }

   static Cut belowAll() {
      return Cut.BelowAll.access$000();
   }

   static Cut aboveAll() {
      return Cut.AboveAll.access$100();
   }

   static Cut belowValue(Comparable var0) {
      return new Cut.BelowValue(var0);
   }

   static Cut aboveValue(Comparable var0) {
      return new Cut.AboveValue(var0);
   }

   public int compareTo(Object var1) {
      return this.compareTo((Cut)var1);
   }

   private static final class AboveValue extends Cut {
      private static final long serialVersionUID = 0L;

      AboveValue(Comparable var1) {
         super((Comparable)Preconditions.checkNotNull(var1));
      }

      boolean isLessThan(Comparable var1) {
         return Range.compareOrThrow(this.endpoint, var1) < 0;
      }

      BoundType typeAsLowerBound() {
         return BoundType.OPEN;
      }

      BoundType typeAsUpperBound() {
         return BoundType.CLOSED;
      }

      Cut withLowerBoundType(BoundType var1, DiscreteDomain var2) {
         switch(var1) {
         case CLOSED:
            Comparable var3 = var2.next(this.endpoint);
            return var3 == null ? Cut.belowAll() : belowValue(var3);
         case OPEN:
            return this;
         default:
            throw new AssertionError();
         }
      }

      Cut withUpperBoundType(BoundType var1, DiscreteDomain var2) {
         switch(var1) {
         case CLOSED:
            return this;
         case OPEN:
            Comparable var3 = var2.next(this.endpoint);
            return var3 == null ? Cut.aboveAll() : belowValue(var3);
         default:
            throw new AssertionError();
         }
      }

      void describeAsLowerBound(StringBuilder var1) {
         var1.append('(').append(this.endpoint);
      }

      void describeAsUpperBound(StringBuilder var1) {
         var1.append(this.endpoint).append(']');
      }

      Comparable leastValueAbove(DiscreteDomain var1) {
         return var1.next(this.endpoint);
      }

      Comparable greatestValueBelow(DiscreteDomain var1) {
         return this.endpoint;
      }

      Cut canonical(DiscreteDomain var1) {
         Comparable var2 = this.leastValueAbove(var1);
         return var2 != null ? belowValue(var2) : Cut.aboveAll();
      }

      public int hashCode() {
         return ~this.endpoint.hashCode();
      }

      public String toString() {
         return "/" + this.endpoint + "\\";
      }

      public int compareTo(Object var1) {
         return super.compareTo((Cut)var1);
      }
   }

   private static final class BelowValue extends Cut {
      private static final long serialVersionUID = 0L;

      BelowValue(Comparable var1) {
         super((Comparable)Preconditions.checkNotNull(var1));
      }

      boolean isLessThan(Comparable var1) {
         return Range.compareOrThrow(this.endpoint, var1) <= 0;
      }

      BoundType typeAsLowerBound() {
         return BoundType.CLOSED;
      }

      BoundType typeAsUpperBound() {
         return BoundType.OPEN;
      }

      Cut withLowerBoundType(BoundType var1, DiscreteDomain var2) {
         switch(var1) {
         case CLOSED:
            return this;
         case OPEN:
            Comparable var3 = var2.previous(this.endpoint);
            return (Cut)(var3 == null ? Cut.belowAll() : new Cut.AboveValue(var3));
         default:
            throw new AssertionError();
         }
      }

      Cut withUpperBoundType(BoundType var1, DiscreteDomain var2) {
         switch(var1) {
         case CLOSED:
            Comparable var3 = var2.previous(this.endpoint);
            return (Cut)(var3 == null ? Cut.aboveAll() : new Cut.AboveValue(var3));
         case OPEN:
            return this;
         default:
            throw new AssertionError();
         }
      }

      void describeAsLowerBound(StringBuilder var1) {
         var1.append('[').append(this.endpoint);
      }

      void describeAsUpperBound(StringBuilder var1) {
         var1.append(this.endpoint).append(')');
      }

      Comparable leastValueAbove(DiscreteDomain var1) {
         return this.endpoint;
      }

      Comparable greatestValueBelow(DiscreteDomain var1) {
         return var1.previous(this.endpoint);
      }

      public int hashCode() {
         return this.endpoint.hashCode();
      }

      public String toString() {
         return "\\" + this.endpoint + "/";
      }

      public int compareTo(Object var1) {
         return super.compareTo((Cut)var1);
      }
   }

   private static final class AboveAll extends Cut {
      private static final Cut.AboveAll INSTANCE = new Cut.AboveAll();
      private static final long serialVersionUID = 0L;

      private AboveAll() {
         super((Comparable)null);
      }

      Comparable endpoint() {
         throw new IllegalStateException("range unbounded on this side");
      }

      boolean isLessThan(Comparable var1) {
         return false;
      }

      BoundType typeAsLowerBound() {
         throw new AssertionError("this statement should be unreachable");
      }

      BoundType typeAsUpperBound() {
         throw new IllegalStateException();
      }

      Cut withLowerBoundType(BoundType var1, DiscreteDomain var2) {
         throw new AssertionError("this statement should be unreachable");
      }

      Cut withUpperBoundType(BoundType var1, DiscreteDomain var2) {
         throw new IllegalStateException();
      }

      void describeAsLowerBound(StringBuilder var1) {
         throw new AssertionError();
      }

      void describeAsUpperBound(StringBuilder var1) {
         var1.append("+∞)");
      }

      Comparable leastValueAbove(DiscreteDomain var1) {
         throw new AssertionError();
      }

      Comparable greatestValueBelow(DiscreteDomain var1) {
         return var1.maxValue();
      }

      public int compareTo(Cut var1) {
         return var1 == this ? 0 : 1;
      }

      public String toString() {
         return "+∞";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public int compareTo(Object var1) {
         return this.compareTo((Cut)var1);
      }

      static Cut.AboveAll access$100() {
         return INSTANCE;
      }
   }

   private static final class BelowAll extends Cut {
      private static final Cut.BelowAll INSTANCE = new Cut.BelowAll();
      private static final long serialVersionUID = 0L;

      private BelowAll() {
         super((Comparable)null);
      }

      Comparable endpoint() {
         throw new IllegalStateException("range unbounded on this side");
      }

      boolean isLessThan(Comparable var1) {
         return true;
      }

      BoundType typeAsLowerBound() {
         throw new IllegalStateException();
      }

      BoundType typeAsUpperBound() {
         throw new AssertionError("this statement should be unreachable");
      }

      Cut withLowerBoundType(BoundType var1, DiscreteDomain var2) {
         throw new IllegalStateException();
      }

      Cut withUpperBoundType(BoundType var1, DiscreteDomain var2) {
         throw new AssertionError("this statement should be unreachable");
      }

      void describeAsLowerBound(StringBuilder var1) {
         var1.append("(-∞");
      }

      void describeAsUpperBound(StringBuilder var1) {
         throw new AssertionError();
      }

      Comparable leastValueAbove(DiscreteDomain var1) {
         return var1.minValue();
      }

      Comparable greatestValueBelow(DiscreteDomain var1) {
         throw new AssertionError();
      }

      Cut canonical(DiscreteDomain var1) {
         try {
            return Cut.belowValue(var1.minValue());
         } catch (NoSuchElementException var3) {
            return this;
         }
      }

      public int compareTo(Cut var1) {
         return var1 == this ? 0 : -1;
      }

      public String toString() {
         return "-∞";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public int compareTo(Object var1) {
         return this.compareTo((Cut)var1);
      }

      static Cut.BelowAll access$000() {
         return INSTANCE;
      }
   }
}
