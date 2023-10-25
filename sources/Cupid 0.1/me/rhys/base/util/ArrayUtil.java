package me.rhys.base.util;

public class ArrayUtil {
  public static String[] offset(String[] array, int offset) {
    if (offset >= array.length)
      return null; 
    String[] copy = new String[array.length - offset];
    if (array.length - offset >= 0)
      System.arraycopy(array, offset, copy, 0, array.length - offset); 
    return copy;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\ArrayUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */