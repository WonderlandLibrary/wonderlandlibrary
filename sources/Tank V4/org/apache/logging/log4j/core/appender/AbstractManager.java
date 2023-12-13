package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractManager {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static final Map MAP = new HashMap();
   private static final Lock LOCK = new ReentrantLock();
   protected int count;
   private final String name;

   protected AbstractManager(String var1) {
      this.name = var1;
      LOGGER.debug("Starting {} {}", this.getClass().getSimpleName(), var1);
   }

   public static AbstractManager getManager(String var0, ManagerFactory var1, Object var2) {
      LOCK.lock();
      AbstractManager var3 = (AbstractManager)MAP.get(var0);
      if (var3 == null) {
         var3 = (AbstractManager)var1.createManager(var0, var2);
         if (var3 == null) {
            throw new IllegalStateException("Unable to create a manager");
         }

         MAP.put(var0, var3);
      }

      ++var3.count;
      LOCK.unlock();
      return var3;
   }

   public static boolean hasManager(String var0) {
      LOCK.lock();
      boolean var1 = MAP.containsKey(var0);
      LOCK.unlock();
      return var1;
   }

   protected void releaseSub() {
   }

   protected int getCount() {
      return this.count;
   }

   public void release() {
      LOCK.lock();
      --this.count;
      if (this.count <= 0) {
         MAP.remove(this.name);
         LOGGER.debug("Shutting down {} {}", this.getClass().getSimpleName(), this.getName());
         this.releaseSub();
      }

      LOCK.unlock();
   }

   public String getName() {
      return this.name;
   }

   public Map getContentFormat() {
      return new HashMap();
   }
}
