package javafx.scene.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

final class DirectoryLock {
   private static final Logger logger = Logger.getLogger(DirectoryLock.class.getName());
   private static final Map descriptors = new HashMap();
   private Descriptor descriptor;

   DirectoryLock(File var1) throws IOException, DirectoryAlreadyInUseException {
      var1 = canonicalize(var1);
      this.descriptor = (Descriptor)descriptors.get(var1);
      if (this.descriptor == null) {
         File var2 = lockFile(var1);
         RandomAccessFile var3 = new RandomAccessFile(var2, "rw");

         try {
            FileLock var4 = var3.getChannel().tryLock();
            if (var4 == null) {
               throw new DirectoryAlreadyInUseException(var1.toString(), (Throwable)null);
            }

            this.descriptor = new Descriptor(var1, var3, var4);
            descriptors.put(var1, this.descriptor);
         } catch (OverlappingFileLockException var12) {
            throw new DirectoryAlreadyInUseException(var1.toString(), var12);
         } finally {
            if (this.descriptor == null) {
               try {
                  var3.close();
               } catch (IOException var11) {
                  logger.log(Level.WARNING, String.format("Error closing [%s]", var2), var11);
               }
            }

         }
      }

      this.descriptor.referenceCount++;
   }

   void close() {
      if (this.descriptor != null) {
         this.descriptor.referenceCount--;
         if (this.descriptor.referenceCount == 0) {
            try {
               this.descriptor.lock.release();
            } catch (IOException var3) {
               logger.log(Level.WARNING, String.format("Error releasing lock on [%s]", lockFile(this.descriptor.directory)), var3);
            }

            try {
               this.descriptor.lockRaf.close();
            } catch (IOException var2) {
               logger.log(Level.WARNING, String.format("Error closing [%s]", lockFile(this.descriptor.directory)), var2);
            }

            descriptors.remove(this.descriptor.directory);
         }

         this.descriptor = null;
      }
   }

   static int referenceCount(File var0) throws IOException {
      Descriptor var1 = (Descriptor)descriptors.get(canonicalize(var0));
      return var1 == null ? 0 : var1.referenceCount;
   }

   static File canonicalize(File var0) throws IOException {
      String var1 = var0.getCanonicalPath();
      if (var1.length() > 0 && var1.charAt(var1.length() - 1) != File.separatorChar) {
         var1 = var1 + File.separatorChar;
      }

      return new File(var1);
   }

   private static File lockFile(File var0) {
      return new File(var0, ".lock");
   }

   final class DirectoryAlreadyInUseException extends Exception {
      DirectoryAlreadyInUseException(String var2, Throwable var3) {
         super(var2, var3);
      }
   }

   private static class Descriptor {
      private final File directory;
      private final RandomAccessFile lockRaf;
      private final FileLock lock;
      private int referenceCount;

      private Descriptor(File var1, RandomAccessFile var2, FileLock var3) {
         this.directory = var1;
         this.lockRaf = var2;
         this.lock = var3;
      }

      // $FF: synthetic method
      Descriptor(File var1, RandomAccessFile var2, FileLock var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
