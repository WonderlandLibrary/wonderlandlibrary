package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.status.StatusLogger;

public final class ClockFactory {
   public static final String PROPERTY_NAME = "log4j.Clock";
   private static final StatusLogger LOGGER = StatusLogger.getLogger();

   private ClockFactory() {
   }

   public static Clock getClock() {
      return createClock();
   }

   private static Clock createClock() {
      String var0 = System.getProperty("log4j.Clock");
      if (var0 != null && !"SystemClock".equals(var0)) {
         if (!CachedClock.class.getName().equals(var0) && !"CachedClock".equals(var0)) {
            if (!CoarseCachedClock.class.getName().equals(var0) && !"CoarseCachedClock".equals(var0)) {
               try {
                  Clock var1 = (Clock)Class.forName(var0).newInstance();
                  LOGGER.debug("Using {} for timestamps", new Object[]{var0});
                  return var1;
               } catch (Exception var3) {
                  String var2 = "Could not create {}: {}, using default SystemClock for timestamps";
                  LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps", new Object[]{var0, var3});
                  return new SystemClock();
               }
            } else {
               LOGGER.debug("Using specified CoarseCachedClock for timestamps");
               return CoarseCachedClock.instance();
            }
         } else {
            LOGGER.debug("Using specified CachedClock for timestamps");
            return CachedClock.instance();
         }
      } else {
         LOGGER.debug("Using default SystemClock for timestamps");
         return new SystemClock();
      }
   }
}
