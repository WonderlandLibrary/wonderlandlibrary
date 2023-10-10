package org.apache.logging.log4j.core.helpers;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Closer {
   public static void closeSilent(Closeable var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Exception var2) {
      }

   }

   public static void close(Closeable var0) throws IOException {
      if (var0 != null) {
         var0.close();
      }

   }

   public static void closeSilent(Statement var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Exception var2) {
      }

   }

   public static void close(Statement var0) throws SQLException {
      if (var0 != null) {
         var0.close();
      }

   }

   public static void closeSilent(Connection var0) {
      try {
         if (var0 != null) {
            var0.close();
         }
      } catch (Exception var2) {
      }

   }

   public static void close(Connection var0) throws SQLException {
      if (var0 != null) {
         var0.close();
      }

   }
}
