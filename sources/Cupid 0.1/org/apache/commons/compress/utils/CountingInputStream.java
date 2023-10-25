package org.apache.commons.compress.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream {
  private long bytesRead;
  
  public CountingInputStream(InputStream in) {
    super(in);
  }
  
  public int read() throws IOException {
    int r = this.in.read();
    if (r >= 0)
      count(1L); 
    return r;
  }
  
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    int r = this.in.read(b, off, len);
    if (r >= 0)
      count(r); 
    return r;
  }
  
  protected final void count(long read) {
    if (read != -1L)
      this.bytesRead += read; 
  }
  
  public long getBytesRead() {
    return this.bytesRead;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compres\\utils\CountingInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */