package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "MongoDb",
   category = "Core",
   printObject = true
)
public final class MongoDBProvider implements NoSQLProvider {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final String collectionName;
   private final DB database;
   private final String description;
   private final WriteConcern writeConcern;

   private MongoDBProvider(DB var1, WriteConcern var2, String var3, String var4) {
      this.database = var1;
      this.writeConcern = var2;
      this.collectionName = var3;
      this.description = "mongoDb{ " + var4 + " }";
   }

   public MongoDBConnection getConnection() {
      return new MongoDBConnection(this.database, this.writeConcern, this.collectionName);
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static MongoDBProvider createNoSQLProvider(@PluginAttribute("collectionName") String var0, @PluginAttribute("writeConcernConstant") String var1, @PluginAttribute("writeConcernConstantClass") String var2, @PluginAttribute("databaseName") String var3, @PluginAttribute("server") String var4, @PluginAttribute("port") String var5, @PluginAttribute("username") String var6, @PluginAttribute("password") String var7, @PluginAttribute("factoryClassName") String var8, @PluginAttribute("factoryMethodName") String var9) {
      DB var10;
      String var11;
      if (var8 != null && var8.length() > 0 && var9 != null && var9.length() > 0) {
         try {
            Class var23 = Class.forName(var8);
            Method var13 = var23.getMethod(var9);
            Object var14 = var13.invoke((Object)null);
            if (var14 instanceof DB) {
               var10 = (DB)var14;
            } else {
               if (!(var14 instanceof MongoClient)) {
                  if (var14 == null) {
                     LOGGER.error("The factory method [{}.{}()] returned null.", var8, var9);
                     return null;
                  }

                  LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", var8, var9, var14.getClass().getName());
                  return null;
               }

               if (var3 == null || var3.length() <= 0) {
                  LOGGER.error("The factory method [{}.{}()] returned a MongoClient so the database name is required.", var8, var9);
                  return null;
               }

               var10 = ((MongoClient)var14).getDB(var3);
            }

            var11 = "database=" + var10.getName();
            List var15 = var10.getMongo().getAllAddress();
            if (var15.size() == 1) {
               var11 = var11 + ", server=" + ((ServerAddress)var15.get(0)).getHost() + ", port=" + ((ServerAddress)var15.get(0)).getPort();
            } else {
               var11 = var11 + ", servers=[";

               ServerAddress var17;
               for(Iterator var16 = var15.iterator(); var16.hasNext(); var11 = var11 + " { " + var17.getHost() + ", " + var17.getPort() + " } ") {
                  var17 = (ServerAddress)var16.next();
               }

               var11 = var11 + "]";
            }
         } catch (ClassNotFoundException var20) {
            LOGGER.error("The factory class [{}] could not be loaded.", var8, var20);
            return null;
         } catch (NoSuchMethodException var21) {
            LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", var8, var9, var21);
            return null;
         } catch (Exception var22) {
            LOGGER.error("The factory method [{}.{}()] could not be invoked.", var8, var9, var22);
            return null;
         }
      } else {
         if (var3 == null || var3.length() <= 0) {
            LOGGER.error("No factory method was provided so the database name is required.");
            return null;
         }

         var11 = "database=" + var3;

         try {
            if (var4 != null && var4.length() > 0) {
               int var12 = AbstractAppender.parseInt(var5, 0);
               var11 = var11 + ", server=" + var4;
               if (var12 > 0) {
                  var11 = var11 + ", port=" + var12;
                  var10 = (new MongoClient(var4, var12)).getDB(var3);
               } else {
                  var10 = (new MongoClient(var4)).getDB(var3);
               }
            } else {
               var10 = (new MongoClient()).getDB(var3);
            }
         } catch (Exception var19) {
            LOGGER.error("Failed to obtain a database instance from the MongoClient at server [{}] and port [{}].", var4, var5);
            return null;
         }
      }

      if (!var10.isAuthenticated()) {
         if (var6 == null || var6.length() <= 0 || var7 == null || var7.length() <= 0) {
            LOGGER.error("The database is not already authenticated so you must supply a username and password for the MongoDB provider.");
            return null;
         }

         var11 = var11 + ", username=" + var6 + ", passwordHash=" + NameUtil.md5(var7 + MongoDBProvider.class.getName());
         MongoDBConnection.authenticate(var10, var6, var7);
      }

      WriteConcern var24;
      if (var1 != null && var1.length() > 0) {
         if (var2 != null && var2.length() > 0) {
            try {
               Class var25 = Class.forName(var2);
               Field var26 = var25.getField(var1);
               var24 = (WriteConcern)var26.get((Object)null);
            } catch (Exception var18) {
               LOGGER.error("Write concern constant [{}.{}] not found, using default.", var2, var1);
               var24 = WriteConcern.ACKNOWLEDGED;
            }
         } else {
            var24 = WriteConcern.valueOf(var1);
            if (var24 == null) {
               LOGGER.warn("Write concern constant [{}] not found, using default.", var1);
               var24 = WriteConcern.ACKNOWLEDGED;
            }
         }
      } else {
         var24 = WriteConcern.ACKNOWLEDGED;
      }

      return new MongoDBProvider(var10, var24, var0, var11);
   }

   public NoSQLConnection getConnection() {
      return this.getConnection();
   }
}
