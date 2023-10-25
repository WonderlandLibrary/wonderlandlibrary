package org.apache.logging.log4j.core.config.plugins.convert;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DateTypeConverter {
  private static final Map<Class<? extends Date>, MethodHandle> CONSTRUCTORS = new ConcurrentHashMap<>();
  
  static {
    MethodHandles.Lookup lookup = MethodHandles.publicLookup();
    for (Class<? extends Date> dateClass : Arrays.<Class<?>[]>asList((Class<?>[][])new Class[] { Date.class, Date.class, Time.class, Timestamp.class })) {
      try {
        CONSTRUCTORS.put(dateClass, lookup
            .findConstructor(dateClass, MethodType.methodType(void.class, long.class)));
      } catch (NoSuchMethodException|IllegalAccessException noSuchMethodException) {}
    } 
  }
  
  public static <D extends Date> D fromMillis(long millis, Class<D> type) {
    try {
      return (D)((MethodHandle)CONSTRUCTORS.get(type)).invoke(millis);
    } catch (Throwable ignored) {
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\convert\DateTypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */