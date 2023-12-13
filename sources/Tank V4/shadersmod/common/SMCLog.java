package shadersmod.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SMCLog {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String PREFIX = "[Shaders] ";

   public static void severe(String var0) {
      LOGGER.error("[Shaders] " + var0);
   }

   public static void warning(String var0) {
      LOGGER.warn("[Shaders] " + var0);
   }

   public static void info(String var0) {
      LOGGER.info("[Shaders] " + var0);
   }

   public static void fine(String var0) {
      LOGGER.debug("[Shaders] " + var0);
   }

   public static void severe(String var0, Object... var1) {
      String var2 = String.format(var0, var1);
      LOGGER.error("[Shaders] " + var2);
   }

   public static void warning(String var0, Object... var1) {
      String var2 = String.format(var0, var1);
      LOGGER.warn("[Shaders] " + var2);
   }

   public static void info(String var0, Object... var1) {
      String var2 = String.format(var0, var1);
      LOGGER.info("[Shaders] " + var2);
   }

   public static void fine(String var0, Object... var1) {
      String var2 = String.format(var0, var1);
      LOGGER.debug("[Shaders] " + var2);
   }
}
