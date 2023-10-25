package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream {
  public CloseShieldInputStream(InputStream in) {
    super(in);
  }
  
  public void close() {
    this.in = new ClosedInputStream();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\input\CloseShieldInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */