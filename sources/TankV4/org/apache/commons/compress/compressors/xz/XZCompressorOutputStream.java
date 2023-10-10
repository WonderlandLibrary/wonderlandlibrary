package org.apache.commons.compress.compressors.xz;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

public class XZCompressorOutputStream extends CompressorOutputStream {
   private final XZOutputStream out;

   public XZCompressorOutputStream(OutputStream var1) throws IOException {
      this.out = new XZOutputStream(var1, new LZMA2Options());
   }

   public XZCompressorOutputStream(OutputStream var1, int var2) throws IOException {
      this.out = new XZOutputStream(var1, new LZMA2Options(var2));
   }

   public void write(int var1) throws IOException {
      this.out.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void finish() throws IOException {
      this.out.finish();
   }

   public void close() throws IOException {
      this.out.close();
   }
}
