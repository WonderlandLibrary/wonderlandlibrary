package org.apache.logging.log4j.core.lookup;

import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ContextAnchor;

@Plugin(
   name = "web",
   category = "Lookup"
)
public class WebLookup implements StrLookup {
   private static final String ATTR_PREFIX = "attr.";
   private static final String INIT_PARAM_PREFIX = "initParam.";

   protected ServletContext getServletContext() {
      LoggerContext var1 = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
      if (var1 == null) {
         var1 = (LoggerContext)LogManager.getContext(false);
      }

      if (var1 == null) {
         return null;
      } else {
         Object var2 = var1.getExternalContext();
         return var2 != null && var2 instanceof ServletContext ? (ServletContext)var2 : null;
      }
   }

   public String lookup(String var1) {
      ServletContext var2 = this.getServletContext();
      if (var2 == null) {
         return null;
      } else {
         String var3;
         if (var1.startsWith("attr.")) {
            var3 = var1.substring(5);
            Object var5 = var2.getAttribute(var3);
            return var5 == null ? null : var5.toString();
         } else if (var1.startsWith("initParam.")) {
            var3 = var1.substring(10);
            return var2.getInitParameter(var3);
         } else if ("rootDir".equals(var1)) {
            var3 = var2.getRealPath("/");
            if (var3 == null) {
               String var4 = "failed to resolve web:rootDir -- servlet container unable to translate virtual path  to real path (probably not deployed as exploded";
               throw new RuntimeException(var4);
            } else {
               return var3;
            }
         } else if ("contextPath".equals(var1)) {
            return var2.getContextPath();
         } else if ("servletContextName".equals(var1)) {
            return var2.getServletContextName();
         } else if ("serverInfo".equals(var1)) {
            return var2.getServerInfo();
         } else if ("effectiveMajorVersion".equals(var1)) {
            return String.valueOf(var2.getEffectiveMajorVersion());
         } else if ("effectiveMinorVersion".equals(var1)) {
            return String.valueOf(var2.getEffectiveMinorVersion());
         } else if ("majorVersion".equals(var1)) {
            return String.valueOf(var2.getMajorVersion());
         } else if ("minorVersion".equals(var1)) {
            return String.valueOf(var2.getMinorVersion());
         } else if (var2.getAttribute(var1) != null) {
            return var2.getAttribute(var1).toString();
         } else if (var2.getInitParameter(var1) != null) {
            return var2.getInitParameter(var1);
         } else {
            var2.log(this.getClass().getName() + " unable to resolve key '" + var1 + "'");
            return null;
         }
      }
   }

   public String lookup(LogEvent var1, String var2) {
      return this.lookup(var2);
   }
}
