package com.sun.webkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

final class FileSystem {
   private static final int TYPE_UNKNOWN = 0;
   private static final int TYPE_FILE = 1;
   private static final int TYPE_DIRECTORY = 2;
   private static final Logger logger = Logger.getLogger(FileSystem.class.getName());

   private FileSystem() {
      throw new AssertionError();
   }

   private static boolean fwkFileExists(String var0) {
      return (new File(var0)).exists();
   }

   private static RandomAccessFile fwkOpenFile(String var0, String var1) {
      try {
         return new RandomAccessFile(var0, var1);
      } catch (SecurityException | FileNotFoundException var3) {
         logger.log(Level.FINE, String.format("Error while creating RandomAccessFile for file [%s]", var0), var3);
         return null;
      }
   }

   private static void fwkCloseFile(RandomAccessFile var0) {
      try {
         var0.close();
      } catch (IOException var2) {
         logger.log(Level.FINE, String.format("Error while closing RandomAccessFile for file [%s]", var0), var2);
      }

   }

   private static int fwkReadFromFile(RandomAccessFile var0, ByteBuffer var1) {
      try {
         FileChannel var2 = var0.getChannel();
         return var2.read(var1);
      } catch (IOException var3) {
         logger.log(Level.FINE, String.format("Error while reading RandomAccessFile for file [%s]", var0), var3);
         return -1;
      }
   }

   private static void fwkSeekFile(RandomAccessFile var0, long var1) {
      try {
         var0.seek(var1);
      } catch (IOException var4) {
         logger.log(Level.FINE, String.format("Error while seek RandomAccessFile for file [%s]", var0), var4);
      }

   }

   private static long fwkGetFileSize(String var0) {
      try {
         File var1 = new File(var0);
         if (var1.exists()) {
            return var1.length();
         }
      } catch (SecurityException var2) {
         logger.log(Level.FINE, String.format("Error determining size of file [%s]", var0), var2);
      }

      return -1L;
   }

   private static boolean fwkGetFileMetadata(String var0, long[] var1) {
      try {
         File var2 = new File(var0);
         if (var2.exists()) {
            var1[0] = var2.lastModified();
            var1[1] = var2.length();
            if (var2.isDirectory()) {
               var1[2] = 2L;
            } else if (var2.isFile()) {
               var1[2] = 1L;
            } else {
               var1[2] = 0L;
            }

            return true;
         }
      } catch (SecurityException var3) {
         logger.log(Level.FINE, String.format("Error determining Metadata for file [%s]", var0), var3);
      }

      return false;
   }

   private static String fwkPathByAppendingComponent(String var0, String var1) {
      return (new File(var0, var1)).getPath();
   }

   private static boolean fwkMakeAllDirectories(String var0) {
      try {
         Files.createDirectories(Paths.get(var0));
         return true;
      } catch (IOException | InvalidPathException var2) {
         logger.log(Level.FINE, String.format("Error creating directory [%s]", var0), var2);
         return false;
      }
   }

   private static String fwkPathGetFileName(String var0) {
      return (new File(var0)).getName();
   }
}
