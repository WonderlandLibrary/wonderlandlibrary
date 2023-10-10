package org.apache.commons.compress.archivers.dump;

public class InvalidFormatException extends DumpArchiveException {
   private static final long serialVersionUID = 1L;
   protected long offset;

   public InvalidFormatException() {
      super("there was an error decoding a tape segment");
   }

   public InvalidFormatException(long var1) {
      super("there was an error decoding a tape segment header at offset " + var1 + ".");
      this.offset = var1;
   }

   public long getOffset() {
      return this.offset;
   }
}
