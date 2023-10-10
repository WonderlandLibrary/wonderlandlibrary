package org.apache.commons.compress.compressors.lzma;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.tukaani.xz.LZMAInputStream;

public class LZMACompressorInputStream extends CompressorInputStream {
   private final InputStream in;

   public LZMACompressorInputStream(InputStream var1) throws IOException {
      this.in = new LZMAInputStream(var1);
   }

   public int read() throws IOException {
      int var1 = this.in.read();
      this.count(var1 == -1 ? 0 : 1);
      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.in.read(var1, var2, var3);
      this.count(var4);
      return var4;
   }

   public long skip(long var1) throws IOException {
      return this.in.skip(var1);
   }

   public int available() throws IOException {
      return this.in.available();
   }

   public void close() throws IOException {
      this.in.close();
   }
}
