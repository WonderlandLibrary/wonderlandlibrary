package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@Beta
@GwtCompatible
public final class UnsignedInts {
   static final long INT_MASK = 4294967295L;

   private UnsignedInts() {
   }

   static int flip(int var0) {
      return var0 ^ Integer.MIN_VALUE;
   }

   public static int compare(int var0, int var1) {
      return Ints.compare(flip(var0), flip(var1));
   }

   public static long toLong(int var0) {
      return (long)var0 & 4294967295L;
   }

   public static int min(int... var0) {
      Preconditions.checkArgument(var0.length > 0);
      int var1 = flip(var0[0]);

      for(int var2 = 1; var2 < var0.length; ++var2) {
         int var3 = flip(var0[var2]);
         if (var3 < var1) {
            var1 = var3;
         }
      }

      return flip(var1);
   }

   public static int max(int... var0) {
      Preconditions.checkArgument(var0.length > 0);
      int var1 = flip(var0[0]);

      for(int var2 = 1; var2 < var0.length; ++var2) {
         int var3 = flip(var0[var2]);
         if (var3 > var1) {
            var1 = var3;
         }
      }

      return flip(var1);
   }

   public static String join(String var0, int... var1) {
      Preconditions.checkNotNull(var0);
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * 5);
         var2.append(toString(var1[0]));

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(toString(var1[var3]));
         }

         return var2.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return UnsignedInts.LexicographicalComparator.INSTANCE;
   }

   public static int divide(int var0, int var1) {
      return (int)(toLong(var0) / toLong(var1));
   }

   public static int remainder(int var0, int var1) {
      return (int)(toLong(var0) % toLong(var1));
   }

   public static int decode(String var0) {
      ParseRequest var1 = ParseRequest.fromString(var0);

      try {
         return parseUnsignedInt(var1.rawValue, var1.radix);
      } catch (NumberFormatException var4) {
         NumberFormatException var3 = new NumberFormatException("Error parsing value: " + var0);
         var3.initCause(var4);
         throw var3;
      }
   }

   public static int parseUnsignedInt(String var0) {
      return parseUnsignedInt(var0, 10);
   }

   public static int parseUnsignedInt(String var0, int var1) {
      Preconditions.checkNotNull(var0);
      long var2 = Long.parseLong(var0, var1);
      if ((var2 & 4294967295L) != var2) {
         throw new NumberFormatException("Input " + var0 + " in base " + var1 + " is not in the range of an unsigned integer");
      } else {
         return (int)var2;
      }
   }

   public static String toString(int var0) {
      return toString(var0, 10);
   }

   public static String toString(int var0, int var1) {
      long var2 = (long)var0 & 4294967295L;
      return Long.toString(var2, var1);
   }

   static enum LexicographicalComparator implements Comparator {
      INSTANCE;

      private static final UnsignedInts.LexicographicalComparator[] $VALUES = new UnsignedInts.LexicographicalComparator[]{INSTANCE};

      public int compare(int[] var1, int[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var1[var4] != var2[var4]) {
               return UnsignedInts.compare(var1[var4], var2[var4]);
            }
         }

         return var1.length - var2.length;
      }

      public int compare(Object var1, Object var2) {
         return this.compare((int[])var1, (int[])var2);
      }
   }
}
