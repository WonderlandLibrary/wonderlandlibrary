package org.apache.logging.log4j.core.appender.db.jpa;

import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Strings;

@Plugin(
   name = "JPA",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public final class JPAAppender extends AbstractDatabaseAppender {
   private final String description = this.getName() + "{ manager=" + this.getManager() + " }";

   private JPAAppender(String var1, Filter var2, boolean var3, JPADatabaseManager var4) {
      super(var1, var2, var3, var4);
   }

   public String toString() {
      return this.description;
   }

   @PluginFactory
   public static JPAAppender createAppender(@PluginAttribute("name") String var0, @PluginAttribute("ignoreExceptions") String var1, @PluginElement("Filter") Filter var2, @PluginAttribute("bufferSize") String var3, @PluginAttribute("entityClassName") String var4, @PluginAttribute("persistenceUnitName") String var5) {
      if (!Strings.isEmpty(var4) && !Strings.isEmpty(var5)) {
         int var6 = AbstractAppender.parseInt(var3, 0);
         boolean var7 = Booleans.parseBoolean(var1, true);

         try {
            Class var8 = Class.forName(var4);
            if (!AbstractLogEventWrapperEntity.class.isAssignableFrom(var8)) {
               LOGGER.error("Entity class [{}] does not extend AbstractLogEventWrapperEntity.", var4);
               return null;
            } else {
               try {
                  var8.getConstructor();
               } catch (NoSuchMethodException var12) {
                  LOGGER.error("Entity class [{}] does not have a no-arg constructor. The JPA provider will reject it.", var4);
                  return null;
               }

               Constructor var9 = var8.getConstructor(LogEvent.class);
               String var10 = "jpaManager{ description=" + var0 + ", bufferSize=" + var6 + ", persistenceUnitName=" + var5 + ", entityClass=" + var8.getName() + "}";
               JPADatabaseManager var11 = JPADatabaseManager.getJPADatabaseManager(var10, var6, var8, var9, var5);
               return var11 == null ? null : new JPAAppender(var0, var2, var7, var11);
            }
         } catch (ClassNotFoundException var13) {
            LOGGER.error("Could not load entity class [{}].", var4, var13);
            return null;
         } catch (NoSuchMethodException var14) {
            LOGGER.error("Entity class [{}] does not have a constructor with a single argument of type LogEvent.", var4);
            return null;
         }
      } else {
         LOGGER.error("Attributes entityClassName and persistenceUnitName are required for JPA Appender.");
         return null;
      }
   }
}
