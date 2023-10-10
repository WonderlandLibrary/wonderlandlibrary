package com.mojang.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

@Plugin(
   name = "Queue",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public class QueueLogAppender extends AbstractAppender {
   private static final int MAX_CAPACITY = 250;
   private static final Map QUEUES = new HashMap();
   private static final ReadWriteLock QUEUE_LOCK = new ReentrantReadWriteLock();
   private final BlockingQueue queue;

   public QueueLogAppender(String var1, Filter var2, Layout var3, boolean var4, BlockingQueue var5) {
      super(var1, var2, var3, var4);
      this.queue = var5;
   }

   public void append(LogEvent var1) {
      if (this.queue.size() >= 250) {
         this.queue.clear();
      }

      this.queue.add(this.getLayout().toSerializable(var1).toString());
   }

   @PluginFactory
   public static QueueLogAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("ignoreExceptions") String var1, @PluginElement("Layout") Layout var2, @PluginElement("Filters") Filter var3, @PluginAttribute("target") String var4) {
      boolean var5 = Boolean.parseBoolean(var1);
      if (var0 == null) {
         LOGGER.error("No name provided for QueueLogAppender");
         return null;
      } else {
         if (var4 == null) {
            var4 = var0;
         }

         QUEUE_LOCK.writeLock().lock();
         Object var6 = (BlockingQueue)QUEUES.get(var4);
         if (var6 == null) {
            var6 = new LinkedBlockingQueue();
            QUEUES.put(var4, var6);
         }

         QUEUE_LOCK.writeLock().unlock();
         if (var2 == null) {
            var2 = PatternLayout.createLayout((String)null, (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
         }

         return new QueueLogAppender(var0, var3, (Layout)var2, var5, (BlockingQueue)var6);
      }
   }

   public static String getNextLogEvent(String var0) {
      QUEUE_LOCK.readLock().lock();
      BlockingQueue var1 = (BlockingQueue)QUEUES.get(var0);
      QUEUE_LOCK.readLock().unlock();
      if (var1 != null) {
         try {
            return (String)var1.take();
         } catch (InterruptedException var3) {
         }
      }

      return null;
   }
}
