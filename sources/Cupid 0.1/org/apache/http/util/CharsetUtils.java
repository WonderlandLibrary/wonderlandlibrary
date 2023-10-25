package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetUtils {
  public static Charset lookup(String name) {
    if (name == null)
      return null; 
    try {
      return Charset.forName(name);
    } catch (UnsupportedCharsetException ex) {
      return null;
    } 
  }
  
  public static Charset get(String name) throws UnsupportedEncodingException {
    if (name == null)
      return null; 
    try {
      return Charset.forName(name);
    } catch (UnsupportedCharsetException ex) {
      throw new UnsupportedEncodingException(name);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\htt\\util\CharsetUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */