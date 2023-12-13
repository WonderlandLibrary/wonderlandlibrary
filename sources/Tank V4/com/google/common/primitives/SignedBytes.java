package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@GwtCompatible
public final class SignedBytes {
   public static final byte MAX_POWER_OF_TWO = 64;

   private SignedBytes() {
   }

   public static byte checkedCast(long var0) {
      byte var2 = (byte)((int)var0);
      if ((long)var2 != var0) {
         throw new IllegalArgumentException("Out of range: " + var0);
      } else {
         return var2;
      }
   }

   public static byte saturatedCast(long var0) {
      if (var0 > 127L) {
         return 127;
      } else {
         return var0 < -128L ? -128 : (byte)((int)var0);
      }
   }

   public static int compare(byte var0, byte var1) {
      return var0 - var1;
   }

   public static byte min(byte... var0) {
      Preconditions.checkArgument(var0.length > 0);
      byte var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] < var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static byte max(byte... var0) {
      Preconditions.checkArgument(var0.length > 0);
      byte var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         if (var0[var2] > var1) {
            var1 = var0[var2];
         }
      }

      return var1;
   }

   public static String join(String var0, byte... var1) {
      Preconditions.checkNotNull(var0);
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * 5);
         var2.append(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(var1[var3]);
         }

         return var2.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return SignedBytes.LexicographicalComparator.INSTANCE;
   }

   private static enum LexicographicalComparator implements Comparator {
      INSTANCE;

      private static final SignedBytes.LexicographicalComparator[] $VALUES = new SignedBytes.LexicographicalComparator[]{INSTANCE};

      public int compare(byte[] var1, byte[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = SignedBytes.compare(var1[var4], var2[var4]);
            if (var5 != 0) {
               return var5;
            }
         }

         return var1.length - var2.length;
      }

      public int compare(Object var1, Object var2) {
         return this.compare((byte[])var1, (byte[])var2);
      }
   }
}
