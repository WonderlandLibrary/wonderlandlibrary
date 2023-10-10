package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.status.StatusLogger;

public final class Server {
   private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";

   private Server() {
   }

   public static String escape(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length() * 2);
      boolean var2 = false;
      int var3 = 0;

      while(var3 < var0.length()) {
         char var4 = var0.charAt(var3);
         switch(var4) {
         case '*':
         case ',':
         case ':':
         case '=':
         case '?':
         case '\\':
            var1.append('\\');
            var2 = true;
         default:
            var1.append(var4);
            ++var3;
         }
      }

      if (var2) {
         var1.insert(0, '"');
         var1.append('"');
      }

      return var1.toString();
   }

   public static void registerMBeans(ContextSelector var0) throws JMException {
      if (Boolean.getBoolean("log4j2.disable.jmx")) {
         StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
      } else {
         MBeanServer var1 = ManagementFactory.getPlatformMBeanServer();
         registerMBeans(var0, var1);
      }
   }

   public static void registerMBeans(ContextSelector var0, MBeanServer var1) throws JMException {
      if (Boolean.getBoolean("log4j2.disable.jmx")) {
         StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
      } else {
         ExecutorService var2 = Executors.newFixedThreadPool(1);
         registerStatusLogger(var1, var2);
         registerContextSelector(var0, var1, var2);
         List var3 = var0.getLoggerContexts();
         registerContexts(var3, var1, var2);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            LoggerContext var5 = (LoggerContext)var4.next();
            var5.addPropertyChangeListener(new PropertyChangeListener(var5, var1, var2) {
               final LoggerContext val$context;
               final MBeanServer val$mbs;
               final Executor val$executor;

               {
                  this.val$context = var1;
                  this.val$mbs = var2;
                  this.val$executor = var3;
               }

               public void propertyChange(PropertyChangeEvent var1) {
                  if ("config".equals(var1.getPropertyName())) {
                     Server.access$000(this.val$context, this.val$mbs);
                     Server.access$100(this.val$context, this.val$mbs);

                     try {
                        Server.access$200(this.val$context, this.val$mbs, this.val$executor);
                        Server.access$300(this.val$context, this.val$mbs, this.val$executor);
                     } catch (Exception var3) {
                        StatusLogger.getLogger().error("Could not register mbeans", var3);
                     }

                  }
               }
            });
         }

      }
   }

   private static void registerStatusLogger(MBeanServer var0, Executor var1) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      StatusLoggerAdmin var2 = new StatusLoggerAdmin(var1);
      var0.registerMBean(var2, var2.getObjectName());
   }

   private static void registerContextSelector(ContextSelector var0, MBeanServer var1, Executor var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      ContextSelectorAdmin var3 = new ContextSelectorAdmin(var0);
      var1.registerMBean(var3, var3.getObjectName());
   }

   private static void registerContexts(List var0, MBeanServer var1, Executor var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         LoggerContext var4 = (LoggerContext)var3.next();
         LoggerContextAdmin var5 = new LoggerContextAdmin(var4, var2);
         var1.registerMBean(var5, var5.getObjectName());
      }

   }

   private static void unregisterLoggerConfigs(LoggerContext var0, MBeanServer var1) {
      String var2 = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";
      String var3 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", var0.getName(), "*");
      unregisterAllMatching(var3, var1);
   }

   private static void unregisterAppenders(LoggerContext var0, MBeanServer var1) {
      String var2 = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";
      String var3 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s", var0.getName(), "*");
      unregisterAllMatching(var3, var1);
   }

   private static void unregisterAllMatching(String var0, MBeanServer var1) {
      try {
         ObjectName var2 = new ObjectName(var0);
         Set var3 = var1.queryNames(var2, (QueryExp)null);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            ObjectName var5 = (ObjectName)var4.next();
            var1.unregisterMBean(var5);
         }
      } catch (Exception var6) {
         StatusLogger.getLogger().error("Could not unregister " + var0, var6);
      }

   }

   private static void registerLoggerConfigs(LoggerContext var0, MBeanServer var1, Executor var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      Map var3 = var0.getConfiguration().getLoggers();
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         LoggerConfig var6 = (LoggerConfig)var3.get(var5);
         LoggerConfigAdmin var7 = new LoggerConfigAdmin(var0.getName(), var6);
         var1.registerMBean(var7, var7.getObjectName());
      }

   }

   private static void registerAppenders(LoggerContext var0, MBeanServer var1, Executor var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      Map var3 = var0.getConfiguration().getAppenders();
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         Appender var6 = (Appender)var3.get(var5);
         AppenderAdmin var7 = new AppenderAdmin(var0.getName(), var6);
         var1.registerMBean(var7, var7.getObjectName());
      }

   }

   static void access$000(LoggerContext var0, MBeanServer var1) {
      unregisterLoggerConfigs(var0, var1);
   }

   static void access$100(LoggerContext var0, MBeanServer var1) {
      unregisterAppenders(var0, var1);
   }

   static void access$200(LoggerContext var0, MBeanServer var1, Executor var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      registerLoggerConfigs(var0, var1, var2);
   }

   static void access$300(LoggerContext var0, MBeanServer var1, Executor var2) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
      registerAppenders(var0, var1, var2);
   }
}
