package org.apache.logging.log4j.core.config.plugins.convert;

import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.LoaderUtil;

public class Base64Converter {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static Method method = null;
  
  private static Object decoder = null;
  
  static {
    try {
      Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
      Method getDecoder = clazz.getMethod("getDecoder", (Class[])null);
      decoder = getDecoder.invoke(null, (Object[])null);
      clazz = decoder.getClass();
      method = clazz.getMethod("decode", new Class[] { String.class });
    } catch (ClassNotFoundException|NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException classNotFoundException) {}
    if (method == null)
      try {
        Class<?> clazz = LoaderUtil.loadClass("javax.xml.bind.DatatypeConverter");
        method = clazz.getMethod("parseBase64Binary", new Class[] { String.class });
      } catch (ClassNotFoundException ex) {
        LOGGER.error("No Base64 Converter is available");
      } catch (NoSuchMethodException noSuchMethodException) {} 
  }
  
  public static byte[] parseBase64Binary(String encoded) {
    if (method == null) {
      LOGGER.error("No base64 converter");
    } else {
      try {
        return (byte[])method.invoke(decoder, new Object[] { encoded });
      } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
        LOGGER.error("Error decoding string - " + ex.getMessage());
      } 
    } 
    return Constants.EMPTY_BYTE_ARRAY;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\convert\Base64Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */