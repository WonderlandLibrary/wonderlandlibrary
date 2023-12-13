package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class ProxyWriter extends FilterWriter {
   public ProxyWriter(Writer var1) {
      super(var1);
   }

   public Writer append(char var1) throws IOException {
      try {
         this.beforeWrite(1);
         this.out.append(var1);
         this.afterWrite(1);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

      return this;
   }

   public Writer append(CharSequence var1, int var2, int var3) throws IOException {
      try {
         this.beforeWrite(var3 - var2);
         this.out.append(var1, var2, var3);
         this.afterWrite(var3 - var2);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

      return this;
   }

   public Writer append(CharSequence var1) throws IOException {
      try {
         int var2 = 0;
         if (var1 != null) {
            var2 = var1.length();
         }

         this.beforeWrite(var2);
         this.out.append(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

      return this;
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

   public void write(char[] var1) throws IOException {
      try {
         int var2 = 0;
         if (var1 != null) {
            var2 = var1.length;
         }

         this.beforeWrite(var2);
         this.out.write(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      try {
         this.beforeWrite(var3);
         this.out.write(var1, var2, var3);
         this.afterWrite(var3);
      } catch (IOException var5) {
         this.handleIOException(var5);
      }

   }

   public void write(String var1) throws IOException {
      try {
         int var2 = 0;
         if (var1 != null) {
            var2 = var1.length();
         }

         this.beforeWrite(var2);
         this.out.write(var1);
         this.afterWrite(var2);
      } catch (IOException var3) {
         this.handleIOException(var3);
      }

   }

   public void write(String var1, int var2, int var3) throws IOException {
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

   public Appendable append(char var1) throws IOException {
      return this.append(var1);
   }

   public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
      return this.append(var1, var2, var3);
   }

   public Appendable append(CharSequence var1) throws IOException {
      return this.append(var1);
   }
}
