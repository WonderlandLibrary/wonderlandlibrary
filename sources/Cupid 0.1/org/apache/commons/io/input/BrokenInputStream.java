package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BrokenInputStream extends InputStream {
  private final IOException exception;
  
  public BrokenInputStream(IOException exception) {
    this.exception = exception;
  }
  
  public BrokenInputStream() {
    this(new IOException("Broken input stream"));
  }
  
  public int read() throws IOException {
    throw this.exception;
  }
  
  public int available() throws IOException {
    throw this.exception;
  }
  
  public long skip(long n) throws IOException {
    throw this.exception;
  }
  
  public void reset() throws IOException {
    throw this.exception;
  }
  
  public void close() throws IOException {
    throw this.exception;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\BrokenInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */