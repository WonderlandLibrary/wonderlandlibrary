package org.apache.logging.log4j.core.helpers;

import java.util.concurrent.locks.LockSupport;

public final class CachedClock implements Clock {
   private static final int UPDATE_THRESHOLD = 1023;
   private static CachedClock instance = new CachedClock();
   private volatile long millis = System.currentTimeMillis();
   private volatile short count = 0;
   private final Thread updater = new Thread(this, "Clock Updater Thread") {
      final CachedClock this$0;

      {
         this.this$0 = var1;
      }

      public void run() {
         while(true) {
            long var1 = System.currentTimeMillis();
            CachedClock.access$002(this.this$0, var1);
            LockSupport.parkNanos(1000000L);
         }
      }
   };

   private CachedClock() {
      this.updater.setDaemon(true);
      this.updater.start();
   }

   public static CachedClock instance() {
      return instance;
   }

   public long currentTimeMillis() {
      if ((++this.count & 1023) == 1023) {
         this.millis = System.currentTimeMillis();
      }

      return this.millis;
   }

   static long access$002(CachedClock var0, long var1) {
      return var0.millis = var1;
   }
}
