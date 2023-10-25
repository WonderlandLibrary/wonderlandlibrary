package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BoundedInputStream extends InputStream {
  private final InputStream in;
  
  private final long max;
  
  private long pos = 0L;
  
  private long mark = -1L;
  
  private boolean propagateClose = true;
  
  public BoundedInputStream(InputStream in, long size) {
    this.max = size;
    this.in = in;
  }
  
  public BoundedInputStream(InputStream in) {
    this(in, -1L);
  }
  
  public int read() throws IOException {
    if (this.max >= 0L && this.pos >= this.max)
      return -1; 
    int result = this.in.read();
    this.pos++;
    return result;
  }
  
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }
  
  public int read(byte[] b, int off, int len) throws IOException {
    if (this.max >= 0L && this.pos >= this.max)
      return -1; 
    long maxRead = (this.max >= 0L) ? Math.min(len, this.max - this.pos) : len;
    int bytesRead = this.in.read(b, off, (int)maxRead);
    if (bytesRead == -1)
      return -1; 
    this.pos += bytesRead;
    return bytesRead;
  }
  
  public long skip(long n) throws IOException {
    long toSkip = (this.max >= 0L) ? Math.min(n, this.max - this.pos) : n;
    long skippedBytes = this.in.skip(toSkip);
    this.pos += skippedBytes;
    return skippedBytes;
  }
  
  public int available() throws IOException {
    if (this.max >= 0L && this.pos >= this.max)
      return 0; 
    return this.in.available();
  }
  
  public String toString() {
    return this.in.toString();
  }
  
  public void close() throws IOException {
    if (this.propagateClose)
      this.in.close(); 
  }
  
  public synchronized void reset() throws IOException {
    this.in.reset();
    this.pos = this.mark;
  }
  
  public synchronized void mark(int readlimit) {
    this.in.mark(readlimit);
    this.mark = this.pos;
  }
  
  public boolean markSupported() {
    return this.in.markSupported();
  }
  
  public boolean isPropagateClose() {
    return this.propagateClose;
  }
  
  public void setPropagateClose(boolean propagateClose) {
    this.propagateClose = propagateClose;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\BoundedInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */