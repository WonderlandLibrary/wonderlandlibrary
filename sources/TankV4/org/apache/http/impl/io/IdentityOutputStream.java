package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;

@NotThreadSafe
public class IdentityOutputStream extends OutputStream {
   private final SessionOutputBuffer out;
   private boolean closed = false;

   public IdentityOutputStream(SessionOutputBuffer var1) {
      this.out = (SessionOutputBuffer)Args.notNull(var1, "Session output buffer");
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.out.flush();
      }

   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted write to closed stream.");
      } else {
         this.out.write(var1, var2, var3);
      }
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(int var1) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted write to closed stream.");
      } else {
         this.out.write(var1);
      }
   }
}
