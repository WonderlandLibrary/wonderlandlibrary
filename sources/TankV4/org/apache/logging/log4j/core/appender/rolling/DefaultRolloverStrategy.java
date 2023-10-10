package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;
import org.apache.logging.log4j.core.appender.rolling.helper.FileRenameAction;
import org.apache.logging.log4j.core.appender.rolling.helper.GZCompressAction;
import org.apache.logging.log4j.core.appender.rolling.helper.ZipCompressAction;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "DefaultRolloverStrategy",
   category = "Core",
   printObject = true
)
public class DefaultRolloverStrategy implements RolloverStrategy {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static final int MIN_WINDOW_SIZE = 1;
   private static final int DEFAULT_WINDOW_SIZE = 7;
   private final int maxIndex;
   private final int minIndex;
   private final boolean useMax;
   private final StrSubstitutor subst;
   private final int compressionLevel;

   protected DefaultRolloverStrategy(int var1, int var2, boolean var3, int var4, StrSubstitutor var5) {
      this.minIndex = var1;
      this.maxIndex = var2;
      this.useMax = var3;
      this.compressionLevel = var4;
      this.subst = var5;
   }

   public RolloverDescription rollover(RollingFileManager var1) throws SecurityException {
      if (this.maxIndex >= 0) {
         int var2;
         if ((var2 = this.purge(this.minIndex, this.maxIndex, var1)) < 0) {
            return null;
         } else {
            StringBuilder var3 = new StringBuilder();
            var1.getPatternProcessor().formatFileName(this.subst, var3, var2);
            String var4 = var1.getFileName();
            String var5 = var3.toString();
            String var6 = var5;
            Object var7 = null;
            if (var5.endsWith(".gz")) {
               var5 = var5.substring(0, var5.length() - 3);
               var7 = new GZCompressAction(new File(var5), new File(var6), true);
            } else if (var5.endsWith(".zip")) {
               var5 = var5.substring(0, var5.length() - 4);
               var7 = new ZipCompressAction(new File(var5), new File(var6), true, this.compressionLevel);
            }

            FileRenameAction var8 = new FileRenameAction(new File(var4), new File(var5), false);
            return new RolloverDescriptionImpl(var4, false, var8, (Action)var7);
         }
      } else {
         return null;
      }
   }

   private int purge(int var1, int var2, RollingFileManager var3) {
      return this.useMax ? this.purgeAscending(var1, var2, var3) : this.purgeDescending(var1, var2, var3);
   }

   private int purgeDescending(int var1, int var2, RollingFileManager var3) {
      byte var4 = 0;
      ArrayList var5 = new ArrayList();
      StringBuilder var6 = new StringBuilder();
      var3.getPatternProcessor().formatFileName(var6, (Object)var1);
      String var7 = this.subst.replace(var6);
      if (var7.endsWith(".gz")) {
         var4 = 3;
      } else if (var7.endsWith(".zip")) {
         var4 = 4;
      }

      int var8;
      for(var8 = var1; var8 <= var2; ++var8) {
         File var9 = new File(var7);
         boolean var10 = false;
         if (var4 > 0) {
            File var11 = new File(var7.substring(0, var7.length() - var4));
            if (var9.exists()) {
               if (var11.exists()) {
                  var11.delete();
               }
            } else {
               var9 = var11;
               var10 = true;
            }
         }

         if (!var9.exists()) {
            break;
         }

         if (var8 == var2) {
            if (!var9.delete()) {
               return -1;
            }
            break;
         }

         var6.setLength(0);
         var3.getPatternProcessor().formatFileName(var6, (Object)(var8 + 1));
         String var15 = this.subst.replace(var6);
         String var12 = var15;
         if (var10) {
            var12 = var15.substring(0, var15.length() - var4);
         }

         var5.add(new FileRenameAction(var9, new File(var12), true));
         var7 = var15;
      }

      for(var8 = var5.size() - 1; var8 >= 0; --var8) {
         Action var14 = (Action)var5.get(var8);

         try {
            if (!var14.execute()) {
               return -1;
            }
         } catch (Exception var13) {
            LOGGER.warn((String)"Exception during purge in RollingFileAppender", (Throwable)var13);
            return -1;
         }
      }

      return var1;
   }

   private int purgeAscending(int var1, int var2, RollingFileManager var3) {
      byte var4 = 0;
      ArrayList var5 = new ArrayList();
      StringBuilder var6 = new StringBuilder();
      var3.getPatternProcessor().formatFileName(var6, (Object)var2);
      String var7 = this.subst.replace(var6);
      if (var7.endsWith(".gz")) {
         var4 = 3;
      } else if (var7.endsWith(".zip")) {
         var4 = 4;
      }

      int var8 = 0;

      int var9;
      for(var9 = var2; var9 >= var1; --var9) {
         File var10 = new File(var7);
         if (var9 == var2 && var10.exists()) {
            var8 = var2;
         } else if (var8 == 0 && var10.exists()) {
            var8 = var9 + 1;
            break;
         }

         boolean var11 = false;
         if (var4 > 0) {
            File var12 = new File(var7.substring(0, var7.length() - var4));
            if (var10.exists()) {
               if (var12.exists()) {
                  var12.delete();
               }
            } else {
               var10 = var12;
               var11 = true;
            }
         }

         if (var10.exists()) {
            if (var9 == var1) {
               if (!var10.delete()) {
                  return -1;
               }
               break;
            }

            var6.setLength(0);
            var3.getPatternProcessor().formatFileName(var6, (Object)(var9 - 1));
            String var16 = this.subst.replace(var6);
            String var13 = var16;
            if (var11) {
               var13 = var16.substring(0, var16.length() - var4);
            }

            var5.add(new FileRenameAction(var10, new File(var13), true));
            var7 = var16;
         } else {
            var6.setLength(0);
            var3.getPatternProcessor().formatFileName(var6, (Object)(var9 - 1));
            var7 = this.subst.replace(var6);
         }
      }

      if (var8 == 0) {
         var8 = var1;
      }

      for(var9 = var5.size() - 1; var9 >= 0; --var9) {
         Action var15 = (Action)var5.get(var9);

         try {
            if (!var15.execute()) {
               return -1;
            }
         } catch (Exception var14) {
            LOGGER.warn((String)"Exception during purge in RollingFileAppender", (Throwable)var14);
            return -1;
         }
      }

      return var8;
   }

   public String toString() {
      return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ")";
   }

   @PluginFactory
   public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") String var0, @PluginAttribute("min") String var1, @PluginAttribute("fileIndex") String var2, @PluginAttribute("compressionLevel") String var3, @PluginConfiguration Configuration var4) {
      boolean var5 = var2 == null ? true : var2.equalsIgnoreCase("max");
      int var6;
      if (var1 != null) {
         var6 = Integer.parseInt(var1);
         if (var6 < 1) {
            LOGGER.error("Minimum window size too small. Limited to 1");
            var6 = 1;
         }
      } else {
         var6 = 1;
      }

      int var7;
      if (var0 != null) {
         var7 = Integer.parseInt(var0);
         if (var7 < var6) {
            var7 = var6 < 7 ? 7 : var6;
            LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + var7);
         }
      } else {
         var7 = 7;
      }

      int var8 = Integers.parseInt(var3, -1);
      return new DefaultRolloverStrategy(var6, var7, var5, var8, var4.getStrSubstitutor());
   }
}
