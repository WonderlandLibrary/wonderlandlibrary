package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Closeables {
   @VisibleForTesting
   static final Logger logger = Logger.getLogger(Closeables.class.getName());

   private Closeables() {
   }

   public static void close(@Nullable Closeable var0, boolean var1) throws IOException {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var3) {
            if (!var1) {
               throw var3;
            }

            logger.log(Level.WARNING, "IOException thrown while closing Closeable.", var3);
         }

      }
   }

   public static void closeQuietly(@Nullable InputStream var0) {
      try {
         close(var0, true);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }

   public static void closeQuietly(@Nullable Reader var0) {
      try {
         close(var0, true);
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }
}
