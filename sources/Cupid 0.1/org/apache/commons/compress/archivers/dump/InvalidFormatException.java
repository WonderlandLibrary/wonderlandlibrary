package org.apache.commons.compress.archivers.dump;

public class InvalidFormatException extends DumpArchiveException {
  private static final long serialVersionUID = 1L;
  
  protected long offset;
  
  public InvalidFormatException() {
    super("there was an error decoding a tape segment");
  }
  
  public InvalidFormatException(long offset) {
    super("there was an error decoding a tape segment header at offset " + offset + ".");
    this.offset = offset;
  }
  
  public long getOffset() {
    return this.offset;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\dump\InvalidFormatException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */