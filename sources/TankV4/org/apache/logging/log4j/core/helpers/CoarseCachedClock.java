package org.apache.logging.log4j.core.helpers;

import java.util.concurrent.locks.LockSupport;

public final class CoarseCachedClock implements Clock {
   private static CoarseCachedClock instance = new CoarseCachedClock();
   private volatile long millis = System.currentTimeMillis();
   private final Thread updater = new Thread(this, "Clock Updater Thread") {
      final CoarseCachedClock this$0;

      {
         this.this$0 = var1;
      }

      public void run() {
         while(true) {
            long var1 = System.currentTimeMillis();
            CoarseCachedClock.access$002(this.this$0, var1);
            LockSupport.parkNanos(1000000L);
         }
      }
   };

   private CoarseCachedClock() {
      this.updater.setDaemon(true);
      this.updater.start();
   }

   public static CoarseCachedClock instance() {
      return instance;
   }

   public long currentTimeMillis() {
      return this.millis;
   }

   static long access$002(CoarseCachedClock var0, long var1) {
      return var0.millis = var1;
   }
}
