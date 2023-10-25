package org.apache.commons.compress.archivers;

public class StreamingNotSupportedException extends ArchiveException {
  private static final long serialVersionUID = 1L;
  
  private final String format;
  
  public StreamingNotSupportedException(String format) {
    super("The " + format + " doesn't support streaming.");
    this.format = format;
  }
  
  public String getFormat() {
    return this.format;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\StreamingNotSupportedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */