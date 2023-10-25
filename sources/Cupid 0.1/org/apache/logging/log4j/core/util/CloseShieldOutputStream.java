package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class CloseShieldOutputStream extends OutputStream {
  private final OutputStream delegate;
  
  public CloseShieldOutputStream(OutputStream delegate) {
    this.delegate = delegate;
  }
  
  public void close() {}
  
  public void flush() throws IOException {
    this.delegate.flush();
  }
  
  public void write(byte[] b) throws IOException {
    this.delegate.write(b);
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    this.delegate.write(b, off, len);
  }
  
  public void write(int b) throws IOException {
    this.delegate.write(b);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\CloseShieldOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */