package org.apache.logging.log4j.core.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.UnavailableException;

public class Log4jServletContextListener implements ServletContextListener {
   private ServletContext servletContext;
   private Log4jWebInitializer initializer;

   public void contextInitialized(ServletContextEvent var1) {
      this.servletContext = var1.getServletContext();
      this.servletContext.log("Log4jServletContextListener ensuring that Log4j starts up properly.");
      this.initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(this.servletContext);

      try {
         this.initializer.initialize();
         this.initializer.setLoggerContext();
      } catch (UnavailableException var3) {
         throw new RuntimeException("Failed to initialize Log4j properly.", var3);
      }
   }

   public void contextDestroyed(ServletContextEvent var1) {
      if (this.servletContext != null && this.initializer != null) {
         this.servletContext.log("Log4jServletContextListener ensuring that Log4j shuts down properly.");
         this.initializer.clearLoggerContext();
         this.initializer.deinitialize();
      } else {
         throw new IllegalStateException("Context destroyed before it was initialized.");
      }
   }
}
