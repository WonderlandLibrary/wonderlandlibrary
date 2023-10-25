package org.apache.logging.log4j.core.util;

import javax.naming.Context;
import javax.naming.NamingException;

public final class JndiCloser {
  public static void close(Context context) throws NamingException {
    if (context != null)
      context.close(); 
  }
  
  public static boolean closeSilently(Context context) {
    try {
      close(context);
      return true;
    } catch (NamingException ignored) {
      return false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\JndiCloser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */