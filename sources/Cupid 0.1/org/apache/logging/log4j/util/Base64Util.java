package org.apache.logging.log4j.util;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LoggingException;

public final class Base64Util {
  private static Method encodeMethod = null;
  
  private static Object encoder = null;
  
  static {
    try {
      Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
      Class<?> encoderClazz = LoaderUtil.loadClass("java.util.Base64$Encoder");
      Method method = clazz.getMethod("getEncoder", new Class[0]);
      encoder = method.invoke(null, new Object[0]);
      encodeMethod = encoderClazz.getMethod("encodeToString", new Class[] { byte[].class });
    } catch (Exception ex) {
      try {
        Class<?> clazz = LoaderUtil.loadClass("javax.xml.bind.DataTypeConverter");
        encodeMethod = clazz.getMethod("printBase64Binary", new Class[0]);
      } catch (Exception ex2) {
        LowLevelLogUtil.logException("Unable to create a Base64 Encoder", ex2);
      } 
    } 
  }
  
  public static String encode(String str) {
    if (str == null)
      return null; 
    byte[] data = str.getBytes();
    if (encodeMethod != null)
      try {
        return (String)encodeMethod.invoke(encoder, new Object[] { data });
      } catch (Exception ex) {
        throw new LoggingException("Unable to encode String", ex);
      }  
    throw new LoggingException("No Encoder, unable to encode string");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Base64Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */