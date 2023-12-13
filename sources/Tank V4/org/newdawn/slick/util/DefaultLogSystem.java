package org.newdawn.slick.util;

import java.io.PrintStream;
import java.util.Date;

public class DefaultLogSystem implements LogSystem {
   public static PrintStream out;

   public void error(String var1, Throwable var2) {
      this.error(var1);
      this.error(var2);
   }

   public void error(Throwable var1) {
      out.println(new Date() + " ERROR:" + var1.getMessage());
      var1.printStackTrace(out);
   }

   public void error(String var1) {
      out.println(new Date() + " ERROR:" + var1);
   }

   public void warn(String var1) {
      out.println(new Date() + " WARN:" + var1);
   }

   public void info(String var1) {
      out.println(new Date() + " INFO:" + var1);
   }

   public void debug(String var1) {
      out.println(new Date() + " DEBUG:" + var1);
   }

   public void warn(String var1, Throwable var2) {
      this.warn(var1);
      var2.printStackTrace(out);
   }

   static {
      out = System.out;
   }
}
