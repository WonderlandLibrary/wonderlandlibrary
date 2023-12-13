package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;

public final class Range implements Serializable {
   private static final long serialVersionUID = 1L;
   private final Comparator comparator;
   private final Object minimum;
   private final Object maximum;
   private transient int hashCode;
   private transient String toString;

   public static Range is(Comparable var0) {
      return between(var0, var0, (Comparator)null);
   }

   public static Range is(Object var0, Comparator var1) {
      return between(var0, var0, var1);
   }

   public static Range between(Comparable var0, Comparable var1) {
      return between(var0, var1, (Comparator)null);
   }

   public static Range between(Object var0, Object var1, Comparator var2) {
      return new Range(var0, var1, var2);
   }

   private Range(Object var1, Object var2, Comparator var3) {
      if (var1 != null && var2 != null) {
         if (var3 == null) {
            this.comparator = Range.ComparableComparator.INSTANCE;
         } else {
            this.comparator = var3;
         }

         if (this.comparator.compare(var1, var2) < 1) {
            this.minimum = var1;
            this.maximum = var2;
         } else {
            this.minimum = var2;
            this.maximum = var1;
         }

      } else {
         throw new IllegalArgumentException("Elements in a range must not be null: element1=" + var1 + ", element2=" + var2);
      }
   }

   public Object getMinimum() {
      return this.minimum;
   }

   public Object getMaximum() {
      return this.maximum;
   }

   public Comparator getComparator() {
      return this.comparator;
   }

   public boolean isNaturalOrdering() {
      return this.comparator == Range.ComparableComparator.INSTANCE;
   }

   public boolean isStartedBy(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.comparator.compare(var1, this.minimum) == 0;
      }
   }

   public boolean isEndedBy(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.comparator.compare(var1, this.maximum) == 0;
      }
   }

   public int elementCompareTo(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("Element is null");
      } else if (var1 == null) {
         return -1;
      } else {
         return var1 == null ? 1 : 0;
      }
   }

   public boolean containsRange(Range var1) {
      if (var1 == null) {
         return false;
      } else {
         Range var10001;
         if (var1.minimum == null) {
            var10001 = this;
            if (var1.maximum == null) {
               boolean var10002 = true;
               return (boolean)var10001;
            }
         }

         var10001 = false;
         return (boolean)var10001;
      }
   }

   public boolean isAfterRange(Range var1) {
      return var1 == null ? false : this.isAfter(var1.maximum);
   }

   public boolean isBeforeRange(Range var1) {
      return var1 == null ? false : this.isBefore(var1.minimum);
   }

   public Range intersectionWith(Range var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", var1));
      } else if (this == var1) {
         return this;
      } else {
         Object var2 = this.getComparator().compare(this.minimum, var1.minimum) < 0 ? var1.minimum : this.minimum;
         Object var3 = this.getComparator().compare(this.maximum, var1.maximum) < 0 ? this.maximum : var1.maximum;
         return between(var2, var3, this.getComparator());
      }
   }

   public int hashCode() {
      int var1 = this.hashCode;
      if (this.hashCode == 0) {
         byte var2 = 17;
         var1 = 37 * var2 + this.getClass().hashCode();
         var1 = 37 * var1 + this.minimum.hashCode();
         var1 = 37 * var1 + this.maximum.hashCode();
         this.hashCode = var1;
      }

      return var1;
   }

   public String toString() {
      String var1 = this.toString;
      if (var1 == null) {
         StringBuilder var2 = new StringBuilder(32);
         var2.append('[');
         var2.append(this.minimum);
         var2.append("..");
         var2.append(this.maximum);
         var2.append(']');
         var1 = var2.toString();
         this.toString = var1;
      }

      return var1;
   }

   public String toString(String var1) {
      return String.format(var1, this.minimum, this.maximum, this.comparator);
   }

   private static enum ComparableComparator implements Comparator {
      INSTANCE;

      private static final Range.ComparableComparator[] $VALUES = new Range.ComparableComparator[]{INSTANCE};

      public int compare(Object var1, Object var2) {
         return ((Comparable)var1).compareTo(var2);
      }
   }
}
