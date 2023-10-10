package org.apache.commons.compress.archivers.dump;

public class UnsupportedCompressionAlgorithmException extends DumpArchiveException {
   private static final long serialVersionUID = 1L;

   public UnsupportedCompressionAlgorithmException() {
      super("this file uses an unsupported compression algorithm.");
   }

   public UnsupportedCompressionAlgorithmException(String var1) {
      super("this file uses an unsupported compression algorithm: " + var1 + ".");
   }
}
