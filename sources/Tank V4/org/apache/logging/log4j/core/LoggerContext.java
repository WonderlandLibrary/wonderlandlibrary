package org.apache.logging.log4j.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContext implements org.apache.logging.log4j.spi.LoggerContext, ConfigurationListener, LifeCycle {
   public static final String PROPERTY_CONFIG = "config";
   private static final StatusLogger LOGGER = StatusLogger.getLogger();
   private final ConcurrentMap loggers;
   private final CopyOnWriteArrayList propertyChangeListeners;
   private volatile Configuration config;
   private Object externalContext;
   private final String name;
   private URI configLocation;
   private LoggerContext.ShutdownThread shutdownThread;
   private volatile LoggerContext.Status status;
   private final Lock configLock;

   public LoggerContext(String var1) {
      this(var1, (Object)null, (URI)((URI)null));
   }

   public LoggerContext(String var1, Object var2) {
      this(var1, var2, (URI)null);
   }

   public LoggerContext(String var1, Object var2, URI var3) {
      this.loggers = new ConcurrentHashMap();
      this.propertyChangeListeners = new CopyOnWriteArrayList();
      this.config = new DefaultConfiguration();
      this.shutdownThread = null;
      this.status = LoggerContext.Status.INITIALIZED;
      this.configLock = new ReentrantLock();
      this.name = var1;
      this.externalContext = var2;
      this.configLocation = var3;
   }

   public LoggerContext(String var1, Object var2, String var3) {
      this.loggers = new ConcurrentHashMap();
      this.propertyChangeListeners = new CopyOnWriteArrayList();
      this.config = new DefaultConfiguration();
      this.shutdownThread = null;
      this.status = LoggerContext.Status.INITIALIZED;
      this.configLock = new ReentrantLock();
      this.name = var1;
      this.externalContext = var2;
      if (var3 != null) {
         URI var4;
         try {
            var4 = (new File(var3)).toURI();
         } catch (Exception var6) {
            var4 = null;
         }

         this.configLocation = var4;
      } else {
         this.configLocation = null;
      }

   }

   public void start() {
      if (this.configLock.tryLock()) {
         if (this.status == LoggerContext.Status.INITIALIZED || this.status == LoggerContext.Status.STOPPED) {
            this.status = LoggerContext.Status.STARTING;
            this.reconfigure();
            if (this.config.isShutdownHookEnabled()) {
               this.shutdownThread = new LoggerContext.ShutdownThread(this, this);

               try {
                  Runtime.getRuntime().addShutdownHook(this.shutdownThread);
               } catch (IllegalStateException var3) {
                  LOGGER.warn("Unable to register shutdown hook due to JVM state");
                  this.shutdownThread = null;
               } catch (SecurityException var4) {
                  LOGGER.warn("Unable to register shutdown hook due to security restrictions");
                  this.shutdownThread = null;
               }
            }

            this.status = LoggerContext.Status.STARTED;
         }

         this.configLock.unlock();
      }

   }

   public void start(Configuration var1) {
      if (this.configLock.tryLock()) {
         if ((this.status == LoggerContext.Status.INITIALIZED || this.status == LoggerContext.Status.STOPPED) && var1.isShutdownHookEnabled()) {
            this.shutdownThread = new LoggerContext.ShutdownThread(this, this);

            try {
               Runtime.getRuntime().addShutdownHook(this.shutdownThread);
            } catch (IllegalStateException var4) {
               LOGGER.warn("Unable to register shutdown hook due to JVM state");
               this.shutdownThread = null;
            } catch (SecurityException var5) {
               LOGGER.warn("Unable to register shutdown hook due to security restrictions");
               this.shutdownThread = null;
            }

            this.status = LoggerContext.Status.STARTED;
         }

         this.configLock.unlock();
      }

      this.setConfiguration(var1);
   }

   public void stop() {
      this.configLock.lock();
      if (this.status == LoggerContext.Status.STOPPED) {
         this.configLock.unlock();
      } else {
         this.status = LoggerContext.Status.STOPPING;
         if (this.shutdownThread != null) {
            Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
            this.shutdownThread = null;
         }

         Configuration var1 = this.config;
         this.config = new NullConfiguration();
         this.updateLoggers();
         var1.stop();
         this.externalContext = null;
         LogManager.getFactory().removeContext(this);
         this.status = LoggerContext.Status.STOPPED;
         this.configLock.unlock();
      }
   }

   public String getName() {
      return this.name;
   }

   public LoggerContext.Status getStatus() {
      return this.status;
   }

   public boolean isStarted() {
      return this.status == LoggerContext.Status.STARTED;
   }

   public void setExternalContext(Object var1) {
      this.externalContext = var1;
   }

   public Object getExternalContext() {
      return this.externalContext;
   }

   public Logger getLogger(String var1) {
      return this.getLogger(var1, (MessageFactory)null);
   }

   public Logger getLogger(String var1, MessageFactory var2) {
      Logger var3 = (Logger)this.loggers.get(var1);
      if (var3 != null) {
         AbstractLogger.checkMessageFactory(var3, var2);
         return var3;
      } else {
         var3 = this.newInstance(this, var1, var2);
         Logger var4 = (Logger)this.loggers.putIfAbsent(var1, var3);
         return var4 == null ? var3 : var4;
      }
   }

   public boolean hasLogger(String var1) {
      return this.loggers.containsKey(var1);
   }

   public Configuration getConfiguration() {
      return this.config;
   }

   public void addFilter(Filter var1) {
      this.config.addFilter(var1);
   }

   public void removeFilter(Filter var1) {
      this.config.removeFilter(var1);
   }

   private synchronized Configuration setConfiguration(Configuration var1) {
      if (var1 == null) {
         throw new NullPointerException("No Configuration was provided");
      } else {
         Configuration var2 = this.config;
         var1.addListener(this);
         HashMap var3 = new HashMap();
         var3.put("hostName", NetUtils.getLocalHostname());
         var3.put("contextName", this.name);
         var1.addComponent("ContextProperties", var3);
         var1.start();
         this.config = var1;
         this.updateLoggers();
         if (var2 != null) {
            var2.removeListener(this);
            var2.stop();
         }

         PropertyChangeEvent var4 = new PropertyChangeEvent(this, "config", var2, var1);
         Iterator var5 = this.propertyChangeListeners.iterator();

         while(var5.hasNext()) {
            PropertyChangeListener var6 = (PropertyChangeListener)var5.next();
            var6.propertyChange(var4);
         }

         return var2;
      }
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propertyChangeListeners.add(Assert.isNotNull(var1, "listener"));
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propertyChangeListeners.remove(var1);
   }

   public synchronized URI getConfigLocation() {
      return this.configLocation;
   }

   public synchronized void setConfigLocation(URI var1) {
      this.configLocation = var1;
      this.reconfigure();
   }

   public synchronized void reconfigure() {
      LOGGER.debug("Reconfiguration started for context " + this.name);
      Configuration var1 = ConfigurationFactory.getInstance().getConfiguration(this.name, this.configLocation);
      this.setConfiguration(var1);
      LOGGER.debug("Reconfiguration completed");
   }

   public void updateLoggers() {
      this.updateLoggers(this.config);
   }

   public void updateLoggers(Configuration var1) {
      Iterator var2 = this.loggers.values().iterator();

      while(var2.hasNext()) {
         Logger var3 = (Logger)var2.next();
         var3.updateConfiguration(var1);
      }

   }

   public synchronized void onChange(Reconfigurable var1) {
      LOGGER.debug("Reconfiguration started for context " + this.name);
      Configuration var2 = var1.reconfigure();
      if (var2 != null) {
         this.setConfiguration(var2);
         LOGGER.debug("Reconfiguration completed");
      } else {
         LOGGER.debug("Reconfiguration failed");
      }

   }

   protected Logger newInstance(LoggerContext var1, String var2, MessageFactory var3) {
      return new Logger(var1, var2, var3);
   }

   public org.apache.logging.log4j.Logger getLogger(String var1, MessageFactory var2) {
      return this.getLogger(var1, var2);
   }

   public org.apache.logging.log4j.Logger getLogger(String var1) {
      return this.getLogger(var1);
   }

   static LoggerContext.ShutdownThread access$002(LoggerContext var0, LoggerContext.ShutdownThread var1) {
      return var0.shutdownThread = var1;
   }

   private class ShutdownThread extends Thread {
      private final LoggerContext context;
      final LoggerContext this$0;

      public ShutdownThread(LoggerContext var1, LoggerContext var2) {
         this.this$0 = var1;
         this.context = var2;
      }

      public void run() {
         LoggerContext.access$002(this.context, (LoggerContext.ShutdownThread)null);
         this.context.stop();
      }
   }

   public static enum Status {
      INITIALIZED,
      STARTING,
      STARTED,
      STOPPING,
      STOPPED;

      private static final LoggerContext.Status[] $VALUES = new LoggerContext.Status[]{INITIALIZED, STARTING, STARTED, STOPPING, STOPPED};
   }
}
