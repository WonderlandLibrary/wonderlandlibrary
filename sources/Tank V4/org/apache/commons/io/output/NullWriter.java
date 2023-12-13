package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer {
   public static final NullWriter NULL_WRITER = new NullWriter();

   public Writer append(char var1) {
      return this;
   }

   public Writer append(CharSequence var1, int var2, int var3) {
      return this;
   }

   public Writer append(CharSequence var1) {
      return this;
   }

   public void write(int var1) {
   }

   public void write(char[] var1) {
   }

   public void write(char[] var1, int var2, int var3) {
   }

   public void write(String var1) {
   }

   public void write(String var1, int var2, int var3) {
   }

   public void flush() {
   }

   public void close() {
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
