package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;

@Immutable
public class CloneUtils {
  public static <T> T cloneObject(T obj) throws CloneNotSupportedException {
    if (obj == null)
      return null; 
    if (obj instanceof Cloneable) {
      Method m;
      Class<?> clazz = obj.getClass();
      try {
        m = clazz.getMethod("clone", (Class[])null);
      } catch (NoSuchMethodException ex) {
        throw new NoSuchMethodError(ex.getMessage());
      } 
      try {
        T result = (T)m.invoke(obj, (Object[])null);
        return result;
      } catch (InvocationTargetException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof CloneNotSupportedException)
          throw (CloneNotSupportedException)cause; 
        throw new Error("Unexpected exception", cause);
      } catch (IllegalAccessException ex) {
        throw new IllegalAccessError(ex.getMessage());
      } 
    } 
    throw new CloneNotSupportedException();
  }
  
  public static Object clone(Object obj) throws CloneNotSupportedException {
    return cloneObject(obj);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\clien\\utils\CloneUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */