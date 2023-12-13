package org.apache.commons.compress.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream {
   private long bytesWritten = 0L;

   public CountingOutputStream(OutputStream var1) {
      super(var1);
   }

   public void write(int var1) throws IOException {
      this.out.write(var1);
      this.count(1L);
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
      this.count((long)var3);
   }

   protected void count(long var1) {
      if (var1 != -1L) {
         this.bytesWritten += var1;
      }

   }

   public long getBytesWritten() {
      return this.bytesWritten;
   }
}
