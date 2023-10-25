package org.apache.commons.compress.archivers.tar;

import java.io.IOException;

public class TarArchiveSparseEntry implements TarConstants {
  private final boolean isExtended;
  
  public TarArchiveSparseEntry(byte[] headerBuf) throws IOException {
    int offset = 0;
    offset += 504;
    this.isExtended = TarUtils.parseBoolean(headerBuf, offset);
  }
  
  public boolean isExtended() {
    return this.isExtended;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\tar\TarArchiveSparseEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */