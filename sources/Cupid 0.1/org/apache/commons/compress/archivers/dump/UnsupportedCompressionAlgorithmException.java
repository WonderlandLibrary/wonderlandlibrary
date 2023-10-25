package org.apache.commons.compress.archivers.dump;

public class UnsupportedCompressionAlgorithmException extends DumpArchiveException {
  private static final long serialVersionUID = 1L;
  
  public UnsupportedCompressionAlgorithmException() {
    super("this file uses an unsupported compression algorithm.");
  }
  
  public UnsupportedCompressionAlgorithmException(String alg) {
    super("this file uses an unsupported compression algorithm: " + alg + ".");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\dump\UnsupportedCompressionAlgorithmException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */