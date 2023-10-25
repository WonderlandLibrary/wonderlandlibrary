package org.apache.logging.log4j.core.util;

public final class SystemNanoClock implements NanoClock {
  public long nanoTime() {
    return System.nanoTime();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\SystemNanoClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */