package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UncaughtExceptionHandlers {
   private UncaughtExceptionHandlers() {
   }

   public static UncaughtExceptionHandler systemExit() {
      return new UncaughtExceptionHandlers.Exiter(Runtime.getRuntime());
   }

   @VisibleForTesting
   static final class Exiter implements UncaughtExceptionHandler {
      private static final Logger logger = Logger.getLogger(UncaughtExceptionHandlers.Exiter.class.getName());
      private final Runtime runtime;

      Exiter(Runtime var1) {
         this.runtime = var1;
      }

      public void uncaughtException(Thread var1, Throwable var2) {
         try {
            logger.log(Level.SEVERE, String.format("Caught an exception in %s.  Shutting down.", var1), var2);
         } catch (Throwable var5) {
            System.err.println(var2.getMessage());
            System.err.println(var5.getMessage());
            this.runtime.exit(1);
            return;
         }

         this.runtime.exit(1);
      }
   }
}
