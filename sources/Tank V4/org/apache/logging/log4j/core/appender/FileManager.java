package org.apache.logging.log4j.core.appender;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;

public class FileManager extends OutputStreamManager {
   private static final FileManager.FileManagerFactory FACTORY = new FileManager.FileManagerFactory();
   private final boolean isAppend;
   private final boolean isLocking;
   private final String advertiseURI;

   protected FileManager(String var1, OutputStream var2, boolean var3, boolean var4, String var5, Layout var6) {
      super(var2, var1, var6);
      this.isAppend = var3;
      this.isLocking = var4;
      this.advertiseURI = var5;
   }

   public static FileManager getFileManager(String var0, boolean var1, boolean var2, boolean var3, String var4, Layout var5) {
      if (var2 && var3) {
         var2 = false;
      }

      return (FileManager)getManager(var0, new FileManager.FactoryData(var1, var2, var3, var4, var5), FACTORY);
   }

   protected synchronized void write(byte[] var1, int var2, int var3) {
      if (this.isLocking) {
         FileChannel var4 = ((FileOutputStream)this.getOutputStream()).getChannel();

         try {
            FileLock var5 = var4.lock(0L, Long.MAX_VALUE, false);
            super.write(var1, var2, var3);
            var5.release();
         } catch (IOException var7) {
            throw new AppenderLoggingException("Unable to obtain lock on " + this.getName(), var7);
         }
      } else {
         super.write(var1, var2, var3);
      }

   }

   public String getFileName() {
      return this.getName();
   }

   public boolean isAppend() {
      return this.isAppend;
   }

   public boolean isLocking() {
      return this.isLocking;
   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("fileURI", this.advertiseURI);
      return var1;
   }

   private static class FileManagerFactory implements ManagerFactory {
      private FileManagerFactory() {
      }

      public FileManager createManager(String var1, FileManager.FactoryData var2) {
         File var3 = new File(var1);
         File var4 = var3.getParentFile();
         if (null != var4 && !var4.exists()) {
            var4.mkdirs();
         }

         try {
            Object var5 = new FileOutputStream(var1, FileManager.FactoryData.access$100(var2));
            if (FileManager.FactoryData.access$200(var2)) {
               var5 = new BufferedOutputStream((OutputStream)var5);
            }

            return new FileManager(var1, (OutputStream)var5, FileManager.FactoryData.access$100(var2), FileManager.FactoryData.access$300(var2), FileManager.FactoryData.access$400(var2), FileManager.FactoryData.access$500(var2));
         } catch (FileNotFoundException var7) {
            AbstractManager.LOGGER.error("FileManager (" + var1 + ") " + var7);
            return null;
         }
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (FileManager.FactoryData)var2);
      }

      FileManagerFactory(Object var1) {
         this();
      }
   }

   private static class FactoryData {
      private final boolean append;
      private final boolean locking;
      private final boolean bufferedIO;
      private final String advertiseURI;
      private final Layout layout;

      public FactoryData(boolean var1, boolean var2, boolean var3, String var4, Layout var5) {
         this.append = var1;
         this.locking = var2;
         this.bufferedIO = var3;
         this.advertiseURI = var4;
         this.layout = var5;
      }

      static boolean access$100(FileManager.FactoryData var0) {
         return var0.append;
      }

      static boolean access$200(FileManager.FactoryData var0) {
         return var0.bufferedIO;
      }

      static boolean access$300(FileManager.FactoryData var0) {
         return var0.locking;
      }

      static String access$400(FileManager.FactoryData var0) {
         return var0.advertiseURI;
      }

      static Layout access$500(FileManager.FactoryData var0) {
         return var0.layout;
      }
   }
}
