package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public final class GZCompressAction extends AbstractAction {
   private static final int BUF_SIZE = 8102;
   private final File source;
   private final File destination;
   private final boolean deleteSource;

   public GZCompressAction(File var1, File var2, boolean var3) {
      if (var1 == null) {
         throw new NullPointerException("source");
      } else if (var2 == null) {
         throw new NullPointerException("destination");
      } else {
         this.source = var1;
         this.destination = var2;
         this.deleteSource = var3;
      }
   }

   public boolean execute() throws IOException {
      return execute(this.source, this.destination, this.deleteSource);
   }

   public static boolean execute(File var0, File var1, boolean var2) throws IOException {
      if (!var0.exists()) {
         return false;
      } else {
         FileInputStream var3 = new FileInputStream(var0);
         FileOutputStream var4 = new FileOutputStream(var1);
         GZIPOutputStream var5 = new GZIPOutputStream(var4);
         BufferedOutputStream var6 = new BufferedOutputStream(var5);
         byte[] var7 = new byte[8102];

         int var8;
         while((var8 = var3.read(var7)) != -1) {
            var6.write(var7, 0, var8);
         }

         var6.close();
         var3.close();
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
