package org.apache.commons.compress.archivers.dump;

import java.io.IOException;

public class DumpArchiveException extends IOException {
   private static final long serialVersionUID = 1L;

   public DumpArchiveException() {
   }

   public DumpArchiveException(String var1) {
      super(var1);
   }

   public DumpArchiveException(Throwable var1) {
      this.initCause(var1);
   }

   public DumpArchiveException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
