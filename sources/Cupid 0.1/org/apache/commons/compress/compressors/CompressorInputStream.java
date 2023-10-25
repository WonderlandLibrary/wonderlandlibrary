package org.apache.commons.compress.compressors;

import java.io.InputStream;

public abstract class CompressorInputStream extends InputStream {
  private long bytesRead = 0L;
  
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
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\CompressorInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */