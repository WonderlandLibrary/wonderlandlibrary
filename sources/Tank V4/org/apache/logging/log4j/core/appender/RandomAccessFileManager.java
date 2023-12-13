package org.apache.logging.log4j.core.appender;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;

public class RandomAccessFileManager extends OutputStreamManager {
   static final int DEFAULT_BUFFER_SIZE = 262144;
   private static final RandomAccessFileManager.RandomAccessFileManagerFactory FACTORY = new RandomAccessFileManager.RandomAccessFileManagerFactory();
   private final boolean isImmediateFlush;
   private final String advertiseURI;
   private final RandomAccessFile randomAccessFile;
   private final ByteBuffer buffer;
   private final ThreadLocal isEndOfBatch = new ThreadLocal();

   protected RandomAccessFileManager(RandomAccessFile var1, String var2, OutputStream var3, boolean var4, String var5, Layout var6) {
      super(var3, var2, var6);
      this.isImmediateFlush = var4;
      this.randomAccessFile = var1;
      this.advertiseURI = var5;
      this.isEndOfBatch.set(Boolean.FALSE);
      this.buffer = ByteBuffer.allocate(262144);
   }

   public static RandomAccessFileManager getFileManager(String var0, boolean var1, boolean var2, String var3, Layout var4) {
      return (RandomAccessFileManager)getManager(var0, new RandomAccessFileManager.FactoryData(var1, var2, var3, var4), FACTORY);
   }

   public Boolean isEndOfBatch() {
      return (Boolean)this.isEndOfBatch.get();
   }

   public void setEndOfBatch(boolean var1) {
      this.isEndOfBatch.set(var1);
   }

   protected synchronized void write(byte[] var1, int var2, int var3) {
      super.write(var1, var2, var3);
      boolean var4 = false;

      do {
         if (var3 > this.buffer.remaining()) {
            this.flush();
         }

         int var5 = Math.min(var3, this.buffer.remaining());
         this.buffer.put(var1, var2, var5);
         var2 += var5;
         var3 -= var5;
      } while(var3 > 0);

      if (this.isImmediateFlush || this.isEndOfBatch.get() == Boolean.TRUE) {
         this.flush();
      }

   }

   public synchronized void flush() {
      this.buffer.flip();

      try {
         this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
      } catch (IOException var3) {
         String var2 = "Error writing to RandomAccessFile " + this.getName();
         throw new AppenderLoggingException(var2, var3);
      }

      this.buffer.clear();
   }

   public synchronized void close() {
      this.flush();

      try {
         this.randomAccessFile.close();
      } catch (IOException var2) {
         LOGGER.error("Unable to close RandomAccessFile " + this.getName() + ". " + var2);
      }

   }

   public String getFileName() {
      return this.getName();
   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("fileURI", this.advertiseURI);
      return var1;
   }

   private static class RandomAccessFileManagerFactory implements ManagerFactory {
      private RandomAccessFileManagerFactory() {
      }

      public RandomAccessFileManager createManager(String var1, RandomAccessFileManager.FactoryData var2) {
         File var3 = new File(var1);
         File var4 = var3.getParentFile();
         if (null != var4 && !var4.exists()) {
            var4.mkdirs();
         }

         if (!RandomAccessFileManager.FactoryData.access$100(var2)) {
            var3.delete();
         }

         RandomAccessFileManager.DummyOutputStream var5 = new RandomAccessFileManager.DummyOutputStream();

         try {
            RandomAccessFile var6 = new RandomAccessFile(var1, "rw");
            if (RandomAccessFileManager.FactoryData.access$100(var2)) {
               var6.seek(var6.length());
            } else {
               var6.setLength(0L);
            }

            return new RandomAccessFileManager(var6, var1, var5, RandomAccessFileManager.FactoryData.access$200(var2), RandomAccessFileManager.FactoryData.access$300(var2), RandomAccessFileManager.FactoryData.access$400(var2));
         } catch (Exception var8) {
            AbstractManager.LOGGER.error("RandomAccessFileManager (" + var1 + ") " + var8);
            return null;
         }
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (RandomAccessFileManager.FactoryData)var2);
      }

      RandomAccessFileManagerFactory(Object var1) {
         this();
      }
   }

   private static class FactoryData {
      private final boolean append;
      private final boolean immediateFlush;
      private final String advertiseURI;
      private final Layout layout;

      public FactoryData(boolean var1, boolean var2, String var3, Layout var4) {
         this.append = var1;
         this.immediateFlush = var2;
         this.advertiseURI = var3;
         this.layout = var4;
      }

      static boolean access$100(RandomAccessFileManager.FactoryData var0) {
         return var0.append;
      }

      static boolean access$200(RandomAccessFileManager.FactoryData var0) {
         return var0.immediateFlush;
      }

      static String access$300(RandomAccessFileManager.FactoryData var0) {
         return var0.advertiseURI;
      }

      static Layout access$400(RandomAccessFileManager.FactoryData var0) {
         return var0.layout;
      }
   }

   static class DummyOutputStream extends OutputStream {
      public void write(int var1) throws IOException {
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
      }
   }
}
