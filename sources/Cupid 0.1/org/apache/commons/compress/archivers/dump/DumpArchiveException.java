package org.apache.commons.compress.archivers.dump;

import java.io.IOException;

public class DumpArchiveException extends IOException {
  private static final long serialVersionUID = 1L;
  
  public DumpArchiveException() {}
  
  public DumpArchiveException(String msg) {
    super(msg);
  }
  
  public DumpArchiveException(Throwable cause) {
    initCause(cause);
  }
  
  public DumpArchiveException(String msg, Throwable cause) {
    super(msg);
    initCause(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\dump\DumpArchiveException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */