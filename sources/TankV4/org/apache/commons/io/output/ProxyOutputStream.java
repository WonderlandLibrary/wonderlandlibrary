package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream extends FilterOutputStream {
   public ProxyOutputStream(OutputStream var1) {
      super(var1);
   }

   public void write(int var1) throws IOException {
      try {
         this.beforeWrite(1);
         this.out.write(var1);
         this.afterWrite(1);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(byte[] var1) throws IOException {
      try {
         int var2 = var1 != null ? var1.length : 0;
         this.beforeWrite(var2);
         this.out.write(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      try {
         this.beforeWrite(var3);
         this.out.write(var1, var2, var3);
         this.afterWrite(var3);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void flush() throws IOException {
      try {
         this.out.flush();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   public void close() throws IOException {
      try {
         this.out.close();
      } catch (IOException var2) {
         this.handleIOException(var2);
      }

   }

   protected void beforeWrite(int var1) throws IOException {
   }

   protected void afterWrite(int var1) throws IOException {
   }

   protected void handleIOException(IOException var1) throws IOException {
      throw var1;
   }
}
