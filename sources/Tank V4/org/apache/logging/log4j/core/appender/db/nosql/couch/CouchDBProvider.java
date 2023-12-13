package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

@Plugin(
   name = "CouchDb",
   category = "Core",
   printObject = true
)
public final class CouchDBProvider implements NoSQLProvider {
   private static final int HTTP = 80;
   private static final int HTTPS = 443;
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final CouchDbClient client;
   private final String description;

   private CouchDBProvider(CouchDbClient var1, String var2) {
      this.client = var1;
      this.description = "couchDb{ " + var2 + " }";
   }

   public CouchDBConnection getConnection() {
      return new CouchDBConnection(this.client);
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static CouchDBProvider createNoSQLProvider(@PluginAttribute("databaseName") String var0, @PluginAttribute("protocol") String var1, @PluginAttribute("server") String var2, @PluginAttribute("port") String var3, @PluginAttribute("username") String var4, @PluginAttribute("password") String var5, @PluginAttribute("factoryClassName") String var6, @PluginAttribute("factoryMethodName") String var7) {
      CouchDbClient var8;
      String var9;
      if (var6 != null && var6.length() > 0 && var7 != null && var7.length() > 0) {
         try {
            Class var17 = Class.forName(var6);
            Method var11 = var17.getMethod(var7);
            Object var12 = var11.invoke((Object)null);
            if (var12 instanceof CouchDbClient) {
               var8 = (CouchDbClient)var12;
               var9 = "uri=" + var8.getDBUri();
            } else {
               if (!(var12 instanceof CouchDbProperties)) {
                  if (var12 == null) {
                     LOGGER.error("The factory method [{}.{}()] returned null.", var6, var7);
                     return null;
                  }

                  LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", var6, var7, var12.getClass().getName());
                  return null;
               }

               CouchDbProperties var13 = (CouchDbProperties)var12;
               var8 = new CouchDbClient(var13);
               var9 = "uri=" + var8.getDBUri() + ", username=" + var13.getUsername() + ", passwordHash=" + NameUtil.md5(var5 + CouchDBProvider.class.getName()) + ", maxConnections=" + var13.getMaxConnections() + ", connectionTimeout=" + var13.getConnectionTimeout() + ", socketTimeout=" + var13.getSocketTimeout();
            }
         } catch (ClassNotFoundException var14) {
            LOGGER.error("The factory class [{}] could not be loaded.", var6, var14);
            return null;
         } catch (NoSuchMethodException var15) {
            LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", var6, var7, var15);
            return null;
         } catch (Exception var16) {
            LOGGER.error("The factory method [{}.{}()] could not be invoked.", var6, var7, var16);
            return null;
         }
      } else {
         if (var0 == null || var0.length() <= 0) {
            LOGGER.error("No factory method was provided so the database name is required.");
            return null;
         }

         if (var1 != null && var1.length() > 0) {
            var1 = var1.toLowerCase();
            if (!var1.equals("http") && !var1.equals("https")) {
               LOGGER.error("Only protocols [http] and [https] are supported, [{}] specified.", var1);
               return null;
            }
         } else {
            var1 = "http";
            LOGGER.warn("No protocol specified, using default port [http].");
         }

         int var10 = AbstractAppender.parseInt(var3, var1.equals("https") ? 443 : 80);
         if (Strings.isEmpty(var2)) {
            var2 = "localhost";
            LOGGER.warn("No server specified, using default server localhost.");
         }

         if (Strings.isEmpty(var4) || Strings.isEmpty(var5)) {
            LOGGER.error("You must provide a username and password for the CouchDB provider.");
            return null;
         }

         var8 = new CouchDbClient(var0, false, var1, var2, var10, var4, var5);
         var9 = "uri=" + var8.getDBUri() + ", username=" + var4 + ", passwordHash=" + NameUtil.md5(var5 + CouchDBProvider.class.getName());
      }

      return new CouchDBProvider(var8, var9);
   }

   public NoSQLConnection getConnection() {
      return this.getConnection();
   }
}
