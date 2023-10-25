package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;

@Immutable
public class JdkIdn implements Idn {
  private final Method toUnicode;
  
  public JdkIdn() throws ClassNotFoundException {
    Class<?> clazz = Class.forName("java.net.IDN");
    try {
      this.toUnicode = clazz.getMethod("toUnicode", new Class[] { String.class });
    } catch (SecurityException e) {
      throw new IllegalStateException(e.getMessage(), e);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e.getMessage(), e);
    } 
  }
  
  public String toUnicode(String punycode) {
    try {
      return (String)this.toUnicode.invoke(null, new Object[] { punycode });
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      Throwable t = e.getCause();
      throw new RuntimeException(t.getMessage(), t);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\clien\\utils\JdkIdn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */