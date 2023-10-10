package org.apache.commons.compress.archivers.jar;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class JarArchiveInputStream extends ZipArchiveInputStream {
   public JarArchiveInputStream(InputStream var1) {
      super(var1);
   }

   public JarArchiveEntry getNextJarEntry() throws IOException {
      ZipArchiveEntry var1 = this.getNextZipEntry();
      return var1 == null ? null : new JarArchiveEntry(var1);
   }

   public ArchiveEntry getNextEntry() throws IOException {
      return this.getNextJarEntry();
   }

   public static boolean matches(byte[] var0, int var1) {
      return ZipArchiveInputStream.matches(var0, var1);
   }
}
