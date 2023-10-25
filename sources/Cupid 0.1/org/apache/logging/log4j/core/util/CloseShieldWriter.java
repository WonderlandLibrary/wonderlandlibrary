package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.Writer;

public class CloseShieldWriter extends Writer {
  private final Writer delegate;
  
  public CloseShieldWriter(Writer delegate) {
    this.delegate = delegate;
  }
  
  public void close() throws IOException {}
  
  public void flush() throws IOException {
    this.delegate.flush();
  }
  
  public void write(char[] cbuf, int off, int len) throws IOException {
    this.delegate.write(cbuf, off, len);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\CloseShieldWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */