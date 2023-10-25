package org.apache.commons.compress.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream {
  private long bytesWritten = 0L;
  
  public CountingOutputStream(OutputStream out) {
    super(out);
  }
  
  public void write(int b) throws IOException {
    this.out.write(b);
    count(1L);
  }
  
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    this.out.write(b, off, len);
    count(len);
  }
  
  protected void count(long written) {
    if (written != -1L)
      this.bytesWritten += written; 
  }
  
  public long getBytesWritten() {
    return this.bytesWritten;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compres\\utils\CountingOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */