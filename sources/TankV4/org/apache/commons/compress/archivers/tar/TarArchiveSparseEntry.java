package org.apache.commons.compress.archivers.tar;

import java.io.IOException;

public class TarArchiveSparseEntry implements TarConstants {
   private final boolean isExtended;

   public TarArchiveSparseEntry(byte[] var1) throws IOException {
      byte var2 = 0;
      int var3 = var2 + 504;
      this.isExtended = TarUtils.parseBoolean(var1, var3);
   }

   public boolean isExtended() {
      return this.isExtended;
   }
}
