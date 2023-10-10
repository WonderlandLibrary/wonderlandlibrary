package org.apache.commons.compress.compressors;

import java.io.InputStream;

public abstract class CompressorInputStream extends InputStream {
   private long bytesRead = 0L;

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
}
