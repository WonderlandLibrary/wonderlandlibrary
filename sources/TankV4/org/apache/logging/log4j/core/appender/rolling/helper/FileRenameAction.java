package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileRenameAction extends AbstractAction {
   private final File source;
   private final File destination;
   private final boolean renameEmptyFiles;

   public FileRenameAction(File var1, File var2, boolean var3) {
      this.source = var1;
      this.destination = var2;
      this.renameEmptyFiles = var3;
   }

   public boolean execute() {
      return execute(this.source, this.destination, this.renameEmptyFiles);
   }

   public static boolean execute(File var0, File var1, boolean var2) {
      if (!var2 && var0.length() <= 0L) {
         try {
            var0.delete();
         } catch (Exception var6) {
            LOGGER.error("Unable to delete empty file " + var0.getAbsolutePath());
         }
      } else {
         File var3 = var1.getParentFile();
         if (var3 != null && !var3.exists() && !var3.mkdirs()) {
            LOGGER.error("Unable to create directory {}", var3.getAbsolutePath());
            return false;
         }

         try {
            if (!var0.renameTo(var1)) {
               try {
                  copyFile(var0, var1);
                  return var0.delete();
               } catch (IOException var7) {
                  LOGGER.error("Unable to rename file {} to {} - {}", var0.getAbsolutePath(), var1.getAbsolutePath(), var7.getMessage());
               }
            }

            return true;
         } catch (Exception var9) {
            try {
               copyFile(var0, var1);
               return var0.delete();
            } catch (IOException var8) {
               LOGGER.error("Unable to rename file {} to {} - {}", var0.getAbsolutePath(), var1.getAbsolutePath(), var8.getMessage());
            }
         }
      }

      return false;
   }

   private static void copyFile(File var0, File var1) throws IOException {
      if (!var1.exists()) {
         var1.createNewFile();
      }

      FileChannel var2 = null;
      FileChannel var3 = null;
      FileInputStream var4 = null;
      FileOutputStream var5 = null;
      var4 = new FileInputStream(var0);
      var5 = new FileOutputStream(var1);
      var2 = var4.getChannel();
      var3 = var5.getChannel();
      var3.transferFrom(var2, 0L, var2.size());
      if (var2 != null) {
         var2.close();
      }

      if (var4 != null) {
         var4.close();
      }

      if (var3 != null) {
         var3.close();
      }

      if (var5 != null) {
         var5.close();
      }

   }
}
