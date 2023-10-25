package org.apache.commons.compress.archivers.dump;

public class ShortFileException extends DumpArchiveException {
  private static final long serialVersionUID = 1L;
  
  public ShortFileException() {
    super("unexpected EOF");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\dump\ShortFileException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */