package org.apache.logging.log4j.core.web;

import java.util.EnumSet;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.FilterRegistration.Dynamic;

public class Log4jServletContainerInitializer implements ServletContainerInitializer {
   public void onStartup(Set var1, ServletContext var2) throws ServletException {
      if (var2.getMajorVersion() > 2) {
         var2.log("Log4jServletContainerInitializer starting up Log4j in Servlet 3.0+ environment.");
         Log4jWebInitializer var3 = Log4jWebInitializerImpl.getLog4jWebInitializer(var2);
         var3.initialize();
         var3.setLoggerContext();
         var2.addListener(new Log4jServletContextListener());
         Dynamic var4 = var2.addFilter("log4jServletFilter", new Log4jServletFilter());
         if (var4 == null) {
            throw new UnavailableException("In a Servlet 3.0+ application, you must not define a log4jServletFilter in web.xml. Log4j 2 defines this for you automatically.");
         }

         var4.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, new String[]{"/*"});
      }

   }
}
