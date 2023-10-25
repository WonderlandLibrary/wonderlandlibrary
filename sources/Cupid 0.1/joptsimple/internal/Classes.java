package joptsimple.internal;

import java.util.HashMap;
import java.util.Map;

public final class Classes {
  private static final Map<Class<?>, Class<?>> WRAPPERS = new HashMap<Class<?>, Class<?>>(13);
  
  static {
    WRAPPERS.put(boolean.class, Boolean.class);
    WRAPPERS.put(byte.class, Byte.class);
    WRAPPERS.put(char.class, Character.class);
    WRAPPERS.put(double.class, Double.class);
    WRAPPERS.put(float.class, Float.class);
    WRAPPERS.put(int.class, Integer.class);
    WRAPPERS.put(long.class, Long.class);
    WRAPPERS.put(short.class, Short.class);
    WRAPPERS.put(void.class, Void.class);
  }
  
  private Classes() {
    throw new UnsupportedOperationException();
  }
  
  public static String shortNameOf(String className) {
    return className.substring(className.lastIndexOf('.') + 1);
  }
  
  public static <T> Class<T> wrapperOf(Class<T> clazz) {
    return clazz.isPrimitive() ? (Class<T>)WRAPPERS.get(clazz) : clazz;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\internal\Classes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */