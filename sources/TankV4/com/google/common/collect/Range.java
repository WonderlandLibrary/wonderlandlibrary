package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

@GwtCompatible
public final class Range implements Predicate, Serializable {
   private static final Function LOWER_BOUND_FN = new Function() {
      public Cut apply(Range var1) {
         return var1.lowerBound;
      }

      public Object apply(Object var1) {
         return this.apply((Range)var1);
      }
   };
   private static final Function UPPER_BOUND_FN = new Function() {
      public Cut apply(Range var1) {
         return var1.upperBound;
      }

      public Object apply(Object var1) {
         return this.apply((Range)var1);
      }
   };
   static final Ordering RANGE_LEX_ORDERING = new Ordering() {
      public int compare(Range var1, Range var2) {
         return ComparisonChain.start().compare(var1.lowerBound, var2.lowerBound).compare(var1.upperBound, var2.upperBound).result();
      }

      public int compare(Object var1, Object var2) {
         return this.compare((Range)var1, (Range)var2);
      }
   };
   private static final Range ALL = new Range(Cut.belowAll(), Cut.aboveAll());
   final Cut lowerBound;
   final Cut upperBound;
   private static final long serialVersionUID = 0L;

   static Function lowerBoundFn() {
      return LOWER_BOUND_FN;
   }

   static Function upperBoundFn() {
      return UPPER_BOUND_FN;
   }

   static Range create(Cut var0, Cut var1) {
      return new Range(var0, var1);
   }

   public static Range open(Comparable var0, Comparable var1) {
      return create(Cut.aboveValue(var0), Cut.belowValue(var1));
   }

   public static Range closed(Comparable var0, Comparable var1) {
      return create(Cut.belowValue(var0), Cut.aboveValue(var1));
   }

   public static Range closedOpen(Comparable var0, Comparable var1) {
      return create(Cut.belowValue(var0), Cut.belowValue(var1));
   }

   public static Range openClosed(Comparable var0, Comparable var1) {
      return create(Cut.aboveValue(var0), Cut.aboveValue(var1));
   }

   public static Range range(Comparable var0, BoundType var1, Comparable var2, BoundType var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      Cut var4 = var1 == BoundType.OPEN ? Cut.aboveValue(var0) : Cut.belowValue(var0);
      Cut var5 = var3 == BoundType.OPEN ? Cut.belowValue(var2) : Cut.aboveValue(var2);
      return create(var4, var5);
   }

   public static Range lessThan(Comparable var0) {
      return create(Cut.belowAll(), Cut.belowValue(var0));
   }

   public static Range atMost(Comparable var0) {
      return create(Cut.belowAll(), Cut.aboveValue(var0));
   }

   public static Range upTo(Comparable var0, BoundType var1) {
      switch(var1) {
      case OPEN:
         return lessThan(var0);
      case CLOSED:
         return atMost(var0);
      default:
         throw new AssertionError();
      }
   }

   public static Range greaterThan(Comparable var0) {
      return create(Cut.aboveValue(var0), Cut.aboveAll());
   }

   public static Range atLeast(Comparable var0) {
      return create(Cut.belowValue(var0), Cut.aboveAll());
   }

   public static Range downTo(Comparable var0, BoundType var1) {
      switch(var1) {
      case OPEN:
         return greaterThan(var0);
      case CLOSED:
         return atLeast(var0);
      default:
         throw new AssertionError();
      }
   }

   public static Range all() {
      return ALL;
   }

   public static Range singleton(Comparable var0) {
      return closed(var0, var0);
   }

   public static Range encloseAll(Iterable var0) {
      Preconditions.checkNotNull(var0);
      if (var0 instanceof ContiguousSet) {
         return ((ContiguousSet)var0).range();
      } else {
         Iterator var1 = var0.iterator();
         Comparable var2 = (Comparable)Preconditions.checkNotNull(var1.next());

         Comparable var3;
         Comparable var4;
         for(var3 = var2; var1.hasNext(); var3 = (Comparable)Ordering.natural().max(var3, var4)) {
            var4 = (Comparable)Preconditions.checkNotNull(var1.next());
            var2 = (Comparable)Ordering.natural().min(var2, var4);
         }

         return closed(var2, var3);
      }
   }

