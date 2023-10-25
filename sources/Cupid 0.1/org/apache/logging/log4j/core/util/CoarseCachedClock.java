package org.apache.logging.log4j.core.util;

import java.util.concurrent.locks.LockSupport;

public final class CoarseCachedClock implements Clock {
  private static volatile CoarseCachedClock instance;
  
  private static final Object INSTANCE_LOCK = new Object();
  
  private volatile long millis = System.currentTimeMillis();
  
  private final Thread updater = new Log4jThread("CoarseCachedClock Updater Thread") {
      public void run() {
        while (true) {
          CoarseCachedClock.this.millis = System.currentTimeMillis();
          LockSupport.parkNanos(1000000L);
        } 
      }
    };
  
  private CoarseCachedClock() {
    this.updater.setDaemon(true);
    this.updater.start();
  }
  
  public static CoarseCachedClock instance() {
    CoarseCachedClock result = instance;
    if (result == null)
      synchronized (INSTANCE_LOCK) {
        result = instance;
        if (result == null)
          instance = result = new CoarseCachedClock(); 
      }  
    return result;
  }
  
  public long currentTimeMillis() {
    return this.millis;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\CoarseCachedClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */