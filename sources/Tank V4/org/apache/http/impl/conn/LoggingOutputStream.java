package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LoggingOutputStream extends OutputStream {
   private final OutputStream out;
   private final Wire wire;

   public LoggingOutputStream(OutputStream var1, Wire var2) {
      this.out = var1;
      this.wire = var2;
   }

   public void write(int var1) throws IOException {
      try {
         this.wire.output(var1);
      } catch (IOException var3) {
         this.wire.output("[write] I/O error: " + var3.getMessage());
         throw var3;
      }
   }

   public void write(byte[] var1) throws IOException {
      try {
         this.wire.output(var1);
         this.out.write(var1);
      } catch (IOException var3) {
         this.wire.output("[write] I/O error: " + var3.getMessage());
         throw var3;
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      try {
         this.wire.output(var1, var2, var3);
         this.out.write(var1, var2, var3);
      } catch (IOException var5) {
         this.wire.output("[write] I/O error: " + var5.getMessage());
         throw var5;
      }
   }

   public void flush() throws IOException {
      try {
         this.out.flush();
      } catch (IOException var2) {
         this.wire.output("[flush] I/O error: " + var2.getMessage());
         throw var2;
      }
   }

   public void close() throws IOException {
      try {
         this.out.close();
      } catch (IOException var2) {
         this.wire.output("[close] I/O error: " + var2.getMessage());
         throw var2;
      }
   }
}
