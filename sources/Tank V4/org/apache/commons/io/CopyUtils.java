package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/** @deprecated */
@Deprecated
public class CopyUtils {
   private static final int DEFAULT_BUFFER_SIZE = 4096;

   public static void copy(byte[] var0, OutputStream var1) throws IOException {
      var1.write(var0);
   }

   public static void copy(byte[] var0, Writer var1) throws IOException {
      ByteArrayInputStream var2 = new ByteArrayInputStream(var0);
      copy((InputStream)var2, (Writer)var1);
   }

   public static void copy(byte[] var0, Writer var1, String var2) throws IOException {
      ByteArrayInputStream var3 = new ByteArrayInputStream(var0);
      copy((InputStream)var3, var1, var2);
   }

   public static int copy(InputStream var0, OutputStream var1) throws IOException {
      byte[] var2 = new byte[4096];
      int var3 = 0;

      int var5;
      for(boolean var4 = false; -1 != (var5 = var0.read(var2)); var3 += var5) {
         var1.write(var2, 0, var5);
      }

      return var3;
   }

   public static int copy(Reader var0, Writer var1) throws IOException {
      char[] var2 = new char[4096];
      int var3 = 0;

      int var5;
      for(boolean var4 = false; -1 != (var5 = var0.read(var2)); var3 += var5) {
         var1.write(var2, 0, var5);
      }

      return var3;
   }

   public static void copy(InputStream var0, Writer var1) throws IOException {
      InputStreamReader var2 = new InputStreamReader(var0);
      copy((Reader)var2, (Writer)var1);
   }

   public static void copy(InputStream var0, Writer var1, String var2) throws IOException {
      InputStreamReader var3 = new InputStreamReader(var0, var2);
      copy((Reader)var3, (Writer)var1);
   }

   public static void copy(Reader var0, OutputStream var1) throws IOException {
      OutputStreamWriter var2 = new OutputStreamWriter(var1);
      copy((Reader)var0, (Writer)var2);
      var2.flush();
   }

   public static void copy(String var0, OutputStream var1) throws IOException {
      StringReader var2 = new StringReader(var0);
      OutputStreamWriter var3 = new OutputStreamWriter(var1);
      copy((Reader)var2, (Writer)var3);
      var3.flush();
   }

   public static void copy(String var0, Writer var1) throws IOException {
      var1.write(var0);
   }
}
