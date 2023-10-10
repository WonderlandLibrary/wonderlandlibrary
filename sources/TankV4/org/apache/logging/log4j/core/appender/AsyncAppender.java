package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;

@Plugin(
   name = "Async",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class AsyncAppender extends AbstractAppender {
   private static final int DEFAULT_QUEUE_SIZE = 128;
   private static final String SHUTDOWN = "Shutdown";
   private final BlockingQueue queue;
   private final boolean blocking;
   private final Configuration config;
   private final AppenderRef[] appenderRefs;
   private final String errorRef;
   private final boolean includeLocation;
   private AppenderControl errorAppender;
   private AsyncAppender.AsyncThread thread;
   private static final AtomicLong threadSequence = new AtomicLong(1L);

   private AsyncAppender(String var1, Filter var2, AppenderRef[] var3, String var4, int var5, boolean var6, boolean var7, Configuration var8, boolean var9) {
      super(var1, var2, (Layout)null, var7);
      this.queue = new ArrayBlockingQueue(var5);
      this.blocking = var6;
      this.config = var8;
      this.appenderRefs = var3;
      this.errorRef = var4;
      this.includeLocation = var9;
   }

   public void start() {
      Map var1 = this.config.getAppenders();
      ArrayList var2 = new ArrayList();
      AppenderRef[] var3 = this.appenderRefs;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         AppenderRef var6 = var3[var5];
         if (var1.containsKey(var6.getRef())) {
            var2.add(new AppenderControl((Appender)var1.get(var6.getRef()), var6.getLevel(), var6.getFilter()));
         } else {
            LOGGER.error("No appender named {} was configured", var6);
         }
      }

      if (this.errorRef != null) {
         if (var1.containsKey(this.errorRef)) {
            this.errorAppender = new AppenderControl((Appender)var1.get(this.errorRef), (Level)null, (Filter)null);
         } else {
            LOGGER.error("Unable to set up error Appender. No appender named {} was configured", this.errorRef);
         }
      }

      if (var2.size() > 0) {
         this.thread = new AsyncAppender.AsyncThread(this, var2, this.queue);
         this.thread.setName("AsyncAppender-" + this.getName());
      } else if (this.errorRef == null) {
         throw new ConfigurationException("No appenders are available for AsyncAppender " + this.getName());
      }

      this.thread.start();
      super.start();
   }

   public void stop() {
      super.stop();
      this.thread.shutdown();

      try {
         this.thread.join();
      } catch (InterruptedException var2) {
         LOGGER.warn("Interrupted while stopping AsyncAppender {}", this.getName());
      }

   }

   public void append(LogEvent var1) {
      if (!this.isStarted()) {
         throw new IllegalStateException("AsyncAppender " + this.getName() + " is not active");
      } else {
         if (var1 instanceof Log4jLogEvent) {
            boolean var2 = false;
            if (this.blocking) {
               try {
                  this.queue.put(Log4jLogEvent.serialize((Log4jLogEvent)var1, this.includeLocation));
                  var2 = true;
               } catch (InterruptedException var4) {
                  LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", this.getName());
               }
            } else {
               var2 = this.queue.offer(Log4jLogEvent.serialize((Log4jLogEvent)var1, this.includeLocation));
               if (!var2) {
                  this.error("Appender " + this.getName() + " is unable to write primary appenders. queue is full");
               }
            }

            if (!var2 && this.errorAppender != null) {
               this.errorAppender.callAppender(var1);
            }
         }

      }
   }

   @PluginFactory
   public static AsyncAppender createAppender(@PluginElement("AppenderRef") AppenderRef[] var0, @PluginAttribute("errorRef") @PluginAliases({"error-ref"}) String var1, @PluginAttribute("blocking") String var2, @PluginAttribute("bufferSize") String var3, @PluginAttribute("name") String var4, @PluginAttribute("includeLocation") String var5, @PluginElement("Filter") Filter var6, @PluginConfiguration Configuration var7, @PluginAttribute("ignoreExceptions") String var8) {
      if (var4 == null) {
         LOGGER.error("No name provided for AsyncAppender");
         return null;
      } else {
         if (var0 == null) {
            LOGGER.error("No appender references provided to AsyncAppender {}", var4);
         }

         boolean var9 = Booleans.parseBoolean(var2, true);
         int var10 = AbstractAppender.parseInt(var3, 128);
         boolean var11 = Boolean.parseBoolean(var5);
         boolean var12 = Booleans.parseBoolean(var8, true);
         return new AsyncAppender(var4, var6, var0, var1, var10, var9, var12, var7, var11);
      }
   }

   static AtomicLong access$000() {
      return threadSequence;
   }

   static AppenderControl access$100(AsyncAppender var0) {
      return var0.errorAppender;
   }

   private class AsyncThread extends Thread {
      private volatile boolean shutdown;
      private final List appenders;
      private final BlockingQueue queue;
      final AsyncAppender this$0;

      public AsyncThread(AsyncAppender var1, List var2, BlockingQueue var3) {
         this.this$0 = var1;
         this.shutdown = false;
         this.appenders = var2;
         this.queue = var3;
         this.setDaemon(true);
         this.setName("AsyncAppenderThread" + AsyncAppender.access$000().getAndIncrement());
      }

      public void run() {
         Serializable var1;
         Log4jLogEvent var2;
         while(!this.shutdown) {
            try {
               var1 = (Serializable)this.queue.take();
               if (var1 != null && var1 instanceof String && "Shutdown".equals(var1.toString())) {
                  this.shutdown = true;
                  continue;
               }
            } catch (InterruptedException var9) {
               continue;
            }

            var2 = Log4jLogEvent.deserialize(var1);
            var2.setEndOfBatch(this.queue.isEmpty());
            boolean var3 = false;
            Iterator var4 = this.appenders.iterator();

            while(var4.hasNext()) {
               AppenderControl var5 = (AppenderControl)var4.next();

               try {
                  var5.callAppender(var2);
                  var3 = true;
               } catch (Exception var8) {
               }
            }

            if (!var3 && AsyncAppender.access$100(this.this$0) != null) {
               try {
                  AsyncAppender.access$100(this.this$0).callAppender(var2);
               } catch (Exception var7) {
               }
            }
         }

         while(!this.queue.isEmpty()) {
            try {
               var1 = (Serializable)this.queue.take();
               if (var1 instanceof Log4jLogEvent) {
                  var2 = Log4jLogEvent.deserialize(var1);
                  var2.setEndOfBatch(this.queue.isEmpty());
                  Iterator var11 = this.appenders.iterator();

                  while(var11.hasNext()) {
                     AppenderControl var12 = (AppenderControl)var11.next();
                     var12.callAppender(var2);
                  }
               }
            } catch (InterruptedException var10) {
            }
         }

      }

      public void shutdown() {
         this.shutdown = true;
         if (this.queue.isEmpty()) {
            this.queue.offer("Shutdown");
         }

      }
   }
}
