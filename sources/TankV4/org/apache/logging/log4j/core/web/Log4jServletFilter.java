package org.apache.logging.log4j.core.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Log4jServletFilter implements Filter {
   static final String ALREADY_FILTERED_ATTRIBUTE = Log4jServletFilter.class.getName() + ".FILTERED";
   private ServletContext servletContext;
   private Log4jWebInitializer initializer;

   public void init(FilterConfig var1) throws ServletException {
      this.servletContext = var1.getServletContext();
      this.servletContext.log("Log4jServletFilter initialized.");
      this.initializer = Log4jWebInitializerImpl.getLog4jWebInitializer(this.servletContext);
      this.initializer.clearLoggerContext();
   }

   public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
      if (var1.getAttribute(ALREADY_FILTERED_ATTRIBUTE) != null) {
         var3.doFilter(var1, var2);
      } else {
         var1.setAttribute(ALREADY_FILTERED_ATTRIBUTE, true);
         this.initializer.setLoggerContext();
         var3.doFilter(var1, var2);
         this.initializer.clearLoggerContext();
      }

   }

   public void destroy() {
      if (this.servletContext != null && this.initializer != null) {
         this.servletContext.log("Log4jServletFilter destroyed.");
         this.initializer.setLoggerContext();
      } else {
         throw new IllegalStateException("Filter destroyed before it was initialized.");
      }
   }
}
