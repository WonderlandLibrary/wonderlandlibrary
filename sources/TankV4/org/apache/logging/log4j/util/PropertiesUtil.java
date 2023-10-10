package org.apache.logging.log4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class PropertiesUtil {
   private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final Properties props;

   public PropertiesUtil(Properties var1) {
      this.props = var1;
   }

   static Properties loadClose(InputStream var0, Object var1) {
      Properties var2 = new Properties();
      if (null != var0) {
         try {
            var2.load(var0);
         } catch (IOException var8) {
            LOGGER.error((String)("Unable to read " + var1), (Throwable)var8);

            try {
               var0.close();
            } catch (IOException var6) {
               LOGGER.error((String)("Unable to close " + var1), (Throwable)var6);
            }

            return var2;
         }

         try {
            var0.close();
         } catch (IOException var7) {
            LOGGER.error((String)("Unable to close " + var1), (Throwable)var7);
         }
      }

      return var2;
   }

   public PropertiesUtil(String var1) {
      ClassLoader var2 = ProviderUtil.findClassLoader();
      InputStream var3 = var2.getResourceAsStream(var1);
      this.props = loadClose(var3, var1);
   }

   public static PropertiesUtil getProperties() {
      return LOG4J_PROPERTIES;
   }

   public String getStringProperty(String var1) {
      String var2 = null;

      try {
         var2 = System.getProperty(var1);
      } catch (SecurityException var4) {
      }

      return var2 == null ? this.props.getProperty(var1) : var2;
   }

   public int getIntegerProperty(String var1, int var2) {
      String var3 = null;

      try {
         var3 = System.getProperty(var1);
      } catch (SecurityException var6) {
      }

      if (var3 == null) {
         var3 = this.props.getProperty(var1);
      }

      if (var3 != null) {
         try {
            return Integer.parseInt(var3);
         } catch (Exception var5) {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public long getLongProperty(String var1, long var2) {
      String var4 = null;

      try {
         var4 = System.getProperty(var1);
      } catch (SecurityException var7) {
      }

      if (var4 == null) {
         var4 = this.props.getProperty(var1);
      }

      if (var4 != null) {
         try {
            return Long.parseLong(var4);
         } catch (Exception var6) {
            return var2;
         }
      } else {
         return var2;
      }
   }

   public String getStringProperty(String var1, String var2) {
      String var3 = this.getStringProperty(var1);
      return var3 == null ? var2 : var3;
   }

   public boolean getBooleanProperty(String var1) {
      return this.getBooleanProperty(var1, false);
   }

   public boolean getBooleanProperty(String var1, boolean var2) {
      String var3 = this.getStringProperty(var1);
      return var3 == null ? var2 : "true".equalsIgnoreCase(var3);
   }

   public static Properties getSystemProperties() {
      try {
         return new Properties(System.getProperties());
      } catch (SecurityException var1) {
         StatusLogger.getLogger().error("Unable to access system properties.");
         return new Properties();
      }
   }
}
