package org.apache.logging.log4j.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class StatusLogger extends AbstractLogger {
   public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
   private static final String NOT_AVAIL = "?";
   private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
   private static final int MAX_ENTRIES;
   private static final String DEFAULT_STATUS_LEVEL;
   private static final StatusLogger STATUS_LOGGER;
   private final SimpleLogger logger;
   private final CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
   private final ReentrantReadWriteLock listenersLock = new ReentrantReadWriteLock();
   private final Queue messages;
   private final ReentrantLock msgLock;
   private int listenersLevel;

   private StatusLogger() {
      this.messages = new StatusLogger.BoundedQueue(this, MAX_ENTRIES);
      this.msgLock = new ReentrantLock();
      this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", (MessageFactory)null, PROPS, System.err);
      this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
   }

   public static StatusLogger getLogger() {
      return STATUS_LOGGER;
   }

   public Level getLevel() {
      return this.logger.getLevel();
   }

   public void setLevel(Level var1) {
      this.logger.setLevel(var1);
   }

   public void registerListener(StatusListener var1) {
      this.listenersLock.writeLock().lock();
      this.listeners.add(var1);
      Level var2 = var1.getStatusLevel();
      if (this.listenersLevel < var2.intLevel()) {
         this.listenersLevel = var2.intLevel();
      }

      this.listenersLock.writeLock().unlock();
   }

   public void removeListener(StatusListener var1) {
      this.listenersLock.writeLock().lock();
      this.listeners.remove(var1);
      int var2 = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         StatusListener var4 = (StatusListener)var3.next();
         int var5 = var4.getStatusLevel().intLevel();
         if (var2 < var5) {
            var2 = var5;
         }
      }

      this.listenersLevel = var2;
      this.listenersLock.writeLock().unlock();
   }

   public Iterator getListeners() {
      return this.listeners.iterator();
   }

   public void reset() {
      this.listeners.clear();
      this.clear();
   }

   public List getStatusData() {
      this.msgLock.lock();
      ArrayList var1 = new ArrayList(this.messages);
      this.msgLock.unlock();
      return var1;
   }

   public void clear() {
      this.msgLock.lock();
      this.messages.clear();
      this.msgLock.unlock();
   }

   public void log(Marker var1, String var2, Level var3, Message var4, Throwable var5) {
      StackTraceElement var6 = null;
      if (var2 != null) {
         var6 = this.getStackTraceElement(var2, Thread.currentThread().getStackTrace());
      }

      StatusData var7 = new StatusData(var6, var3, var4, var5);
      this.msgLock.lock();
      this.messages.add(var7);
      this.msgLock.unlock();
      if (this.listeners.size() > 0) {
         Iterator var8 = this.listeners.iterator();

         while(var8.hasNext()) {
            StatusListener var9 = (StatusListener)var8.next();
            if (var7.getLevel().isAtLeastAsSpecificAs(var9.getStatusLevel())) {
               var9.log(var7);
            }
         }
      } else {
         this.logger.log(var1, var2, var3, var4, var5);
      }

   }

   private StackTraceElement getStackTraceElement(String var1, StackTraceElement[] var2) {
      if (var1 == null) {
         return null;
      } else {
         boolean var3 = false;
         StackTraceElement[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StackTraceElement var7 = var4[var6];
            if (var3) {
               return var7;
            }

            String var8 = var7.getClassName();
            if (var1.equals(var8)) {
               var3 = true;
            } else if ("?".equals(var8)) {
               break;
            }
         }

         return null;
      }
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3, Object... var4) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4) {
      return this.isEnabled(var1, var2);
   }

   public boolean isEnabled(Level var1, Marker var2) {
      if (this.listeners.size() > 0) {
         return this.listenersLevel >= var1.intLevel();
      } else {
         switch(var1) {
         case FATAL:
            return this.logger.isFatalEnabled(var2);
         case TRACE:
            return this.logger.isTraceEnabled(var2);
         case DEBUG:
            return this.logger.isDebugEnabled(var2);
         case INFO:
            return this.logger.isInfoEnabled(var2);
         case WARN:
            return this.logger.isWarnEnabled(var2);
         case ERROR:
            return this.logger.isErrorEnabled(var2);
         default:
            return false;
         }
      }
   }

   static Queue access$000(StatusLogger var0) {
      return var0.messages;
   }

   static {
      MAX_ENTRIES = PROPS.getIntegerProperty("log4j2.status.entries", 200);
      DEFAULT_STATUS_LEVEL = PROPS.getStringProperty("log4j2.StatusLogger.level");
      STATUS_LOGGER = new StatusLogger();
   }

   private class BoundedQueue extends ConcurrentLinkedQueue {
      private static final long serialVersionUID = -3945953719763255337L;
      private final int size;
      final StatusLogger this$0;

      public BoundedQueue(StatusLogger var1, int var2) {
         this.this$0 = var1;
         this.size = var2;
      }

      public boolean add(Object var1) {
         while(StatusLogger.access$000(this.this$0).size() > this.size) {
            StatusLogger.access$000(this.this$0).poll();
         }

         return super.add(var1);
      }
   }
}
