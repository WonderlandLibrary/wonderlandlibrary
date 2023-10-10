package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LazyDecompressingInputStream extends InputStream {
   private final InputStream wrappedStream;
   private final DecompressingEntity decompressingEntity;
   private InputStream wrapperStream;

   public LazyDecompressingInputStream(InputStream var1, DecompressingEntity var2) {
      this.wrappedStream = var1;
      this.decompressingEntity = var2;
   }

   private void initWrapper() throws IOException {
      if (this.wrapperStream == null) {
         this.wrapperStream = this.decompressingEntity.decorate(this.wrappedStream);
      }

   }

   public int read() throws IOException {
      this.initWrapper();
      return this.wrapperStream.read();
   }

   public int read(byte[] var1) throws IOException {
      this.initWrapper();
      return this.wrapperStream.read(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      this.initWrapper();
      return this.wrapperStream.read(var1, var2, var3);
   }

   public long skip(long var1) throws IOException {
      this.initWrapper();
      return this.wrapperStream.skip(var1);
   }

   public boolean markSupported() {
      return false;
   }

   public int available() throws IOException {
      this.initWrapper();
      return this.wrapperStream.available();
   }

   public void close() throws IOException {
      if (this.wrapperStream != null) {
         this.wrapperStream.close();
      }

      this.wrappedStream.close();
   }
}
