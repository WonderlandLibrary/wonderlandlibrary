package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;

public class Util {
   public static Object func_181617_a(FutureTask var0, Logger var1) {
      try {
         var0.run();
         return var0.get();
      } catch (ExecutionException var3) {
         var1.fatal((String)"Error executing task", (Throwable)var3);
      } catch (InterruptedException var4) {
         var1.fatal((String)"Error executing task", (Throwable)var4);
      }

      return null;
   }

   public static Util.EnumOS getOSType() {
      String var0 = System.getProperty("os.name").toLowerCase();
      return var0.contains("win") ? Util.EnumOS.WINDOWS : (var0.contains("mac") ? Util.EnumOS.OSX : (var0.contains("solaris") ? Util.EnumOS.SOLARIS : (var0.contains("sunos") ? Util.EnumOS.SOLARIS : (var0.contains("linux") ? Util.EnumOS.LINUX : (var0.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN)))));
   }

   public static enum EnumOS {
      LINUX,
      OSX,
      SOLARIS,
      WINDOWS;

      private static final Util.EnumOS[] ENUM$VALUES = new Util.EnumOS[]{LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN};
      UNKNOWN;
   }
}
