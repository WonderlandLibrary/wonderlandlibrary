package org.apache.commons.compress.archivers.jar;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class JarArchiveInputStream extends ZipArchiveInputStream {
  public JarArchiveInputStream(InputStream inputStream) {
    super(inputStream);
  }
  
  public JarArchiveEntry getNextJarEntry() throws IOException {
    ZipArchiveEntry entry = getNextZipEntry();
    return (entry == null) ? null : new JarArchiveEntry(entry);
  }
  
  public ArchiveEntry getNextEntry() throws IOException {
    return (ArchiveEntry)getNextJarEntry();
  }
  
  public static boolean matches(byte[] signature, int length) {
    return ZipArchiveInputStream.matches(signature, length);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\jar\JarArchiveInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */