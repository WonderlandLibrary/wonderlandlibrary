package org.apache.logging.log4j.core.helpers;

public class Integers {
   public static int parseInt(String var0, int var1) {
      return Strings.isEmpty(var0) ? var1 : Integer.parseInt(var0);
   }

   public static int parseInt(String var0) {
      return parseInt(var0, 0);
   }
}
