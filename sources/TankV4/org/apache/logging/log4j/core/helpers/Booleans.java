package org.apache.logging.log4j.core.helpers;

public class Booleans {
   public static boolean parseBoolean(String var0, boolean var1) {
      return "true".equalsIgnoreCase(var0) || var1 && !"false".equalsIgnoreCase(var0);
   }
}
