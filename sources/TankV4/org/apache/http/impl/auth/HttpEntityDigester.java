package org.apache.http.impl.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

class HttpEntityDigester extends OutputStream {
   private final MessageDigest digester;
   private boolean closed;
   private byte[] digest;

   HttpEntityDigester(MessageDigest var1) {
      this.digester = var1;
      this.digester.reset();
   }

   public void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been already closed");
      } else {
         this.digester.update((byte)var1);
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("Stream has been already closed");
      } else {
         this.digester.update(var1, var2, var3);
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.digest = this.digester.digest();
         super.close();
      }
   }

   public byte[] getDigest() {
      return this.digest;
   }
}
