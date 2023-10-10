package org.apache.commons.io;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.output.NullOutputStream;

public class FileUtils {
   public static final long ONE_KB = 1024L;
   public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
   public static final long ONE_MB = 1048576L;
   public static final BigInteger ONE_MB_BI;
   private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
   public static final long ONE_GB = 1073741824L;
   public static final BigInteger ONE_GB_BI;
   public static final long ONE_TB = 1099511627776L;
   public static final BigInteger ONE_TB_BI;
   public static final long ONE_PB = 1125899906842624L;
   public static final BigInteger ONE_PB_BI;
   public static final long ONE_EB = 1152921504606846976L;
   public static final BigInteger ONE_EB_BI;
   public static final BigInteger ONE_ZB;
   public static final BigInteger ONE_YB;
   public static final File[] EMPTY_FILE_ARRAY;
   private static final Charset UTF8;

   public static File getFile(File var0, String... var1) {
      if (var0 == null) {
         throw new NullPointerException("directorydirectory must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("names must not be null");
      } else {
         File var2 = var0;
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            var2 = new File(var2, var6);
         }

         return var2;
      }
   }

   public static File getFile(String... var0) {
      if (var0 == null) {
         throw new NullPointerException("names must not be null");
      } else {
         File var1 = null;
         String[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (var1 == null) {
               var1 = new File(var5);
            } else {
               var1 = new File(var1, var5);
            }
         }

         return var1;
      }
   }

   public static String getTempDirectoryPath() {
      return System.getProperty("java.io.tmpdir");
   }

   public static File getTempDirectory() {
      return new File(getTempDirectoryPath());
   }

   public static String getUserDirectoryPath() {
      return System.getProperty("user.home");
   }

   public static File getUserDirectory() {
      return new File(getUserDirectoryPath());
   }

   public static FileInputStream openInputStream(File var0) throws IOException {
      if (var0.exists()) {
         if (var0.isDirectory()) {
            throw new IOException("File '" + var0 + "' exists but is a directory");
         } else if (!var0.canRead()) {
            throw new IOException("File '" + var0 + "' cannot be read");
         } else {
            return new FileInputStream(var0);
         }
      } else {
         throw new FileNotFoundException("File '" + var0 + "' does not exist");
      }
   }

   public static FileOutputStream openOutputStream(File var0) throws IOException {
      return openOutputStream(var0, false);
   }

   public static FileOutputStream openOutputStream(File var0, boolean var1) throws IOException {
      if (var0.exists()) {
         if (var0.isDirectory()) {
            throw new IOException("File '" + var0 + "' exists but is a directory");
         }

         if (!var0.canWrite()) {
            throw new IOException("File '" + var0 + "' cannot be written to");
         }
      } else {
         File var2 = var0.getParentFile();
         if (var2 != null && !var2.mkdirs() && !var2.isDirectory()) {
            throw new IOException("Directory '" + var2 + "' could not be created");
         }
      }

      return new FileOutputStream(var0, var1);
   }

   public static String byteCountToDisplaySize(BigInteger var0) {
      String var1;
      if (var0.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_EB_BI) + " EB";
      } else if (var0.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_PB_BI) + " PB";
      } else if (var0.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_TB_BI) + " TB";
      } else if (var0.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_GB_BI) + " GB";
      } else if (var0.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_MB_BI) + " MB";
      } else if (var0.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
         var1 = var0.divide(ONE_KB_BI) + " KB";
      } else {
         var1 = var0 + " bytes";
      }

      return var1;
   }

   public static String byteCountToDisplaySize(long var0) {
      return byteCountToDisplaySize(BigInteger.valueOf(var0));
   }

   public static void touch(File var0) throws IOException {
      if (!var0.exists()) {
         FileOutputStream var1 = openOutputStream(var0);
         IOUtils.closeQuietly((OutputStream)var1);
      }

      boolean var2 = var0.setLastModified(System.currentTimeMillis());
      if (!var2) {
         throw new IOException("Unable to set the last modification time for " + var0);
      }
   }

   public static File[] convertFileCollectionToFileArray(Collection var0) {
      return (File[])var0.toArray(new File[var0.size()]);
   }

   private static void innerListFiles(Collection var0, File var1, IOFileFilter var2, boolean var3) {
      File[] var4 = var1.listFiles(var2);
      if (var4 != null) {
         File[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File var8 = var5[var7];
            if (var8.isDirectory()) {
               if (var3) {
                  var0.add(var8);
               }

               innerListFiles(var0, var8, var2, var3);
            } else {
               var0.add(var8);
            }
         }
      }

   }

   public static Collection listFiles(File var0, IOFileFilter var1, IOFileFilter var2) {
      validateListFilesParameters(var0, var1);
      IOFileFilter var3 = setUpEffectiveFileFilter(var1);
      IOFileFilter var4 = setUpEffectiveDirFilter(var2);
      LinkedList var5 = new LinkedList();
      innerListFiles(var5, var0, FileFilterUtils.or(var3, var4), false);
      return var5;
   }

   private static void validateListFilesParameters(File var0, IOFileFilter var1) {
      if (!var0.isDirectory()) {
         throw new IllegalArgumentException("Parameter 'directory' is not a directory");
      } else if (var1 == null) {
         throw new NullPointerException("Parameter 'fileFilter' is null");
      }
   }

   private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter var0) {
      return FileFilterUtils.and(var0, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
   }

   private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter var0) {
      return var0 == null ? FalseFileFilter.INSTANCE : FileFilterUtils.and(var0, DirectoryFileFilter.INSTANCE);
   }

   public static Collection listFilesAndDirs(File var0, IOFileFilter var1, IOFileFilter var2) {
      validateListFilesParameters(var0, var1);
      IOFileFilter var3 = setUpEffectiveFileFilter(var1);
      IOFileFilter var4 = setUpEffectiveDirFilter(var2);
      LinkedList var5 = new LinkedList();
      if (var0.isDirectory()) {
         var5.add(var0);
      }

      innerListFiles(var5, var0, FileFilterUtils.or(var3, var4), true);
      return var5;
   }

   public static Iterator iterateFiles(File var0, IOFileFilter var1, IOFileFilter var2) {
      return listFiles(var0, var1, var2).iterator();
   }

   public static Iterator iterateFilesAndDirs(File var0, IOFileFilter var1, IOFileFilter var2) {
      return listFilesAndDirs(var0, var1, var2).iterator();
   }

   private static String[] toSuffixes(String[] var0) {
      String[] var1 = new String[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = "." + var0[var2];
      }

      return var1;
   }

   public static Collection listFiles(File var0, String[] var1, boolean var2) {
      Object var3;
      if (var1 == null) {
         var3 = TrueFileFilter.INSTANCE;
      } else {
         String[] var4 = toSuffixes(var1);
         var3 = new SuffixFileFilter(var4);
      }

      return listFiles(var0, (IOFileFilter)var3, var2 ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
   }

   public static Iterator iterateFiles(File var0, String[] var1, boolean var2) {
      return listFiles(var0, var1, var2).iterator();
   }

   public static boolean contentEquals(File var0, File var1) throws IOException {
      boolean var2 = var0.exists();
      if (var2 != var1.exists()) {
         return false;
      } else if (!var2) {
         return true;
      } else if (!var0.isDirectory() && !var1.isDirectory()) {
         if (var0.length() != var1.length()) {
            return false;
         } else if (var0.getCanonicalFile().equals(var1.getCanonicalFile())) {
            return true;
         } else {
            FileInputStream var3 = null;
            FileInputStream var4 = null;
            var3 = new FileInputStream(var0);
            var4 = new FileInputStream(var1);
            boolean var5 = IOUtils.contentEquals((InputStream)var3, (InputStream)var4);
            IOUtils.closeQuietly((InputStream)var3);
            IOUtils.closeQuietly((InputStream)var4);
            return var5;
         }
      } else {
         throw new IOException("Can't compare directories, only files");
      }
   }

   public static boolean contentEqualsIgnoreEOL(File var0, File var1, String var2) throws IOException {
      boolean var3 = var0.exists();
      if (var3 != var1.exists()) {
         return false;
      } else if (!var3) {
         return true;
      } else if (!var0.isDirectory() && !var1.isDirectory()) {
         if (var0.getCanonicalFile().equals(var1.getCanonicalFile())) {
            return true;
         } else {
            InputStreamReader var4 = null;
            InputStreamReader var5 = null;
            if (var2 == null) {
               var4 = new InputStreamReader(new FileInputStream(var0));
               var5 = new InputStreamReader(new FileInputStream(var1));
            } else {
               var4 = new InputStreamReader(new FileInputStream(var0), var2);
               var5 = new InputStreamReader(new FileInputStream(var1), var2);
            }

            boolean var6 = IOUtils.contentEqualsIgnoreEOL(var4, var5);
            IOUtils.closeQuietly((Reader)var4);
            IOUtils.closeQuietly((Reader)var5);
            return var6;
         }
      } else {
         throw new IOException("Can't compare directories, only files");
      }
   }

   public static File toFile(URL var0) {
      if (var0 != null && "file".equalsIgnoreCase(var0.getProtocol())) {
         String var1 = var0.getFile().replace('/', File.separatorChar);
         var1 = decodeUrl(var1);
         return new File(var1);
      } else {
         return null;
      }
   }

   static String decodeUrl(String var0) {
      String var1 = var0;
      if (var0 != null && var0.indexOf(37) >= 0) {
         int var2 = var0.length();
         StringBuffer var3 = new StringBuffer();
         ByteBuffer var4 = ByteBuffer.allocate(var2);
         int var5 = 0;

         while(true) {
            label40:
            while(true) {
               if (var5 >= var2) {
                  var1 = var3.toString();
                  return var1;
               }

               if (var0.charAt(var5) == '%') {
                  try {
                     while(true) {
                        byte var6 = (byte)Integer.parseInt(var0.substring(var5 + 1, var5 + 3), 16);
                        var4.put(var6);
                        var5 += 3;
                        if (var5 >= var2 || var0.charAt(var5) != '%') {
                           break label40;
                        }
                     }
                  } catch (RuntimeException var8) {
                     if (var4.position() > 0) {
                        var4.flip();
                        var3.append(UTF8.decode(var4).toString());
                        var4.clear();
                     }
                  }
               }

               var3.append(var0.charAt(var5++));
            }

            if (var4.position() > 0) {
               var4.flip();
               var3.append(UTF8.decode(var4).toString());
               var4.clear();
            }
         }
      } else {
         return var1;
      }
   }

   public static File[] toFiles(URL[] var0) {
      if (var0 != null && var0.length != 0) {
         File[] var1 = new File[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            URL var3 = var0[var2];
            if (var3 != null) {
               if (!var3.getProtocol().equals("file")) {
                  throw new IllegalArgumentException("URL could not be converted to a File: " + var3);
               }

               var1[var2] = toFile(var3);
            }
         }

         return var1;
      } else {
         return EMPTY_FILE_ARRAY;
      }
   }

   public static URL[] toURLs(File[] var0) throws IOException {
      URL[] var1 = new URL[var0.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var0[var2].toURI().toURL();
      }

      return var1;
   }

   public static void copyFileToDirectory(File var0, File var1) throws IOException {
      copyFileToDirectory(var0, var1, true);
   }

   public static void copyFileToDirectory(File var0, File var1, boolean var2) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (var1.exists() && !var1.isDirectory()) {
         throw new IllegalArgumentException("Destination '" + var1 + "' is not a directory");
      } else {
         File var3 = new File(var1, var0.getName());
         copyFile(var0, var3, var2);
      }
   }

   public static void copyFile(File var0, File var1) throws IOException {
      copyFile(var0, var1, true);
   }

   public static void copyFile(File var0, File var1, boolean var2) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + var0 + "' does not exist");
      } else if (var0.isDirectory()) {
         throw new IOException("Source '" + var0 + "' exists but is a directory");
      } else if (var0.getCanonicalPath().equals(var1.getCanonicalPath())) {
         throw new IOException("Source '" + var0 + "' and destination '" + var1 + "' are the same");
      } else {
         File var3 = var1.getParentFile();
         if (var3 != null && !var3.mkdirs() && !var3.isDirectory()) {
            throw new IOException("Destination '" + var3 + "' directory cannot be created");
         } else if (var1.exists() && !var1.canWrite()) {
            throw new IOException("Destination '" + var1 + "' exists but is read-only");
         } else {
            doCopyFile(var0, var1, var2);
         }
      }
   }

   public static long copyFile(File var0, OutputStream var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var0);
      long var3 = IOUtils.copyLarge((InputStream)var2, (OutputStream)var1);
      var2.close();
      return var3;
   }

   private static void doCopyFile(File var0, File var1, boolean var2) throws IOException {
      if (var1.exists() && var1.isDirectory()) {
         throw new IOException("Destination '" + var1 + "' exists but is a directory");
      } else {
         FileInputStream var3 = null;
         FileOutputStream var4 = null;
         FileChannel var5 = null;
         FileChannel var6 = null;
         var3 = new FileInputStream(var0);
         var4 = new FileOutputStream(var1);
         var5 = var3.getChannel();
         var6 = var4.getChannel();
         long var7 = var5.size();
         long var9 = 0L;

         for(long var11 = 0L; var9 < var7; var9 += var6.transferFrom(var5, var9, var11)) {
            var11 = var7 - var9 > 31457280L ? 31457280L : var7 - var9;
         }

         IOUtils.closeQuietly((Closeable)var6);
         IOUtils.closeQuietly((OutputStream)var4);
         IOUtils.closeQuietly((Closeable)var5);
         IOUtils.closeQuietly((InputStream)var3);
         if (var0.length() != var1.length()) {
            throw new IOException("Failed to copy full contents from '" + var0 + "' to '" + var1 + "'");
         } else {
            if (var2) {
               var1.setLastModified(var0.lastModified());
            }

         }
      }
   }

   public static void copyDirectoryToDirectory(File var0, File var1) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var0.exists() && !var0.isDirectory()) {
         throw new IllegalArgumentException("Source '" + var1 + "' is not a directory");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (var1.exists() && !var1.isDirectory()) {
         throw new IllegalArgumentException("Destination '" + var1 + "' is not a directory");
      } else {
         copyDirectory(var0, new File(var1, var0.getName()), true);
      }
   }

   public static void copyDirectory(File var0, File var1) throws IOException {
      copyDirectory(var0, var1, true);
   }

   public static void copyDirectory(File var0, File var1, boolean var2) throws IOException {
      copyDirectory(var0, var1, (FileFilter)null, var2);
   }

   public static void copyDirectory(File var0, File var1, FileFilter var2) throws IOException {
      copyDirectory(var0, var1, var2, true);
   }

   public static void copyDirectory(File var0, File var1, FileFilter var2, boolean var3) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + var0 + "' does not exist");
      } else if (!var0.isDirectory()) {
         throw new IOException("Source '" + var0 + "' exists but is not a directory");
      } else if (var0.getCanonicalPath().equals(var1.getCanonicalPath())) {
         throw new IOException("Source '" + var0 + "' and destination '" + var1 + "' are the same");
      } else {
         ArrayList var4 = null;
         if (var1.getCanonicalPath().startsWith(var0.getCanonicalPath())) {
            File[] var5 = var2 == null ? var0.listFiles() : var0.listFiles(var2);
            if (var5 != null && var5.length > 0) {
               var4 = new ArrayList(var5.length);
               File[] var6 = var5;
               int var7 = var5.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  File var9 = var6[var8];
                  File var10 = new File(var1, var9.getName());
                  var4.add(var10.getCanonicalPath());
               }
            }
         }

         doCopyDirectory(var0, var1, var2, var3, var4);
      }
   }

   private static void doCopyDirectory(File var0, File var1, FileFilter var2, boolean var3, List var4) throws IOException {
      File[] var5 = var2 == null ? var0.listFiles() : var0.listFiles(var2);
      if (var5 == null) {
         throw new IOException("Failed to list contents of " + var0);
      } else {
         if (var1.exists()) {
            if (!var1.isDirectory()) {
               throw new IOException("Destination '" + var1 + "' exists but is not a directory");
            }
         } else if (!var1.mkdirs() && !var1.isDirectory()) {
            throw new IOException("Destination '" + var1 + "' directory cannot be created");
         }

         if (!var1.canWrite()) {
            throw new IOException("Destination '" + var1 + "' cannot be written to");
         } else {
            File[] var6 = var5;
            int var7 = var5.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               File var9 = var6[var8];
               File var10 = new File(var1, var9.getName());
               if (var4 == null || !var4.contains(var9.getCanonicalPath())) {
                  if (var9.isDirectory()) {
                     doCopyDirectory(var9, var10, var2, var3, var4);
                  } else {
                     doCopyFile(var9, var10, var3);
                  }
               }
            }

            if (var3) {
               var1.setLastModified(var0.lastModified());
            }

         }
      }
   }

   public static void copyURLToFile(URL var0, File var1) throws IOException {
      InputStream var2 = var0.openStream();
      copyInputStreamToFile(var2, var1);
   }

   public static void copyURLToFile(URL var0, File var1, int var2, int var3) throws IOException {
      URLConnection var4 = var0.openConnection();
      var4.setConnectTimeout(var2);
      var4.setReadTimeout(var3);
      InputStream var5 = var4.getInputStream();
      copyInputStreamToFile(var5, var1);
   }

   public static void copyInputStreamToFile(InputStream var0, File var1) throws IOException {
      FileOutputStream var2 = openOutputStream(var1);
      IOUtils.copy((InputStream)var0, (OutputStream)var2);
      var2.close();
      IOUtils.closeQuietly((OutputStream)var2);
      IOUtils.closeQuietly(var0);
   }

   public static void deleteDirectory(File var0) throws IOException {
      if (var0.exists()) {
         if (var0 == null) {
            cleanDirectory(var0);
         }

         if (!var0.delete()) {
            String var1 = "Unable to delete directory " + var0 + ".";
            throw new IOException(var1);
         }
      }
   }

   public static boolean deleteQuietly(File var0) {
      if (var0 == null) {
         return false;
      } else {
         try {
            if (var0.isDirectory()) {
               cleanDirectory(var0);
            }
         } catch (Exception var3) {
         }

         try {
            return var0.delete();
         } catch (Exception var2) {
            return false;
         }
      }
   }

   public static boolean directoryContains(File var0, File var1) throws IOException {
      if (var0 == null) {
         throw new IllegalArgumentException("Directory must not be null");
      } else if (!var0.isDirectory()) {
         throw new IllegalArgumentException("Not a directory: " + var0);
      } else if (var1 == null) {
         return false;
      } else if (var0.exists() && var1.exists()) {
         String var2 = var0.getCanonicalPath();
         String var3 = var1.getCanonicalPath();
         return FilenameUtils.directoryContains(var2, var3);
      } else {
         return false;
      }
   }

   public static void cleanDirectory(File var0) throws IOException {
      String var9;
      if (!var0.exists()) {
         var9 = var0 + " does not exist";
         throw new IllegalArgumentException(var9);
      } else if (!var0.isDirectory()) {
         var9 = var0 + " is not a directory";
         throw new IllegalArgumentException(var9);
      } else {
         File[] var1 = var0.listFiles();
         if (var1 == null) {
            throw new IOException("Failed to list contents of " + var0);
         } else {
            IOException var2 = null;
            File[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               File var6 = var3[var5];

               try {
                  forceDelete(var6);
               } catch (IOException var8) {
                  var2 = var8;
               }
            }

            if (null != var2) {
               throw var2;
            }
         }
      }
   }

   public static boolean waitFor(File var0, int var1) {
      int var2 = 0;
      int var3 = 0;

      while(!var0.exists()) {
         if (var3++ >= 10) {
            var3 = 0;
            if (var2++ > var1) {
               return false;
            }
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var5) {
         } catch (Exception var6) {
            break;
         }
      }

      return true;
   }

   public static String readFileToString(File var0, Charset var1) throws IOException {
      FileInputStream var2 = null;
      var2 = openInputStream(var0);
      String var3 = IOUtils.toString((InputStream)var2, (Charset)Charsets.toCharset(var1));
      IOUtils.closeQuietly((InputStream)var2);
      return var3;
   }

   public static String readFileToString(File var0, String var1) throws IOException {
      return readFileToString(var0, Charsets.toCharset(var1));
   }

   public static String readFileToString(File var0) throws IOException {
      return readFileToString(var0, Charset.defaultCharset());
   }

   public static byte[] readFileToByteArray(File var0) throws IOException {
      FileInputStream var1 = null;
      var1 = openInputStream(var0);
      byte[] var2 = IOUtils.toByteArray(var1, var0.length());
      IOUtils.closeQuietly((InputStream)var1);
      return var2;
   }

   public static List readLines(File var0, Charset var1) throws IOException {
      FileInputStream var2 = null;
      var2 = openInputStream(var0);
      List var3 = IOUtils.readLines(var2, (Charset)Charsets.toCharset(var1));
      IOUtils.closeQuietly((InputStream)var2);
      return var3;
   }

   public static List readLines(File var0, String var1) throws IOException {
      return readLines(var0, Charsets.toCharset(var1));
   }

   public static List readLines(File var0) throws IOException {
      return readLines(var0, Charset.defaultCharset());
   }

   public static LineIterator lineIterator(File var0, String var1) throws IOException {
      FileInputStream var2 = null;

      try {
         var2 = openInputStream(var0);
         return IOUtils.lineIterator(var2, (String)var1);
      } catch (IOException var4) {
         IOUtils.closeQuietly((InputStream)var2);
         throw var4;
      } catch (RuntimeException var5) {
         IOUtils.closeQuietly((InputStream)var2);
         throw var5;
      }
   }

   public static LineIterator lineIterator(File var0) throws IOException {
      return lineIterator(var0, (String)null);
   }

   public static void writeStringToFile(File var0, String var1, Charset var2) throws IOException {
      writeStringToFile(var0, var1, var2, false);
   }

   public static void writeStringToFile(File var0, String var1, String var2) throws IOException {
      writeStringToFile(var0, var1, var2, false);
   }

   public static void writeStringToFile(File var0, String var1, Charset var2, boolean var3) throws IOException {
      FileOutputStream var4 = null;
      var4 = openOutputStream(var0, var3);
      IOUtils.write((String)var1, (OutputStream)var4, (Charset)var2);
      var4.close();
      IOUtils.closeQuietly((OutputStream)var4);
   }

   public static void writeStringToFile(File var0, String var1, String var2, boolean var3) throws IOException {
      writeStringToFile(var0, var1, Charsets.toCharset(var2), var3);
   }

   public static void writeStringToFile(File var0, String var1) throws IOException {
      writeStringToFile(var0, var1, Charset.defaultCharset(), false);
   }

   public static void writeStringToFile(File var0, String var1, boolean var2) throws IOException {
      writeStringToFile(var0, var1, Charset.defaultCharset(), var2);
   }

   public static void write(File var0, CharSequence var1) throws IOException {
      write(var0, var1, Charset.defaultCharset(), false);
   }

   public static void write(File var0, CharSequence var1, boolean var2) throws IOException {
      write(var0, var1, Charset.defaultCharset(), var2);
   }

   public static void write(File var0, CharSequence var1, Charset var2) throws IOException {
      write(var0, var1, var2, false);
   }

   public static void write(File var0, CharSequence var1, String var2) throws IOException {
      write(var0, var1, var2, false);
   }

   public static void write(File var0, CharSequence var1, Charset var2, boolean var3) throws IOException {
      String var4 = var1 == null ? null : var1.toString();
      writeStringToFile(var0, var4, var2, var3);
   }

   public static void write(File var0, CharSequence var1, String var2, boolean var3) throws IOException {
      write(var0, var1, Charsets.toCharset(var2), var3);
   }

   public static void writeByteArrayToFile(File var0, byte[] var1) throws IOException {
      writeByteArrayToFile(var0, var1, false);
   }

   public static void writeByteArrayToFile(File var0, byte[] var1, boolean var2) throws IOException {
      FileOutputStream var3 = null;
      var3 = openOutputStream(var0, var2);
      var3.write(var1);
      var3.close();
      IOUtils.closeQuietly((OutputStream)var3);
   }

   public static void writeLines(File var0, String var1, Collection var2) throws IOException {
      writeLines(var0, var1, var2, (String)null, false);
   }

   public static void writeLines(File var0, String var1, Collection var2, boolean var3) throws IOException {
      writeLines(var0, var1, var2, (String)null, var3);
   }

   public static void writeLines(File var0, Collection var1) throws IOException {
      writeLines(var0, (String)null, var1, (String)null, false);
   }

   public static void writeLines(File var0, Collection var1, boolean var2) throws IOException {
      writeLines(var0, (String)null, var1, (String)null, var2);
   }

   public static void writeLines(File var0, String var1, Collection var2, String var3) throws IOException {
      writeLines(var0, var1, var2, var3, false);
   }

   public static void writeLines(File var0, String var1, Collection var2, String var3, boolean var4) throws IOException {
      FileOutputStream var5 = null;
      var5 = openOutputStream(var0, var4);
      BufferedOutputStream var6 = new BufferedOutputStream(var5);
      IOUtils.writeLines(var2, var3, var6, (String)var1);
      var6.flush();
      var5.close();
      IOUtils.closeQuietly((OutputStream)var5);
   }

   public static void writeLines(File var0, Collection var1, String var2) throws IOException {
      writeLines(var0, (String)null, var1, var2, false);
   }

   public static void writeLines(File var0, Collection var1, String var2, boolean var3) throws IOException {
      writeLines(var0, (String)null, var1, var2, var3);
   }

   public static void forceDelete(File var0) throws IOException {
      if (var0.isDirectory()) {
         deleteDirectory(var0);
      } else {
         boolean var1 = var0.exists();
         if (!var0.delete()) {
            if (!var1) {
               throw new FileNotFoundException("File does not exist: " + var0);
            }

            String var2 = "Unable to delete file: " + var0;
            throw new IOException(var2);
         }
      }

   }

   public static void forceDeleteOnExit(File var0) throws IOException {
      if (var0.isDirectory()) {
         deleteDirectoryOnExit(var0);
      } else {
         var0.deleteOnExit();
      }

   }

   private static void deleteDirectoryOnExit(File var0) throws IOException {
      if (var0.exists()) {
         var0.deleteOnExit();
         if (var0 == null) {
            cleanDirectoryOnExit(var0);
         }

      }
   }

   private static void cleanDirectoryOnExit(File var0) throws IOException {
      String var9;
      if (!var0.exists()) {
         var9 = var0 + " does not exist";
         throw new IllegalArgumentException(var9);
      } else if (!var0.isDirectory()) {
         var9 = var0 + " is not a directory";
         throw new IllegalArgumentException(var9);
      } else {
         File[] var1 = var0.listFiles();
         if (var1 == null) {
            throw new IOException("Failed to list contents of " + var0);
         } else {
            IOException var2 = null;
            File[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               File var6 = var3[var5];

               try {
                  forceDeleteOnExit(var6);
               } catch (IOException var8) {
                  var2 = var8;
               }
            }

            if (null != var2) {
               throw var2;
            }
         }
      }
   }

   public static void forceMkdir(File var0) throws IOException {
      String var1;
      if (var0.exists()) {
         if (!var0.isDirectory()) {
            var1 = "File " + var0 + " exists and is " + "not a directory. Unable to create directory.";
            throw new IOException(var1);
         }
      } else if (!var0.mkdirs() && !var0.isDirectory()) {
         var1 = "Unable to create directory " + var0;
         throw new IOException(var1);
      }

   }

   public static long sizeOf(File var0) {
      if (!var0.exists()) {
         String var1 = var0 + " does not exist";
         throw new IllegalArgumentException(var1);
      } else {
         return var0.isDirectory() ? sizeOfDirectory(var0) : var0.length();
      }
   }

   public static BigInteger sizeOfAsBigInteger(File var0) {
      if (!var0.exists()) {
         String var1 = var0 + " does not exist";
         throw new IllegalArgumentException(var1);
      } else {
         return var0.isDirectory() ? sizeOfDirectoryAsBigInteger(var0) : BigInteger.valueOf(var0.length());
      }
   }

   public static long sizeOfDirectory(File var0) {
      checkDirectory(var0);
      File[] var1 = var0.listFiles();
      if (var1 == null) {
         return 0L;
      } else {
         long var2 = 0L;
         File[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];

            try {
               if (var7 == null) {
                  var2 += sizeOf(var7);
                  if (var2 < 0L) {
                     break;
                  }
               }
            } catch (IOException var9) {
            }
         }

         return var2;
      }
   }

   public static BigInteger sizeOfDirectoryAsBigInteger(File var0) {
      checkDirectory(var0);
      File[] var1 = var0.listFiles();
      if (var1 == null) {
         return BigInteger.ZERO;
      } else {
         BigInteger var2 = BigInteger.ZERO;
         File[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];

            try {
               if (var6 == null) {
                  var2 = var2.add(BigInteger.valueOf(sizeOf(var6)));
               }
            } catch (IOException var8) {
            }
         }

         return var2;
      }
   }

   private static void checkDirectory(File var0) {
      if (!var0.exists()) {
         throw new IllegalArgumentException(var0 + " does not exist");
      } else if (!var0.isDirectory()) {
         throw new IllegalArgumentException(var0 + " is not a directory");
      }
   }

   public static boolean isFileNewer(File var0, File var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No specified reference file");
      } else if (!var1.exists()) {
         throw new IllegalArgumentException("The reference file '" + var1 + "' doesn't exist");
      } else {
         return isFileNewer(var0, var1.lastModified());
      }
   }

   public static boolean isFileNewer(File var0, Date var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No specified date");
      } else {
         return isFileNewer(var0, var1.getTime());
      }
   }

   public static boolean isFileNewer(File var0, long var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("No specified file");
      } else if (!var0.exists()) {
         return false;
      } else {
         return var0.lastModified() > var1;
      }
   }

   public static boolean isFileOlder(File var0, File var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No specified reference file");
      } else if (!var1.exists()) {
         throw new IllegalArgumentException("The reference file '" + var1 + "' doesn't exist");
      } else {
         return isFileOlder(var0, var1.lastModified());
      }
   }

   public static boolean isFileOlder(File var0, Date var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No specified date");
      } else {
         return isFileOlder(var0, var1.getTime());
      }
   }

   public static boolean isFileOlder(File var0, long var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("No specified file");
      } else if (!var0.exists()) {
         return false;
      } else {
         return var0.lastModified() < var1;
      }
   }

   public static long checksumCRC32(File var0) throws IOException {
      CRC32 var1 = new CRC32();
      checksum(var0, var1);
      return var1.getValue();
   }

   public static Checksum checksum(File var0, Checksum var1) throws IOException {
      if (var0.isDirectory()) {
         throw new IllegalArgumentException("Checksums can't be computed on directories");
      } else {
         CheckedInputStream var2 = null;
         var2 = new CheckedInputStream(new FileInputStream(var0), var1);
         IOUtils.copy((InputStream)var2, (OutputStream)(new NullOutputStream()));
         IOUtils.closeQuietly((InputStream)var2);
         return var1;
      }
   }

   public static void moveDirectory(File var0, File var1) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + var0 + "' does not exist");
      } else if (!var0.isDirectory()) {
         throw new IOException("Source '" + var0 + "' is not a directory");
      } else if (var1.exists()) {
         throw new FileExistsException("Destination '" + var1 + "' already exists");
      } else {
         boolean var2 = var0.renameTo(var1);
         if (!var2) {
            if (var1.getCanonicalPath().startsWith(var0.getCanonicalPath())) {
               throw new IOException("Cannot move directory: " + var0 + " to a subdirectory of itself: " + var1);
            }

            copyDirectory(var0, var1);
            deleteDirectory(var0);
            if (var0.exists()) {
               throw new IOException("Failed to delete original directory '" + var0 + "' after copy to '" + var1 + "'");
            }
         }

      }
   }

   public static void moveDirectoryToDirectory(File var0, File var1, boolean var2) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination directory must not be null");
      } else {
         if (!var1.exists() && var2) {
            var1.mkdirs();
         }

         if (!var1.exists()) {
            throw new FileNotFoundException("Destination directory '" + var1 + "' does not exist [createDestDir=" + var2 + "]");
         } else if (!var1.isDirectory()) {
            throw new IOException("Destination '" + var1 + "' is not a directory");
         } else {
            moveDirectory(var0, new File(var1, var0.getName()));
         }
      }
   }

   public static void moveFile(File var0, File var1) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + var0 + "' does not exist");
      } else if (var0.isDirectory()) {
         throw new IOException("Source '" + var0 + "' is a directory");
      } else if (var1.exists()) {
         throw new FileExistsException("Destination '" + var1 + "' already exists");
      } else if (var1.isDirectory()) {
         throw new IOException("Destination '" + var1 + "' is a directory");
      } else {
         boolean var2 = var0.renameTo(var1);
         if (!var2) {
            copyFile(var0, var1);
            if (!var0.delete()) {
               deleteQuietly(var1);
               throw new IOException("Failed to delete original file '" + var0 + "' after copy to '" + var1 + "'");
            }
         }

      }
   }

   public static void moveFileToDirectory(File var0, File var1, boolean var2) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination directory must not be null");
      } else {
         if (!var1.exists() && var2) {
            var1.mkdirs();
         }

         if (!var1.exists()) {
            throw new FileNotFoundException("Destination directory '" + var1 + "' does not exist [createDestDir=" + var2 + "]");
         } else if (!var1.isDirectory()) {
            throw new IOException("Destination '" + var1 + "' is not a directory");
         } else {
            moveFile(var0, new File(var1, var0.getName()));
         }
      }
   }

   public static void moveToDirectory(File var0, File var1, boolean var2) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Source must not be null");
      } else if (var1 == null) {
         throw new NullPointerException("Destination must not be null");
      } else if (!var0.exists()) {
         throw new FileNotFoundException("Source '" + var0 + "' does not exist");
      } else {
         if (var0.isDirectory()) {
            moveDirectoryToDirectory(var0, var1, var2);
         } else {
            moveFileToDirectory(var0, var1, var2);
         }

      }
   }

   static {
      ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
      ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
      ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
      ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
      ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
      ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
      ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
      EMPTY_FILE_ARRAY = new File[0];
      UTF8 = Charset.forName("UTF-8");
   }
}
