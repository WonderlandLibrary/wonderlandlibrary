package org.apache.logging.log4j.core.util;

import java.lang.reflect.Array;

public class ArrayUtils {
  public static boolean isEmpty(byte[] array) {
    return (getLength(array) == 0);
  }
  
  public static int getLength(Object array) {
    if (array == null)
      return 0; 
    return Array.getLength(array);
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
  
  public static <T> T[] remove(T[] array, int index) {
    return (T[])remove(array, index);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\ArrayUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */