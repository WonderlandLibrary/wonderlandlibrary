package com.sun.javafx.binding;

import sun.util.logging.PlatformLogger;

public class Logging {
   public static PlatformLogger getLogger() {
      return Logging.LoggerHolder.INSTANCE;
   }

   private static class LoggerHolder {
      private static final PlatformLogger INSTANCE = PlatformLogger.getLogger("javafx.beans");
   }
}