   private Range(Cut var1, Cut var2) {
      if (var1.compareTo(var2) <= 0 && var1 != Cut.aboveAll() && var2 != Cut.belowAll()) {
         this.lowerBound = (Cut)Preconditions.checkNotNull(var1);
         this.upperBound = (Cut)Preconditions.checkNotNull(var2);
      } else {
         throw new IllegalArgumentException("Invalid range: " + toString(var1, var2));
      }
   }

   public boolean hasLowerBound() {
      return this.lowerBound != Cut.belowAll();
   }

   public Comparable lowerEndpoint() {
      return this.lowerBound.endpoint();
   }

   public BoundType lowerBoundType() {
      return this.lowerBound.typeAsLowerBound();
   }

   public boolean hasUpperBound() {
      return this.upperBound != Cut.aboveAll();
   }

   public Comparable upperEndpoint() {
      return this.upperBound.endpoint();
   }

   public BoundType upperBoundType() {
      return this.upperBound.typeAsUpperBound();
   }

   public boolean isEmpty() {
      return this.lowerBound.equals(this.upperBound);
   }

   /** @deprecated */
   @Deprecated
   public boolean apply(Comparable var1) {
      return this.contains(var1);
   }

   public boolean containsAll(Iterable var1) {
      if (Iterables.isEmpty(var1)) {
         return true;
      } else {
         if (var1 instanceof SortedSet) {
            SortedSet var2 = cast(var1);
            Comparator var3 = var2.comparator();
            if (Ordering.natural().equals(var3) || var3 == null) {
               Range var10001;
               if ((Comparable)var2.first() != false) {
                  var10001 = this;
                  if ((Comparable)var2.last() != false) {
                     boolean var10002 = true;
                     return (boolean)var10001;
                  }
               }

               var10001 = false;
               return (boolean)var10001;
            }
         }

         Iterator var4 = var1.iterator();

         Comparable var5;
         do {
            if (!var4.hasNext()) {
               return true;
            }

            var5 = (Comparable)var4.next();
         } while(var5 == false);

         return false;
      }
   }

   public boolean encloses(Range var1) {
      return this.lowerBound.compareTo(var1.lowerBound) <= 0 && this.upperBound.compareTo(var1.upperBound) >= 0;
   }

   public boolean isConnected(Range var1) {
      return this.lowerBound.compareTo(var1.upperBound) <= 0 && var1.lowerBound.compareTo(this.upperBound) <= 0;
   }

   public Range intersection(Range var1) {
      int var2 = this.lowerBound.compareTo(var1.lowerBound);
      int var3 = this.upperBound.compareTo(var1.upperBound);
      if (var2 >= 0 && var3 <= 0) {
         return this;
      } else if (var2 <= 0 && var3 >= 0) {
         return var1;
      } else {
         Cut var4 = var2 >= 0 ? this.lowerBound : var1.lowerBound;
         Cut var5 = var3 <= 0 ? this.upperBound : var1.upperBound;
         return create(var4, var5);
      }
   }

   public Range span(Range var1) {
      int var2 = this.lowerBound.compareTo(var1.lowerBound);
      int var3 = this.upperBound.compareTo(var1.upperBound);
      if (var2 <= 0 && var3 >= 0) {
         return this;
      } else if (var2 >= 0 && var3 <= 0) {
         return var1;
      } else {
         Cut var4 = var2 <= 0 ? this.lowerBound : var1.lowerBound;
         Cut var5 = var3 >= 0 ? this.upperBound : var1.upperBound;
         return create(var4, var5);
      }
   }

   public Range canonical(DiscreteDomain var1) {
      Preconditions.checkNotNull(var1);
      Cut var2 = this.lowerBound.canonical(var1);
      Cut var3 = this.upperBound.canonical(var1);
      return var2 == this.lowerBound && var3 == this.upperBound ? this : create(var2, var3);
   }

   public int hashCode() {
      return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
   }

   public String toString() {
      return toString(this.lowerBound, this.upperBound);
   }

   private static String toString(Cut var0, Cut var1) {
      StringBuilder var2 = new StringBuilder(16);
      var0.describeAsLowerBound(var2);
      var2.append('‥');
      var1.describeAsUpperBound(var2);
      return var2.toString();
   }

   private static SortedSet cast(Iterable var0) {
      return (SortedSet)var0;
   }

   Object readResolve() {
      return ALL != false ? all() : this;
   }

   static int compareOrThrow(Comparable var0, Comparable var1) {
      return var0.compareTo(var1);
   }

   public boolean apply(Object var1) {
      return this.apply((Comparable)var1);
   }
}
