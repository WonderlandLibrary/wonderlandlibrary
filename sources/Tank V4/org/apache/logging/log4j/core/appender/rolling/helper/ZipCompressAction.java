package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipCompressAction extends AbstractAction {
   private static final int BUF_SIZE = 8102;
   private final File source;
   private final File destination;
   private final boolean deleteSource;
   private final int level;

   public ZipCompressAction(File var1, File var2, boolean var3, int var4) {
      if (var1 == null) {
         throw new NullPointerException("source");
      } else if (var2 == null) {
         throw new NullPointerException("destination");
      } else {
         this.source = var1;
         this.destination = var2;
         this.deleteSource = var3;
         this.level = var4;
      }
   }

   public boolean execute() throws IOException {
      return execute(this.source, this.destination, this.deleteSource, this.level);
   }

   public static boolean execute(File var0, File var1, boolean var2, int var3) throws IOException {
      if (!var0.exists()) {
         return false;
      } else {
         FileInputStream var4 = new FileInputStream(var0);
         FileOutputStream var5 = new FileOutputStream(var1);
         ZipOutputStream var6 = new ZipOutputStream(var5);
         var6.setLevel(var3);
         ZipEntry var7 = new ZipEntry(var0.getName());
         var6.putNextEntry(var7);
         byte[] var8 = new byte[8102];

         int var9;
         while((var9 = var4.read(var8)) != -1) {
            var6.write(var8, 0, var9);
         }

         var6.close();
         var4.close();
         if (var2 && !var0.delete()) {
            LOGGER.warn("Unable to delete " + var0.toString() + '.');
         }

         return true;
      }
   }

   protected void reportException(Exception var1) {
      LOGGER.warn((String)("Exception during compression of '" + this.source.toString() + "'."), (Throwable)var1);
   }
}
