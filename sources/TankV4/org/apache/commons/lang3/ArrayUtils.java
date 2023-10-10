package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.mutable.MutableInt;

public class ArrayUtils {
   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
   public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
   public static final String[] EMPTY_STRING_ARRAY = new String[0];
   public static final long[] EMPTY_LONG_ARRAY = new long[0];
   public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
   public static final int[] EMPTY_INT_ARRAY = new int[0];
   public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
   public static final short[] EMPTY_SHORT_ARRAY = new short[0];
   public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
   public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
   public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
   public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
   public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
   public static final int INDEX_NOT_FOUND = -1;

   public static String toString(Object var0) {
      return toString(var0, "{}");
   }

   public static String toString(Object var0, String var1) {
      return var0 == null ? var1 : (new ToStringBuilder(var0, ToStringStyle.SIMPLE_STYLE)).append(var0).toString();
   }

   public static int hashCode(Object var0) {
      return (new HashCodeBuilder()).append(var0).toHashCode();
   }

   /** @deprecated */
   @Deprecated
   public static boolean isEquals(Object var0, Object var1) {
      return (new EqualsBuilder()).append(var0, var1).isEquals();
   }

   public static Map toMap(Object[] var0) {
      if (var0 == null) {
         return null;
      } else {
         HashMap var1 = new HashMap((int)((double)var0.length * 1.5D));

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Object var3 = var0[var2];
            if (var3 instanceof Entry) {
               Entry var4 = (Entry)var3;
               var1.put(var4.getKey(), var4.getValue());
            } else {
               if (!(var3 instanceof Object[])) {
                  throw new IllegalArgumentException("Array element " + var2 + ", '" + var3 + "', is neither of type Map.Entry nor an Array");
               }

               Object[] var5 = (Object[])((Object[])var3);
               if (var5.length < 2) {
                  throw new IllegalArgumentException("Array element " + var2 + ", '" + var3 + "', has a length less than 2");
               }

               var1.put(var5[0], var5[1]);
            }
         }

         return var1;
      }
   }

   public static Object[] toArray(Object... var0) {
      return var0;
   }

   public static Object[] clone(Object[] var0) {
      return var0 == null ? null : (Object[])var0.clone();
   }

   public static long[] clone(long[] var0) {
      return var0 == null ? null : (long[])var0.clone();
   }

   public static int[] clone(int[] var0) {
      return var0 == null ? null : (int[])var0.clone();
   }

   public static short[] clone(short[] var0) {
      return var0 == null ? null : (short[])var0.clone();
   }

   public static char[] clone(char[] var0) {
      return var0 == null ? null : (char[])var0.clone();
   }

   public static byte[] clone(byte[] var0) {
      return var0 == null ? null : (byte[])var0.clone();
   }

   public static double[] clone(double[] var0) {
      return var0 == null ? null : (double[])var0.clone();
   }

   public static float[] clone(float[] var0) {
      return var0 == null ? null : (float[])var0.clone();
   }

   public static boolean[] clone(boolean[] var0) {
      return var0 == null ? null : (boolean[])var0.clone();
   }

   public static Object[] nullToEmpty(Object[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_OBJECT_ARRAY;
   }

   public static Class[] nullToEmpty(Class[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_CLASS_ARRAY;
   }

   public static String[] nullToEmpty(String[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_STRING_ARRAY;
   }

   public static long[] nullToEmpty(long[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_LONG_ARRAY;
   }

   public static int[] nullToEmpty(int[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_INT_ARRAY;
   }

   public static short[] nullToEmpty(short[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_SHORT_ARRAY;
   }

   public static char[] nullToEmpty(char[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_CHAR_ARRAY;
   }

   public static byte[] nullToEmpty(byte[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_BYTE_ARRAY;
   }

   public static double[] nullToEmpty(double[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_DOUBLE_ARRAY;
   }

   public static float[] nullToEmpty(float[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_FLOAT_ARRAY;
   }

   public static boolean[] nullToEmpty(boolean[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_BOOLEAN_ARRAY;
   }

   public static Long[] nullToEmpty(Long[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_LONG_OBJECT_ARRAY;
   }

   public static Integer[] nullToEmpty(Integer[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_INTEGER_OBJECT_ARRAY;
   }

   public static Short[] nullToEmpty(Short[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_SHORT_OBJECT_ARRAY;
   }

   public static Character[] nullToEmpty(Character[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_CHARACTER_OBJECT_ARRAY;
   }

   public static Byte[] nullToEmpty(Byte[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_BYTE_OBJECT_ARRAY;
   }

   public static Double[] nullToEmpty(Double[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_DOUBLE_OBJECT_ARRAY;
   }

   public static Float[] nullToEmpty(Float[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_FLOAT_OBJECT_ARRAY;
   }

   public static Boolean[] nullToEmpty(Boolean[] var0) {
      return var0 != null && var0.length != 0 ? var0 : EMPTY_BOOLEAN_OBJECT_ARRAY;
   }

   public static Object[] subarray(Object[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         Class var4 = var0.getClass().getComponentType();
         Object[] var5;
         if (var3 <= 0) {
            var5 = (Object[])((Object[])Array.newInstance(var4, 0));
            return var5;
         } else {
            var5 = (Object[])((Object[])Array.newInstance(var4, var3));
            System.arraycopy(var0, var1, var5, 0, var3);
            return var5;
         }
      }
   }

   public static long[] subarray(long[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_LONG_ARRAY;
         } else {
            long[] var4 = new long[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static int[] subarray(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_INT_ARRAY;
         } else {
            int[] var4 = new int[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static short[] subarray(short[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_SHORT_ARRAY;
         } else {
            short[] var4 = new short[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static char[] subarray(char[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_CHAR_ARRAY;
         } else {
            char[] var4 = new char[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static byte[] subarray(byte[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_BYTE_ARRAY;
         } else {
            byte[] var4 = new byte[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static double[] subarray(double[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_DOUBLE_ARRAY;
         } else {
            double[] var4 = new double[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static float[] subarray(float[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_FLOAT_ARRAY;
         } else {
            float[] var4 = new float[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static boolean[] subarray(boolean[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
         } else {
            boolean[] var4 = new boolean[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static boolean isSameLength(Object[] var0, Object[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(long[] var0, long[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(int[] var0, int[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(short[] var0, short[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(char[] var0, char[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(byte[] var0, byte[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(double[] var0, double[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(float[] var0, float[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static boolean isSameLength(boolean[] var0, boolean[] var1) {
      return (var0 != null || var1 == null || var1.length <= 0) && (var1 != null || var0 == null || var0.length <= 0) && (var0 == null || var1 == null || var0.length == var1.length);
   }

   public static int getLength(Object var0) {
      return var0 == null ? 0 : Array.getLength(var0);
   }

   public static boolean isSameType(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         return var0.getClass().getName().equals(var1.getClass().getName());
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static void reverse(Object[] var0) {
      if (var0 != null) {
         reverse((Object[])var0, 0, var0.length);
      }
   }

   public static void reverse(long[] var0) {
      if (var0 != null) {
         reverse((long[])var0, 0, var0.length);
      }
   }

   public static void reverse(int[] var0) {
      if (var0 != null) {
         reverse((int[])var0, 0, var0.length);
      }
   }

   public static void reverse(short[] var0) {
      if (var0 != null) {
         reverse((short[])var0, 0, var0.length);
      }
   }

   public static void reverse(char[] var0) {
      if (var0 != null) {
         reverse((char[])var0, 0, var0.length);
      }
   }

   public static void reverse(byte[] var0) {
      if (var0 != null) {
         reverse((byte[])var0, 0, var0.length);
      }
   }

   public static void reverse(double[] var0) {
      if (var0 != null) {
         reverse((double[])var0, 0, var0.length);
      }
   }

   public static void reverse(float[] var0) {
      if (var0 != null) {
         reverse((float[])var0, 0, var0.length);
      }
   }

   public static void reverse(boolean[] var0) {
      if (var0 != null) {
         reverse((boolean[])var0, 0, var0.length);
      }
   }

   public static void reverse(boolean[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            boolean var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            byte var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(char[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            char var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(double[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            double var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(float[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            float var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(int[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            int var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(long[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            long var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(Object[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            Object var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(short[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = var1 < 0 ? 0 : var1;

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            short var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static int indexOf(Object[] var0, Object var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(Object[] var0, Object var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         int var3;
         if (var1 == null) {
            for(var3 = var2; var3 < var0.length; ++var3) {
               if (var0[var3] == null) {
                  return var3;
               }
            }
         } else if (var0.getClass().getComponentType().isInstance(var1)) {
            for(var3 = var2; var3 < var0.length; ++var3) {
               if (var1.equals(var0[var3])) {
                  return var3;
               }
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(Object[] var0, Object var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(Object[] var0, Object var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         int var3;
         if (var1 == null) {
            for(var3 = var2; var3 >= 0; --var3) {
               if (var0[var3] == null) {
                  return var3;
               }
            }
         } else if (var0.getClass().getComponentType().isInstance(var1)) {
            for(var3 = var2; var3 >= 0; --var3) {
               if (var1.equals(var0[var3])) {
                  return var3;
               }
            }
         }

         return -1;
      }
   }

   public static boolean contains(Object[] var0, Object var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(long[] var0, long var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(long[] var0, long var1, int var3) {
      if (var0 == null) {
         return -1;
      } else {
         if (var3 < 0) {
            var3 = 0;
         }

         for(int var4 = var3; var4 < var0.length; ++var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(long[] var0, long var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(long[] var0, long var1, int var3) {
      if (var0 == null) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         if (var3 >= var0.length) {
            var3 = var0.length - 1;
         }

         for(int var4 = var3; var4 >= 0; --var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static boolean contains(long[] var0, long var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(int[] var0, int var1) {
      return indexOf((int[])var0, (int)var1, 0);
   }

   public static int indexOf(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(int[] var0, int var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean contains(int[] var0, int var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(short[] var0, short var1) {
      return indexOf((short[])var0, (short)var1, 0);
   }

   public static int indexOf(short[] var0, short var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(short[] var0, short var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(short[] var0, short var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean contains(short[] var0, short var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(char[] var0, char var1) {
      return indexOf((char[])var0, (char)var1, 0);
   }

   public static int indexOf(char[] var0, char var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(char[] var0, char var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(char[] var0, char var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean contains(char[] var0, char var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(byte[] var0, byte var1) {
      return indexOf((byte[])var0, (byte)var1, 0);
   }

   public static int indexOf(byte[] var0, byte var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(byte[] var0, byte var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(byte[] var0, byte var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean contains(byte[] var0, byte var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(double[] var0, double var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(double[] var0, double var1, double var3) {
      return indexOf(var0, var1, 0, var3);
   }

   public static int indexOf(double[] var0, double var1, int var3) {
      if (var0 != null) {
         return -1;
      } else {
         if (var3 < 0) {
            var3 = 0;
         }

         for(int var4 = var3; var4 < var0.length; ++var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int indexOf(double[] var0, double var1, int var3, double var4) {
      if (var0 != null) {
         return -1;
      } else {
         if (var3 < 0) {
            var3 = 0;
         }

         double var6 = var1 - var4;
         double var8 = var1 + var4;

         for(int var10 = var3; var10 < var0.length; ++var10) {
            if (var0[var10] >= var6 && var0[var10] <= var8) {
               return var10;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(double[] var0, double var1, double var3) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE, var3);
   }

   public static int lastIndexOf(double[] var0, double var1, int var3) {
      if (var0 != null) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         if (var3 >= var0.length) {
            var3 = var0.length - 1;
         }

         for(int var4 = var3; var4 >= 0; --var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1, int var3, double var4) {
      if (var0 != null) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         if (var3 >= var0.length) {
            var3 = var0.length - 1;
         }

         double var6 = var1 - var4;
         double var8 = var1 + var4;

         for(int var10 = var3; var10 >= 0; --var10) {
            if (var0[var10] >= var6 && var0[var10] <= var8) {
               return var10;
            }
         }

         return -1;
      }
   }

   public static boolean contains(double[] var0, double var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(double[] var0, double var1, double var3) {
      return indexOf(var0, var1, 0, var3) != -1;
   }

   public static int indexOf(float[] var0, float var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(float[] var0, float var1, int var2) {
      if (var0 != null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(float[] var0, float var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(float[] var0, float var1, int var2) {
      if (var0 != null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean contains(float[] var0, float var1) {
      return indexOf(var0, var1) != -1;
   }

   public static int indexOf(boolean[] var0, boolean var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(boolean[] var0, boolean var1, int var2) {
      if (var0 != null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(boolean[] var0, boolean var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(boolean[] var0, boolean var1, int var2) {
      if (var0 != null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean contains(boolean[] var0, boolean var1) {
      return indexOf(var0, var1) != -1;
   }

   public static char[] toPrimitive(Character[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var1 = new char[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static char[] toPrimitive(Character[] var0, char var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var2 = new char[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Character var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static Character[] toObject(char[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHARACTER_OBJECT_ARRAY;
      } else {
         Character[] var1 = new Character[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static long[] toPrimitive(Long[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_ARRAY;
      } else {
         long[] var1 = new long[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static long[] toPrimitive(Long[] var0, long var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_ARRAY;
      } else {
         long[] var3 = new long[var0.length];

         for(int var4 = 0; var4 < var0.length; ++var4) {
            Long var5 = var0[var4];
            var3[var4] = var5 == null ? var1 : var5;
         }

         return var3;
      }
   }

   public static Long[] toObject(long[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_OBJECT_ARRAY;
      } else {
         Long[] var1 = new Long[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static int[] toPrimitive(Integer[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INT_ARRAY;
      } else {
         int[] var1 = new int[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static int[] toPrimitive(Integer[] var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INT_ARRAY;
      } else {
         int[] var2 = new int[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Integer var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static Integer[] toObject(int[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INTEGER_OBJECT_ARRAY;
      } else {
         Integer[] var1 = new Integer[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static short[] toPrimitive(Short[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_ARRAY;
      } else {
         short[] var1 = new short[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static short[] toPrimitive(Short[] var0, short var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_ARRAY;
      } else {
         short[] var2 = new short[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Short var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static Short[] toObject(short[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_OBJECT_ARRAY;
      } else {
         Short[] var1 = new Short[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static byte[] toPrimitive(Byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var1 = new byte[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static byte[] toPrimitive(Byte[] var0, byte var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var2 = new byte[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Byte var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static Byte[] toObject(byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_OBJECT_ARRAY;
      } else {
         Byte[] var1 = new Byte[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static double[] toPrimitive(Double[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_ARRAY;
      } else {
         double[] var1 = new double[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static double[] toPrimitive(Double[] var0, double var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_ARRAY;
      } else {
         double[] var3 = new double[var0.length];

         for(int var4 = 0; var4 < var0.length; ++var4) {
            Double var5 = var0[var4];
            var3[var4] = var5 == null ? var1 : var5;
         }

         return var3;
      }
   }

   public static Double[] toObject(double[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_OBJECT_ARRAY;
      } else {
         Double[] var1 = new Double[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static float[] toPrimitive(Float[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_ARRAY;
      } else {
         float[] var1 = new float[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static float[] toPrimitive(Float[] var0, float var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_ARRAY;
      } else {
         float[] var2 = new float[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Float var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static Float[] toObject(float[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_OBJECT_ARRAY;
      } else {
         Float[] var1 = new Float[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static boolean[] toPrimitive(Boolean[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_ARRAY;
      } else {
         boolean[] var1 = new boolean[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static boolean[] toPrimitive(Boolean[] var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_ARRAY;
      } else {
         boolean[] var2 = new boolean[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Boolean var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static Boolean[] toObject(boolean[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_OBJECT_ARRAY;
      } else {
         Boolean[] var1 = new Boolean[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2] ? Boolean.TRUE : Boolean.FALSE;
         }

         return var1;
      }
   }

   public static boolean isNotEmpty(Object[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(long[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(short[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(char[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(byte[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(double[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(float[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static boolean isNotEmpty(boolean[] var0) {
      return var0 != null && var0.length != 0;
   }

   public static Object[] addAll(Object[] var0, Object... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         Class var2 = var0.getClass().getComponentType();
         Object[] var3 = (Object[])((Object[])Array.newInstance(var2, var0.length + var1.length));
         System.arraycopy(var0, 0, var3, 0, var0.length);

         try {
            System.arraycopy(var1, 0, var3, var0.length, var1.length);
            return var3;
         } catch (ArrayStoreException var6) {
            Class var5 = var1.getClass().getComponentType();
            if (!var2.isAssignableFrom(var5)) {
               throw new IllegalArgumentException("Cannot store " + var5.getName() + " in an array of " + var2.getName(), var6);
            } else {
               throw var6;
            }
         }
      }
   }

   public static boolean[] addAll(boolean[] var0, boolean... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         boolean[] var2 = new boolean[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static char[] addAll(char[] var0, char... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         char[] var2 = new char[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static byte[] addAll(byte[] var0, byte... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         byte[] var2 = new byte[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static short[] addAll(short[] var0, short... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         short[] var2 = new short[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static int[] addAll(int[] var0, int... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         int[] var2 = new int[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static long[] addAll(long[] var0, long... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         long[] var2 = new long[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static float[] addAll(float[] var0, float... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         float[] var2 = new float[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static double[] addAll(double[] var0, double... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         double[] var2 = new double[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static Object[] add(Object[] var0, Object var1) {
      Class var2;
      if (var0 != null) {
         var2 = var0.getClass();
      } else {
         if (var1 == null) {
            throw new IllegalArgumentException("Arguments cannot both be null");
         }

         var2 = var1.getClass();
      }

      Object[] var3 = (Object[])((Object[])copyArrayGrow1(var0, var2));
      var3[var3.length - 1] = var1;
      return var3;
   }

   public static boolean[] add(boolean[] var0, boolean var1) {
      boolean[] var2 = (boolean[])((boolean[])copyArrayGrow1(var0, Boolean.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   public static byte[] add(byte[] var0, byte var1) {
      byte[] var2 = (byte[])((byte[])copyArrayGrow1(var0, Byte.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   public static char[] add(char[] var0, char var1) {
      char[] var2 = (char[])((char[])copyArrayGrow1(var0, Character.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   public static double[] add(double[] var0, double var1) {
      double[] var3 = (double[])((double[])copyArrayGrow1(var0, Double.TYPE));
      var3[var3.length - 1] = var1;
      return var3;
   }

   public static float[] add(float[] var0, float var1) {
      float[] var2 = (float[])((float[])copyArrayGrow1(var0, Float.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   public static int[] add(int[] var0, int var1) {
      int[] var2 = (int[])((int[])copyArrayGrow1(var0, Integer.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   public static long[] add(long[] var0, long var1) {
      long[] var3 = (long[])((long[])copyArrayGrow1(var0, Long.TYPE));
      var3[var3.length - 1] = var1;
      return var3;
   }

   public static short[] add(short[] var0, short var1) {
      short[] var2 = (short[])((short[])copyArrayGrow1(var0, Short.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   private static Object copyArrayGrow1(Object var0, Class var1) {
      if (var0 != null) {
         int var2 = Array.getLength(var0);
         Object var3 = Array.newInstance(var0.getClass().getComponentType(), var2 + 1);
         System.arraycopy(var0, 0, var3, 0, var2);
         return var3;
      } else {
         return Array.newInstance(var1, 1);
      }
   }

   public static Object[] add(Object[] var0, int var1, Object var2) {
      Class var3 = null;
      if (var0 != null) {
         var3 = var0.getClass().getComponentType();
      } else {
         if (var2 == null) {
            throw new IllegalArgumentException("Array and element cannot both be null");
         }

         var3 = var2.getClass();
      }

      Object[] var4 = (Object[])((Object[])add(var0, var1, var2, var3));
      return var4;
   }

   public static boolean[] add(boolean[] var0, int var1, boolean var2) {
      return (boolean[])((boolean[])add(var0, var1, var2, Boolean.TYPE));
   }

   public static char[] add(char[] var0, int var1, char var2) {
      return (char[])((char[])add(var0, var1, var2, Character.TYPE));
   }

   public static byte[] add(byte[] var0, int var1, byte var2) {
      return (byte[])((byte[])add(var0, var1, var2, Byte.TYPE));
   }

   public static short[] add(short[] var0, int var1, short var2) {
      return (short[])((short[])add(var0, var1, var2, Short.TYPE));
   }

   public static int[] add(int[] var0, int var1, int var2) {
      return (int[])((int[])add(var0, var1, var2, Integer.TYPE));
   }

   public static long[] add(long[] var0, int var1, long var2) {
      return (long[])((long[])add(var0, var1, var2, Long.TYPE));
   }

   public static float[] add(float[] var0, int var1, float var2) {
      return (float[])((float[])add(var0, var1, var2, Float.TYPE));
   }

   public static double[] add(double[] var0, int var1, double var2) {
      return (double[])((double[])add(var0, var1, var2, Double.TYPE));
   }

   private static Object add(Object var0, int var1, Object var2, Class var3) {
      if (var0 == null) {
         if (var1 != 0) {
            throw new IndexOutOfBoundsException("Index: " + var1 + ", Length: 0");
         } else {
            Object var6 = Array.newInstance(var3, 1);
            Array.set(var6, 0, var2);
            return var6;
         }
      } else {
         int var4 = Array.getLength(var0);
         if (var1 <= var4 && var1 >= 0) {
            Object var5 = Array.newInstance(var3, var4 + 1);
            System.arraycopy(var0, 0, var5, 0, var1);
            Array.set(var5, var1, var2);
            if (var1 < var4) {
               System.arraycopy(var0, var1, var5, var1 + 1, var4 - var1);
            }

            return var5;
         } else {
            throw new IndexOutOfBoundsException("Index: " + var1 + ", Length: " + var4);
         }
      }
   }

   public static Object[] remove(Object[] var0, int var1) {
      return (Object[])((Object[])remove((Object)var0, var1));
   }

   public static Object[] removeElement(Object[] var0, Object var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static boolean[] remove(boolean[] var0, int var1) {
      return (boolean[])((boolean[])remove((Object)var0, var1));
   }

   public static boolean[] removeElement(boolean[] var0, boolean var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static byte[] remove(byte[] var0, int var1) {
      return (byte[])((byte[])remove((Object)var0, var1));
   }

   public static byte[] removeElement(byte[] var0, byte var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static char[] remove(char[] var0, int var1) {
      return (char[])((char[])remove((Object)var0, var1));
   }

   public static char[] removeElement(char[] var0, char var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static double[] remove(double[] var0, int var1) {
      return (double[])((double[])remove((Object)var0, var1));
   }

   public static double[] removeElement(double[] var0, double var1) {
      int var3 = indexOf(var0, var1);
      return var3 == -1 ? clone(var0) : remove(var0, var3);
   }

   public static float[] remove(float[] var0, int var1) {
      return (float[])((float[])remove((Object)var0, var1));
   }

   public static float[] removeElement(float[] var0, float var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static int[] remove(int[] var0, int var1) {
      return (int[])((int[])remove((Object)var0, var1));
   }

   public static int[] removeElement(int[] var0, int var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static long[] remove(long[] var0, int var1) {
      return (long[])((long[])remove((Object)var0, var1));
   }

   public static long[] removeElement(long[] var0, long var1) {
      int var3 = indexOf(var0, var1);
      return var3 == -1 ? clone(var0) : remove(var0, var3);
   }

   public static short[] remove(short[] var0, int var1) {
      return (short[])((short[])remove((Object)var0, var1));
   }

   public static short[] removeElement(short[] var0, short var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   private static Object remove(Object var0, int var1) {
      int var2 = getLength(var0);
      if (var1 >= 0 && var1 < var2) {
         Object var3 = Array.newInstance(var0.getClass().getComponentType(), var2 - 1);
         System.arraycopy(var0, 0, var3, 0, var1);
         if (var1 < var2 - 1) {
            System.arraycopy(var0, var1 + 1, var3, var1, var2 - var1 - 1);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Length: " + var2);
      }
   }

   public static Object[] removeAll(Object[] var0, int... var1) {
      return (Object[])((Object[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static Object[] removeElements(Object[] var0, Object... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         Object[] var3 = var1;
         int var4 = var1.length;

         Object var6;
         for(int var5 = 0; var5 < var4; ++var5) {
            var6 = var3[var5];
            MutableInt var7 = (MutableInt)var2.get(var6);
            if (var7 == null) {
               var2.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var13 = (Entry)var11.next();
            var6 = var13.getKey();
            int var14 = 0;
            int var8 = 0;

            for(int var9 = ((MutableInt)var13.getValue()).intValue(); var8 < var9; ++var8) {
               var14 = indexOf(var0, var6, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         Object[] var12 = (Object[])((Object[])removeAll((Object)var0, (BitSet)var10));
         return var12;
      } else {
         return clone(var0);
      }
   }

   public static byte[] removeAll(byte[] var0, int... var1) {
      return (byte[])((byte[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static byte[] removeElements(byte[] var0, byte... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         byte[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte var6 = var3[var5];
            Byte var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Byte var13 = (Byte)var12.getKey();
            int var14 = 0;
            int var15 = 0;

            for(int var9 = ((MutableInt)var12.getValue()).intValue(); var15 < var9; ++var15) {
               var14 = indexOf(var0, var13, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         return (byte[])((byte[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static short[] removeAll(short[] var0, int... var1) {
      return (short[])((short[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static short[] removeElements(short[] var0, short... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         short[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            short var6 = var3[var5];
            Short var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Short var13 = (Short)var12.getKey();
            int var14 = 0;
            int var15 = 0;

            for(int var9 = ((MutableInt)var12.getValue()).intValue(); var15 < var9; ++var15) {
               var14 = indexOf(var0, var13, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         return (short[])((short[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static int[] removeAll(int[] var0, int... var1) {
      return (int[])((int[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static int[] removeElements(int[] var0, int... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         int[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int var6 = var3[var5];
            Integer var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Integer var13 = (Integer)var12.getKey();
            int var14 = 0;
            int var15 = 0;

            for(int var9 = ((MutableInt)var12.getValue()).intValue(); var15 < var9; ++var15) {
               var14 = indexOf(var0, var13, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         return (int[])((int[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static char[] removeAll(char[] var0, int... var1) {
      return (char[])((char[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static char[] removeElements(char[] var0, char... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         char[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            Character var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Character var13 = (Character)var12.getKey();
            int var14 = 0;
            int var15 = 0;

            for(int var9 = ((MutableInt)var12.getValue()).intValue(); var15 < var9; ++var15) {
               var14 = indexOf(var0, var13, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         return (char[])((char[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static long[] removeAll(long[] var0, int... var1) {
      return (long[])((long[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static long[] removeElements(long[] var0, long... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         long[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            long var6 = var3[var5];
            Long var8 = var6;
            MutableInt var9 = (MutableInt)var2.get(var8);
            if (var9 == null) {
               var2.put(var8, new MutableInt(1));
            } else {
               var9.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Long var13 = (Long)var12.getKey();
            int var7 = 0;
            int var14 = 0;

            for(int var15 = ((MutableInt)var12.getValue()).intValue(); var14 < var15; ++var14) {
               var7 = indexOf(var0, var13, var7);
               if (var7 < 0) {
                  break;
               }

               var10.set(var7++);
            }
         }

         return (long[])((long[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static float[] removeAll(float[] var0, int... var1) {
      return (float[])((float[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static float[] removeElements(float[] var0, float... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         float[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            float var6 = var3[var5];
            Float var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Float var13 = (Float)var12.getKey();
            int var14 = 0;
            int var15 = 0;

            for(int var9 = ((MutableInt)var12.getValue()).intValue(); var15 < var9; ++var15) {
               var14 = indexOf(var0, var13, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         return (float[])((float[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static double[] removeAll(double[] var0, int... var1) {
      return (double[])((double[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static double[] removeElements(double[] var0, double... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(var1.length);
         double[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            double var6 = var3[var5];
            Double var8 = var6;
            MutableInt var9 = (MutableInt)var2.get(var8);
            if (var9 == null) {
               var2.put(var8, new MutableInt(1));
            } else {
               var9.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Double var13 = (Double)var12.getKey();
            int var7 = 0;
            int var14 = 0;

            for(int var15 = ((MutableInt)var12.getValue()).intValue(); var14 < var15; ++var14) {
               var7 = indexOf(var0, var13, var7);
               if (var7 < 0) {
                  break;
               }

               var10.set(var7++);
            }
         }

         return (double[])((double[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static boolean[] removeAll(boolean[] var0, int... var1) {
      return (boolean[])((boolean[])removeAll((Object)var0, (int[])clone(var1)));
   }

   public static boolean[] removeElements(boolean[] var0, boolean... var1) {
      if (var0 != null && var1 == null) {
         HashMap var2 = new HashMap(2);
         boolean[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            boolean var6 = var3[var5];
            Boolean var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var10 = new BitSet();
         Iterator var11 = var2.entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            Boolean var13 = (Boolean)var12.getKey();
            int var14 = 0;
            int var15 = 0;

            for(int var9 = ((MutableInt)var12.getValue()).intValue(); var15 < var9; ++var15) {
               var14 = indexOf(var0, var13, var14);
               if (var14 < 0) {
                  break;
               }

               var10.set(var14++);
            }
         }

         return (boolean[])((boolean[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   static Object removeAll(Object var0, int... var1) {
      int var2 = getLength(var0);
      int var3 = 0;
      int var5;
      int var6;
      if (var1 != null) {
         Arrays.sort(var1);
         int var4 = var1.length;
         var5 = var2;

         while(true) {
            --var4;
            if (var4 < 0) {
               break;
            }

            var6 = var1[var4];
            if (var6 < 0 || var6 >= var2) {
               throw new IndexOutOfBoundsException("Index: " + var6 + ", Length: " + var2);
            }

            if (var6 < var5) {
               ++var3;
               var5 = var6;
            }
         }
      }

      Object var10 = Array.newInstance(var0.getClass().getComponentType(), var2 - var3);
      if (var3 < var2) {
         var5 = var2;
         var6 = var2 - var3;

         for(int var7 = var1.length - 1; var7 >= 0; --var7) {
            int var8 = var1[var7];
            if (var5 - var8 > 1) {
               int var9 = var5 - var8 - 1;
               var6 -= var9;
               System.arraycopy(var0, var8 + 1, var10, var6, var9);
            }

            var5 = var8;
         }

         if (var5 > 0) {
            System.arraycopy(var0, 0, var10, 0, var5);
         }
      }

      return var10;
   }

   static Object removeAll(Object var0, BitSet var1) {
      int var2 = getLength(var0);
      int var3 = var1.cardinality();
      Object var4 = Array.newInstance(var0.getClass().getComponentType(), var2 - var3);
      int var5 = 0;

      int var6;
      int var7;
      int var8;
      for(var6 = 0; (var8 = var1.nextSetBit(var5)) != -1; var5 = var1.nextClearBit(var8)) {
         var7 = var8 - var5;
         if (var7 > 0) {
            System.arraycopy(var0, var5, var4, var6, var7);
            var6 += var7;
         }
      }

      var7 = var2 - var5;
      if (var7 > 0) {
         System.arraycopy(var0, var5, var4, var6, var7);
      }

      return var4;
   }
}
