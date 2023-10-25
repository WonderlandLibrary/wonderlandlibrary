package org.apache.commons.compress.archivers.jar;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.JarMarker;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipExtraField;

public class JarArchiveOutputStream extends ZipArchiveOutputStream {
  private boolean jarMarkerAdded = false;
  
  public JarArchiveOutputStream(OutputStream out) {
    super(out);
  }
  
  public void putArchiveEntry(ArchiveEntry ze) throws IOException {
    if (!this.jarMarkerAdded) {
      ((ZipArchiveEntry)ze).addAsFirstExtraField((ZipExtraField)JarMarker.getInstance());
      this.jarMarkerAdded = true;
    } 
    super.putArchiveEntry(ze);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\jar\JarArchiveOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */