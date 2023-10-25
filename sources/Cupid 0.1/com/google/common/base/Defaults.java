package com.google.common.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Defaults {
  private static final Map<Class<?>, Object> DEFAULTS;
  
  static {
    Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    put(map, boolean.class, Boolean.valueOf(false));
    put(map, char.class, Character.valueOf(false));
    put(map, byte.class, Byte.valueOf((byte)0));
    put(map, short.class, Short.valueOf((short)0));
    put(map, int.class, Integer.valueOf(0));
    put(map, long.class, Long.valueOf(0L));
    put(map, float.class, Float.valueOf(0.0F));
    put(map, double.class, Double.valueOf(0.0D));
    DEFAULTS = Collections.unmodifiableMap(map);
  }
  
  private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
    map.put(type, value);
  }
  
  public static <T> T defaultValue(Class<T> type) {
    T t = (T)DEFAULTS.get(Preconditions.checkNotNull(type));
    return t;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\Defaults.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */