package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LoggingInputStream extends InputStream {
   private final InputStream in;
   private final Wire wire;

   public LoggingInputStream(InputStream var1, Wire var2) {
      this.in = var1;
      this.wire = var2;
   }

   public int read() throws IOException {
      try {
         int var1 = this.in.read();
         if (var1 == -1) {
            this.wire.input("end of stream");
         } else {
            this.wire.input(var1);
         }

         return var1;
      } catch (IOException var2) {
         this.wire.input("[read] I/O error: " + var2.getMessage());
         throw var2;
      }
   }

   public int read(byte[] var1) throws IOException {
      try {
         int var2 = this.in.read(var1);
         if (var2 == -1) {
            this.wire.input("end of stream");
         } else if (var2 > 0) {
            this.wire.input(var1, 0, var2);
         }

         return var2;
      } catch (IOException var3) {
         this.wire.input("[read] I/O error: " + var3.getMessage());
         throw var3;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      try {
         int var4 = this.in.read(var1, var2, var3);
         if (var4 == -1) {
            this.wire.input("end of stream");
         } else if (var4 > 0) {
            this.wire.input(var1, var2, var4);
         }

         return var4;
      } catch (IOException var5) {
         this.wire.input("[read] I/O error: " + var5.getMessage());
         throw var5;
      }
   }

   public long skip(long var1) throws IOException {
      try {
         return super.skip(var1);
      } catch (IOException var4) {
         this.wire.input("[skip] I/O error: " + var4.getMessage());
         throw var4;
      }
   }

   public int available() throws IOException {
      try {
         return this.in.available();
      } catch (IOException var2) {
         this.wire.input("[available] I/O error : " + var2.getMessage());
         throw var2;
      }
   }

   public void mark(int var1) {
      super.mark(var1);
   }

   public void reset() throws IOException {
      super.reset();
   }

   public boolean markSupported() {
      return false;
   }

   public void close() throws IOException {
      try {
         this.in.close();
      } catch (IOException var2) {
         this.wire.input("[close] I/O error: " + var2.getMessage());
         throw var2;
      }
   }
}
