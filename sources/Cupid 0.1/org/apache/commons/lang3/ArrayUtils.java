package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.mutable.MutableInt;

public class ArrayUtils {
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  
  public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
  
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
  
  public static String toString(Object array) {
    return toString(array, "{}");
  }
  
  public static String toString(Object array, String stringIfNull) {
    if (array == null)
      return stringIfNull; 
    return (new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE)).append(array).toString();
  }
  
  public static int hashCode(Object array) {
    return (new HashCodeBuilder()).append(array).toHashCode();
  }
  
  @Deprecated
  public static boolean isEquals(Object array1, Object array2) {
    return (new EqualsBuilder()).append(array1, array2).isEquals();
  }
  
  public static Map<Object, Object> toMap(Object[] array) {
    if (array == null)
      return null; 
    Map<Object, Object> map = new HashMap<Object, Object>((int)(array.length * 1.5D));
    for (int i = 0; i < array.length; i++) {
      Object object = array[i];
      if (object instanceof Map.Entry) {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
        map.put(entry.getKey(), entry.getValue());
      } else if (object instanceof Object[]) {
        Object[] entry = (Object[])object;
        if (entry.length < 2)
          throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2"); 
        map.put(entry[0], entry[1]);
      } else {
        throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
      } 
    } 
    return map;
  }
  
  public static <T> T[] toArray(T... items) {
    return items;
  }
  
  public static <T> T[] clone(T[] array) {
    if (array == null)
      return null; 
    return (T[])array.clone();
  }
  
  public static long[] clone(long[] array) {
    if (array == null)
      return null; 
    return (long[])array.clone();
  }
  
  public static int[] clone(int[] array) {
    if (array == null)
      return null; 
    return (int[])array.clone();
  }
  
  public static short[] clone(short[] array) {
    if (array == null)
      return null; 
    return (short[])array.clone();
  }
  
  public static char[] clone(char[] array) {
    if (array == null)
      return null; 
    return (char[])array.clone();
  }
  
  public static byte[] clone(byte[] array) {
    if (array == null)
      return null; 
    return (byte[])array.clone();
  }
  
  public static double[] clone(double[] array) {
    if (array == null)
      return null; 
    return (double[])array.clone();
  }
  
  public static float[] clone(float[] array) {
    if (array == null)
      return null; 
    return (float[])array.clone();
  }
  
  public static boolean[] clone(boolean[] array) {
    if (array == null)
      return null; 
    return (boolean[])array.clone();
  }
  
  public static Object[] nullToEmpty(Object[] array) {
    if (array == null || array.length == 0)
      return EMPTY_OBJECT_ARRAY; 
    return array;
  }
  
  public static Class<?>[] nullToEmpty(Class<?>[] array) {
    if (array == null || array.length == 0)
      return EMPTY_CLASS_ARRAY; 
    return array;
  }
  
  public static String[] nullToEmpty(String[] array) {
    if (array == null || array.length == 0)
      return EMPTY_STRING_ARRAY; 
    return array;
  }
  
  public static long[] nullToEmpty(long[] array) {
    if (array == null || array.length == 0)
      return EMPTY_LONG_ARRAY; 
    return array;
  }
  
  public static int[] nullToEmpty(int[] array) {
    if (array == null || array.length == 0)
      return EMPTY_INT_ARRAY; 
    return array;
  }
  
  public static short[] nullToEmpty(short[] array) {
    if (array == null || array.length == 0)
      return EMPTY_SHORT_ARRAY; 
    return array;
  }
  
  public static char[] nullToEmpty(char[] array) {
    if (array == null || array.length == 0)
      return EMPTY_CHAR_ARRAY; 
    return array;
  }
  
  public static byte[] nullToEmpty(byte[] array) {
    if (array == null || array.length == 0)
      return EMPTY_BYTE_ARRAY; 
    return array;
  }
  
  public static double[] nullToEmpty(double[] array) {
    if (array == null || array.length == 0)
      return EMPTY_DOUBLE_ARRAY; 
    return array;
  }
  
  public static float[] nullToEmpty(float[] array) {
    if (array == null || array.length == 0)
      return EMPTY_FLOAT_ARRAY; 
    return array;
  }
  
  public static boolean[] nullToEmpty(boolean[] array) {
    if (array == null || array.length == 0)
      return EMPTY_BOOLEAN_ARRAY; 
    return array;
  }
  
  public static Long[] nullToEmpty(Long[] array) {
    if (array == null || array.length == 0)
      return EMPTY_LONG_OBJECT_ARRAY; 
    return array;
  }
  
  public static Integer[] nullToEmpty(Integer[] array) {
    if (array == null || array.length == 0)
      return EMPTY_INTEGER_OBJECT_ARRAY; 
    return array;
  }
  
  public static Short[] nullToEmpty(Short[] array) {
    if (array == null || array.length == 0)
      return EMPTY_SHORT_OBJECT_ARRAY; 
    return array;
  }
  
  public static Character[] nullToEmpty(Character[] array) {
    if (array == null || array.length == 0)
      return EMPTY_CHARACTER_OBJECT_ARRAY; 
    return array;
  }
  
  public static Byte[] nullToEmpty(Byte[] array) {
    if (array == null || array.length == 0)
      return EMPTY_BYTE_OBJECT_ARRAY; 
    return array;
  }
  
  public static Double[] nullToEmpty(Double[] array) {
    if (array == null || array.length == 0)
      return EMPTY_DOUBLE_OBJECT_ARRAY; 
    return array;
  }
  
  public static Float[] nullToEmpty(Float[] array) {
    if (array == null || array.length == 0)
      return EMPTY_FLOAT_OBJECT_ARRAY; 
    return array;
  }
  
  public static Boolean[] nullToEmpty(Boolean[] array) {
    if (array == null || array.length == 0)
      return EMPTY_BOOLEAN_OBJECT_ARRAY; 
    return array;
  }
  
  public static <T> T[] subarray(T[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    Class<?> type = array.getClass().getComponentType();
    if (newSize <= 0) {
      T[] emptyArray = (T[])Array.newInstance(type, 0);
      return emptyArray;
    } 
    T[] subarray = (T[])Array.newInstance(type, newSize);
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static long[] subarray(long[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_LONG_ARRAY; 
    long[] subarray = new long[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static int[] subarray(int[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_INT_ARRAY; 
    int[] subarray = new int[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static short[] subarray(short[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_SHORT_ARRAY; 
    short[] subarray = new short[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static char[] subarray(char[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_CHAR_ARRAY; 
    char[] subarray = new char[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_BYTE_ARRAY; 
    byte[] subarray = new byte[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static double[] subarray(double[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_DOUBLE_ARRAY; 
    double[] subarray = new double[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static float[] subarray(float[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_FLOAT_ARRAY; 
    float[] subarray = new float[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static boolean[] subarray(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return null; 
    if (startIndexInclusive < 0)
      startIndexInclusive = 0; 
    if (endIndexExclusive > array.length)
      endIndexExclusive = array.length; 
    int newSize = endIndexExclusive - startIndexInclusive;
    if (newSize <= 0)
      return EMPTY_BOOLEAN_ARRAY; 
    boolean[] subarray = new boolean[newSize];
    System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
    return subarray;
  }
  
  public static boolean isSameLength(Object[] array1, Object[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(long[] array1, long[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(int[] array1, int[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(short[] array1, short[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(char[] array1, char[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(byte[] array1, byte[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(double[] array1, double[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(float[] array1, float[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static boolean isSameLength(boolean[] array1, boolean[] array2) {
    if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length != array2.length))
      return false; 
    return true;
  }
  
  public static int getLength(Object array) {
    if (array == null)
      return 0; 
    return Array.getLength(array);
  }
  
  public static boolean isSameType(Object array1, Object array2) {
    if (array1 == null || array2 == null)
      throw new IllegalArgumentException("The Array must not be null"); 
    return array1.getClass().getName().equals(array2.getClass().getName());
  }
  
  public static void reverse(Object[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(long[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(int[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(short[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(char[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(byte[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(double[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(float[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(boolean[] array) {
    if (array == null)
      return; 
    reverse(array, 0, array.length);
  }
  
  public static void reverse(boolean[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      boolean tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(byte[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      byte tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(char[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      char tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(double[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      double tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(float[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      float tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(int[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      int tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(long[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      long tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(Object[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      Object tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static void reverse(short[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null)
      return; 
    int i = (startIndexInclusive < 0) ? 0 : startIndexInclusive;
    int j = Math.min(array.length, endIndexExclusive) - 1;
    while (j > i) {
      short tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    } 
  }
  
  public static int indexOf(Object[] array, Object objectToFind) {
    return indexOf(array, objectToFind, 0);
  }
  
  public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    if (objectToFind == null) {
      for (int i = startIndex; i < array.length; i++) {
        if (array[i] == null)
          return i; 
      } 
    } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
      for (int i = startIndex; i < array.length; i++) {
        if (objectToFind.equals(array[i]))
          return i; 
      } 
    } 
    return -1;
  }
  
  public static int lastIndexOf(Object[] array, Object objectToFind) {
    return lastIndexOf(array, objectToFind, 2147483647);
  }
  
  public static int lastIndexOf(Object[] array, Object objectToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    if (objectToFind == null) {
      for (int i = startIndex; i >= 0; i--) {
        if (array[i] == null)
          return i; 
      } 
    } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
      for (int i = startIndex; i >= 0; i--) {
        if (objectToFind.equals(array[i]))
          return i; 
      } 
    } 
    return -1;
  }
  
  public static boolean contains(Object[] array, Object objectToFind) {
    return (indexOf(array, objectToFind) != -1);
  }
  
  public static int indexOf(long[] array, long valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(long[] array, long valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(long[] array, long valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(long[] array, long valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(long[] array, long valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static int indexOf(int[] array, int valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(int[] array, int valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(int[] array, int valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(int[] array, int valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(int[] array, int valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static int indexOf(short[] array, short valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(short[] array, short valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(short[] array, short valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(short[] array, short valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(short[] array, short valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static int indexOf(char[] array, char valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(char[] array, char valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(char[] array, char valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(char[] array, char valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(char[] array, char valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static int indexOf(byte[] array, byte valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(byte[] array, byte valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(byte[] array, byte valueToFind, int startIndex) {
    if (array == null)
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(byte[] array, byte valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static int indexOf(double[] array, double valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(double[] array, double valueToFind, double tolerance) {
    return indexOf(array, valueToFind, 0, tolerance);
  }
  
  public static int indexOf(double[] array, double valueToFind, int startIndex) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    double min = valueToFind - tolerance;
    double max = valueToFind + tolerance;
    for (int i = startIndex; i < array.length; i++) {
      if (array[i] >= min && array[i] <= max)
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(double[] array, double valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(double[] array, double valueToFind, double tolerance) {
    return lastIndexOf(array, valueToFind, 2147483647, tolerance);
  }
  
  public static int lastIndexOf(double[] array, double valueToFind, int startIndex) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    double min = valueToFind - tolerance;
    double max = valueToFind + tolerance;
    for (int i = startIndex; i >= 0; i--) {
      if (array[i] >= min && array[i] <= max)
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(double[] array, double valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static boolean contains(double[] array, double valueToFind, double tolerance) {
    return (indexOf(array, valueToFind, 0, tolerance) != -1);
  }
  
  public static int indexOf(float[] array, float valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(float[] array, float valueToFind, int startIndex) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(float[] array, float valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(float[] array, float valueToFind, int startIndex) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(float[] array, float valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static int indexOf(boolean[] array, boolean valueToFind) {
    return indexOf(array, valueToFind, 0);
  }
  
  public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      startIndex = 0; 
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static int lastIndexOf(boolean[] array, boolean valueToFind) {
    return lastIndexOf(array, valueToFind, 2147483647);
  }
  
  public static int lastIndexOf(boolean[] array, boolean valueToFind, int startIndex) {
    if (isEmpty(array))
      return -1; 
    if (startIndex < 0)
      return -1; 
    if (startIndex >= array.length)
      startIndex = array.length - 1; 
    for (int i = startIndex; i >= 0; i--) {
      if (valueToFind == array[i])
        return i; 
    } 
    return -1;
  }
  
  public static boolean contains(boolean[] array, boolean valueToFind) {
    return (indexOf(array, valueToFind) != -1);
  }
  
  public static char[] toPrimitive(Character[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_CHAR_ARRAY; 
    char[] result = new char[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].charValue(); 
    return result;
  }
  
  public static char[] toPrimitive(Character[] array, char valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_CHAR_ARRAY; 
    char[] result = new char[array.length];
    for (int i = 0; i < array.length; i++) {
      Character b = array[i];
      result[i] = (b == null) ? valueForNull : b.charValue();
    } 
    return result;
  }
  
  public static Character[] toObject(char[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_CHARACTER_OBJECT_ARRAY; 
    Character[] result = new Character[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Character.valueOf(array[i]); 
    return result;
  }
  
  public static long[] toPrimitive(Long[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_LONG_ARRAY; 
    long[] result = new long[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].longValue(); 
    return result;
  }
  
  public static long[] toPrimitive(Long[] array, long valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_LONG_ARRAY; 
    long[] result = new long[array.length];
    for (int i = 0; i < array.length; i++) {
      Long b = array[i];
      result[i] = (b == null) ? valueForNull : b.longValue();
    } 
    return result;
  }
  
  public static Long[] toObject(long[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_LONG_OBJECT_ARRAY; 
    Long[] result = new Long[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Long.valueOf(array[i]); 
    return result;
  }
  
  public static int[] toPrimitive(Integer[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_INT_ARRAY; 
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].intValue(); 
    return result;
  }
  
  public static int[] toPrimitive(Integer[] array, int valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_INT_ARRAY; 
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      Integer b = array[i];
      result[i] = (b == null) ? valueForNull : b.intValue();
    } 
    return result;
  }
  
  public static Integer[] toObject(int[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_INTEGER_OBJECT_ARRAY; 
    Integer[] result = new Integer[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Integer.valueOf(array[i]); 
    return result;
  }
  
  public static short[] toPrimitive(Short[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_SHORT_ARRAY; 
    short[] result = new short[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].shortValue(); 
    return result;
  }
  
  public static short[] toPrimitive(Short[] array, short valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_SHORT_ARRAY; 
    short[] result = new short[array.length];
    for (int i = 0; i < array.length; i++) {
      Short b = array[i];
      result[i] = (b == null) ? valueForNull : b.shortValue();
    } 
    return result;
  }
  
  public static Short[] toObject(short[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_SHORT_OBJECT_ARRAY; 
    Short[] result = new Short[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Short.valueOf(array[i]); 
    return result;
  }
  
  public static byte[] toPrimitive(Byte[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_BYTE_ARRAY; 
    byte[] result = new byte[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].byteValue(); 
    return result;
  }
  
  public static byte[] toPrimitive(Byte[] array, byte valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_BYTE_ARRAY; 
    byte[] result = new byte[array.length];
    for (int i = 0; i < array.length; i++) {
      Byte b = array[i];
      result[i] = (b == null) ? valueForNull : b.byteValue();
    } 
    return result;
  }
  
  public static Byte[] toObject(byte[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_BYTE_OBJECT_ARRAY; 
    Byte[] result = new Byte[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Byte.valueOf(array[i]); 
    return result;
  }
  
  public static double[] toPrimitive(Double[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_DOUBLE_ARRAY; 
    double[] result = new double[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].doubleValue(); 
    return result;
  }
  
  public static double[] toPrimitive(Double[] array, double valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_DOUBLE_ARRAY; 
    double[] result = new double[array.length];
    for (int i = 0; i < array.length; i++) {
      Double b = array[i];
      result[i] = (b == null) ? valueForNull : b.doubleValue();
    } 
    return result;
  }
  
  public static Double[] toObject(double[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_DOUBLE_OBJECT_ARRAY; 
    Double[] result = new Double[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Double.valueOf(array[i]); 
    return result;
  }
  
  public static float[] toPrimitive(Float[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_FLOAT_ARRAY; 
    float[] result = new float[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].floatValue(); 
    return result;
  }
  
  public static float[] toPrimitive(Float[] array, float valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_FLOAT_ARRAY; 
    float[] result = new float[array.length];
    for (int i = 0; i < array.length; i++) {
      Float b = array[i];
      result[i] = (b == null) ? valueForNull : b.floatValue();
    } 
    return result;
  }
  
  public static Float[] toObject(float[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_FLOAT_OBJECT_ARRAY; 
    Float[] result = new Float[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = Float.valueOf(array[i]); 
    return result;
  }
  
  public static boolean[] toPrimitive(Boolean[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_BOOLEAN_ARRAY; 
    boolean[] result = new boolean[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i].booleanValue(); 
    return result;
  }
  
  public static boolean[] toPrimitive(Boolean[] array, boolean valueForNull) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_BOOLEAN_ARRAY; 
    boolean[] result = new boolean[array.length];
    for (int i = 0; i < array.length; i++) {
      Boolean b = array[i];
      result[i] = (b == null) ? valueForNull : b.booleanValue();
    } 
    return result;
  }
  
  public static Boolean[] toObject(boolean[] array) {
    if (array == null)
      return null; 
    if (array.length == 0)
      return EMPTY_BOOLEAN_OBJECT_ARRAY; 
    Boolean[] result = new Boolean[array.length];
    for (int i = 0; i < array.length; i++)
      result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE; 
    return result;
  }
  
  public static boolean isEmpty(Object[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(long[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(int[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(short[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(char[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(byte[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(double[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(float[] array) {
    return (array == null || array.length == 0);
  }
  
  public static boolean isEmpty(boolean[] array) {
    return (array == null || array.length == 0);
  }
  
  public static <T> boolean isNotEmpty(T[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(long[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(int[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(short[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(char[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(byte[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(double[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(float[] array) {
    return (array != null && array.length != 0);
  }
  
  public static boolean isNotEmpty(boolean[] array) {
    return (array != null && array.length != 0);
  }
  
  public static <T> T[] addAll(T[] array1, T... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    Class<?> type1 = array1.getClass().getComponentType();
    T[] joinedArray = (T[])Array.newInstance(type1, array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    try {
      System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    } catch (ArrayStoreException ase) {
      Class<?> type2 = array2.getClass().getComponentType();
      if (!type1.isAssignableFrom(type2))
        throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase); 
      throw ase;
    } 
    return joinedArray;
  }
  
  public static boolean[] addAll(boolean[] array1, boolean... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    boolean[] joinedArray = new boolean[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static char[] addAll(char[] array1, char... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    char[] joinedArray = new char[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static byte[] addAll(byte[] array1, byte... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    byte[] joinedArray = new byte[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static short[] addAll(short[] array1, short... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    short[] joinedArray = new short[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static int[] addAll(int[] array1, int... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    int[] joinedArray = new int[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static long[] addAll(long[] array1, long... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    long[] joinedArray = new long[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static float[] addAll(float[] array1, float... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    float[] joinedArray = new float[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static double[] addAll(double[] array1, double... array2) {
    if (array1 == null)
      return clone(array2); 
    if (array2 == null)
      return clone(array1); 
    double[] joinedArray = new double[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }
  
  public static <T> T[] add(T[] array, T element) {
    Class<?> type;
    if (array != null) {
      type = array.getClass();
    } else if (element != null) {
      type = element.getClass();
    } else {
      throw new IllegalArgumentException("Arguments cannot both be null");
    } 
    T[] newArray = (T[])copyArrayGrow1(array, type);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static boolean[] add(boolean[] array, boolean element) {
    boolean[] newArray = (boolean[])copyArrayGrow1(array, boolean.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static byte[] add(byte[] array, byte element) {
    byte[] newArray = (byte[])copyArrayGrow1(array, byte.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static char[] add(char[] array, char element) {
    char[] newArray = (char[])copyArrayGrow1(array, char.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static double[] add(double[] array, double element) {
    double[] newArray = (double[])copyArrayGrow1(array, double.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static float[] add(float[] array, float element) {
    float[] newArray = (float[])copyArrayGrow1(array, float.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static int[] add(int[] array, int element) {
    int[] newArray = (int[])copyArrayGrow1(array, int.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static long[] add(long[] array, long element) {
    long[] newArray = (long[])copyArrayGrow1(array, long.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  public static short[] add(short[] array, short element) {
    short[] newArray = (short[])copyArrayGrow1(array, short.class);
    newArray[newArray.length - 1] = element;
    return newArray;
  }
  
  private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
    if (array != null) {
      int arrayLength = Array.getLength(array);
      Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
      System.arraycopy(array, 0, newArray, 0, arrayLength);
      return newArray;
    } 
    return Array.newInstance(newArrayComponentType, 1);
  }
  
  public static <T> T[] add(T[] array, int index, T element) {
    Class<?> clss = null;
    if (array != null) {
      clss = array.getClass().getComponentType();
    } else if (element != null) {
      clss = element.getClass();
    } else {
      throw new IllegalArgumentException("Array and element cannot both be null");
    } 
    T[] newArray = (T[])add(array, index, element, clss);
    return newArray;
  }
  
  public static boolean[] add(boolean[] array, int index, boolean element) {
    return (boolean[])add(array, index, Boolean.valueOf(element), boolean.class);
  }
  
  public static char[] add(char[] array, int index, char element) {
    return (char[])add(array, index, Character.valueOf(element), char.class);
  }
  
  public static byte[] add(byte[] array, int index, byte element) {
    return (byte[])add(array, index, Byte.valueOf(element), byte.class);
  }
  
  public static short[] add(short[] array, int index, short element) {
    return (short[])add(array, index, Short.valueOf(element), short.class);
  }
  
  public static int[] add(int[] array, int index, int element) {
    return (int[])add(array, index, Integer.valueOf(element), int.class);
  }
  
  public static long[] add(long[] array, int index, long element) {
    return (long[])add(array, index, Long.valueOf(element), long.class);
  }
  
  public static float[] add(float[] array, int index, float element) {
    return (float[])add(array, index, Float.valueOf(element), float.class);
  }
  
  public static double[] add(double[] array, int index, double element) {
    return (double[])add(array, index, Double.valueOf(element), double.class);
  }
  
  private static Object add(Object array, int index, Object element, Class<?> clss) {
    if (array == null) {
      if (index != 0)
        throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0"); 
      Object joinedArray = Array.newInstance(clss, 1);
      Array.set(joinedArray, 0, element);
      return joinedArray;
    } 
    int length = Array.getLength(array);
    if (index > length || index < 0)
      throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length); 
    Object result = Array.newInstance(clss, length + 1);
    System.arraycopy(array, 0, result, 0, index);
    Array.set(result, index, element);
    if (index < length)
      System.arraycopy(array, index, result, index + 1, length - index); 
    return result;
  }
  
  public static <T> T[] remove(T[] array, int index) {
    return (T[])remove(array, index);
  }
  
  public static <T> T[] removeElement(T[] array, Object element) {
    int index = indexOf((Object[])array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static boolean[] remove(boolean[] array, int index) {
    return (boolean[])remove(array, index);
  }
  
  public static boolean[] removeElement(boolean[] array, boolean element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static byte[] remove(byte[] array, int index) {
    return (byte[])remove(array, index);
  }
  
  public static byte[] removeElement(byte[] array, byte element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static char[] remove(char[] array, int index) {
    return (char[])remove(array, index);
  }
  
  public static char[] removeElement(char[] array, char element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static double[] remove(double[] array, int index) {
    return (double[])remove(array, index);
  }
  
  public static double[] removeElement(double[] array, double element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static float[] remove(float[] array, int index) {
    return (float[])remove(array, index);
  }
  
  public static float[] removeElement(float[] array, float element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static int[] remove(int[] array, int index) {
    return (int[])remove(array, index);
  }
  
  public static int[] removeElement(int[] array, int element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static long[] remove(long[] array, int index) {
    return (long[])remove(array, index);
  }
  
  public static long[] removeElement(long[] array, long element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  public static short[] remove(short[] array, int index) {
    return (short[])remove(array, index);
  }
  
  public static short[] removeElement(short[] array, short element) {
    int index = indexOf(array, element);
    if (index == -1)
      return clone(array); 
    return remove(array, index);
  }
  
  private static Object remove(Object array, int index) {
    int length = getLength(array);
    if (index < 0 || index >= length)
      throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length); 
    Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
    System.arraycopy(array, 0, result, 0, index);
    if (index < length - 1)
      System.arraycopy(array, index + 1, result, index, length - index - 1); 
    return result;
  }
  
  public static <T> T[] removeAll(T[] array, int... indices) {
    return (T[])removeAll(array, clone(indices));
  }
  
  public static <T> T[] removeElements(T[] array, T... values) {
    if (isEmpty((Object[])array) || isEmpty((Object[])values))
      return clone(array); 
    HashMap<T, MutableInt> occurrences = new HashMap<T, MutableInt>(values.length);
    for (T v : values) {
      MutableInt count = occurrences.get(v);
      if (count == null) {
        occurrences.put(v, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
      T v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf((Object[])array, v, found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    T[] result = (T[])removeAll(array, toRemove);
    return result;
  }
  
  public static byte[] removeAll(byte[] array, int... indices) {
    return (byte[])removeAll(array, clone(indices));
  }
  
  public static byte[] removeElements(byte[] array, byte... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Byte, MutableInt> occurrences = new HashMap<Byte, MutableInt>(values.length);
    for (byte v : values) {
      Byte boxed = Byte.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Byte, MutableInt> e : occurrences.entrySet()) {
      Byte v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.byteValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (byte[])removeAll(array, toRemove);
  }
  
  public static short[] removeAll(short[] array, int... indices) {
    return (short[])removeAll(array, clone(indices));
  }
  
  public static short[] removeElements(short[] array, short... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Short, MutableInt> occurrences = new HashMap<Short, MutableInt>(values.length);
    for (short v : values) {
      Short boxed = Short.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Short, MutableInt> e : occurrences.entrySet()) {
      Short v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.shortValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (short[])removeAll(array, toRemove);
  }
  
  public static int[] removeAll(int[] array, int... indices) {
    return (int[])removeAll(array, clone(indices));
  }
  
  public static int[] removeElements(int[] array, int... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Integer, MutableInt> occurrences = new HashMap<Integer, MutableInt>(values.length);
    for (int v : values) {
      Integer boxed = Integer.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Integer, MutableInt> e : occurrences.entrySet()) {
      Integer v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.intValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (int[])removeAll(array, toRemove);
  }
  
  public static char[] removeAll(char[] array, int... indices) {
    return (char[])removeAll(array, clone(indices));
  }
  
  public static char[] removeElements(char[] array, char... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Character, MutableInt> occurrences = new HashMap<Character, MutableInt>(values.length);
    for (char v : values) {
      Character boxed = Character.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Character, MutableInt> e : occurrences.entrySet()) {
      Character v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.charValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (char[])removeAll(array, toRemove);
  }
  
  public static long[] removeAll(long[] array, int... indices) {
    return (long[])removeAll(array, clone(indices));
  }
  
  public static long[] removeElements(long[] array, long... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Long, MutableInt> occurrences = new HashMap<Long, MutableInt>(values.length);
    for (long v : values) {
      Long boxed = Long.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Long, MutableInt> e : occurrences.entrySet()) {
      Long v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.longValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (long[])removeAll(array, toRemove);
  }
  
  public static float[] removeAll(float[] array, int... indices) {
    return (float[])removeAll(array, clone(indices));
  }
  
  public static float[] removeElements(float[] array, float... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Float, MutableInt> occurrences = new HashMap<Float, MutableInt>(values.length);
    for (float v : values) {
      Float boxed = Float.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Float, MutableInt> e : occurrences.entrySet()) {
      Float v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.floatValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (float[])removeAll(array, toRemove);
  }
  
  public static double[] removeAll(double[] array, int... indices) {
    return (double[])removeAll(array, clone(indices));
  }
  
  public static double[] removeElements(double[] array, double... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Double, MutableInt> occurrences = new HashMap<Double, MutableInt>(values.length);
    for (double v : values) {
      Double boxed = Double.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Double, MutableInt> e : occurrences.entrySet()) {
      Double v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.doubleValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (double[])removeAll(array, toRemove);
  }
  
  public static boolean[] removeAll(boolean[] array, int... indices) {
    return (boolean[])removeAll(array, clone(indices));
  }
  
  public static boolean[] removeElements(boolean[] array, boolean... values) {
    if (isEmpty(array) || isEmpty(values))
      return clone(array); 
    HashMap<Boolean, MutableInt> occurrences = new HashMap<Boolean, MutableInt>(2);
    for (boolean v : values) {
      Boolean boxed = Boolean.valueOf(v);
      MutableInt count = occurrences.get(boxed);
      if (count == null) {
        occurrences.put(boxed, new MutableInt(1));
      } else {
        count.increment();
      } 
    } 
    BitSet toRemove = new BitSet();
    for (Map.Entry<Boolean, MutableInt> e : occurrences.entrySet()) {
      Boolean v = e.getKey();
      int found = 0;
      for (int i = 0, ct = ((MutableInt)e.getValue()).intValue(); i < ct; i++) {
        found = indexOf(array, v.booleanValue(), found);
        if (found < 0)
          break; 
        toRemove.set(found++);
      } 
    } 
    return (boolean[])removeAll(array, toRemove);
  }
  
  static Object removeAll(Object array, int... indices) {
    int length = getLength(array);
    int diff = 0;
    if (isNotEmpty(indices)) {
      Arrays.sort(indices);
      int i = indices.length;
      int prevIndex = length;
      while (--i >= 0) {
        int index = indices[i];
        if (index < 0 || index >= length)
          throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length); 
        if (index >= prevIndex)
          continue; 
        diff++;
        prevIndex = index;
      } 
    } 
    Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
    if (diff < length) {
      int end = length;
      int dest = length - diff;
      for (int i = indices.length - 1; i >= 0; i--) {
        int index = indices[i];
        if (end - index > 1) {
          int cp = end - index - 1;
          dest -= cp;
          System.arraycopy(array, index + 1, result, dest, cp);
        } 
        end = index;
      } 
      if (end > 0)
        System.arraycopy(array, 0, result, 0, end); 
    } 
    return result;
  }
  
  static Object removeAll(Object array, BitSet indices) {
    int srcLength = getLength(array);
    int removals = indices.cardinality();
    Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
    int srcIndex = 0;
    int destIndex = 0;
    int set;
    while ((set = indices.nextSetBit(srcIndex)) != -1) {
      int i = set - srcIndex;
      if (i > 0) {
        System.arraycopy(array, srcIndex, result, destIndex, i);
        destIndex += i;
      } 
      srcIndex = indices.nextClearBit(set);
    } 
    int count = srcLength - srcIndex;
    if (count > 0)
      System.arraycopy(array, srcIndex, result, destIndex, count); 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\ArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */