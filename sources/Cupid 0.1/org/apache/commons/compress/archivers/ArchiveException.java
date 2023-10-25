package org.apache.commons.compress.archivers;

public class ArchiveException extends Exception {
  private static final long serialVersionUID = 2772690708123267100L;
  
  public ArchiveException(String message) {
    super(message);
  }
  
  public ArchiveException(String message, Exception cause) {
    super(message);
    initCause(cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\ArchiveException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */