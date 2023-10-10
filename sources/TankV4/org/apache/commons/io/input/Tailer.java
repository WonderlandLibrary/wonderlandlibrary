package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Tailer implements Runnable {
   private static final int DEFAULT_DELAY_MILLIS = 1000;
   private static final String RAF_MODE = "r";
   private static final int DEFAULT_BUFSIZE = 4096;
   private final byte[] inbuf;
   private final File file;
   private final long delayMillis;
   private final boolean end;
   private final TailerListener listener;
   private final boolean reOpen;
   private volatile boolean run;

   public Tailer(File var1, TailerListener var2) {
      this(var1, var2, 1000L);
   }

   public Tailer(File var1, TailerListener var2, long var3) {
      this(var1, var2, var3, false);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5) {
      this(var1, var2, var3, var5, 4096);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5, boolean var6) {
      this(var1, var2, var3, var5, var6, 4096);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5, int var6) {
      this(var1, var2, var3, var5, false, var6);
   }

   public Tailer(File var1, TailerListener var2, long var3, boolean var5, boolean var6, int var7) {
      this.run = true;
      this.file = var1;
      this.delayMillis = var3;
      this.end = var5;
      this.inbuf = new byte[var7];
      this.listener = var2;
      var2.init(this);
      this.reOpen = var6;
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4, int var5) {
      Tailer var6 = new Tailer(var0, var1, var2, var4, var5);
      Thread var7 = new Thread(var6);
      var7.setDaemon(true);
      var7.start();
      return var6;
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4, boolean var5, int var6) {
      Tailer var7 = new Tailer(var0, var1, var2, var4, var5, var6);
      Thread var8 = new Thread(var7);
      var8.setDaemon(true);
      var8.start();
      return var7;
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4) {
      return create(var0, var1, var2, var4, 4096);
   }

   public static Tailer create(File var0, TailerListener var1, long var2, boolean var4, boolean var5) {
      return create(var0, var1, var2, var4, var5, 4096);
   }

   public static Tailer create(File var0, TailerListener var1, long var2) {
      return create(var0, var1, var2, false);
   }

   public static Tailer create(File var0, TailerListener var1) {
      return create(var0, var1, 1000L, false);
   }

   public File getFile() {
      return this.file;
   }

   public long getDelay() {
      return this.delayMillis;
   }

   public void run() {
      RandomAccessFile var1 = null;

      try {
         long var2 = 0L;
         long var4 = 0L;

         while(this.run && var1 == null) {
            try {
               var1 = new RandomAccessFile(this.file, "r");
            } catch (FileNotFoundException var14) {
               this.listener.fileNotFound();
            }

            if (var1 == null) {
               try {
                  Thread.sleep(this.delayMillis);
               } catch (InterruptedException var13) {
               }
            } else {
               var4 = this.end ? this.file.length() : 0L;
               var2 = System.currentTimeMillis();
               var1.seek(var4);
            }
         }

         while(this.run) {
            boolean var6 = FileUtils.isFileNewer(this.file, var2);
            long var7 = this.file.length();
            if (var7 < var4) {
               this.listener.fileRotated();

               try {
                  RandomAccessFile var9 = var1;
                  var1 = new RandomAccessFile(this.file, "r");
                  var4 = 0L;
                  IOUtils.closeQuietly((Closeable)var9);
               } catch (FileNotFoundException var12) {
                  this.listener.fileNotFound();
               }
            } else {
               if (var7 > var4) {
                  var4 = this.readLines(var1);
                  var2 = System.currentTimeMillis();
               } else if (var6) {
                  var4 = 0L;
                  var1.seek(var4);
                  var4 = this.readLines(var1);
                  var2 = System.currentTimeMillis();
               }

               if (this.reOpen) {
                  IOUtils.closeQuietly((Closeable)var1);
               }

               try {
                  Thread.sleep(this.delayMillis);
               } catch (InterruptedException var11) {
               }

               if (this.run && this.reOpen) {
                  var1 = new RandomAccessFile(this.file, "r");
                  var1.seek(var4);
               }
            }
         }
      } catch (Exception var15) {
         this.listener.handle(var15);
         IOUtils.closeQuietly((Closeable)var1);
         return;
      }

      IOUtils.closeQuietly((Closeable)var1);
   }

   public void stop() {
      this.run = false;
   }

   private long readLines(RandomAccessFile var1) throws IOException {
      StringBuilder var2 = new StringBuilder();
      long var3 = var1.getFilePointer();
      long var5 = var3;

      int var7;
      for(boolean var8 = false; this.run && (var7 = var1.read(this.inbuf)) != -1; var3 = var1.getFilePointer()) {
         for(int var9 = 0; var9 < var7; ++var9) {
            byte var10 = this.inbuf[var9];
            switch(var10) {
            case 10:
               var8 = false;
               this.listener.handle(var2.toString());
               var2.setLength(0);
               var5 = var3 + (long)var9 + 1L;
               break;
            case 13:
               if (var8) {
                  var2.append('\r');
               }

               var8 = true;
               break;
            default:
               if (var8) {
                  var8 = false;
                  this.listener.handle(var2.toString());
                  var2.setLength(0);
                  var5 = var3 + (long)var9 + 1L;
               }

               var2.append((char)var10);
            }
         }
      }

      var1.seek(var5);
      return var5;
   }
}
