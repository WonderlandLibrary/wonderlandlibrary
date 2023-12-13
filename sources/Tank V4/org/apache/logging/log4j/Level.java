package org.apache.logging.log4j;

import java.util.Locale;

public enum Level {
   OFF(0),
   FATAL(1),
   ERROR(2),
   WARN(3),
   INFO(4),
   DEBUG(5),
   TRACE(6),
   ALL(Integer.MAX_VALUE);

   private final int intLevel;
   private static final Level[] $VALUES = new Level[]{OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL};

   private Level(int var3) {
      this.intLevel = var3;
   }

   public static Level toLevel(String var0) {
      return toLevel(var0, DEBUG);
   }

   public static Level toLevel(String var0, Level var1) {
      if (var0 == null) {
         return var1;
      } else {
         String var2 = var0.toUpperCase(Locale.ENGLISH);
         Level[] var3 = values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Level var6 = var3[var5];
            if (var6.name().equals(var2)) {
               return var6;
            }
         }

         return var1;
      }
   }

   public boolean isAtLeastAsSpecificAs(Level var1) {
      return this.intLevel <= var1.intLevel;
   }

   public boolean isAtLeastAsSpecificAs(int var1) {
      return this.intLevel <= var1;
   }

   public boolean lessOrEqual(Level var1) {
      return this.intLevel <= var1.intLevel;
   }

   public boolean lessOrEqual(int var1) {
      return this.intLevel <= var1;
   }

   public int intLevel() {
      return this.intLevel;
   }
}
