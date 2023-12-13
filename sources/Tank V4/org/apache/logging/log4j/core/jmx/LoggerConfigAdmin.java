package org.apache.logging.log4j.core.jmx;

import java.util.List;
import javax.management.ObjectName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.helpers.Assert;

public class LoggerConfigAdmin implements LoggerConfigAdminMBean {
   private final String contextName;
   private final LoggerConfig loggerConfig;
   private final ObjectName objectName;

   public LoggerConfigAdmin(String var1, LoggerConfig var2) {
      this.contextName = (String)Assert.isNotNull(var1, "contextName");
      this.loggerConfig = (LoggerConfig)Assert.isNotNull(var2, "loggerConfig");

      try {
         String var3 = Server.escape(this.contextName);
         String var4 = Server.escape(var2.getName());
         String var5 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", var3, var4);
         this.objectName = new ObjectName(var5);
      } catch (Exception var6) {
         throw new IllegalStateException(var6);
      }
   }

   public ObjectName getObjectName() {
      return this.objectName;
   }

   public String getName() {
      return this.loggerConfig.getName();
   }

   public String getLevel() {
      return this.loggerConfig.getLevel().name();
   }

   public void setLevel(String var1) {
      this.loggerConfig.setLevel(Level.valueOf(var1));
   }

   public boolean isAdditive() {
      return this.loggerConfig.isAdditive();
   }

   public void setAdditive(boolean var1) {
      this.loggerConfig.setAdditive(var1);
   }

   public boolean isIncludeLocation() {
      return this.loggerConfig.isIncludeLocation();
   }

   public String getFilter() {
      return String.valueOf(this.loggerConfig.getFilter());
   }

   public String[] getAppenderRefs() {
      List var1 = this.loggerConfig.getAppenderRefs();
      String[] var2 = new String[var1.size()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = ((AppenderRef)var1.get(var3)).getRef();
      }

      return var2;
   }
}
