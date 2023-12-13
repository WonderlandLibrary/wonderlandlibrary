package org.apache.logging.log4j.core.helpers;

public class SystemClock implements Clock {
   public long currentTimeMillis() {
      return System.currentTimeMillis();
   }
}
