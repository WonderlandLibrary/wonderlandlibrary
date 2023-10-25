package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;

public final class Closer {
  public static boolean close(AutoCloseable closeable) throws Exception {
    if (closeable != null) {
      StatusLogger.getLogger().debug("Closing {} {}", closeable.getClass().getSimpleName(), closeable);
      closeable.close();
      return true;
    } 
    return false;
  }
  
  public static boolean closeSilently(AutoCloseable closeable) {
    try {
      return close(closeable);
    } catch (Exception ignored) {
      return false;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */