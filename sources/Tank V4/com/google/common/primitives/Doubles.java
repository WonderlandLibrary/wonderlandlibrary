package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Doubles {
   public static final int BYTES = 8;
   @GwtIncompatible("regular expressions")
   static final Pattern FLOATING_POINT_PATTERN = fpPattern();

   private Doubles() {
   }

   public static int hashCode(double var0) {
      return Double.valueOf(var0).hashCode();
   }

   public static int compare(double var0, double var2) {
      return Double.compare(var0, var2);
   }

   public static boolean isFinite(double var0) {
      return Double.NEGATIVE_INFINITY < var0 & var0 < Double.POSITIVE_INFINITY;
   }

   public static boolean contains(double[] var0, double var1) {
      double[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         double var6 = var3[var5];
         if (var6 == var1) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(double[] var0, double var1) {
      return indexOf(var0, var1, 0, var0.length);
   }

   private static int indexOf(double[] var0, double var1, int var3, int var4) {
      for(int var5 = var3; var5 < var4; ++var5) {
         if (var0[var5] == var1) {
            return var5;
         }
      }

      return -1;
   }

   public static int indexOf(double[] var0, double[] var1) {
      Preconditions.checkNotNull(var0, "array");
      Preconditions.checkNotNull(var1, "target");
      if (var1.length == 0) {
         return 0;
      } else {
         label28:
         for(int var2 = 0; var2 < var0.length - var1.length + 1; ++var2) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if (var0[var2 + var3] != var1[var3]) {
                  continue label28;
               }
            }

            return var2;
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1) {
      return lastIndexOf(var0, var1, 0, var0.length);
   }

   private static int lastIndexOf(double[] var0, double var1, int var3, int var4) {
      for(int var5 = var4 - 1; var5 >= var3; --var5) {
         if (var0[var5] == var1) {
            return var5;
         }
      }

      return -1;
   }

   public static double min(double... var0) {
      Preconditions.checkArgument(var0.length > 0);
      double var1 = var0[0];

      for(int var3 = 1; var3 < var0.length; ++var3) {
         var1 = Math.min(var1, var0[var3]);
      }

      return var1;
   }

   public static double max(double... var0) {
      Preconditions.checkArgument(var0.length > 0);
      double var1 = var0[0];

      for(int var3 = 1; var3 < var0.length; ++var3) {
         var1 = Math.max(var1, var0[var3]);
      }

      return var1;
   }

   public static double[] concat(double[]... var0) {
      int var1 = 0;
      double[][] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double[] var5 = var2[var4];
         var1 += var5.length;
      }

      double[] var8 = new double[var1];
      var3 = 0;
      double[][] var9 = var0;
      int var10 = var0.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         double[] var7 = var9[var6];
         System.arraycopy(var7, 0, var8, var3, var7.length);
         var3 += var7.length;
      }

      return var8;
   }

   @Beta
   public static Converter stringConverter() {
      return Doubles.DoubleConverter.INSTANCE;
   }

   public static double[] ensureCapacity(double[] var0, int var1, int var2) {
      Preconditions.checkArgument(var1 >= 0, "Invalid minLength: %s", var1);
      Preconditions.checkArgument(var2 >= 0, "Invalid padding: %s", var2);
      return var0.length < var1 ? copyOf(var0, var1 + var2) : var0;
   }

   private static double[] copyOf(double[] var0, int var1) {
      double[] var2 = new double[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static String join(String var0, double... var1) {
      Preconditions.checkNotNull(var0);
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * 12);
         var2.append(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(var1[var3]);
         }

         return var2.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return Doubles.LexicographicalComparator.INSTANCE;
   }

   public static double[] toArray(Collection var0) {
      if (var0 instanceof Doubles.DoubleArrayAsList) {
         return ((Doubles.DoubleArrayAsList)var0).toDoubleArray();
      } else {
         Object[] var1 = var0.toArray();
         int var2 = var1.length;
         double[] var3 = new double[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = ((Number)Preconditions.checkNotNull(var1[var4])).doubleValue();
         }

         return var3;
      }
   }

   public static List asList(double... var0) {
      return (List)(var0.length == 0 ? Collections.emptyList() : new Doubles.DoubleArrayAsList(var0));
   }

   @GwtIncompatible("regular expressions")
   private static Pattern fpPattern() {
      String var0 = "(?:\\d++(?:\\.\\d*+)?|\\.\\d++)";
      String var1 = var0 + "(?:[eE][+-]?\\d++)?[fFdD]?";
      String var2 = "(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)";
      String var3 = "0[xX]" + var2 + "[pP][+-]?\\d++[fFdD]?";
      String var4 = "[+-]?(?:NaN|Infinity|" + var1 + "|" + var3 + ")";
      return Pattern.compile(var4);
   }

   @Nullable
   @GwtIncompatible("regular expressions")
   @Beta
   public static Double tryParse(String var0) {
      if (FLOATING_POINT_PATTERN.matcher(var0).matches()) {
         try {
            return Double.parseDouble(var0);
         } catch (NumberFormatException var2) {
         }
      }

      return null;
   }

   static int access$000(double[] var0, double var1, int var3, int var4) {
      return indexOf(var0, var1, var3, var4);
   }

   static int access$100(double[] var0, double var1, int var3, int var4) {
      return lastIndexOf(var0, var1, var3, var4);
   }

   @GwtCompatible
   private static class DoubleArrayAsList extends AbstractList implements RandomAccess, Serializable {
      final double[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      DoubleArrayAsList(double[] var1) {
         this(var1, 0, var1.length);
      }

      DoubleArrayAsList(double[] var1, int var2, int var3) {
         this.array = var1;
         this.start = var2;
         this.end = var3;
      }

      public int size() {
         return this.end - this.start;
      }

      public boolean isEmpty() {
         return false;
      }

      public Double get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.array[this.start + var1];
      }

      public boolean contains(Object var1) {
         return var1 instanceof Double && Doubles.access$000(this.array, (Double)var1, this.start, this.end) != -1;
      }

      public int indexOf(Object var1) {
         if (var1 instanceof Double) {
            int var2 = Doubles.access$000(this.array, (Double)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(Object var1) {
         if (var1 instanceof Double) {
            int var2 = Doubles.access$100(this.array, (Double)var1, this.start, this.end);
            if (var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public Double set(int var1, Double var2) {
         Preconditions.checkElementIndex(var1, this.size());
         double var3 = this.array[this.start + var1];
         this.array[this.start + var1] = (Double)Preconditions.checkNotNull(var2);
         return var3;
      }

      public List subList(int var1, int var2) {
         int var3 = this.size();
         Preconditions.checkPositionIndexes(var1, var2, var3);
         return (List)(var1 == var2 ? Collections.emptyList() : new Doubles.DoubleArrayAsList(this.array, this.start + var1, this.start + var2));
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof Doubles.DoubleArrayAsList) {
            Doubles.DoubleArrayAsList var2 = (Doubles.DoubleArrayAsList)var1;
            int var3 = this.size();
            if (var2.size() != var3) {
               return false;
            } else {
               for(int var4 = 0; var4 < var3; ++var4) {
                  if (this.array[this.start + var4] != var2.array[var2.start + var4]) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         int var1 = 1;

         for(int var2 = this.start; var2 < this.end; ++var2) {
            var1 = 31 * var1 + Doubles.hashCode(this.array[var2]);
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(this.size() * 12);
         var1.append('[').append(this.array[this.start]);

         for(int var2 = this.start + 1; var2 < this.end; ++var2) {
            var1.append(", ").append(this.array[var2]);
         }

         return var1.append(']').toString();
      }

      double[] toDoubleArray() {
         int var1 = this.size();
         double[] var2 = new double[var1];
         System.arraycopy(this.array, this.start, var2, 0, var1);
         return var2;
      }

      public Object set(int var1, Object var2) {
         return this.set(var1, (Double)var2);
      }

      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;

      private static final Doubles.LexicographicalComparator[] $VALUES = new Doubles.LexicographicalComparator[]{INSTANCE};

      public int compare(double[] var1, double[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = Doubles.compare(var1[var4], var2[var4]);
            if (var5 != 0) {
               return var5;
            }
         }

         return var1.length - var2.length;
      }

      public int compare(Object var1, Object var2) {
         return this.compare((double[])var1, (double[])var2);
      }
   }

   private static final class DoubleConverter extends Converter implements Serializable {
      static final Doubles.DoubleConverter INSTANCE = new Doubles.DoubleConverter();
      private static final long serialVersionUID = 1L;

      protected Double doForward(String var1) {
         return Double.valueOf(var1);
      }

      protected String doBackward(Double var1) {
         return var1.toString();
      }

      public String toString() {
         return "Doubles.stringConverter()";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      protected Object doBackward(Object var1) {
         return this.doBackward((Double)var1);
      }

      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }
}
