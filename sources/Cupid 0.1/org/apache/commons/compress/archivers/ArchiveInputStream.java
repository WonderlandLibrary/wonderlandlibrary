package org.apache.commons.compress.archivers;

import java.io.IOException;
import java.io.InputStream;

public abstract class ArchiveInputStream extends InputStream {
  private final byte[] SINGLE = new byte[1];
  
  private static final int BYTE_MASK = 255;
  
  private long bytesRead = 0L;
  
  public abstract ArchiveEntry getNextEntry() throws IOException;
  
  public int read() throws IOException {
    int num = read(this.SINGLE, 0, 1);
    return (num == -1) ? -1 : (this.SINGLE[0] & 0xFF);
  }
  
  protected void count(int read) {
    count(read);
  }
  
  protected void count(long read) {
    if (read != -1L)
      this.bytesRead += read; 
  }
  
  protected void pushedBackBytes(long pushedBack) {
    this.bytesRead -= pushedBack;
  }
  
  @Deprecated
  public int getCount() {
    return (int)this.bytesRead;
  }
  
  public long getBytesRead() {
    return this.bytesRead;
  }
  
  public boolean canReadEntryData(ArchiveEntry archiveEntry) {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\ArchiveInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */