package org.apache.logging.log4j.core.jmx;

import javax.management.ObjectName;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.helpers.Assert;

public class AppenderAdmin implements AppenderAdminMBean {
   private final String contextName;
   private final Appender appender;
   private final ObjectName objectName;

   public AppenderAdmin(String var1, Appender var2) {
      this.contextName = (String)Assert.isNotNull(var1, "contextName");
      this.appender = (Appender)Assert.isNotNull(var2, "appender");

      try {
         String var3 = Server.escape(this.contextName);
         String var4 = Server.escape(var2.getName());
         String var5 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s", var3, var4);
         this.objectName = new ObjectName(var5);
      } catch (Exception var6) {
         throw new IllegalStateException(var6);
      }
   }

   public ObjectName getObjectName() {
      return this.objectName;
   }

   public String getName() {
      return this.appender.getName();
   }

   public String getLayout() {
      return String.valueOf(this.appender.getLayout());
   }

   public boolean isExceptionSuppressed() {
      return this.appender.ignoreExceptions();
   }

   public String getErrorHandler() {
      return String.valueOf(this.appender.getHandler());
   }
}
