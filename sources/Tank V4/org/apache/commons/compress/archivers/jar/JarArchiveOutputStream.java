package org.apache.commons.compress.archivers.jar;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.JarMarker;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class JarArchiveOutputStream extends ZipArchiveOutputStream {
   private boolean jarMarkerAdded = false;

   public JarArchiveOutputStream(OutputStream var1) {
      super(var1);
   }

   public void putArchiveEntry(ArchiveEntry var1) throws IOException {
      if (!this.jarMarkerAdded) {
         ((ZipArchiveEntry)var1).addAsFirstExtraField(JarMarker.getInstance());
         this.jarMarkerAdded = true;
      }

      super.putArchiveEntry(var1);
   }
}
