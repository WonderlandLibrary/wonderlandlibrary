package org.apache.logging.log4j.core.filter;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(
   name = "BurstFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class BurstFilter extends AbstractFilter {
   private static final long NANOS_IN_SECONDS = 1000000000L;
   private static final int DEFAULT_RATE = 10;
   private static final int DEFAULT_RATE_MULTIPLE = 100;
   private static final int HASH_SHIFT = 32;
   private final Level level;
   private final long burstInterval;
   private final DelayQueue history = new DelayQueue();
   private final Queue available = new ConcurrentLinkedQueue();

   private BurstFilter(Level var1, float var2, long var3, Filter.Result var5, Filter.Result var6) {
      super(var5, var6);
      this.level = var1;
      this.burstInterval = (long)(1.0E9F * ((float)var3 / var2));

      for(int var7 = 0; (long)var7 < var3; ++var7) {
         this.available.add(new BurstFilter.LogDelay(this));
      }

   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(LogEvent var1) {
      return this.filter(var1.getLevel());
   }

   private Filter.Result filter(Level var1) {
      if (!this.level.isAtLeastAsSpecificAs(var1)) {
         return this.onMatch;
      } else {
         BurstFilter.LogDelay var2;
         for(var2 = (BurstFilter.LogDelay)this.history.poll(); var2 != null; var2 = (BurstFilter.LogDelay)this.history.poll()) {
            this.available.add(var2);
         }

         var2 = (BurstFilter.LogDelay)this.available.poll();
         if (var2 != null) {
            var2.setDelay(this.burstInterval);
            this.history.add(var2);
            return this.onMatch;
         } else {
            return this.onMismatch;
         }
      }
   }

   public int getAvailable() {
      return this.available.size();
   }

   public void clear() {
      Iterator var1 = this.history.iterator();

      while(var1.hasNext()) {
         BurstFilter.LogDelay var2 = (BurstFilter.LogDelay)var1.next();
         this.history.remove(var2);
         this.available.add(var2);
      }

   }

   public String toString() {
      return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
   }

   @PluginFactory
   public static BurstFilter createFilter(@PluginAttribute("level") String var0, @PluginAttribute("rate") String var1, @PluginAttribute("maxBurst") String var2, @PluginAttribute("onMatch") String var3, @PluginAttribute("onMismatch") String var4) {
      Filter.Result var5 = Filter.Result.toResult(var3, Filter.Result.NEUTRAL);
      Filter.Result var6 = Filter.Result.toResult(var4, Filter.Result.DENY);
      Level var7 = Level.toLevel(var0, Level.WARN);
      float var8 = var1 == null ? 10.0F : Float.parseFloat(var1);
      if (var8 <= 0.0F) {
         var8 = 10.0F;
      }

      long var9 = var2 == null ? (long)(var8 * 100.0F) : Long.parseLong(var2);
      return new BurstFilter(var7, var8, var9, var5, var6);
   }

   private class LogDelay implements Delayed {
      private long expireTime;
      final BurstFilter this$0;

      public LogDelay(BurstFilter var1) {
         this.this$0 = var1;
      }

      public void setDelay(long var1) {
         this.expireTime = var1 + System.nanoTime();
      }

      public long getDelay(TimeUnit var1) {
         return var1.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
      }

      public int compareTo(Delayed var1) {
         if (this.expireTime < ((BurstFilter.LogDelay)var1).expireTime) {
            return -1;
         } else {
            return this.expireTime > ((BurstFilter.LogDelay)var1).expireTime ? 1 : 0;
         }
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            BurstFilter.LogDelay var2 = (BurstFilter.LogDelay)var1;
            return this.expireTime == var2.expireTime;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (int)(this.expireTime ^ this.expireTime >>> 32);
      }

      public int compareTo(Object var1) {
         return this.compareTo((Delayed)var1);
      }
   }
}
