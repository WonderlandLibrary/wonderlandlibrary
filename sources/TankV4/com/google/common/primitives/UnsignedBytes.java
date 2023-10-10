package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import sun.misc.Unsafe;

public final class UnsignedBytes {
   public static final byte MAX_POWER_OF_TWO = -128;
   public static final byte MAX_VALUE = -1;
   private static final int UNSIGNED_MASK = 255;

   private UnsignedBytes() {
   }

   public static int toInt(byte var0) {
      return var0 & 255;
   }

   public static byte checkedCast(long var0) {
      if (var0 >> 8 != 0L) {
         throw new IllegalArgumentException("Out of range: " + var0);
      } else {
         return (byte)((int)var0);
      }
   }

   public static byte saturatedCast(long var0) {
      if (var0 > (long)toInt((byte)-1)) {
         return -1;
      } else {
         return var0 < 0L ? 0 : (byte)((int)var0);
      }
   }

   public static int compare(byte var0, byte var1) {
      return toInt(var0) - toInt(var1);
   }

   public static byte min(byte... var0) {
      Preconditions.checkArgument(var0.length > 0);
      int var1 = toInt(var0[0]);

      for(int var2 = 1; var2 < var0.length; ++var2) {
         int var3 = toInt(var0[var2]);
         if (var3 < var1) {
            var1 = var3;
         }
      }

      return (byte)var1;
   }

   public static byte max(byte... var0) {
      Preconditions.checkArgument(var0.length > 0);
      int var1 = toInt(var0[0]);

      for(int var2 = 1; var2 < var0.length; ++var2) {
         int var3 = toInt(var0[var2]);
         if (var3 > var1) {
            var1 = var3;
         }
      }

      return (byte)var1;
   }

   @Beta
   public static String toString(byte var0) {
      return toString(var0, 10);
   }

   @Beta
   public static String toString(byte var0, int var1) {
      Preconditions.checkArgument(var1 >= 2 && var1 <= 36, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", var1);
      return Integer.toString(toInt(var0), var1);
   }

   @Beta
   public static byte parseUnsignedByte(String var0) {
      return parseUnsignedByte(var0, 10);
   }

   @Beta
   public static byte parseUnsignedByte(String var0, int var1) {
      int var2 = Integer.parseInt((String)Preconditions.checkNotNull(var0), var1);
      if (var2 >> 8 == 0) {
         return (byte)var2;
      } else {
         throw new NumberFormatException("out of range: " + var2);
      }
   }

   public static String join(String var0, byte... var1) {
      Preconditions.checkNotNull(var0);
      if (var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * (3 + var0.length()));
         var2.append(toInt(var1[0]));

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(toString(var1[var3]));
         }

         return var2.toString();
      }
   }

   public static Comparator lexicographicalComparator() {
      return UnsignedBytes.LexicographicalComparatorHolder.BEST_COMPARATOR;
   }

   @VisibleForTesting
   static Comparator lexicographicalComparatorJavaImpl() {
      return UnsignedBytes.LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
   }

   @VisibleForTesting
   static class LexicographicalComparatorHolder {
      static final String UNSAFE_COMPARATOR_NAME = UnsignedBytes.LexicographicalComparatorHolder.class.getName() + "$UnsafeComparator";
      static final Comparator BEST_COMPARATOR = getBestComparator();

      static Comparator getBestComparator() {
         try {
            Class var0 = Class.forName(UNSAFE_COMPARATOR_NAME);
            Comparator var1 = (Comparator)var0.getEnumConstants()[0];
            return var1;
         } catch (Throwable var2) {
            return UnsignedBytes.lexicographicalComparatorJavaImpl();
         }
      }

      static enum PureJavaComparator implements Comparator {
         INSTANCE;

         private static final UnsignedBytes.LexicographicalComparatorHolder.PureJavaComparator[] $VALUES = new UnsignedBytes.LexicographicalComparatorHolder.PureJavaComparator[]{INSTANCE};

         public int compare(byte[] var1, byte[] var2) {
            int var3 = Math.min(var1.length, var2.length);

            for(int var4 = 0; var4 < var3; ++var4) {
               int var5 = UnsignedBytes.compare(var1[var4], var2[var4]);
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

      @VisibleForTesting
      static enum UnsafeComparator implements Comparator {
         INSTANCE;

         static final boolean BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
         static final Unsafe theUnsafe = getUnsafe();
         static final int BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
         private static final UnsignedBytes.LexicographicalComparatorHolder.UnsafeComparator[] $VALUES = new UnsignedBytes.LexicographicalComparatorHolder.UnsafeComparator[]{INSTANCE};

         private static Unsafe getUnsafe() {
            try {
               return Unsafe.getUnsafe();
            } catch (SecurityException var2) {
               try {
                  return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
                     public Unsafe run() throws Exception {
                        Class var1 = Unsafe.class;
                        Field[] var2 = var1.getDeclaredFields();
                        int var3 = var2.length;

                        for(int var4 = 0; var4 < var3; ++var4) {
                           Field var5 = var2[var4];
                           var5.setAccessible(true);
                           Object var6 = var5.get((Object)null);
                           if (var1.isInstance(var6)) {
                              return (Unsafe)var1.cast(var6);
                           }
                        }

                        throw new NoSuchFieldError("the Unsafe");
                     }

                     public Object run() throws Exception {
                        return this.run();
                     }
                  });
               } catch (PrivilegedActionException var1) {
                  throw new RuntimeException("Could not initialize intrinsics", var1.getCause());
               }
            }
         }

         public int compare(byte[] var1, byte[] var2) {
            int var3 = Math.min(var1.length, var2.length);
            int var4 = var3 / 8;

            int var5;
            for(var5 = 0; var5 < var4 * 8; var5 += 8) {
               long var6 = theUnsafe.getLong(var1, (long)BYTE_ARRAY_BASE_OFFSET + (long)var5);
               long var8 = theUnsafe.getLong(var2, (long)BYTE_ARRAY_BASE_OFFSET + (long)var5);
               if (var6 != var8) {
                  if (BIG_ENDIAN) {
                     return UnsignedLongs.compare(var6, var8);
                  }

                  int var10 = Long.numberOfTrailingZeros(var6 ^ var8) & -8;
                  return (int)((var6 >>> var10 & 255L) - (var8 >>> var10 & 255L));
               }
            }

            for(var5 = var4 * 8; var5 < var3; ++var5) {
               int var11 = UnsignedBytes.compare(var1[var5], var2[var5]);
               if (var11 != 0) {
                  return var11;
               }
            }

            return var1.length - var2.length;
         }

         public int compare(Object var1, Object var2) {
            return this.compare((byte[])var1, (byte[])var2);
         }

         static {
            if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
               throw new AssertionError();
            }
         }
      }
   }
}
