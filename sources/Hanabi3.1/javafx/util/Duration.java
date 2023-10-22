package javafx.util;

import java.io.Serializable;
import javafx.beans.NamedArg;

public class Duration implements Comparable, Serializable {
   public static final Duration ZERO = new Duration(0.0);
   public static final Duration ONE = new Duration(1.0);
   public static final Duration INDEFINITE = new Duration(Double.POSITIVE_INFINITY);
   public static final Duration UNKNOWN = new Duration(Double.NaN);
   private final double millis;

   public static Duration valueOf(String var0) {
      int var1 = -1;

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         if (!Character.isDigit(var3) && var3 != '.' && var3 != '-') {
            var1 = var2;
            break;
         }
      }

      if (var1 == -1) {
         throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
      } else {
         double var5 = Double.parseDouble(var0.substring(0, var1));
         String var4 = var0.substring(var1);
         if ("ms".equals(var4)) {
            return millis(var5);
         } else if ("s".equals(var4)) {
            return seconds(var5);
         } else if ("m".equals(var4)) {
            return minutes(var5);
         } else if ("h".equals(var4)) {
            return hours(var5);
         } else {
            throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
         }
      }
   }

   public static Duration millis(double var0) {
      if (var0 == 0.0) {
         return ZERO;
      } else if (var0 == 1.0) {
         return ONE;
      } else if (var0 == Double.POSITIVE_INFINITY) {
         return INDEFINITE;
      } else {
         return Double.isNaN(var0) ? UNKNOWN : new Duration(var0);
      }
   }

   public static Duration seconds(double var0) {
      if (var0 == 0.0) {
         return ZERO;
      } else if (var0 == Double.POSITIVE_INFINITY) {
         return INDEFINITE;
      } else {
         return Double.isNaN(var0) ? UNKNOWN : new Duration(var0 * 1000.0);
      }
   }

   public static Duration minutes(double var0) {
      if (var0 == 0.0) {
         return ZERO;
      } else if (var0 == Double.POSITIVE_INFINITY) {
         return INDEFINITE;
      } else {
         return Double.isNaN(var0) ? UNKNOWN : new Duration(var0 * 60000.0);
      }
   }

   public static Duration hours(double var0) {
      if (var0 == 0.0) {
         return ZERO;
      } else if (var0 == Double.POSITIVE_INFINITY) {
         return INDEFINITE;
      } else {
         return Double.isNaN(var0) ? UNKNOWN : new Duration(var0 * 3600000.0);
      }
   }

   public Duration(@NamedArg("millis") double var1) {
      this.millis = var1;
   }

   public double toMillis() {
      return this.millis;
   }

   public double toSeconds() {
      return this.millis / 1000.0;
   }

   public double toMinutes() {
      return this.millis / 60000.0;
   }

   public double toHours() {
      return this.millis / 3600000.0;
   }

   public Duration add(Duration var1) {
      return millis(this.millis + var1.millis);
   }

   public Duration subtract(Duration var1) {
      return millis(this.millis - var1.millis);
   }

   /** @deprecated */
   @Deprecated
   public Duration multiply(Duration var1) {
      return millis(this.millis * var1.millis);
   }

   public Duration multiply(double var1) {
      return millis(this.millis * var1);
   }

   public Duration divide(double var1) {
      return millis(this.millis / var1);
   }

   /** @deprecated */
   @Deprecated
   public Duration divide(Duration var1) {
      return millis(this.millis / var1.millis);
   }

   public Duration negate() {
      return millis(-this.millis);
   }

   public boolean isIndefinite() {
      return this.millis == Double.POSITIVE_INFINITY;
   }

   public boolean isUnknown() {
      return Double.isNaN(this.millis);
   }

   public boolean lessThan(Duration var1) {
      return this.millis < var1.millis;
   }

   public boolean lessThanOrEqualTo(Duration var1) {
      return this.millis <= var1.millis;
   }

   public boolean greaterThan(Duration var1) {
      return this.millis > var1.millis;
   }

   public boolean greaterThanOrEqualTo(Duration var1) {
      return this.millis >= var1.millis;
   }

   public String toString() {
      return this.isIndefinite() ? "INDEFINITE" : (this.isUnknown() ? "UNKNOWN" : this.millis + " ms");
   }

   public int compareTo(Duration var1) {
      return Double.compare(this.millis, var1.millis);
   }

   public boolean equals(Object var1) {
      return var1 == this || var1 instanceof Duration && this.millis == ((Duration)var1).millis;
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.millis);
      return (int)(var1 ^ var1 >>> 32);
   }
}
