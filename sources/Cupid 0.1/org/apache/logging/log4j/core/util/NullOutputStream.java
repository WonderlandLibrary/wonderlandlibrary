package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
  private static final NullOutputStream INSTANCE = new NullOutputStream();
  
  @Deprecated
  public static final NullOutputStream NULL_OUTPUT_STREAM = INSTANCE;
  
  public static NullOutputStream getInstance() {
    return INSTANCE;
  }
  
  public void write(byte[] b, int off, int len) {}
  
  public void write(int b) {}
  
  public void write(byte[] b) throws IOException {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\NullOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */