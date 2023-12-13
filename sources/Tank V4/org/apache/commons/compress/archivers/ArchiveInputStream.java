package org.apache.commons.compress.archivers;

import java.io.IOException;
import java.io.InputStream;

public abstract class ArchiveInputStream extends InputStream {
   private final byte[] SINGLE = new byte[1];
   private static final int BYTE_MASK = 255;
   private long bytesRead = 0L;

   public abstract ArchiveEntry getNextEntry() throws IOException;

   public int read() throws IOException {
      int var1 = this.read(this.SINGLE, 0, 1);
      return var1 == -1 ? -1 : this.SINGLE[0] & 255;
   }

   protected void count(int var1) {
      this.count((long)var1);
   }

   protected void count(long var1) {
      if (var1 != -1L) {
         this.bytesRead += var1;
      }

   }

   protected void pushedBackBytes(long var1) {
      this.bytesRead -= var1;
   }

   /** @deprecated */
   @Deprecated
   public int getCount() {
      return (int)this.bytesRead;
   }

   public long getBytesRead() {
      return this.bytesRead;
   }

   public boolean canReadEntryData(ArchiveEntry var1) {
      return true;
   }
}
