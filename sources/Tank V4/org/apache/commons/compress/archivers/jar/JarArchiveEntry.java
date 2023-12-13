package org.apache.commons.compress.archivers.jar;

import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

public class JarArchiveEntry extends ZipArchiveEntry {
   private final Attributes manifestAttributes = null;
   private final Certificate[] certificates = null;

   public JarArchiveEntry(ZipEntry var1) throws ZipException {
      super(var1);
   }

   public JarArchiveEntry(String var1) {
      super(var1);
   }

   public JarArchiveEntry(ZipArchiveEntry var1) throws ZipException {
      super(var1);
   }

   public JarArchiveEntry(JarEntry var1) throws ZipException {
      super((ZipEntry)var1);
   }

   /** @deprecated */
   @Deprecated
   public Attributes getManifestAttributes() {
      return this.manifestAttributes;
   }

   /** @deprecated */
   @Deprecated
   public Certificate[] getCertificates() {
      if (this.certificates != null) {
         Certificate[] var1 = new Certificate[this.certificates.length];
         System.arraycopy(this.certificates, 0, var1, 0, var1.length);
         return var1;
      } else {
         return null;
      }
   }
}
