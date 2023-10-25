package org.apache.logging.log4j.core.util;

public final class SystemMillisClock implements Clock {
  public long currentTimeMillis() {
    return System.currentTimeMillis();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\SystemMillisClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */