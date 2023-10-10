package org.apache.commons.compress.archivers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactory {
   public static final String AR = "ar";
   public static final String ARJ = "arj";
   public static final String CPIO = "cpio";
   public static final String DUMP = "dump";
   public static final String JAR = "jar";
   public static final String TAR = "tar";
   public static final String ZIP = "zip";
   public static final String SEVEN_Z = "7z";
   private String entryEncoding = null;

   public String getEntryEncoding() {
      return this.entryEncoding;
   }

   public void setEntryEncoding(String var1) {
      this.entryEncoding = var1;
   }

   public ArchiveInputStream createArchiveInputStream(String var1, InputStream var2) throws ArchiveException {
      if (var1 == null) {
         throw new IllegalArgumentException("Archivername must not be null.");
      } else if (var2 == null) {
         throw new IllegalArgumentException("InputStream must not be null.");
      } else if ("ar".equalsIgnoreCase(var1)) {
         return new ArArchiveInputStream(var2);
      } else if ("arj".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new ArjArchiveInputStream(var2, this.entryEncoding) : new ArjArchiveInputStream(var2);
      } else if ("zip".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new ZipArchiveInputStream(var2, this.entryEncoding) : new ZipArchiveInputStream(var2);
      } else if ("tar".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new TarArchiveInputStream(var2, this.entryEncoding) : new TarArchiveInputStream(var2);
      } else if ("jar".equalsIgnoreCase(var1)) {
         return new JarArchiveInputStream(var2);
      } else if ("cpio".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new CpioArchiveInputStream(var2, this.entryEncoding) : new CpioArchiveInputStream(var2);
      } else if ("dump".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new DumpArchiveInputStream(var2, this.entryEncoding) : new DumpArchiveInputStream(var2);
      } else if ("7z".equalsIgnoreCase(var1)) {
         throw new StreamingNotSupportedException("7z");
      } else {
         throw new ArchiveException("Archiver: " + var1 + " not found.");
      }
   }

   public ArchiveOutputStream createArchiveOutputStream(String var1, OutputStream var2) throws ArchiveException {
      if (var1 == null) {
         throw new IllegalArgumentException("Archivername must not be null.");
      } else if (var2 == null) {
         throw new IllegalArgumentException("OutputStream must not be null.");
      } else if ("ar".equalsIgnoreCase(var1)) {
         return new ArArchiveOutputStream(var2);
      } else if ("zip".equalsIgnoreCase(var1)) {
         ZipArchiveOutputStream var3 = new ZipArchiveOutputStream(var2);
         if (this.entryEncoding != null) {
            var3.setEncoding(this.entryEncoding);
         }

         return var3;
      } else if ("tar".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new TarArchiveOutputStream(var2, this.entryEncoding) : new TarArchiveOutputStream(var2);
      } else if ("jar".equalsIgnoreCase(var1)) {
         return new JarArchiveOutputStream(var2);
      } else if ("cpio".equalsIgnoreCase(var1)) {
         return this.entryEncoding != null ? new CpioArchiveOutputStream(var2, this.entryEncoding) : new CpioArchiveOutputStream(var2);
      } else if ("7z".equalsIgnoreCase(var1)) {
         throw new StreamingNotSupportedException("7z");
      } else {
         throw new ArchiveException("Archiver: " + var1 + " not found.");
      }
   }

   public ArchiveInputStream createArchiveInputStream(InputStream var1) throws ArchiveException {
      if (var1 == null) {
         throw new IllegalArgumentException("Stream must not be null.");
      } else if (!var1.markSupported()) {
         throw new IllegalArgumentException("Mark is not supported.");
      } else {
         byte[] var2 = new byte[12];
         var1.mark(var2.length);

         try {
            int var3 = IOUtils.readFully(var1, var2);
            var1.reset();
            if (ZipArchiveInputStream.matches(var2, var3)) {
               if (this.entryEncoding != null) {
                  return new ZipArchiveInputStream(var1, this.entryEncoding);
               }

               return new ZipArchiveInputStream(var1);
            }

            if (JarArchiveInputStream.matches(var2, var3)) {
               return new JarArchiveInputStream(var1);
            }

            if (ArArchiveInputStream.matches(var2, var3)) {
               return new ArArchiveInputStream(var1);
            }

            if (CpioArchiveInputStream.matches(var2, var3)) {
               return new CpioArchiveInputStream(var1);
            }

            if (ArjArchiveInputStream.matches(var2, var3)) {
               return new ArjArchiveInputStream(var1);
            }

            if (SevenZFile.matches(var2, var3)) {
               throw new StreamingNotSupportedException("7z");
            }

            byte[] var4 = new byte[32];
            var1.mark(var4.length);
            var3 = IOUtils.readFully(var1, var4);
            var1.reset();
            if (DumpArchiveInputStream.matches(var4, var3)) {
               return new DumpArchiveInputStream(var1);
            }

            byte[] var5 = new byte[512];
            var1.mark(var5.length);
            var3 = IOUtils.readFully(var1, var5);
            var1.reset();
            if (TarArchiveInputStream.matches(var5, var3)) {
               if (this.entryEncoding != null) {
                  return new TarArchiveInputStream(var1, this.entryEncoding);
               }

               return new TarArchiveInputStream(var1);
            }

            if (var3 >= 512) {
               TarArchiveInputStream var6 = null;

               TarArchiveInputStream var7;
               label119: {
                  try {
                     var6 = new TarArchiveInputStream(new ByteArrayInputStream(var5));
                     if (var6.getNextTarEntry().isCheckSumOK()) {
                        var7 = new TarArchiveInputStream(var1);
                        break label119;
                     }
                  } catch (Exception var9) {
                     IOUtils.closeQuietly(var6);
                     throw new ArchiveException("No Archiver found for the stream signature");
                  }

                  IOUtils.closeQuietly(var6);
                  throw new ArchiveException("No Archiver found for the stream signature");
               }

               IOUtils.closeQuietly(var6);
               return var7;
            }
         } catch (IOException var10) {
            throw new ArchiveException("Could not use reset and mark operations.", var10);
         }

         throw new ArchiveException("No Archiver found for the stream signature");
      }
   }
}
