package org.apache.logging.log4j.core.helpers;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Throwables {
   public static List toStringList(Throwable var0) {
      StringWriter var1 = new StringWriter();
      PrintWriter var2 = new PrintWriter(var1);

      try {
         var0.printStackTrace(var2);
      } catch (RuntimeException var6) {
      }

      var2.flush();
      LineNumberReader var3 = new LineNumberReader(new StringReader(var1.toString()));
      ArrayList var4 = new ArrayList();

      try {
         for(String var5 = var3.readLine(); var5 != null; var5 = var3.readLine()) {
            var4.add(var5);
         }
      } catch (IOException var7) {
         if (var7 instanceof InterruptedIOException) {
            Thread.currentThread().interrupt();
         }

         var4.add(var7.toString());
      }

      return var4;
   }
}
