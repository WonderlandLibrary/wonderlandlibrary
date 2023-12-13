package org.apache.commons.compress.archivers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ArchiveOutputStream extends OutputStream {
   private final byte[] oneByte = new byte[1];
   static final int BYTE_MASK = 255;
   private long bytesWritten = 0L;

   public abstract void putArchiveEntry(ArchiveEntry var1) throws IOException;

   public abstract void closeArchiveEntry() throws IOException;

   public abstract void finish() throws IOException;

   public abstract ArchiveEntry createArchiveEntry(File var1, String var2) throws IOException;

   public void write(int var1) throws IOException {
      this.oneByte[0] = (byte)(var1 & 255);
      this.write(this.oneByte, 0, 1);
   }

   protected void count(int var1) {
      this.count((long)var1);
   }

   protected void count(long var1) {
      if (var1 != -1L) {
         this.bytesWritten += var1;
      }

   }

   /** @deprecated */
   @Deprecated
   public int getCount() {
      return (int)this.bytesWritten;
   }

   public long getBytesWritten() {
      return this.bytesWritten;
   }

   public boolean canWriteEntryData(ArchiveEntry var1) {
      return true;
   }
}
