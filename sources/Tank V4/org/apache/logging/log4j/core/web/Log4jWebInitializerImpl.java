package org.apache.logging.log4j.core.web;

import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.Log4jContextFactory;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.core.selector.NamedContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;

final class Log4jWebInitializerImpl implements Log4jWebInitializer {
   private static final Object MUTEX = new Object();
   private final StrSubstitutor substitutor = new StrSubstitutor(new Interpolator());
   private final ServletContext servletContext;
   private String name;
   private NamedContextSelector selector;
   private LoggerContext loggerContext;
   private boolean initialized = false;
   private boolean deinitialized = false;

   private Log4jWebInitializerImpl(ServletContext var1) {
      this.servletContext = var1;
   }

   public synchronized void initialize() throws UnavailableException {
      if (this.deinitialized) {
         throw new IllegalStateException("Cannot initialize Log4jWebInitializer after it was destroyed.");
      } else {
         if (!this.initialized) {
            this.initialized = true;
            this.name = this.substitutor.replace(this.servletContext.getInitParameter("log4jContextName"));
            String var1 = this.substitutor.replace(this.servletContext.getInitParameter("log4jConfiguration"));
            boolean var2 = "true".equals(this.servletContext.getInitParameter("isLog4jContextSelectorNamed"));
            if (var2) {
               this.initializeJndi(var1);
            } else {
               this.initializeNonJndi(var1);
            }
         }

      }
   }

   private void initializeJndi(String var1) throws UnavailableException {
      URI var2 = null;
      if (var1 != null) {
         try {
            var2 = new URI(var1);
         } catch (Exception var6) {
            this.servletContext.log("Unable to convert configuration location [" + var1 + "] to a URI!", var6);
         }
      }

      if (this.name == null) {
         throw new UnavailableException("A log4jContextName context parameter is required");
      } else {
         LoggerContextFactory var4 = LogManager.getFactory();
         if (var4 instanceof Log4jContextFactory) {
            ContextSelector var5 = ((Log4jContextFactory)var4).getSelector();
            if (var5 instanceof NamedContextSelector) {
               this.selector = (NamedContextSelector)var5;
               LoggerContext var3 = this.selector.locateContext(this.name, this.servletContext, var2);
               ContextAnchor.THREAD_CONTEXT.set(var3);
               if (var3.getStatus() == LoggerContext.Status.INITIALIZED) {
                  var3.start();
               }

               ContextAnchor.THREAD_CONTEXT.remove();
               this.loggerContext = var3;
               this.servletContext.log("Created logger context for [" + this.name + "] using [" + var3.getClass().getClassLoader() + "].");
            } else {
               this.servletContext.log("Potential problem: Selector is not an instance of NamedContextSelector.");
            }
         } else {
            this.servletContext.log("Potential problem: Factory is not an instance of Log4jContextFactory.");
         }
      }
   }

   private void initializeNonJndi(String var1) {
      if (this.name == null) {
         this.name = this.servletContext.getServletContextName();
      }

      if (this.name == null && var1 == null) {
         this.servletContext.log("No Log4j context configuration provided. This is very unusual.");
      } else {
         this.loggerContext = Configurator.initialize(this.name, this.getClassLoader(), (String)var1, this.servletContext);
      }
   }

   public synchronized void deinitialize() {
      if (!this.initialized) {
         throw new IllegalStateException("Cannot deinitialize Log4jWebInitializer because it has not initialized.");
      } else {
         if (!this.deinitialized) {
            this.deinitialized = true;
            if (this.loggerContext != null) {
               this.servletContext.log("Removing LoggerContext for [" + this.name + "].");
               if (this.selector != null) {
                  this.selector.removeContext(this.name);
               }

               this.loggerContext.stop();
               this.loggerContext.setExternalContext((Object)null);
               this.loggerContext = null;
            }
         }

      }
   }

   public void setLoggerContext() {
      if (this.loggerContext != null) {
         ContextAnchor.THREAD_CONTEXT.set(this.loggerContext);
      }

   }

   public void clearLoggerContext() {
      ContextAnchor.THREAD_CONTEXT.remove();
   }

   private ClassLoader getClassLoader() {
      try {
         return this.servletContext.getClassLoader();
      } catch (Throwable var2) {
         return Log4jWebInitializerImpl.class.getClassLoader();
      }
   }

   static Log4jWebInitializer getLog4jWebInitializer(ServletContext var0) {
      Object var1;
      synchronized(var1 = MUTEX){}
      Object var2 = (Log4jWebInitializer)var0.getAttribute(INITIALIZER_ATTRIBUTE);
      if (var2 == null) {
         var2 = new Log4jWebInitializerImpl(var0);
         var0.setAttribute(INITIALIZER_ATTRIBUTE, var2);
      }

      return (Log4jWebInitializer)var2;
   }

   static {
      Class.forName("org.apache.logging.log4j.core.web.JNDIContextFilter");
      throw new IllegalStateException("You are using Log4j 2 in a web application with the old, extinct log4j-web artifact. This is not supported and could cause serious runtime problems. Pleaseremove the log4j-web JAR file from your application.");
   }
}
