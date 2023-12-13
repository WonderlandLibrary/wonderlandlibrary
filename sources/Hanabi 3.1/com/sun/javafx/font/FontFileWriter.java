package com.sun.javafx.font;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class FontFileWriter implements FontConstants {
   byte[] header;
   int pos;
   int headerPos;
   int writtenBytes;
   FontTracker tracker;
   File file;
   RandomAccessFile raFile;

   public FontFileWriter() {
      if (!hasTempPermission()) {
         this.tracker = FontFileWriter.FontTracker.getTracker();
      }

   }

   protected void setLength(int var1) throws IOException {
      if (this.raFile == null) {
         throw new IOException("File not open");
      } else {
         this.checkTracker(var1);
         this.raFile.setLength((long)var1);
      }
   }

   public void seek(int var1) throws IOException {
      if (this.raFile == null) {
         throw new IOException("File not open");
      } else {
         if (var1 != this.pos) {
            this.raFile.seek((long)var1);
            this.pos = var1;
         }

      }
   }

   public File getFile() {
      return this.file;
   }

   public File openFile() throws PrivilegedActionException {
      this.pos = 0;
      this.writtenBytes = 0;
      this.file = (File)AccessController.doPrivileged(() -> {
         try {
            return Files.createTempFile("+JXF", ".tmp").toFile();
         } catch (IOException var1) {
            throw new IOException("Unable to create temporary file");
         }
      });
      if (this.tracker != null) {
         this.tracker.add(this.file);
      }

      this.raFile = (RandomAccessFile)AccessController.doPrivileged(() -> {
         return new RandomAccessFile(this.file, "rw");
      });
      if (this.tracker != null) {
         this.tracker.set(this.file, this.raFile);
      }

      if (PrismFontFactory.debugFonts) {
         System.err.println("Temp file created: " + this.file.getPath());
      }

      return this.file;
   }

   public void closeFile() throws IOException {
      if (this.header != null) {
         this.raFile.seek(0L);
         this.raFile.write(this.header);
         this.header = null;
      }

      if (this.raFile != null) {
         this.raFile.close();
         this.raFile = null;
      }

      if (this.tracker != null) {
         this.tracker.remove(this.file);
      }

   }

   public void deleteFile() {
      if (this.file != null) {
         if (this.tracker != null) {
            this.tracker.subBytes(this.writtenBytes);
         }

         try {
            this.closeFile();
         } catch (Exception var3) {
         }

         try {
            AccessController.doPrivileged(() -> {
               this.file.delete();
               return null;
            });
            if (PrismFontFactory.debugFonts) {
               System.err.println("Temp file delete: " + this.file.getPath());
            }
         } catch (Exception var2) {
         }

         this.file = null;
         this.raFile = null;
      }

   }

   public boolean isTracking() {
      return this.tracker != null;
   }

   private void checkTracker(int var1) throws IOException {
      if (this.tracker != null) {
         if (var1 < 0 || this.pos > 33554432 - var1) {
            throw new IOException("File too big.");
         }

         if (this.tracker.getNumBytes() > 335544320 - var1) {
            throw new IOException("Total files too big.");
         }
      }

   }

   private void checkSize(int var1) throws IOException {
      if (this.tracker != null) {
         this.checkTracker(var1);
         this.tracker.addBytes(var1);
         this.writtenBytes += var1;
      }

   }

   private void setHeaderPos(int var1) {
      this.headerPos = var1;
   }

   public void writeHeader(int var1, short var2) throws IOException {
      int var3 = 12 + 16 * var2;
      this.checkSize(var3);
      this.header = new byte[var3];
      short var4 = (short)(var2 | var2 >> 1);
      var4 = (short)(var4 | var4 >> 2);
      var4 = (short)(var4 | var4 >> 4);
      var4 = (short)(var4 | var4 >> 8);
      var4 = (short)(var4 & ~(var4 >> 1));
      short var5 = (short)(var4 * 16);

      short var6;
      for(var6 = 0; var4 > 1; var4 = (short)(var4 >> 1)) {
         ++var6;
      }

      short var7 = (short)(var2 * 16 - var5);
      this.setHeaderPos(0);
      this.writeInt(var1);
      this.writeShort(var2);
      this.writeShort(var5);
      this.writeShort(var6);
      this.writeShort(var7);
   }

   public void writeDirectoryEntry(int var1, int var2, int var3, int var4, int var5) throws IOException {
      this.setHeaderPos(12 + 16 * var1);
      this.writeInt(var2);
      this.writeInt(var3);
      this.writeInt(var4);
      this.writeInt(var5);
   }

   private void writeInt(int var1) throws IOException {
      this.header[this.headerPos++] = (byte)((var1 & -16777216) >> 24);
      this.header[this.headerPos++] = (byte)((var1 & 16711680) >> 16);
      this.header[this.headerPos++] = (byte)((var1 & '\uff00') >> 8);
      this.header[this.headerPos++] = (byte)(var1 & 255);
   }

   private void writeShort(short var1) throws IOException {
      this.header[this.headerPos++] = (byte)((var1 & '\uff00') >> 8);
      this.header[this.headerPos++] = (byte)(var1 & 255);
   }

   public void writeBytes(byte[] var1) throws IOException {
      this.writeBytes(var1, 0, var1.length);
   }

   public void writeBytes(byte[] var1, int var2, int var3) throws IOException {
      this.checkSize(var3);
      this.raFile.write(var1, var2, var3);
      this.pos += var3;
   }

   static boolean hasTempPermission() {
      if (System.getSecurityManager() == null) {
         return true;
      } else {
         File var0 = null;
         boolean var1 = false;

         try {
            var0 = Files.createTempFile("+JXF", ".tmp").toFile();
            var0.delete();
            var0 = null;
            var1 = true;
         } catch (Throwable var3) {
         }

         return var1;
      }
   }

   static class FontTracker {
      public static final int MAX_FILE_SIZE = 33554432;
      public static final int MAX_TOTAL_BYTES = 335544320;
      static int numBytes;
      static FontTracker tracker;
      private static Semaphore cs = null;

      public static synchronized FontTracker getTracker() {
         if (tracker == null) {
            tracker = new FontTracker();
         }

         return tracker;
      }

      public synchronized int getNumBytes() {
         return numBytes;
      }

      public synchronized void addBytes(int var1) {
         numBytes += var1;
      }

      public synchronized void subBytes(int var1) {
         numBytes -= var1;
      }

      private static synchronized Semaphore getCS() {
         if (cs == null) {
            cs = new Semaphore(5, true);
         }

         return cs;
      }

      public boolean acquirePermit() throws InterruptedException {
         return getCS().tryAcquire(120L, TimeUnit.SECONDS);
      }

      public void releasePermit() {
         getCS().release();
      }

      public void add(File var1) {
         FontFileWriter.FontTracker.TempFileDeletionHook.add(var1);
      }

      public void set(File var1, RandomAccessFile var2) {
         FontFileWriter.FontTracker.TempFileDeletionHook.set(var1, var2);
      }

      public void remove(File var1) {
         FontFileWriter.FontTracker.TempFileDeletionHook.remove(var1);
      }

      private static class TempFileDeletionHook {
         private static HashMap files = new HashMap();
         private static Thread t = null;

         static void init() {
            if (t == null) {
               AccessController.doPrivileged(() -> {
                  t = new Thread(() -> {
                     runHooks();
                  });
                  Runtime.getRuntime().addShutdownHook(t);
                  return null;
               });
            }

         }

         static synchronized void add(File var0) {
            init();
            files.put(var0, (Object)null);
         }

         static synchronized void set(File var0, RandomAccessFile var1) {
            files.put(var0, var1);
         }

         static synchronized void remove(File var0) {
            files.remove(var0);
         }

         static synchronized void runHooks() {
            if (!files.isEmpty()) {
               Map.Entry var1;
               for(Iterator var0 = files.entrySet().iterator(); var0.hasNext(); ((File)var1.getKey()).delete()) {
                  var1 = (Map.Entry)var0.next();

                  try {
                     if (var1.getValue() != null) {
                        ((RandomAccessFile)var1.getValue()).close();
                     }
                  } catch (Exception var3) {
                  }
               }

            }
         }
      }
   }
}
