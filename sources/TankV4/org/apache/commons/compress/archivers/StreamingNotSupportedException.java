package org.apache.commons.compress.archivers;

public class StreamingNotSupportedException extends ArchiveException {
   private static final long serialVersionUID = 1L;
   private final String format;

   public StreamingNotSupportedException(String var1) {
      super("The " + var1 + " doesn't support streaming.");
      this.format = var1;
   }

   public String getFormat() {
      return this.format;
   }
}
