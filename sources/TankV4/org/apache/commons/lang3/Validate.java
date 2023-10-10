package org.apache.commons.lang3;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class Validate {
   private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
   private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
   private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
   private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
   private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
   private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
   private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
   private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
   private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
   private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
   private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
   private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
   private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
   private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
   private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
   private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
   private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
   private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";

   public static void isTrue(boolean var0, String var1, long var2) {
      if (!var0) {
         throw new IllegalArgumentException(String.format(var1, var2));
      }
   }

   public static void isTrue(boolean var0, String var1, double var2) {
      if (!var0) {
         throw new IllegalArgumentException(String.format(var1, var2));
      }
   }

   public static void isTrue(boolean var0, String var1, Object... var2) {
      if (!var0) {
         throw new IllegalArgumentException(String.format(var1, var2));
      }
   }

   public static void isTrue(boolean var0) {
      if (!var0) {
         throw new IllegalArgumentException("The validated expression is false");
      }
   }

   public static Object notNull(Object var0) {
      return notNull(var0, "The validated object is null");
   }

   public static Object notNull(Object var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new NullPointerException(String.format(var1, var2));
      } else {
         return var0;
      }
   }

   public static Object[] notEmpty(Object[] var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new NullPointerException(String.format(var1, var2));
      } else if (var0.length == 0) {
         throw new IllegalArgumentException(String.format(var1, var2));
      } else {
         return var0;
      }
   }

   public static Object[] notEmpty(Object[] var0) {
      return notEmpty(var0, "The validated array is empty");
   }

   public static Collection notEmpty(Collection var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new NullPointerException(String.format(var1, var2));
      } else if (var0.isEmpty()) {
         throw new IllegalArgumentException(String.format(var1, var2));
      } else {
         return var0;
      }
   }

   public static Collection notEmpty(Collection var0) {
      return notEmpty(var0, "The validated collection is empty");
   }

   public static Map notEmpty(Map var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new NullPointerException(String.format(var1, var2));
      } else if (var0.isEmpty()) {
         throw new IllegalArgumentException(String.format(var1, var2));
      } else {
         return var0;
      }
   }

   public static Map notEmpty(Map var0) {
      return notEmpty(var0, "The validated map is empty");
   }

   public static CharSequence notEmpty(CharSequence var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new NullPointerException(String.format(var1, var2));
      } else if (var0.length() == 0) {
         throw new IllegalArgumentException(String.format(var1, var2));
      } else {
         return var0;
      }
   }

   public static CharSequence notEmpty(CharSequence var0) {
      return notEmpty(var0, "The validated character sequence is empty");
   }

   public static CharSequence notBlank(CharSequence var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new NullPointerException(String.format(var1, var2));
      } else if (StringUtils.isBlank(var0)) {
         throw new IllegalArgumentException(String.format(var1, var2));
      } else {
         return var0;
      }
   }

   public static CharSequence notBlank(CharSequence var0) {
      return notBlank(var0, "The validated character sequence is blank");
   }

   public static Object[] noNullElements(Object[] var0, String var1, Object... var2) {
      notNull(var0);

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (var0[var3] == null) {
            Object[] var4 = ArrayUtils.add(var2, var3);
            throw new IllegalArgumentException(String.format(var1, var4));
         }
      }

      return var0;
   }

   public static Object[] noNullElements(Object[] var0) {
      return noNullElements(var0, "The validated array contains null element at index: %d");
   }

   public static Iterable noNullElements(Iterable var0, String var1, Object... var2) {
      notNull(var0);
      int var3 = 0;

      for(Iterator var4 = var0.iterator(); var4.hasNext(); ++var3) {
         if (var4.next() == null) {
            Object[] var5 = ArrayUtils.addAll(var2, var3);
            throw new IllegalArgumentException(String.format(var1, var5));
         }
      }

      return var0;
   }

   public static Iterable noNullElements(Iterable var0) {
      return noNullElements(var0, "The validated collection contains null element at index: %d");
   }

   public static Object[] validIndex(Object[] var0, int var1, String var2, Object... var3) {
      notNull(var0);
      if (var1 >= 0 && var1 < var0.length) {
         return var0;
      } else {
         throw new IndexOutOfBoundsException(String.format(var2, var3));
      }
   }

   public static Object[] validIndex(Object[] var0, int var1) {
      return validIndex(var0, var1, "The validated array index is invalid: %d", var1);
   }

   public static Collection validIndex(Collection var0, int var1, String var2, Object... var3) {
      notNull(var0);
      if (var1 >= 0 && var1 < var0.size()) {
         return var0;
      } else {
         throw new IndexOutOfBoundsException(String.format(var2, var3));
      }
   }

   public static Collection validIndex(Collection var0, int var1) {
      return validIndex(var0, var1, "The validated collection index is invalid: %d", var1);
   }

   public static CharSequence validIndex(CharSequence var0, int var1, String var2, Object... var3) {
      notNull(var0);
      if (var1 >= 0 && var1 < var0.length()) {
         return var0;
      } else {
         throw new IndexOutOfBoundsException(String.format(var2, var3));
      }
   }

   public static CharSequence validIndex(CharSequence var0, int var1) {
      return validIndex(var0, var1, "The validated character sequence index is invalid: %d", var1);
   }

   public static void validState(boolean var0) {
      if (!var0) {
         throw new IllegalStateException("The validated state is false");
      }
   }

   public static void validState(boolean var0, String var1, Object... var2) {
      if (!var0) {
         throw new IllegalStateException(String.format(var1, var2));
      }
   }

   public static void matchesPattern(CharSequence var0, String var1) {
      if (!Pattern.matches(var1, var0)) {
         throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", var0, var1));
      }
   }

   public static void matchesPattern(CharSequence var0, String var1, String var2, Object... var3) {
      if (!Pattern.matches(var1, var0)) {
         throw new IllegalArgumentException(String.format(var2, var3));
      }
   }

   public static void inclusiveBetween(Object var0, Object var1, Comparable var2) {
      if (var2.compareTo(var0) < 0 || var2.compareTo(var1) > 0) {
         throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", var2, var0, var1));
      }
   }

   public static void inclusiveBetween(Object var0, Object var1, Comparable var2, String var3, Object... var4) {
      if (var2.compareTo(var0) < 0 || var2.compareTo(var1) > 0) {
         throw new IllegalArgumentException(String.format(var3, var4));
      }
   }

   public static void inclusiveBetween(long var0, long var2, long var4) {
      if (var4 < var0 || var4 > var2) {
         throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", var4, var0, var2));
      }
   }

   public static void inclusiveBetween(long var0, long var2, long var4, String var6) {
      if (var4 < var0 || var4 > var2) {
         throw new IllegalArgumentException(String.format(var6));
      }
   }

   public static void inclusiveBetween(double var0, double var2, double var4) {
      if (var4 < var0 || var4 > var2) {
         throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", var4, var0, var2));
      }
   }

   public static void inclusiveBetween(double var0, double var2, double var4, String var6) {
      if (var4 < var0 || var4 > var2) {
         throw new IllegalArgumentException(String.format(var6));
      }
   }

   public static void exclusiveBetween(Object var0, Object var1, Comparable var2) {
      if (var2.compareTo(var0) <= 0 || var2.compareTo(var1) >= 0) {
         throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", var2, var0, var1));
      }
   }

   public static void exclusiveBetween(Object var0, Object var1, Comparable var2, String var3, Object... var4) {
      if (var2.compareTo(var0) <= 0 || var2.compareTo(var1) >= 0) {
         throw new IllegalArgumentException(String.format(var3, var4));
      }
   }

   public static void exclusiveBetween(long var0, long var2, long var4) {
      if (var4 <= var0 || var4 >= var2) {
         throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", var4, var0, var2));
      }
   }

   public static void exclusiveBetween(long var0, long var2, long var4, String var6) {
      if (var4 <= var0 || var4 >= var2) {
         throw new IllegalArgumentException(String.format(var6));
      }
   }

   public static void exclusiveBetween(double var0, double var2, double var4) {
      if (var4 <= var0 || var4 >= var2) {
         throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", var4, var0, var2));
      }
   }

   public static void exclusiveBetween(double var0, double var2, double var4, String var6) {
      if (var4 <= var0 || var4 >= var2) {
         throw new IllegalArgumentException(String.format(var6));
      }
   }

   public static void isInstanceOf(Class var0, Object var1) {
      if (!var0.isInstance(var1)) {
         throw new IllegalArgumentException(String.format("Expected type: %s, actual: %s", var0.getName(), var1 == null ? "null" : var1.getClass().getName()));
      }
   }

   public static void isInstanceOf(Class var0, Object var1, String var2, Object... var3) {
      if (!var0.isInstance(var1)) {
         throw new IllegalArgumentException(String.format(var2, var3));
      }
   }

   public static void isAssignableFrom(Class var0, Class var1) {
      if (!var0.isAssignableFrom(var1)) {
         throw new IllegalArgumentException(String.format("Cannot assign a %s to a %s", var1 == null ? "null" : var1.getName(), var0.getName()));
      }
   }

   public static void isAssignableFrom(Class var0, Class var1, String var2, Object... var3) {
      if (!var0.isAssignableFrom(var1)) {
         throw new IllegalArgumentException(String.format(var2, var3));
      }
   }
}
