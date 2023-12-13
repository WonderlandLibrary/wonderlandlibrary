package org.apache.http.impl.conn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class Wire {
   private final Log log;
   private final String id;

   public Wire(Log var1, String var2) {
      this.log = var1;
      this.id = var2;
   }

   public Wire(Log var1) {
      this(var1, "");
   }

   private void wire(String var1, InputStream var2) throws IOException {
      StringBuilder var3 = new StringBuilder();

      while(true) {
         int var4;
         while((var4 = var2.read()) != -1) {
            if (var4 == 13) {
               var3.append("[\\r]");
            } else if (var4 == 10) {
               var3.append("[\\n]\"");
               var3.insert(0, "\"");
               var3.insert(0, var1);
               this.log.debug(this.id + " " + var3.toString());
               var3.setLength(0);
            } else if (var4 >= 32 && var4 <= 127) {
               var3.append((char)var4);
            } else {
               var3.append("[0x");
               var3.append(Integer.toHexString(var4));
               var3.append("]");
            }
         }

         if (var3.length() > 0) {
            var3.append('"');
            var3.insert(0, '"');
            var3.insert(0, var1);
            this.log.debug(this.id + " " + var3.toString());
         }

         return;
      }
   }

   public boolean enabled() {
      return this.log.isDebugEnabled();
   }

   public void output(InputStream var1) throws IOException {
      Args.notNull(var1, "Output");
      this.wire(">> ", var1);
   }

   public void input(InputStream var1) throws IOException {
      Args.notNull(var1, "Input");
      this.wire("<< ", var1);
   }

   public void output(byte[] var1, int var2, int var3) throws IOException {
      Args.notNull(var1, "Output");
      this.wire(">> ", new ByteArrayInputStream(var1, var2, var3));
   }

   public void input(byte[] var1, int var2, int var3) throws IOException {
      Args.notNull(var1, "Input");
      this.wire("<< ", new ByteArrayInputStream(var1, var2, var3));
   }

   public void output(byte[] var1) throws IOException {
      Args.notNull(var1, "Output");
      this.wire(">> ", new ByteArrayInputStream(var1));
   }

   public void input(byte[] var1) throws IOException {
      Args.notNull(var1, "Input");
      this.wire("<< ", new ByteArrayInputStream(var1));
   }

   public void output(int var1) throws IOException {
      this.output(new byte[]{(byte)var1});
   }

   public void input(int var1) throws IOException {
      this.input(new byte[]{(byte)var1});
   }

   public void output(String var1) throws IOException {
      Args.notNull(var1, "Output");
      this.output(var1.getBytes());
   }

   public void input(String var1) throws IOException {
      Args.notNull(var1, "Input");
      this.input(var1.getBytes());
   }
}
