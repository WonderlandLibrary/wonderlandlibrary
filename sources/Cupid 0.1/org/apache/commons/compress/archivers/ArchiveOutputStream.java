package org.apache.commons.compress.archivers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ArchiveOutputStream extends OutputStream {
  private final byte[] oneByte = new byte[1];
  
  static final int BYTE_MASK = 255;
  
  private long bytesWritten = 0L;
  
  public abstract void putArchiveEntry(ArchiveEntry paramArchiveEntry) throws IOException;
  
  public abstract void closeArchiveEntry() throws IOException;
  
  public abstract void finish() throws IOException;
  
  public abstract ArchiveEntry createArchiveEntry(File paramFile, String paramString) throws IOException;
  
  public void write(int b) throws IOException {
    this.oneByte[0] = (byte)(b & 0xFF);
    write(this.oneByte, 0, 1);
  }
  
  protected void count(int written) {
    count(written);
  }
  
  protected void count(long written) {
    if (written != -1L)
      this.bytesWritten += written; 
  }
  
  @Deprecated
  public int getCount() {
    return (int)this.bytesWritten;
  }
  
  public long getBytesWritten() {
    return this.bytesWritten;
  }
  
  public boolean canWriteEntryData(ArchiveEntry archiveEntry) {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\ArchiveOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */